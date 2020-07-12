package com.leonardobishop.quests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import com.leonardobishop.quests.api.QuestsPlaceholders;
import com.leonardobishop.quests.commands.CommandQuests;
import com.leonardobishop.quests.events.EventInventory;
import com.leonardobishop.quests.events.EventPlayerJoin;
import com.leonardobishop.quests.events.EventPlayerLeave;
import com.leonardobishop.quests.itemgetter.ItemGetter;
import com.leonardobishop.quests.itemgetter.ItemGetterLatest;
import com.leonardobishop.quests.obj.Messages;
import com.leonardobishop.quests.player.QPlayer;
import com.leonardobishop.quests.player.QPlayerManager;
import com.leonardobishop.quests.quests.QuestManager;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import com.leonardobishop.quests.quests.tasktypes.TaskTypeManager;
import com.leonardobishop.quests.quests.tasktypes.types.BentoBoxLevelTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.BreedingCertainTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.BreedingTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.BrewingTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.BuildingCertainTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.BuildingTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.CookingCertainTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.CookingTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.CraftingCertainTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.CraftingTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.DealDamageTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.EnchantingTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.ExpEarnTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.FishingCertainTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.FishingTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.HarvestCertainTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.HarvestTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.InventoryTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.MilkingTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.MiningCertainTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.MiningTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.MobkillingCertainTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.MobkillingTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.PermissionTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.PlaceholderAPIEvaluateTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.PlayerkillingTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.PlaytimeTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.PositionTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.ShearingTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.TamingCertainTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.TamingTaskType;
import com.leonardobishop.quests.quests.tasktypes.types.WalkingTaskType;
import com.leonardobishop.quests.title.Title;
import com.leonardobishop.quests.title.Title_Bukkit;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Quests extends JavaPlugin {

  private static QuestManager questManager;
  private static QPlayerManager qPlayerManager;
  private static TaskTypeManager taskTypeManager;

  private static Title title;
  private ItemGetter itemGetter;
  private QuestsConfigLoader questsConfigLoader;
  private QuestsLogger questsLogger;

  private boolean brokenConfig = false;
  private BukkitTask questCompleterTask;
  private BukkitTask questAutosaveTask;

  public static Quests get() {
    return (Quests) Bukkit.getPluginManager().getPlugin("Quests");
  }

  public QuestManager getQuestManager() {
    return questManager;
  }

  public QPlayerManager getPlayerManager() {
    return qPlayerManager;
  }

  public TaskTypeManager getTaskTypeManager() {
    return taskTypeManager;
  }

  public boolean isBrokenConfig() {
    return brokenConfig;
  }

  public void setBrokenConfig(boolean brokenConfig) {
    this.brokenConfig = brokenConfig;
  }

  public Title getTitle() {
    return title;
  }

  public QuestsConfigLoader getQuestsConfigLoader() {
    return questsConfigLoader;
  }

  public String convertToFormat(long m) { // seconds please
    long hours = m / 3600;
    long minutes = (m % 3600) / 60;
    long seconds = ((m % 3600) % 60) % 60;

    return Messages.TIME_FORMAT.getMessage().replace("{hours}", String.format("%02d", hours))
        .replace("{minutes}", String.format("%02d", minutes))
        .replace("{seconds}", String.format("%02d", seconds));
  }

  @Override
  public void onEnable() {
    questsLogger = new QuestsLogger(this, LoggingLevel.INFO);

    taskTypeManager = new TaskTypeManager(this);
    questManager = new QuestManager(this);
    qPlayerManager = new QPlayerManager(this);

    dataGenerator();
    setupVersionSpecific();

    Bukkit.getPluginCommand("quests").setExecutor(new CommandQuests(this));
    Bukkit.getPluginManager().registerEvents(new EventPlayerJoin(this), this);
    Bukkit.getPluginManager().registerEvents(new EventInventory(this), this);
    Bukkit.getPluginManager().registerEvents(new EventPlayerLeave(this), this);

    if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
      new QuestsPlaceholders(this).register();
    }

    questsConfigLoader = new QuestsConfigLoader(this);

    // register task types after the server has fully started
    Bukkit.getScheduler().runTask(this, () -> {
      BentoBoxLevelTaskType.register(taskTypeManager);
      taskTypeManager.registerTaskType(new BreedingCertainTaskType());
      taskTypeManager.registerTaskType(new BreedingTaskType());
      taskTypeManager.registerTaskType(new BrewingTaskType());
      taskTypeManager.registerTaskType(new BuildingCertainTaskType());
      taskTypeManager.registerTaskType(new BuildingTaskType());
      taskTypeManager.registerTaskType(new CookingCertainTaskType());
      taskTypeManager.registerTaskType(new CookingTaskType());
      taskTypeManager.registerTaskType(new CraftingCertainTaskType());
      taskTypeManager.registerTaskType(new CraftingTaskType());
      taskTypeManager.registerTaskType(new DealDamageTaskType());
      taskTypeManager.registerTaskType(new EnchantingTaskType());
      taskTypeManager.registerTaskType(new ExpEarnTaskType());
      taskTypeManager.registerTaskType(new FishingCertainTaskType());
      taskTypeManager.registerTaskType(new FishingTaskType());
      taskTypeManager.registerTaskType(new HarvestCertainTaskType());
      taskTypeManager.registerTaskType(new HarvestTaskType());
      taskTypeManager.registerTaskType(new InventoryTaskType());
      taskTypeManager.registerTaskType(new MilkingTaskType());
      taskTypeManager.registerTaskType(new MiningCertainTaskType());
      taskTypeManager.registerTaskType(new MiningTaskType());
      taskTypeManager.registerTaskType(new MobkillingCertainTaskType());
      taskTypeManager.registerTaskType(new MobkillingTaskType());
      taskTypeManager.registerTaskType(new PermissionTaskType());
      taskTypeManager.registerTaskType(new PlaceholderAPIEvaluateTaskType());
      taskTypeManager.registerTaskType(new PlayerkillingTaskType());
      taskTypeManager.registerTaskType(new PlaytimeTaskType());
      taskTypeManager.registerTaskType(new PositionTaskType());
      taskTypeManager.registerTaskType(new ShearingTaskType());
      taskTypeManager.registerTaskType(new TamingCertainTaskType());
      taskTypeManager.registerTaskType(new TamingTaskType());
      taskTypeManager.registerTaskType(new WalkingTaskType());

      taskTypeManager.closeRegistrations();
      reloadQuests();
      if (!questsConfigLoader.getBrokenFiles().isEmpty()) {
        this.getQuestsLogger().severe("Quests has failed to load the following files:");
        for (Map.Entry<String, QuestsConfigLoader.ConfigLoadError> entry : questsConfigLoader
            .getBrokenFiles().entrySet()) {
          this.getQuestsLogger()
              .severe(" - " + entry.getKey() + ": " + entry.getValue().getMessage());
        }
      }

      for (Player player : Bukkit.getOnlinePlayers()) {
        qPlayerManager.loadPlayer(player.getUniqueId(), false);
      }
    });
  }

  @Override
  public void onDisable() {
    for (TaskType taskType : getTaskTypeManager().getTaskTypes()) {
      try {
        taskType.onDisable();
      } catch (Exception ignored) {
      }
    }
    for (QPlayer qPlayer : qPlayerManager.getQPlayers()) {
      if (qPlayer.isOnlyDataLoaded()) {
        continue;
      }
      qPlayer.getQuestProgressFile().saveToDisk(true);
    }
  }

  public void reloadQuests() {
    questManager.getQuests().clear();
    questManager.getCategories().clear();
    taskTypeManager.resetTaskTypes();

    questsConfigLoader.loadConfig();

    long autocompleteInterval = 12000;
    long completerPollInterval = 100;
    if (!isBrokenConfig()) {
      autocompleteInterval = this.getConfig()
          .getLong("options.performance-tweaking.quest-autocomplete-interval", 12000);
      completerPollInterval = this.getConfig()
          .getLong("options.performance-tweaking.quest-completer-poll-interval", 100);
    }
    if (questAutosaveTask != null) {
      try {
        questAutosaveTask.cancel();
      } catch (Exception ignored) {
      }
    }
    questAutosaveTask = Bukkit.getScheduler().runTaskTimer(this, () -> {
      for (QPlayer qPlayer : qPlayerManager.getQPlayers()) {
        if (qPlayer.isOnlyDataLoaded()) {
          continue;
        }
        qPlayer.getQuestProgressFile().saveToDisk(false);
      }
    }, autocompleteInterval, autocompleteInterval);
    if (questCompleterTask != null) {
      try {
        questCompleterTask.cancel();
      } catch (Exception ignored) {
      }
    }
    questCompleterTask = Bukkit.getScheduler().runTaskTimer(this, new QuestCompleter(this), 20,
        completerPollInterval);
  }

  public ItemStack getItemStack(String path, ConfigurationSection config,
      ItemGetter.Filter... excludes) {
    return itemGetter.getItem(path, config, this, excludes);
  }

  private void setupVersionSpecific() {
    String version =
        Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    getQuestsLogger().info("Your server is running version " + version + ".");
    title = new Title_Bukkit();
    itemGetter = new ItemGetterLatest();
  }

  private void dataGenerator() {
    File directory = new File(String.valueOf(this.getDataFolder()));
    if (!directory.exists() && !directory.isDirectory()) {
      directory.mkdir();
    }

    File config = new File(this.getDataFolder() + File.separator + "config.yml");
    if (!config.exists()) {
      InputStream in = null;
      OutputStream out = null;
      try {
        config.createNewFile();
        in = this.getResource("config.yml");
        out = new FileOutputStream(config);
        byte[] buffer = new byte[1024];
        int lenght = in.read(buffer);
        while (lenght != -1) {
          out.write(buffer, 0, lenght);
          lenght = in.read(buffer);
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      } finally {
        try {
          if (in != null)
            in.close();
        } catch (IOException ex) {
        }
        try {
          if (out != null)
            out.close();
        } catch (IOException ex) {
        }
      }

    }

    File questsDirectory = new File(this.getDataFolder() + File.separator + "quests");
    if (!questsDirectory.exists() && !questsDirectory.isDirectory()) {
      questsDirectory.mkdir();

      ArrayList<String> examples = new ArrayList<>();
      examples.add("example1.yml");
      examples.add("example2.yml");
      examples.add("example3.yml");
      examples.add("example4.yml");
      examples.add("example5.yml");
      examples.add("example6.yml");
      examples.add("Breeding1.yml");
      examples.add("Breeding2.yml");
      examples.add("Cooking1.yml");
      examples.add("Cooking2.yml");
      examples.add("CraftingPlacing1.yml");
      examples.add("Fishing1.yml");
      examples.add("Fishing2.yml");
      examples.add("Harvest1.yml");
      examples.add("Harvest2.yml");
      examples.add("Taming1.yml");
      examples.add("Taming2.yml");
      examples.add("README.txt");

      for (String name : examples) {
        File file =
            new File(this.getDataFolder() + File.separator + "quests" + File.separator + name);

        InputStream in = null;
        OutputStream out = null;
        try {
          file.createNewFile();
          in = this.getResource("quests/" + name);
          out = new FileOutputStream(file);
          byte[] buffer = new byte[1024];
          int lenght = in.read(buffer);
          while (lenght != -1) {
            out.write(buffer, 0, lenght);
            lenght = in.read(buffer);
          }
          // ByteStreams.copy(in, out); BETA method, data losses ahead
        } catch (IOException ex) {
          ex.printStackTrace();
        } finally {
          try {
            if (in != null)
              in.close();
          } catch (IOException ex) {
          }
          try {
            if (out != null)
              out.close();
          } catch (IOException ex) {
          }
        }

      }
    }
  }

  public QuestsLogger getQuestsLogger() {
    return questsLogger;
  }
}

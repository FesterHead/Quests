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
import com.leonardobishop.quests.quests.tasktypes.types.BentoBoxLevel;
import com.leonardobishop.quests.quests.tasktypes.types.Break;
import com.leonardobishop.quests.quests.tasktypes.types.BreakHigh;
import com.leonardobishop.quests.quests.tasktypes.types.Breed;
import com.leonardobishop.quests.quests.tasktypes.types.Brew;
import com.leonardobishop.quests.quests.tasktypes.types.Craft;
import com.leonardobishop.quests.quests.tasktypes.types.Damage;
import com.leonardobishop.quests.quests.tasktypes.types.Death;
import com.leonardobishop.quests.quests.tasktypes.types.Drop;
import com.leonardobishop.quests.quests.tasktypes.types.Enchant;
import com.leonardobishop.quests.quests.tasktypes.types.Exp;
import com.leonardobishop.quests.quests.tasktypes.types.Fish;
import com.leonardobishop.quests.quests.tasktypes.types.Furnace;
import com.leonardobishop.quests.quests.tasktypes.types.Inventory;
import com.leonardobishop.quests.quests.tasktypes.types.Location;
import com.leonardobishop.quests.quests.tasktypes.types.Milk;
import com.leonardobishop.quests.quests.tasktypes.types.Move;
import com.leonardobishop.quests.quests.tasktypes.types.Permission;
import com.leonardobishop.quests.quests.tasktypes.types.Place;
import com.leonardobishop.quests.quests.tasktypes.types.PlaceHigh;
import com.leonardobishop.quests.quests.tasktypes.types.Playtime;
import com.leonardobishop.quests.quests.tasktypes.types.Shear;
import com.leonardobishop.quests.quests.tasktypes.types.Tame;
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
      taskTypeManager.registerTaskType(new Break());
      taskTypeManager.registerTaskType(new BreakHigh());
      taskTypeManager.registerTaskType(new Breed());
      taskTypeManager.registerTaskType(new Brew());
      taskTypeManager.registerTaskType(new Craft());
      taskTypeManager.registerTaskType(new Damage());
      taskTypeManager.registerTaskType(new Death());
      taskTypeManager.registerTaskType(new Drop());
      taskTypeManager.registerTaskType(new Enchant());
      taskTypeManager.registerTaskType(new Exp());
      taskTypeManager.registerTaskType(new Fish());
      taskTypeManager.registerTaskType(new Furnace());
      taskTypeManager.registerTaskType(new Inventory());
      taskTypeManager.registerTaskType(new Location());
      taskTypeManager.registerTaskType(new Milk());
      taskTypeManager.registerTaskType(new Move());
      taskTypeManager.registerTaskType(new Permission());
      taskTypeManager.registerTaskType(new Place());
      taskTypeManager.registerTaskType(new PlaceHigh());
      taskTypeManager.registerTaskType(new Playtime());
      taskTypeManager.registerTaskType(new Shear());
      taskTypeManager.registerTaskType(new Tame());
      if (Bukkit.getPluginManager().isPluginEnabled("BentoBox")) {
        BentoBoxLevel.register(taskTypeManager);
      }

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
      examples.add("Place1.yml");
      examples.add("Place2.yml");
      examples.add("Place3.yml");
      examples.add("Breed1.yml");
      examples.add("Breed2.yml");

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

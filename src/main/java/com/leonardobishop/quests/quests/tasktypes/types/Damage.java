package com.leonardobishop.quests.quests.tasktypes.types;

import com.leonardobishop.quests.QuestsLogger;
import com.leonardobishop.quests.api.QuestsAPI;
import com.leonardobishop.quests.player.QPlayer;
import com.leonardobishop.quests.player.questprogressfile.QuestProgress;
import com.leonardobishop.quests.player.questprogressfile.QuestProgressFile;
import com.leonardobishop.quests.player.questprogressfile.TaskProgress;
import com.leonardobishop.quests.quests.Quest;
import com.leonardobishop.quests.quests.Task;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Damage extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();
  private QuestsLogger questLogger = QuestsAPI.getQuestManager().getPlugin().getQuestsLogger();

  public Damage() {
    super("damage", "toasted, FesterHead", "Deal a certain amount of damage.");
    this.creatorConfigValues.add(new ConfigValue(AMOUNT_KEY, true, "The amount of damage."));
    this.creatorConfigValues
        .add(new ConfigValue(PRESENT_KEY, false, "Optional present-tense action verb."));
    this.creatorConfigValues
        .add(new ConfigValue(PAST_KEY, false, "Optional past-tense action verb."));
    this.creatorConfigValues
        .add(new ConfigValue(WORLD_KEY, false, "Optional world where this task is valid."));
  }

  @Override
  public List<ConfigValue> getCreatorConfigValues() {
    return creatorConfigValues;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onDamage(EntityDamageByEntityEvent event) {

    if (!(event.getDamager() instanceof Player)) {
      return;
    }

    Player player = (Player) event.getDamager();
    double damage = event.getDamage();
    QPlayer qPlayer = QuestsAPI.getPlayerManager().getPlayer(player.getUniqueId(), true);
    QuestProgressFile progressFile = qPlayer.getQuestProgressFile();

    for (Quest quest : super.getRegisteredQuests()) {

      if (progressFile.hasStartedQuest(quest)) {

        questLogger.debug("§4--------------------");
        questLogger.debug("              Quest: §6" + quest.getId());
        QuestProgress questProgress = progressFile.getQuestProgress(quest);

        for (Task task : quest.getTasksOfType(super.getType())) {

          // Check if a world is configured for this task
          if (Objects.nonNull(task.getConfigValue(WORLD_KEY))) {
            World world = Bukkit.getWorld((String) task.getConfigValue(WORLD_KEY));
            if (Objects.isNull(world)) {
              questLogger.debug("                     §aWorld is NULL!");
              return;
            }
            questLogger.debug("     Expected world: §8" + world);
            questLogger.debug(
                "        Exact world: §8" + Bukkit.getPlayer(player.getUniqueId()).getWorld());
            if (Objects.equals(Bukkit.getPlayer(player.getUniqueId()).getWorld(), world)) {
              questLogger.debug("                     §aMatch!");
            } else {
              questLogger.debug("                     §aNO match!");
              continue;
            }
          }

          TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());
          if (taskProgress.isCompleted()) {
            questLogger.debug("                §aTask complete!");
            continue;
          }

          double progressDamage;
          int damageNeeded = (int) task.getConfigValue(AMOUNT_KEY);

          if (Objects.isNull(taskProgress.getProgress())) {
            progressDamage = 0.0;
          } else {
            progressDamage = (double) taskProgress.getProgress();
          }

          // Helpful debug information to console
          questLogger.debug("");
          questLogger.debug("      Checking task: §8" + task.getId());
          questLogger.debug("               Type: §8" + task.getType());
          questLogger.debug("           Progress: §d" + progressDamage);
          questLogger.debug("             Needed: §d" + damageNeeded);
          questLogger.debug("    Incoming damage: §5" + damage);

          taskProgress.setProgress(progressDamage + damage);

          if (((double) taskProgress.getProgress()) >= (double) damageNeeded) {
            taskProgress.setCompleted(true);
            questLogger.debug("                     §6Completed!");
          }
        }
      }
    }
  }
}

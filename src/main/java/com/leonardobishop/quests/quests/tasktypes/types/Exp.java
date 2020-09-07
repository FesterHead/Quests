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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Exp extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();
  private QuestsLogger questLogger = QuestsAPI.getQuestManager().getPlugin().getQuestsLogger();

  public Exp() {
    super("exp", "toasted, FesterHead", "Earn a set amount of experience.");
    this.creatorConfigValues.add(new ConfigValue(AMOUNT_KEY, true, "The amount of experience."));
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
  public void onExpEarn(PlayerExpChangeEvent event) {

    QPlayer player = QuestsAPI.getPlayerManager().getPlayer(event.getPlayer().getUniqueId(), true);
    QuestProgressFile progressFile = player.getQuestProgressFile();

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
            questLogger.debug("        Exact world: §8"
                + Bukkit.getPlayer(event.getPlayer().getUniqueId()).getWorld());
            if (Objects.equals(Bukkit.getPlayer(event.getPlayer().getUniqueId()).getWorld(),
                world)) {
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

          int amount = event.getAmount();
          int expNeeded = (int) task.getConfigValue("amount");

          int progressExp;
          if (taskProgress.getProgress() == null) {
            progressExp = 0;
          } else {
            progressExp = (int) taskProgress.getProgress();
          }

          // Helpful debug information to console
          questLogger.debug("");
          questLogger.debug("      Checking task: §8" + task.getId());
          questLogger.debug("               Type: §8" + task.getType());
          questLogger.debug("           Progress: §d" + progressExp);
          questLogger.debug("             Needed: §d" + expNeeded);
          questLogger.debug("       Incoming exp: §5" + amount);

          taskProgress.setProgress(progressExp + amount);

          if (((int) taskProgress.getProgress()) >= expNeeded) {
            taskProgress.setCompleted(true);
            questLogger.debug("                     §6Completed!");
          }
        }
      }
    }
  }
}

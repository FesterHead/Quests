package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;

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

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.FurnaceExtractEvent;

public final class CookingTaskType extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();
  private QuestsLogger questLogger = QuestsAPI.getQuestManager().getPlugin().getQuestsLogger();

  public CookingTaskType() {
    super("cooking", "FesterHead", "Cook/Smelt a set amount of items.");
    this.creatorConfigValues.add(new ConfigValue(AMOUNT_KEY, true, "The number of items to cook/smelt."));
    this.creatorConfigValues.add(new ConfigValue(PRESENT_KEY, true, "Present-tense action verb."));
    this.creatorConfigValues.add(new ConfigValue(PAST_KEY, true, "Past-tense action verb."));
  }

  @Override
  public List<ConfigValue> getCreatorConfigValues() {
    return creatorConfigValues;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onFurnaceExtract(FurnaceExtractEvent event) {
    QPlayer qPlayer = QuestsAPI.getPlayerManager().getPlayer(event.getPlayer().getUniqueId(), true);
    QuestProgressFile questProgressFile = qPlayer.getQuestProgressFile();

    for (Quest quest : super.getRegisteredQuests()) {
      if (questProgressFile.hasStartedQuest(quest)) {
        questLogger.debug("§4--------------------");
        questLogger.debug("              Quest: §6" + quest.getId());
        QuestProgress questProgress = questProgressFile.getQuestProgress(quest);

        // Special code to get the incoming object for this task
        Material incomingObject = event.getItemType();
        questLogger.debug("    Incoming object: §b" + incomingObject.toString());

        for (Task task : quest.getTasksOfType(super.getType())) {
          TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());
          int taskProgressCounter = (taskProgress.getProgress() == null) ? 0 : (int) taskProgress.getProgress();

          questLogger.debug("");
          questLogger.debug("      Checking task: §8" + task.getId());
          questLogger.debug("               Type: §8" + task.getType());
          questLogger.debug("           Progress: §d" + taskProgressCounter);
          questLogger.debug("               Need: §5" + (int) task.getConfigValue(AMOUNT_KEY));
          questLogger.debug("          Completed: §6" + taskProgress.isCompleted());

          if (taskProgress.isCompleted()) {
            continue;
          }

          int progressIncrement = event.getItemAmount();
          questLogger.debug("          Increment: §2" + progressIncrement);

          taskProgress.setProgress(taskProgressCounter + progressIncrement);
          questLogger.debug("       New progress: §e" + taskProgress.getProgress().toString());

          if (((int) taskProgress.getProgress()) >= (int) task.getConfigValue(AMOUNT_KEY)) {
            taskProgress.setCompleted(true);
            questLogger.debug("           §6Completed!");
          }

          return;

        }
      }
    }

    //////////////////////////////
    for (Quest quest : super.getRegisteredQuests()) {
      if (questProgressFile.hasStartedQuest(quest)) {
        QuestProgress questProgress = questProgressFile.getQuestProgress(quest);

        for (Task task : quest.getTasksOfType(super.getType())) {
          TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());

          if (taskProgress.isCompleted()) {
            continue;
          }

          taskProgress.setProgress(
              (taskProgress.getProgress() == null) ? 0 : (int) taskProgress.getProgress() + event.getItemAmount());

          if (((int) taskProgress.getProgress()) >= (int) task.getConfigValue("amount")) {
            taskProgress.setCompleted(true);
          }
        }
      }
    }
  }
}

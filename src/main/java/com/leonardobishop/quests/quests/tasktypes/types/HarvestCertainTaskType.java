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
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockDropItemEvent;

public final class HarvestCertainTaskType extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();
  private QuestsLogger questLogger = QuestsAPI.getQuestManager().getPlugin().getQuestsLogger();

  public HarvestCertainTaskType() {
    super("harvestcertain", "FesterHead", "Harvest a set amount of a specific crop.");
    this.creatorConfigValues.add(new ConfigValue(ITEM_KEY, true,
        "The crop to harvest. Can really any item dropped from a block break."));
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The number of items to harvest."));
    this.creatorConfigValues.add(new ConfigValue(PRESENT_KEY, true, "Present-tense action verb."));
    this.creatorConfigValues.add(new ConfigValue(PAST_KEY, true, "Past-tense action verb."));
  }

  @Override
  public List<ConfigValue> getCreatorConfigValues() {
    return creatorConfigValues;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onHarvest(BlockDropItemEvent event) {
    QPlayer qPlayer = QuestsAPI.getPlayerManager().getPlayer(event.getPlayer().getUniqueId(), true);
    QuestProgressFile questProgressFile = qPlayer.getQuestProgressFile();

    for (Quest quest : super.getRegisteredQuests()) {
      if (questProgressFile.hasStartedQuest(quest)) {
        questLogger.debug("§4--------------------");
        questLogger.debug("              Quest: §6" + quest.getId());
        QuestProgress questProgress = questProgressFile.getQuestProgress(quest);

        for (Task task : quest.getTasksOfType(super.getType())) {
          Material expectedObject =
              Material.getMaterial(String.valueOf(task.getConfigValue(ITEM_KEY)).toUpperCase());
          TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());
          int taskProgressCounter =
              (taskProgress.getProgress() == null) ? 0 : (int) taskProgress.getProgress();

          questLogger.debug("");
          questLogger.debug("      Checking task: §8" + task.getId());
          questLogger.debug("               Type: §8" + task.getType());
          questLogger.debug("    Expected object: §3" + expectedObject.toString());
          questLogger.debug("           Progress: §d" + taskProgressCounter);
          questLogger.debug("               Need: §5" + (int) task.getConfigValue(AMOUNT_KEY));
          questLogger.debug("          Completed: §6" + taskProgress.isCompleted());

          if (taskProgress.isCompleted()) {
            continue;
          }

          for (Item item : event.getItems()) {
            // Special code to get the incoming object for this task
            Material incomingObject = item.getItemStack().getType();
            questLogger.debug("    Incoming object: §b" + incomingObject.toString());

            if (incomingObject.equals(expectedObject)) {
              questLogger.debug("               §aMatch!");

              int progressIncrement = item.getItemStack().getAmount();
              questLogger.debug("          Increment: §2" + progressIncrement);

              taskProgress.setProgress(taskProgressCounter + progressIncrement);
              questLogger.debug("       New progress: §e" + taskProgress.getProgress().toString());

              if (((int) taskProgress.getProgress()) >= (int) task.getConfigValue(AMOUNT_KEY)) {
                taskProgress.setCompleted(true);
                questLogger.debug("         §6Completed!");
              }
            }
          }
        }
      }
    }
  }
}

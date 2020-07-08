package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;

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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public final class MiningCertainTaskType extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public MiningCertainTaskType() {
    super("blockbreakcertain", "LMBishop", "Break a set amount of a specific block.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "Amount of blocks to be broken."));
    this.creatorConfigValues.add(new ConfigValue(ITEM_KEY, true, "Name or ID of block."));
    this.creatorConfigValues.add(new ConfigValue(REVERSE_KEY, true,
        "Will reverse progression if block of same type is placed."));
    this.creatorConfigValues.add(new ConfigValue(PRESENT_KEY, true, "Present-tense action verb."));
    this.creatorConfigValues.add(new ConfigValue(PAST_KEY, true, "Past-tense action verb."));
  }

  @Override
  public List<ConfigValue> getCreatorConfigValues() {
    return creatorConfigValues;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockBreak(BlockBreakEvent event) {
    QPlayer qPlayer = QuestsAPI.getPlayerManager().getPlayer(event.getPlayer().getUniqueId(), true);
    QuestProgressFile questProgressFile = qPlayer.getQuestProgressFile();

    for (Quest quest : super.getRegisteredQuests()) {
      if (questProgressFile.hasStartedQuest(quest)) {
        QuestProgress questProgress = questProgressFile.getQuestProgress(quest);

        for (Task task : quest.getTasksOfType(super.getType())) {
          TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());

          if (taskProgress.isCompleted()) {
            continue;
          }

          Material incomingObject = event.getBlock().getType();
          Material expectedObject =
              Material.getMaterial(String.valueOf(task.getConfigValue("block")).toUpperCase());

          if (incomingObject.equals(expectedObject)) {
            increment(task, taskProgress, 1);
          }
        }
      }
    }
  }

  // subtract if enabled
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockPlace(BlockPlaceEvent event) {
    QPlayer qPlayer = QuestsAPI.getPlayerManager().getPlayer(event.getPlayer().getUniqueId(), true);
    QuestProgressFile questProgressFile = qPlayer.getQuestProgressFile();

    for (Quest quest : super.getRegisteredQuests()) {
      if (questProgressFile.hasStartedQuest(quest)) {
        QuestProgress questProgress = questProgressFile.getQuestProgress(quest);

        for (Task task : quest.getTasksOfType(super.getType())) {
          TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());

          if (taskProgress.isCompleted()) {
            continue;
          }

          Material incomingObject = event.getBlock().getType();
          Material expectedObject =
              Material.getMaterial(String.valueOf(task.getConfigValue("block")).toUpperCase());

          if (task.getConfigValue("reverse-if-placed") != null
              && ((boolean) task.getConfigValue("reverse-if-placed"))) {
            if (incomingObject.equals(expectedObject)) {
              increment(task, taskProgress, -1);
            }
          }
        }
      }
    }
  }

  private void increment(Task task, TaskProgress taskProgress, int amount) {
    int brokenBlocksNeeded = (int) task.getConfigValue("amount");

    int progressBlocksBroken;
    if (taskProgress.getProgress() == null) {
      progressBlocksBroken = 0;
    } else {
      progressBlocksBroken = (int) taskProgress.getProgress();
    }

    taskProgress.setProgress(progressBlocksBroken + amount);

    if (((int) taskProgress.getProgress()) >= brokenBlocksNeeded) {
      taskProgress.setCompleted(true);
    }
  }

}

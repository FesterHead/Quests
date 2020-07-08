package com.leonardobishop.quests.quests.tasktypes.types;

import com.leonardobishop.quests.api.QuestsAPI;
import com.leonardobishop.quests.player.QPlayer;
import com.leonardobishop.quests.player.questprogressfile.QuestProgress;
import com.leonardobishop.quests.player.questprogressfile.QuestProgressFile;
import com.leonardobishop.quests.player.questprogressfile.TaskProgress;
import com.leonardobishop.quests.quests.Quest;
import com.leonardobishop.quests.quests.Task;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

public final class MiningTaskType extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public MiningTaskType() {
    super("blockbreak", "LMBishop", "Break a set amount of blocks.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "Amount of blocks to be broken."));
    this.creatorConfigValues.add(new ConfigValue(PRESENT_KEY, false, "Present-tense action verb."));
    this.creatorConfigValues.add(new ConfigValue(PAST_KEY, false, "Past-tense action verb."));
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

          int brokenBlocksNeeded = (int) task.getConfigValue("amount");

          int progressBlocksBroken;
          if (taskProgress.getProgress() == null) {
            progressBlocksBroken = 0;
          } else {
            progressBlocksBroken = (int) taskProgress.getProgress();
          }

          taskProgress.setProgress(progressBlocksBroken + 1);

          if (((int) taskProgress.getProgress()) >= brokenBlocksNeeded) {
            taskProgress.setCompleted(true);
          }
        }
      }
    }
  }

}

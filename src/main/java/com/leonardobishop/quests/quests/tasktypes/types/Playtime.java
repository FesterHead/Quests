package com.leonardobishop.quests.quests.tasktypes.types;

import com.leonardobishop.quests.Quests;
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
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public final class Playtime extends TaskType {

  private BukkitTask poll;
  private List<ConfigValue> creatorConfigValues = new ArrayList<>();
  private QuestsLogger questLogger = QuestsAPI.getQuestManager().getPlugin().getQuestsLogger();

  public Playtime() {
    super("playtime", "Reinatix, FesterHead", "Play a certain amount of time.");
    this.creatorConfigValues.add(new ConfigValue(AMOUNT_KEY, true, "The time to play in minutes."));
    this.creatorConfigValues
        .add(new ConfigValue(WORLD_KEY, false, "Optional world where this task is valid."));
  }

  @Override
  public void onReady() {
    this.poll = new BukkitRunnable() {
      @Override
      public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
          QPlayer qPlayer = QuestsAPI.getPlayerManager().getPlayer(player.getUniqueId(), true);
          QuestProgressFile questProgressFile = qPlayer.getQuestProgressFile();
          for (Quest quest : Playtime.super.getRegisteredQuests()) {
            if (questProgressFile.hasStartedQuest(quest)) {
              questLogger.debug("§4--------------------");
              questLogger.debug("              Quest: §6" + quest.getId());
              QuestProgress questProgress = questProgressFile.getQuestProgress(quest);
              for (Task task : quest.getTasksOfType(Playtime.super.getType())) {
                TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());
                if (taskProgress.isCompleted()) {
                  questLogger.debug("                     §aDONE!");
                  this.cancel();
                  continue;
                }
                int minutes = (int) task.getConfigValue(AMOUNT_KEY);
                if (taskProgress.getProgress() == null) {
                  taskProgress.setProgress(1);
                } else {
                  taskProgress.setProgress((int) taskProgress.getProgress() + 1);
                }

                questLogger.debug("");
                questLogger.debug("      Checking task: §8" + task.getId());
                questLogger.debug("               Type: §8" + task.getType());
                questLogger.debug("           Progress: §d" + taskProgress.getProgress());
                questLogger
                    .debug("               Need: §5" + (int) task.getConfigValue(AMOUNT_KEY));

                if (((int) taskProgress.getProgress()) >= minutes) {
                  questLogger.debug("                     §6Completed!");
                  taskProgress.setCompleted(true);
                  this.cancel();
                }
              }
            }
          }
        }
      }
    }.runTaskTimer(Quests.get(), 0L, 20L * 60);
  }

  @Override
  public void onDisable() {
    if (this.poll != null) {
      this.poll.cancel();
    }
  }

  @Override
  public List<ConfigValue> getCreatorConfigValues() {
    return creatorConfigValues;
  }
}

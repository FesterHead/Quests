package com.leonardobishop.quests.quests.tasktypes.types;

import com.leonardobishop.quests.Quests;
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

public final class Permission extends TaskType {

  private BukkitTask poll;
  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public Permission() {
    super("permission", "LMBishop", "Require a specific permission");
    this.creatorConfigValues
        .add(new ConfigValue("permission", true, "The specific required permission."));
  }

  @Override
  public void onReady() {
    this.poll = new BukkitRunnable() {
      @Override
      public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
          QPlayer qPlayer = QuestsAPI.getPlayerManager().getPlayer(player.getUniqueId(), true);
          QuestProgressFile questProgressFile = qPlayer.getQuestProgressFile();
          for (Quest quest : Permission.super.getRegisteredQuests()) {
            if (questProgressFile.hasStartedQuest(quest)) {
              QuestProgress questProgress = questProgressFile.getQuestProgress(quest);
              for (Task task : quest.getTasksOfType(Permission.super.getType())) {
                TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());
                if (taskProgress.isCompleted()) {
                  continue;
                }
                String permission = (String) task.getConfigValue("permission");
                if (permission != null) {
                  if (player.hasPermission(permission)) {
                    taskProgress.setCompleted(true);
                  }
                }
              }
            }
          }
        }
      }
    }.runTaskTimer(Quests.get(), 30L, 30L);
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

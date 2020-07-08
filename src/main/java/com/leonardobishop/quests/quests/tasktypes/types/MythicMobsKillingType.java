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
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.ArrayList;
import java.util.List;

public final class MythicMobsKillingType extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public MythicMobsKillingType() {
    super("mythicmobs_killing", "LMBishop", "Kill a set amount of a MythicMobs entity.");
    this.creatorConfigValues.add(new ConfigValue(AMOUNT_KEY, true, "Amount of mobs to be killed."));
    this.creatorConfigValues
        .add(new ConfigValue("name", true, "The 'internal name' of the MythicMob."));
    this.creatorConfigValues.add(new ConfigValue(PRESENT_KEY, true, "Present-tense action verb."));
    this.creatorConfigValues.add(new ConfigValue(PAST_KEY, true, "Past-tense action verb."));
  }

  @Override
  public List<ConfigValue> getCreatorConfigValues() {
    return creatorConfigValues;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onMobKill(MythicMobDeathEvent event) {
    Entity killer = event.getKiller();
    Entity mob = event.getEntity();

    if (mob == null || mob instanceof Player) {
      return;
    }

    if (killer == null) {
      return;
    }

    String mobName = event.getMobType().getInternalName();

    QPlayer qPlayer = QuestsAPI.getPlayerManager().getPlayer(killer.getUniqueId(), true);
    QuestProgressFile questProgressFile = qPlayer.getQuestProgressFile();

    for (Quest quest : super.getRegisteredQuests()) {
      if (questProgressFile.hasStartedQuest(quest)) {
        QuestProgress questProgress = questProgressFile.getQuestProgress(quest);

        for (Task task : quest.getTasksOfType(super.getType())) {
          TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());

          if (taskProgress.isCompleted()) {
            continue;
          }

          String configName = (String) task.getConfigValue("name");

          if (!mobName.equals(configName)) {
            return;
          }

          int mobKillsNeeded = (int) task.getConfigValue("amount");

          int progressKills;
          if (taskProgress.getProgress() == null) {
            progressKills = 0;
          } else {
            progressKills = (int) taskProgress.getProgress();
          }

          taskProgress.setProgress(progressKills + 1);

          if (((int) taskProgress.getProgress()) >= mobKillsNeeded) {
            taskProgress.setCompleted(true);
          }
        }
      }
    }
  }

}

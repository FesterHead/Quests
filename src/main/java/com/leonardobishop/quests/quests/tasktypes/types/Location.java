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
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Location extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();
  private QuestsLogger questLogger = QuestsAPI.getQuestManager().getPlugin().getQuestsLogger();

  public Location() {
    super("location", "LMBishop, FesterHead", "Be at or near a certain location.");
    this.creatorConfigValues.add(new ConfigValue("x", true, "X coordinate."));
    this.creatorConfigValues.add(new ConfigValue("y", true, "Y coordinate."));
    this.creatorConfigValues.add(new ConfigValue("z", true, "Z coordinate."));
    this.creatorConfigValues
        .add(new ConfigValue("padding", false, "Optional padding for when close is good enough."));
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
  public void onMove(PlayerMoveEvent event) {
    if (event.getFrom().getBlockX() == event.getTo().getBlockX()
        && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
      return;
    }

    Player player = event.getPlayer();
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

          int x = (int) task.getConfigValue("x");
          int y = (int) task.getConfigValue("y");
          int z = (int) task.getConfigValue("z");
          int padding = 0;
          if (Objects.nonNull(task.getConfigValue("padding"))) {
            padding = (int) task.getConfigValue("padding");
          }

          World world = Bukkit.getWorld((String) task.getConfigValue(WORLD_KEY));
          org.bukkit.Location location = new org.bukkit.Location(world, x, y, z);

          // Helpful debug information to console
          questLogger.debug("");
          questLogger.debug("      Checking task: §8" + task.getId());
          questLogger.debug("               Type: §8" + task.getType());
          questLogger.debug("           Progress: §dX=" + player.getLocation().getBlockX() + "Y="
              + player.getLocation().getBlockY() + "Z=" + player.getLocation().getBlockZ());
          questLogger.debug("            Padding: §5" + padding);

          if (between(player.getLocation().getBlockX(), location.getBlockX() - padding,
              location.getBlockX() + padding)
              && between(player.getLocation().getBlockY(), location.getBlockY() - padding,
                  location.getBlockY() + padding)
              && between(player.getLocation().getBlockZ(), location.getBlockZ() - padding,
                  location.getBlockZ() + padding)) {
            taskProgress.setCompleted(true);
            questLogger.debug("                     §6Completed!");
          }
        }
      }
    }
  }

  public static boolean between(int i, int minValueInclusive, int maxValueInclusive) {
    return (i >= minValueInclusive && i <= maxValueInclusive);
  }
}

package com.leonardobishop.quests.quests.tasktypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import com.leonardobishop.quests.QuestsLogger;
import com.leonardobishop.quests.api.QuestsAPI;
import com.leonardobishop.quests.player.QPlayer;
import com.leonardobishop.quests.player.questprogressfile.QuestProgress;
import com.leonardobishop.quests.player.questprogressfile.QuestProgressFile;
import com.leonardobishop.quests.player.questprogressfile.TaskProgress;
import com.leonardobishop.quests.quests.Quest;
import com.leonardobishop.quests.quests.Task;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionType;

/**
 * A task type which can be used within Quests. A {@link Quest} will be registered to this if it
 * contains at least 1 task which is of this type. This is so you do not have to iterate through
 * every single quest.
 */
public abstract class TaskType implements Listener {

  public static final String ITEM_KEY = "item";
  public static final String AMOUNT_KEY = "amount";
  public static final String PRESENT_KEY = "present";
  public static final String PAST_KEY = "past";
  public static final String REVERSE_KEY = "reverse-progression";
  public static final String CONTINUE_EVALUATING_KEY = "continue-evaluating";
  public static final String WORLD_KEY = "world";
  public static final String COREPROTECT_KEY = "coreprotect";
  public static final String COREPROTECT_SECONDS_KEY = "coreprotect_seconds";

  private final List<Quest> quests = new ArrayList<>();
  private final String type;
  private String author;
  private String description;

  private QuestsLogger questLogger = QuestsAPI.getQuestManager().getPlugin().getQuestsLogger();

  /**
   * @param type        the name of the task type, should not contain spaces
   * @param author      the name of the person (or people) who wrote it
   * @param description a short, simple description of the task type
   */
  public TaskType(String type, String author, String description) {
    this.type = type;
    this.author = author;
    this.description = description;
  }

  /**
   * @param type the name of the task type, should not contain spaces
   */
  public TaskType(String type) {
    this.type = type;
  }

  /**
   * Registers a {@link Quest} to this task type. This is usually done when all the quests are
   * initially loaded.
   *
   * @param quest the {@link Quest} to register.
   */
  public final void registerQuest(Quest quest) {
    if (!quests.contains(quest)) {
      quests.add(quest);
    }
  }

  /**
   * Clears the list which contains the registered quests.
   */
  public final void unregisterAll() {
    quests.clear();
  }

  /**
   * @return {@link List} of type {@link Quest} of all registered quests.
   */
  public final List<Quest> getRegisteredQuests() {
    return quests;
  }

  public final String getType() {
    return type;
  }

  public String getAuthor() {
    return author;
  }

  public String getDescription() {
    return description;
  }

  public List<ConfigValue> getCreatorConfigValues() {
    return Collections.emptyList();
  }

  public void onReady() {
    // not implemented here
  }

  public void onDisable() {
    // not implemented here
  }

  public void processObject(Event event, Object incoming, UUID uuid, int increment) {

    QPlayer player = QuestsAPI.getPlayerManager().getPlayer(uuid, true);
    QuestProgressFile progressFile = player.getQuestProgressFile();

    for (Quest quest : progressFile.getStartedQuests()) {

      QuestProgress questProgress = progressFile.getQuestProgress(quest);

      for (Task task : quest.getTasksOfType(this.getType())) {

        // If the task is done, skip it
        TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());
        int taskProgressCounter =
            (Objects.isNull(taskProgress.getProgress())) ? 0 : (int) taskProgress.getProgress();
        if (taskProgress.isCompleted()) {
          // questLogger.debug(" §aTask complete!");
          continue;
        }

        questLogger.debug("§4---------------------------------------------------------");
        questLogger.debug("             Player: §6" + Bukkit.getPlayer(uuid).getName());
        questLogger.debug("               UUID: §6" + uuid);
        questLogger.debug("              Event: §6" + event.getEventName());
        questLogger.debug("              Quest: §6" + quest.getId());
        questLogger.debug("               Task: §8" + task.getId());
        questLogger.debug("               Type: §8" + task.getType());

        // Check if a world is configured for this task
        if (Objects.nonNull(task.getConfigValue(WORLD_KEY))) {
          World world = Bukkit.getWorld((String) task.getConfigValue(WORLD_KEY));
          if (Objects.isNull(world)) {
            questLogger.debug("                     §dNot in world "
                + (String) task.getConfigValue(WORLD_KEY) + "!");
            return;
          }
          questLogger.debug("     Expected world: §8" + world);
          questLogger.debug("        Exact world: §8" + Bukkit.getPlayer(uuid).getWorld());
          if (Objects.equals(Bukkit.getPlayer(uuid).getWorld(), world)) {
            questLogger.debug("                     §aMatch!");
          } else {
            questLogger.debug("                     §aNO match!");
            continue;
          }
        }


        // If break task and coreprotect is configured...
        if (task.getType().startsWith("break") && (event instanceof BlockBreakEvent)
            && QuestsAPI.getQuestsCoreProtectAPI().nonNull()
            && Objects.nonNull(task.getConfigValue(COREPROTECT_KEY))
            && (boolean) (task.getConfigValue(COREPROTECT_KEY))) {

          BlockBreakEvent tempEvent = (BlockBreakEvent) event;
          int seconds = Objects.nonNull(task.getConfigValue(COREPROTECT_SECONDS_KEY))
              ? (int) task.getConfigValue(COREPROTECT_SECONDS_KEY)
              : 3600;

          if (QuestsAPI.getQuestsCoreProtectAPI().isPlayerBlock(tempEvent.getBlock(), seconds)) {
            questLogger.debug("Block player placed. Skipping!");
            continue;
          }
        }

        // Use specific item if configured for the task
        Object expected = null;
        if (Objects.nonNull(task.getConfigValue(ITEM_KEY))) {
          if (incoming instanceof Material) {
            expected =
                Material.getMaterial(String.valueOf(task.getConfigValue(ITEM_KEY)).toUpperCase());
          } else if (incoming instanceof EntityType) {
            expected =
                EntityType.valueOf(String.valueOf(task.getConfigValue(ITEM_KEY)).toUpperCase());
          } else if (incoming instanceof PotionType) {
            expected =
                PotionType.valueOf(String.valueOf(task.getConfigValue(ITEM_KEY)).toUpperCase());
          }
        }

        // Helpful debug information to console
        questLogger.debug("    Incoming object: §b" + incoming.toString());
        questLogger.debug("    Expected object: §3"
            + ((Objects.nonNull(expected)) ? expected.toString() : "n/a"));

        // If the expected object is null then this is a general task, all events count
        // Otherwise the incoming object must match the expected object
        if ((Objects.isNull(expected)) || (Objects.equals(incoming, expected))) {
          questLogger.debug("                     §aMatch!");
          questLogger.debug("          Increment: §2" + increment);

          if (Objects.nonNull(task.getConfigValue(REVERSE_KEY))
              && !(boolean) (task.getConfigValue(REVERSE_KEY))) {
            questLogger.debug("                     §aReverse progression skipped!");
          } else {
            taskProgress.setProgress(taskProgressCounter + increment);
            questLogger.debug("           Progress: §d" + taskProgress.getProgress().toString());
            questLogger.debug("               Need: §5" + (int) task.getConfigValue(AMOUNT_KEY));
            if (((int) taskProgress.getProgress()) >= (int) task.getConfigValue(AMOUNT_KEY)) {
              taskProgress.setCompleted(true);
              questLogger.debug("                     §6Completed!");
            }
          }

          if (Objects.nonNull(task.getConfigValue(CONTINUE_EVALUATING_KEY))
              && (boolean) (task.getConfigValue(CONTINUE_EVALUATING_KEY))) {
            questLogger.debug("                     §9Continue task evaluation!");
            continue;
          } else {
            return;
          }
        } else {
          questLogger.debug("                     §aNO match!");
        }
      }
    }
  }
}

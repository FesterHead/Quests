package com.leonardobishop.quests.quests.tasktypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.leonardobishop.quests.QuestsLogger;
import com.leonardobishop.quests.api.QuestsAPI;
import com.leonardobishop.quests.player.QPlayer;
import com.leonardobishop.quests.player.questprogressfile.QuestProgress;
import com.leonardobishop.quests.player.questprogressfile.QuestProgressFile;
import com.leonardobishop.quests.player.questprogressfile.TaskProgress;
import com.leonardobishop.quests.quests.Quest;
import com.leonardobishop.quests.quests.Task;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;

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
  public static final String REVERSE_KEY = "reverse-if-broken";

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

  public void processEntity(EntityType incomingObject, QPlayer qPlayer,
      QuestProgressFile questProgressFile, int progressIncrement) {

    for (Quest quest : this.getRegisteredQuests()) {

      if (questProgressFile.hasStartedQuest(quest)) {

        questLogger.debug("§4--------------------");
        questLogger.debug("              Quest: §6" + quest.getId());
        QuestProgress questProgress = questProgressFile.getQuestProgress(quest);


        for (Task task : quest.getTasksOfType(this.getType())) {

          // If the task is done, skip it
          TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());
          int taskProgressCounter =
              (Objects.isNull(taskProgress.getProgress())) ? 0 : (int) taskProgress.getProgress();
          if (taskProgress.isCompleted()) {
            questLogger.debug("                     §aDONE!");
            continue;
          }

          // Use specific item if configured for the task
          EntityType expectedObject = null;
          if (Objects.nonNull(task.getConfigValue(ITEM_KEY))) {
            expectedObject =
                EntityType.valueOf(String.valueOf(task.getConfigValue(ITEM_KEY)).toUpperCase());
          }

          // Helpful debug information to console
          questLogger.debug("");
          questLogger.debug("      Checking task: §8" + task.getId());
          questLogger.debug("               Type: §8" + task.getType());
          questLogger.debug("    Incoming object: §b" + incomingObject.toString());
          questLogger.debug("    Expected object: §3"
              + ((Objects.nonNull(expectedObject)) ? expectedObject.toString() : "n/a"));
          questLogger.debug("           Progress: §d" + taskProgressCounter);
          questLogger.debug("               Need: §5" + (int) task.getConfigValue(AMOUNT_KEY));
          questLogger.debug("          Completed: §6" + taskProgress.isCompleted());

          // If the expected object is null then this is a general task, all events count
          // Otherwise the incoming object must match the expected object
          if ((Objects.isNull(expectedObject))
              || (Objects.equals(incomingObject, expectedObject))) {
            questLogger.debug("                     §aMatch!");

            questLogger.debug("          Increment: §2" + progressIncrement);

            taskProgress.setProgress(taskProgressCounter + progressIncrement);
            questLogger.debug("       New progress: §e" + taskProgress.getProgress().toString());

            if (((int) taskProgress.getProgress()) >= (int) task.getConfigValue(AMOUNT_KEY)) {
              taskProgress.setCompleted(true);
              questLogger.debug("                     §6Completed!");
            }

            return;
          }
        }
      }
    }
  }

  public void processMaterial(Material incomingObject, QPlayer qPlayer,
      QuestProgressFile questProgressFile, int progressIncrement) {
    for (Quest quest : this.getRegisteredQuests()) {

      if (questProgressFile.hasStartedQuest(quest)) {

        questLogger.debug("§4--------------------");
        questLogger.debug("              Quest: §6" + quest.getId());
        QuestProgress questProgress = questProgressFile.getQuestProgress(quest);

        for (Task task : quest.getTasksOfType(this.getType())) {

          // If the task is done, skip it
          TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());
          int taskProgressCounter =
              (Objects.isNull(taskProgress.getProgress())) ? 0 : (int) taskProgress.getProgress();
          if (taskProgress.isCompleted()) {
            questLogger.debug("                     §aDONE!");
            continue;
          }

          Material expectedObject = null;
          if (Objects.nonNull(task.getConfigValue(ITEM_KEY))) {
            expectedObject =
                Material.getMaterial(String.valueOf(task.getConfigValue(ITEM_KEY)).toUpperCase());
          }

          // Helpful debug information to console
          questLogger.debug("");
          questLogger.debug("      Checking task: §8" + task.getId());
          questLogger.debug("               Type: §8" + task.getType());
          questLogger.debug("    Incoming object: §b" + incomingObject.toString());
          questLogger.debug("    Expected object: §3"
              + ((Objects.nonNull(expectedObject)) ? expectedObject.toString() : "n/a"));
          questLogger.debug("           Progress: §d" + taskProgressCounter);
          questLogger.debug("               Need: §5" + (int) task.getConfigValue(AMOUNT_KEY));
          questLogger.debug("          Completed: §6" + taskProgress.isCompleted());

          // If the expected object is null then this is a general task, all events count
          // Otherwise the incoming object must match the expected object
          if ((Objects.isNull(expectedObject))
              || (Objects.equals(incomingObject, expectedObject))) {
            questLogger.debug("                     §aMatch!");
            questLogger.debug("          Increment: §2" + progressIncrement);
            taskProgress.setProgress(taskProgressCounter + progressIncrement);
            questLogger.debug("       New progress: §e" + taskProgress.getProgress().toString());

            if (((int) taskProgress.getProgress()) >= (int) task.getConfigValue(AMOUNT_KEY)) {
              taskProgress.setCompleted(true);
              questLogger.debug("                     §6Completed!");
            }

            return;
          }
        }
      }
    }
  }
}

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

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityBreedEvent;

public final class BreedingTaskType extends TaskType {

    private List<ConfigValue> creatorConfigValues = new ArrayList<>();
    private static final String AMOUNT_KEY = "amount";

    public BreedingTaskType() {
        super("breeding", "toasted", "Breed a set amount of animals.");
        this.creatorConfigValues.add(new ConfigValue(AMOUNT_KEY, true, "Amount of animals to breed."));
    }

    @Override
    public List<ConfigValue> getCreatorConfigValues() {
        return creatorConfigValues;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreed(EntityBreedEvent event) {
        QPlayer qPlayer = QuestsAPI.getPlayerManager().getPlayer(event.getBreeder().getUniqueId(), true);
        QuestProgressFile questProgressFile = qPlayer.getQuestProgressFile();

        for (Quest quest : super.getRegisteredQuests()) {
            if (questProgressFile.hasStartedQuest(quest)) {
                QuestsAPI.getQuestManager().getPlugin().getQuestsLogger().debug("§4--------------------");
                QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                        .debug("              Quest: §6" + quest.getId());
                QuestProgress questProgress = questProgressFile.getQuestProgress(quest);

                // Special code to get the incoming object for this task
                EntityType incomingObject = event.getEntity().getType();
                QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                        .debug("    Incoming object: §b" + incomingObject.toString());

                for (Task task : quest.getTasksOfType(super.getType())) {
                    TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());
                    int taskProgressCounter = (taskProgress.getProgress() == null) ? 0
                            : (int) taskProgress.getProgress();

                    QuestsAPI.getQuestManager().getPlugin().getQuestsLogger().debug("");
                    QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                            .debug("      Checking task: §8" + task.getId());
                    QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                            .debug("               Type: §8" + task.getType());
                    QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                            .debug("           Progress: §d" + taskProgressCounter);
                    QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                            .debug("               Need: §5" + (int) task.getConfigValue(AMOUNT_KEY));
                    QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                            .debug("          Completed: §6" + taskProgress.isCompleted());

                    if (taskProgress.isCompleted()) {
                        continue;
                    }

                    int progressIncrement = 1;
                    QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                            .debug("          Increment: §2" + progressIncrement);

                    taskProgress.setProgress(taskProgressCounter + progressIncrement);
                    QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                            .debug("       New progress: §e" + taskProgress.getProgress().toString());

                    if (((int) taskProgress.getProgress()) >= (int) task.getConfigValue(AMOUNT_KEY)) {
                        taskProgress.setCompleted(true);
                        QuestsAPI.getQuestManager().getPlugin().getQuestsLogger().debug("           §6Completed!");
                    }

                    return;

                }
            }
        }
    }
}

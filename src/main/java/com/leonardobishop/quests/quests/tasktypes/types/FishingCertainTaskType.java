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
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;

public final class FishingCertainTaskType extends TaskType {

    private List<ConfigValue> creatorConfigValues = new ArrayList<>();
    private static final String MATERIAL_KEY = "item";
    private static final String AMOUNT_KEY = "amount";

    public FishingCertainTaskType() {
        super("fishingcertain", "FesterHead", "Catch a set amount of a specific fish/item from the sea.");
        this.creatorConfigValues.add(new ConfigValue(MATERIAL_KEY, true, "The fish/item to catch."));
        this.creatorConfigValues.add(new ConfigValue(AMOUNT_KEY, true, "The amount of the fish/item to catch."));
    }

    @Override
    public List<ConfigValue> getCreatorConfigValues() {
        return creatorConfigValues;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFishCaught(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) {
            return;
        }

        QPlayer qPlayer = QuestsAPI.getPlayerManager().getPlayer(event.getPlayer().getUniqueId(), true);
        QuestProgressFile questProgressFile = qPlayer.getQuestProgressFile();

        for (Quest quest : super.getRegisteredQuests()) {
            if (questProgressFile.hasStartedQuest(quest)) {
                QuestsAPI.getQuestManager().getPlugin().getQuestsLogger().debug("--------------------");
                QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                        .debug("              Quest: " + quest.getId());
                QuestProgress questProgress = questProgressFile.getQuestProgress(quest);

                for (Task task : quest.getTasksOfType(super.getType())) {
                    TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());
                    int taskProgressCounter = (taskProgress.getProgress() == null) ? 0
                            : (int) taskProgress.getProgress();

                    QuestsAPI.getQuestManager().getPlugin().getQuestsLogger().debug("");
                    QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                            .debug("      Checking task: " + task.getId());
                    QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                            .debug("               Type: " + task.getType());
                    QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                            .debug("           Progress: " + taskProgressCounter);
                    QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                            .debug("               Need: " + (int) task.getConfigValue(AMOUNT_KEY));
                    QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                            .debug("          Completed: " + taskProgress.isCompleted());

                    if (taskProgress.isCompleted()) {
                        continue;
                    }

                    Material targetMaterial = Material.getMaterial(String.valueOf(task.getConfigValue(MATERIAL_KEY)));
                    QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                            .debug("    Target material: " + targetMaterial.toString());

                    Material sourceMaterial = ((Item) event.getCaught()).getItemStack().getType();
                    QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                            .debug("    Source material: " + sourceMaterial.toString());

                    if (sourceMaterial.equals(targetMaterial)) {
                        QuestsAPI.getQuestManager().getPlugin().getQuestsLogger().debug("    Match!");

                        int progressIncrement = ((Item) event.getCaught()).getItemStack().getAmount();
                        QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                                .debug("          Increment: " + progressIncrement);

                        taskProgress.setProgress(taskProgressCounter + progressIncrement);
                        QuestsAPI.getQuestManager().getPlugin().getQuestsLogger()
                                .debug("       New progress: " + taskProgress.getProgress().toString());

                        if (((int) taskProgress.getProgress()) >= (int) task.getConfigValue(AMOUNT_KEY)) {
                            taskProgress.setCompleted(true);
                            QuestsAPI.getQuestManager().getPlugin().getQuestsLogger().debug("           Completed!");
                        }

                        return;
                    }
                }
            }
        }
    }

}

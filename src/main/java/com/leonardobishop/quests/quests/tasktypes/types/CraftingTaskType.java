package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;

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

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public final class CraftingTaskType extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();
  private QuestsLogger questLogger = QuestsAPI.getQuestManager().getPlugin().getQuestsLogger();

  public CraftingTaskType() {
    super("crafting", "FesterHead", "Craft a set amount of materials.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The amount of the material to craft."));
    this.creatorConfigValues.add(new ConfigValue(PRESENT_KEY, true, "Present-tense action verb."));
    this.creatorConfigValues.add(new ConfigValue(PAST_KEY, true, "Past-tense action verb."));
  }

  @Override
  public List<ConfigValue> getCreatorConfigValues() {
    return creatorConfigValues;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onCraftItem(CraftItemEvent event) {
    QPlayer qPlayer =
        QuestsAPI.getPlayerManager().getPlayer(event.getWhoClicked().getUniqueId(), true);
    QuestProgressFile questProgressFile = qPlayer.getQuestProgressFile();

    for (Quest quest : super.getRegisteredQuests()) {
      if (questProgressFile.hasStartedQuest(quest)) {
        questLogger.debug("§4--------------------");
        questLogger.debug("              Quest: §6" + quest.getId());
        QuestProgress questProgress = questProgressFile.getQuestProgress(quest);

        // Special code to get the incoming object for this task
        Material incomingObject = event.getRecipe().getResult().getType();
        questLogger.debug("    Incoming object: §b" + incomingObject.toString());

        for (Task task : quest.getTasksOfType(super.getType())) {
          TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());
          int taskProgressCounter =
              (taskProgress.getProgress() == null) ? 0 : (int) taskProgress.getProgress();

          questLogger.debug("");
          questLogger.debug("      Checking task: §8" + task.getId());
          questLogger.debug("               Type: §8" + task.getType());
          questLogger.debug("           Progress: §d" + taskProgressCounter);
          questLogger.debug("               Need: §5" + (int) task.getConfigValue(AMOUNT_KEY));
          questLogger.debug("          Completed: §6" + taskProgress.isCompleted());

          if (taskProgress.isCompleted()) {
            continue;
          }

          int progressIncrement = getAmountCraftItem(incomingObject, event);
          questLogger.debug("          Increment: §2" + progressIncrement);

          taskProgress.setProgress(taskProgressCounter + progressIncrement);
          questLogger.debug("       New progress: §e" + taskProgress.getProgress().toString());

          if (((int) taskProgress.getProgress()) >= (int) task.getConfigValue(AMOUNT_KEY)) {
            taskProgress.setCompleted(true);
            questLogger.debug("          §6Completed!");
          }

          return;
        }

      }
    }
  }

  // Helper code to get shift-clicks when crafting multiple stacks
  // Otherwise, 3 sets of oak planks which come in 4s, is counted as 4 and not 12
  // https://www.spigotmc.org/threads/util-get-the-crafted-item-amount-from-a-craftitemevent.162952/
  private int getAmountCraftItem(Material material, CraftItemEvent event) {
    if (event.isCancelled())
      return 0;
    if (!event.getRecipe().getResult().getType().equals(material))
      return 0;
    int amount = event.getRecipe().getResult().getAmount();
    if (event.isShiftClick()) {
      int max = event.getInventory().getMaxStackSize();
      ItemStack[] matrix = event.getInventory().getMatrix();
      for (ItemStack is : matrix) {
        if (is == null || is.getType().equals(Material.AIR))
          continue;
        int tmp = is.getAmount();
        if (tmp < max && tmp > 0)
          max = tmp;
      }
      amount *= max;
    }
    return amount;
  }
}

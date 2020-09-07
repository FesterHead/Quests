package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public final class Inventory extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public Inventory() {
    super("inventory", "LMBishop", "Obtain a set of items.");
    this.creatorConfigValues.add(new ConfigValue(AMOUNT_KEY, true, "Amount of item to have."));
    this.creatorConfigValues.add(new ConfigValue(ITEM_KEY, true, "The specific item."));
    this.creatorConfigValues.add(new ConfigValue("remove-items-when-complete", true,
        "Take the items away on completion.  Default is false."));
    this.creatorConfigValues.add(new ConfigValue("update-progress", true,
        "Update progress.  If this causes lag then disable it."));
  }

  @Override
  public List<ConfigValue> getCreatorConfigValues() {
    return creatorConfigValues;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onItemPickup(EntityPickupItemEvent event) {
    if (!(event.getEntity() instanceof Player)) {
      return;
    }
    Bukkit.getScheduler().runTaskLater(Quests.get(),
        () -> this.checkInventory((Player) event.getEntity()), 1L);
  }

  @EventHandler(priority = EventPriority.MONITOR/* , ignoreCancelled = true */)
  public void onInventoryClick(InventoryClickEvent event) {
    if (!(event.getWhoClicked() instanceof Player)) {
      return;
    }
    // Still some work to do as it doesn't really work
    Bukkit.getScheduler().runTaskLater(Quests.get(),
        () -> checkInventory((Player) event.getWhoClicked()), 1L);
  }

  private void checkInventory(Player player) {
    QPlayer qPlayer = QuestsAPI.getPlayerManager().getPlayer(player.getUniqueId(), true);
    if (qPlayer == null) {
      return;
    }

    QuestProgressFile questProgressFile = qPlayer.getQuestProgressFile();

    for (Quest quest : super.getRegisteredQuests()) {
      if (questProgressFile.hasStartedQuest(quest)) {
        QuestProgress questProgress = questProgressFile.getQuestProgress(quest);

        for (Task task : quest.getTasksOfType(super.getType())) {
          TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());

          if (taskProgress.isCompleted()) {
            continue;
          }

          Material material;
          int amount = (int) task.getConfigValue(AMOUNT_KEY);
          Object configBlock = task.getConfigValue(ITEM_KEY);
          Object remove = task.getConfigValue("remove-items-when-complete");

          material = Material.getMaterial(String.valueOf(configBlock).toUpperCase());

          if (Objects.isNull(material)) {
            continue;
          }

          ItemStack is = new ItemStack(material, 1);

          if (Objects.nonNull(task.getConfigValue("update-progress"))
              && (Boolean) task.getConfigValue("update-progress")) {
            taskProgress.setProgress(getAmount(player, is, amount));
          }

          if (player.getInventory().containsAtLeast(is, amount)) {
            is.setAmount(amount);
            taskProgress.setCompleted(true);

            if (Objects.nonNull(remove) && ((Boolean) remove)) {
              player.getInventory().removeItem(is);
            }
          }
        }
      }
    }
  }

  private int getAmount(Player player, ItemStack is, int max) {
    if (is == null) {
      return 0;
    }
    int amount = 0;
    for (int i = 0; i < 36; i++) {
      ItemStack slot = player.getInventory().getItem(i);
      if (slot == null || !slot.isSimilar(is))
        continue;
      amount += slot.getAmount();
    }
    return Math.min(amount, max);
  }

}

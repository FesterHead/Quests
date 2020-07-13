// package com.leonardobishop.quests.quests.tasktypes.types;

// import java.util.ArrayList;
// import java.util.List;

// import com.leonardobishop.quests.Quests;
// import com.leonardobishop.quests.api.QuestsAPI;
// import com.leonardobishop.quests.player.QPlayer;
// import com.leonardobishop.quests.player.questprogressfile.QuestProgress;
// import com.leonardobishop.quests.player.questprogressfile.QuestProgressFile;
// import com.leonardobishop.quests.player.questprogressfile.TaskProgress;
// import com.leonardobishop.quests.quests.Quest;
// import com.leonardobishop.quests.quests.Task;
// import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
// import com.leonardobishop.quests.quests.tasktypes.TaskType;

// import org.bukkit.Bukkit;
// import org.bukkit.Material;
// import org.bukkit.inventory.meta.Damageable;
// import org.bukkit.inventory.meta.ItemMeta;
// import org.bukkit.entity.Player;
// import org.bukkit.event.EventHandler;
// import org.bukkit.event.EventPriority;
// import org.bukkit.event.entity.EntityPickupItemEvent;
// import org.bukkit.event.inventory.InventoryClickEvent;
// import org.bukkit.inventory.ItemStack;

// public final class InventoryTaskType extends TaskType {

// private List<ConfigValue> creatorConfigValues = new ArrayList<>();

// public InventoryTaskType() {
// super("inventory", "LMBishop", "Obtain a set of items.");
// this.creatorConfigValues.add(new ConfigValue(AMOUNT_KEY, true, "Amount of item to retrieve."));
// this.creatorConfigValues.add(new ConfigValue(ITEM_KEY, true, "Name or ID of item."));
// this.creatorConfigValues.add(new ConfigValue("remove-items-when-complete", true,
// "Take the items away from the player on completion (true/false, " + "default = false)."));
// this.creatorConfigValues.add(new ConfigValue("update-progress", true,
// "Update the displayed progress (if this causes lag then disable it)."));
// this.creatorConfigValues.add(new ConfigValue(PRESENT_KEY, false, "Present-tense action verb."));
// this.creatorConfigValues.add(new ConfigValue(PAST_KEY, false, "Past-tense action verb."));
// }

// @Override
// public List<ConfigValue> getCreatorConfigValues() {
// return creatorConfigValues;
// }

// @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
// public void onItemPickup(EntityPickupItemEvent event) {
// Bukkit.getScheduler().runTaskLater(Quests.get(),
// () -> this.checkInventory((Player) event.getEntity()), 1L);
// }

// @EventHandler(priority = EventPriority.MONITOR/* , ignoreCancelled = true */)
// public void onInventoryClick(InventoryClickEvent event) {
// // Still some work to do as it doesn't really work
// Bukkit.getScheduler().runTaskLater(Quests.get(),
// () -> checkInventory((Player) event.getWhoClicked()), 1L);
// }

// private void checkInventory(Player player) {
// QPlayer qPlayer = QuestsAPI.getPlayerManager().getPlayer(player.getUniqueId(), true);
// if (qPlayer == null) {
// return;
// }

// QuestProgressFile questProgressFile = qPlayer.getQuestProgressFile();

// for (Quest quest : super.getRegisteredQuests()) {
// if (questProgressFile.hasStartedQuest(quest)) {
// QuestProgress questProgress = questProgressFile.getQuestProgress(quest);

// for (Task task : quest.getTasksOfType(super.getType())) {
// TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());

// if (taskProgress.isCompleted()) {
// continue;
// }

// Material material;
// int amount = (int) task.getConfigValue("amount");
// Object configBlock = task.getConfigValue("item");
// Object configData = task.getConfigValue("data");
// Object remove = task.getConfigValue("remove-items-when-complete");

// material = Material.getMaterial(String.valueOf(configBlock).toUpperCase());

// if (material == null) {
// continue;
// }
// ItemStack is;
// if (configData != null) {
// is = new ItemStack(material, 1);
// Damageable im = (Damageable) is.getItemMeta();
// im.setDamage(((Integer) configData).shortValue());
// is.setItemMeta((ItemMeta) im);
// } else {
// is = new ItemStack(material, 1);
// }

// if (task.getConfigValue("update-progress") != null
// && (Boolean) task.getConfigValue("update-progress")) {
// taskProgress.setProgress(getAmount(player, is, amount));
// }

// if (player.getInventory().containsAtLeast(is, amount)) {
// is.setAmount(amount);
// taskProgress.setCompleted(true);

// if (remove != null && ((Boolean) remove)) {
// player.getInventory().removeItem(is);
// }
// }
// }
// }
// }
// }

// private int getAmount(Player player, ItemStack is, int max) {
// if (is == null) {
// return 0;
// }
// int amount = 0;
// for (int i = 0; i < 36; i++) {
// ItemStack slot = player.getInventory().getItem(i);
// if (slot == null || !slot.isSimilar(is))
// continue;
// amount += slot.getAmount();
// }
// return Math.min(amount, max);
// }

// }

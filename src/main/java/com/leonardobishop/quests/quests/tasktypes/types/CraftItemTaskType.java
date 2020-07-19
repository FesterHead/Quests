package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public final class CraftItemTaskType extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public CraftItemTaskType() {
    super("craftitem", "FesterHead", "Craft a set amount of a specific material.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The number of items to craft."));
    this.creatorConfigValues
        .add(new ConfigValue(ITEM_KEY, false, "If supplied, the specific item to craft."));
    this.creatorConfigValues
        .add(new ConfigValue(PRESENT_KEY, false, "Optional present-tense action verb."));
    this.creatorConfigValues
        .add(new ConfigValue(PAST_KEY, false, "Optional past-tense action verb."));
  }

  @Override
  public List<ConfigValue> getCreatorConfigValues() {
    return creatorConfigValues;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onCraftItem(CraftItemEvent event) {
    Material incoming = event.getRecipe().getResult().getType();
    processObject(incoming, event.getWhoClicked().getUniqueId(),
        getAmountCraftItem(incoming, event));
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

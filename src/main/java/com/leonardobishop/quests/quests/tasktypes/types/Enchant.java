package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;

public final class Enchant extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public Enchant() {
    super("enchant", "toasted, FesterHead", "Enchant items.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The number of items to enchant."));
    this.creatorConfigValues
        .add(new ConfigValue(ITEM_KEY, false, "If supplied, the specific item to enchant."));
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
  public void onEnchant(EnchantItemEvent event) {
    if (Objects.isNull(event.getEnchanter()) || !(event.getEnchanter() instanceof Player)) {
      return;
    }
    processObject(event.getItem().getType(), event.getEnchanter().getUniqueId(), 1);
  }
}

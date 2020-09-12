package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.FurnaceExtractEvent;

public final class Furnace extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public Furnace() {
    super("furnace", "FesterHead", "Extract items from a furnace, blast furnace, or smoker.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The number of items to extract."));
    this.creatorConfigValues
        .add(new ConfigValue(ITEM_KEY, false, "If supplied, a specific item to extract."));
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
  public void onFurnaceExtract(FurnaceExtractEvent event) {
    if (Objects.isNull(event.getPlayer()) || !(event.getPlayer() instanceof Player)) {
      return;
    }
    processObject(event, event.getItemType(), event.getPlayer().getUniqueId(),
        event.getItemAmount());
  }
}

package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public final class BlockPlaceTaskType extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public BlockPlaceTaskType() {
    super("blockplace", "LMBishop, FesterHead", "Place blocks.");
    this.creatorConfigValues.add(new ConfigValue(AMOUNT_KEY, true, "The number of blocks place."));
    this.creatorConfigValues
        .add(new ConfigValue(ITEM_KEY, false, "If supplied, the specific block to place."));
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
  public void onBlockPlace(BlockPlaceEvent event) {
    processMaterial(event.getBlock().getType(), event.getPlayer().getUniqueId(), 1);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockBreak(BlockBreakEvent event) {
    processMaterial(event.getBlock().getType(), event.getPlayer().getUniqueId(), -1);
  }
}

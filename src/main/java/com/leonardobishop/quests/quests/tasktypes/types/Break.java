package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public final class Break extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public Break() {
    super("break", "LMBishop, FesterHead", "Break blocks.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The number of blocks to break."));
    this.creatorConfigValues.add(new ConfigValue(CONTINUE_EVALUATING_KEY, false,
        "If supplied, will continue evaluating other break tasks."));
    this.creatorConfigValues
        .add(new ConfigValue(ITEM_KEY, false, "If supplied, the specific block to break."));
    this.creatorConfigValues.add(new ConfigValue(REVERSE_KEY, false,
        "If supplied, the same type of block placed or broken will reverse progression."));
    this.creatorConfigValues
        .add(new ConfigValue(PRESENT_KEY, false, "Optional present-tense action verb."));
    this.creatorConfigValues
        .add(new ConfigValue(PAST_KEY, false, "Optional past-tense action verb."));
    this.creatorConfigValues
        .add(new ConfigValue(WORLD_KEY, false, "Optional world where this task is valid."));
    this.creatorConfigValues.add(new ConfigValue(COREPROTECT_KEY, false,
        "Optional CoreProtect checking.  If player placed block no credit!"));
    this.creatorConfigValues.add(new ConfigValue(COREPROTECT_SECONDS_KEY, false,
        "Optional number of seconds to look in CoreProtect log.  Default is 3600 seconds = 1 hour."));
  }

  @Override
  public List<ConfigValue> getCreatorConfigValues() {
    return creatorConfigValues;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockBreak(BlockBreakEvent event) {
    if (Objects.isNull(event.getPlayer()) || !(event.getPlayer() instanceof Player)) {
      return;
    }
    processObject(event, event.getBlock().getType(), event.getPlayer().getUniqueId(), 1);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockPlace(BlockPlaceEvent event) {
    if (Objects.isNull(event.getPlayer()) || !(event.getPlayer() instanceof Player)) {
      return;
    }
    processObject(event, event.getBlock().getType(), event.getPlayer().getUniqueId(), -1);
  }
}

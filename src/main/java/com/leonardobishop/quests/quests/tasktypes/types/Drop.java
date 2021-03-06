package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public final class Drop extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public Drop() {
    super("drop", "FesterHead", "Collect block drops.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The number of block drops to collect."));
    this.creatorConfigValues.add(new ConfigValue(CONTINUE_EVALUATING_KEY, false,
        "If supplied, will continue evaluating other drop tasks."));
    this.creatorConfigValues
        .add(new ConfigValue(ITEM_KEY, false, "If supplied, the specific block drop to collect."));
    this.creatorConfigValues.add(new ConfigValue(REVERSE_KEY, false,
        "If supplied, the same type of block placed or broken will reverse progression."));
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
  public void onDrop(BlockDropItemEvent event) {
    if (Objects.isNull(event.getPlayer()) || !(event.getPlayer() instanceof Player)) {
      return;
    }
    for (Item item : event.getItems()) {
      Material incoming = item.getItemStack().getType();
      int count = item.getItemStack().getAmount();
      processObject(event, incoming, event.getPlayer().getUniqueId(), count);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlace(BlockPlaceEvent event) {
    if (Objects.isNull(event.getPlayer()) || !(event.getPlayer() instanceof Player)) {
      return;
    }
    Material incoming = event.getItemInHand().getType();
    processObject(event, incoming, event.getPlayer().getUniqueId(), -1);
  }
}

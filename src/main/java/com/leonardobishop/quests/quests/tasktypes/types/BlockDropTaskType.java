package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import com.leonardobishop.quests.api.QuestsAPI;
import com.leonardobishop.quests.player.QPlayer;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockDropItemEvent;

public final class BlockDropTaskType extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public BlockDropTaskType() {
    super("blockdrop", "FesterHead", "Collect block drops.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The number of block drops to collect."));
    this.creatorConfigValues
        .add(new ConfigValue(ITEM_KEY, false, "If supplied, the specific block drop to collect."));
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
  public void onHarvest(BlockDropItemEvent event) {

    QPlayer qp = QuestsAPI.getPlayerManager().getPlayer(event.getPlayer().getUniqueId(), true);

    for (Item item : event.getItems()) {

      Material incoming = item.getItemStack().getType();
      int count = item.getItemStack().getAmount();

      processMaterial(incoming, qp, count);
    }
  }
}

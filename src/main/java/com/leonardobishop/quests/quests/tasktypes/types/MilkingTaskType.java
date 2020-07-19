package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.Material;
import org.bukkit.entity.Cow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public final class MilkingTaskType extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public MilkingTaskType() {
    super("milking", "LMBishop, FesterHead", "Milk cows.");
    this.creatorConfigValues.add(new ConfigValue(AMOUNT_KEY, true, "The number of cows to milk."));
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
  public void onMilk(PlayerInteractEntityEvent event) {
    if (!(event.getRightClicked() instanceof Cow)
        || (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.BUCKET)) {
      return;
    }
    processEntity(event.getRightClicked().getType(), event.getPlayer().getUniqueId(), 1);
  }

}

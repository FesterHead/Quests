package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerShearEntityEvent;

public final class ShearTaskType extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public ShearTaskType() {
    super("shear", "LMBishop, FesterHead", "Shear sheep.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The number of sheep to shear."));
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
  public void onShear(PlayerShearEntityEvent event) {
    if (event.getEntity() instanceof Sheep) {
      processObject(event.getEntity().getType(), event.getPlayer().getUniqueId(), 1);
    }
  }
}

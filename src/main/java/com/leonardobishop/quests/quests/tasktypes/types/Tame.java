package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTameEvent;

public final class Tame extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public Tame() {
    super("tame", "FesterHead", "Tame animals.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The number of animals to tame."));
    this.creatorConfigValues
        .add(new ConfigValue(ITEM_KEY, false, "If present, the specific animal to tame."));
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
  public void onTame(EntityTameEvent event) {
    if (Objects.isNull(event.getOwner()) || !(event.getOwner() instanceof Player)) {
      return;
    }
    processObject(event, event.getEntity().getType(), event.getOwner().getUniqueId(), 1);
  }
}

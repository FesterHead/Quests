package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

public final class Death extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public Death() {
    super("death", "LMBishop, FesterHead", "Kill entities.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The number of entites to kill."));
    this.creatorConfigValues
        .add(new ConfigValue(ITEM_KEY, false, "If supplied, the specific entity to kill."));
    this.creatorConfigValues.add(new ConfigValue(PRESENT_KEY, false, "Present-tense action verb."));
    this.creatorConfigValues.add(new ConfigValue(PAST_KEY, false, "Past-tense action verb."));
    this.creatorConfigValues
        .add(new ConfigValue(WORLD_KEY, false, "Optional world where this task is valid."));
  }

  @Override
  public List<ConfigValue> getCreatorConfigValues() {
    return creatorConfigValues;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onMobKill(EntityDeathEvent event) {
    Player killer = event.getEntity().getKiller();
    Entity mob = event.getEntity();
    if (Objects.isNull(killer) || !(killer instanceof Player) || Objects.isNull(mob)
        || mob instanceof Player) {
      return;
    }
    processObject(event, event.getEntity().getType(), killer.getUniqueId(), 1);
  }

}

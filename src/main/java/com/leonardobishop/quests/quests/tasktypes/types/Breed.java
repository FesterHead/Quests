package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityBreedEvent;

public final class Breed extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public Breed() {
    super("breed", "FesterHead", "Breed animals.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The number of animals to breed."));
    this.creatorConfigValues
        .add(new ConfigValue(ITEM_KEY, false, "If supplied, the specific animal to breed."));
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
  public void onBreed(EntityBreedEvent event) {
    if (Objects.isNull(event.getBreeder()) || !(event.getBreeder() instanceof Player)) {
      return;
    }
    processObject(event, event.getEntity().getType(), event.getBreeder().getUniqueId(), 1);
  }
}

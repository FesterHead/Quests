package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import com.leonardobishop.quests.api.QuestsAPI;
import com.leonardobishop.quests.player.QPlayer;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityBreedEvent;

public final class EntityBreedTaskType extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public EntityBreedTaskType() {
    super("breed", "FesterHead", "Breed animals.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The number of animals to breed."));
    this.creatorConfigValues
        .add(new ConfigValue(ITEM_KEY, false, "If supplied, the specific animal to breed."));
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
  public void onBreed(EntityBreedEvent event) {

    QPlayer qp = QuestsAPI.getPlayerManager().getPlayer(event.getBreeder().getUniqueId(), true);
    processEntity(event.getEntity().getType(), qp, 1);
  }
}

package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import com.leonardobishop.quests.api.QuestsAPI;
import com.leonardobishop.quests.player.QPlayer;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTameEvent;

public final class EntityTameTaskType extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public EntityTameTaskType() {
    super("tame", "FesterHead", "Tame animals.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The number of animals to tame."));
    this.creatorConfigValues
        .add(new ConfigValue(ITEM_KEY, false, "If present, the specific animal to tame."));
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
  public void onTame(EntityTameEvent event) {
    if (!(event.getOwner() instanceof Player)) {
      return;
    }

    QPlayer qp = QuestsAPI.getPlayerManager().getPlayer(event.getOwner().getUniqueId(), true);
    processEntity(event.getEntity().getType(), qp, 1);
  }
}

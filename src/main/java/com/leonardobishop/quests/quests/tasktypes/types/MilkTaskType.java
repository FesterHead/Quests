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

public final class MilkTaskType extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public MilkTaskType() {
    super("milk", "LMBishop, FesterHead", "Milk cows.");
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
    Material itemInMainHand = event.getPlayer().getInventory().getItemInMainHand().getType();
    if ((event.getRightClicked() instanceof Cow) && (itemInMainHand.equals(Material.BUCKET))) {
      processEntity(event.getRightClicked().getType(), event.getPlayer().getUniqueId(), 1);
    }
  }
}

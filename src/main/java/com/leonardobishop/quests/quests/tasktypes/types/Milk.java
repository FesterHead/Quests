package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.Material;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public final class Milk extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public Milk() {
    super("milk", "LMBishop, FesterHead", "Milk cows.");
    this.creatorConfigValues.add(new ConfigValue(AMOUNT_KEY, true, "The number of cows to milk."));
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
  public void onMilk(PlayerInteractEntityEvent event) {
    if (Objects.isNull(event.getPlayer()) || !(event.getPlayer() instanceof Player)) {
      return;
    }
    Material itemInMainHand = event.getPlayer().getInventory().getItemInMainHand().getType();
    if ((event.getRightClicked() instanceof Cow) && (itemInMainHand.equals(Material.BUCKET))) {
      processObject(event, event.getRightClicked().getType(), event.getPlayer().getUniqueId(), 1);
    }
  }
}

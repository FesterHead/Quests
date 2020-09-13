package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public final class Brew extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();
  private HashMap<Location, UUID> brewingStands = new HashMap<>();

  public Brew() {
    super("brew", "LMBishop, FesterHead", "Brew potions.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The number of potions to brew."));
    this.creatorConfigValues
        .add(new ConfigValue(ITEM_KEY, false, "If present, the specific potion type to brew."));
    this.creatorConfigValues.add(new ConfigValue(PRESENT_KEY, true, "Present-tense action verb."));
    this.creatorConfigValues.add(new ConfigValue(PAST_KEY, true, "Past-tense action verb."));
    this.creatorConfigValues
        .add(new ConfigValue(WORLD_KEY, false, "Optional world where this task is valid."));
  }

  @Override
  public List<ConfigValue> getCreatorConfigValues() {
    return creatorConfigValues;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onInteract(PlayerInteractEvent event) {
    if (Objects.equals(event.getAction(), Action.RIGHT_CLICK_BLOCK)) {
      if (Objects.equals(event.getClickedBlock().getType(), Material.BREWING_STAND)) {
        brewingStands.put(event.getClickedBlock().getLocation(), event.getPlayer().getUniqueId());
      }
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBrew(BrewEvent event) {
    UUID playerUUID = brewingStands.get(event.getBlock().getLocation());
    if (Objects.nonNull(playerUUID)) {
      Player player = Bukkit.getPlayer(playerUUID);
      if (Objects.isNull(player) || !(player instanceof Player)) {
        return;
      }

      new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
          PotionMeta potionMeta = (PotionMeta) event.getContents().getItem(1).getItemMeta();
          PotionType potionType = potionMeta.getBasePotionData().getType(); // e.g. STRENGTH

          processObject(event, potionType, playerUUID, 1);
        }
      }, 1);
    }
  }
}

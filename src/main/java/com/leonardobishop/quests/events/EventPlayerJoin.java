package com.leonardobishop.quests.events;

import java.util.UUID;

import com.leonardobishop.quests.Quests;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventPlayerJoin implements Listener {

  private Quests plugin;

  public EventPlayerJoin(Quests plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onEvent(PlayerJoinEvent event) {
    UUID playerUuid = event.getPlayer().getUniqueId();
    plugin.getPlayerManager().loadPlayer(playerUuid, false);
  }

}

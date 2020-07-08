package com.leonardobishop.quests.events;

import java.util.UUID;

import com.leonardobishop.quests.Quests;
import com.leonardobishop.quests.obj.Messages;

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
    if (plugin.getDescription().getVersion().contains("beta")
        && event.getPlayer().hasPermission("quests.admin")) {
      event.getPlayer().sendMessage(Messages.BETA_REMINDER.getMessage());
    }
  }

}

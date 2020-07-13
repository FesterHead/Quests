package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import com.leonardobishop.quests.api.QuestsAPI;
import com.leonardobishop.quests.player.QPlayer;
import com.leonardobishop.quests.player.questprogressfile.QuestProgressFile;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;

public final class PlayerFishTaskType extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public PlayerFishTaskType() {
    super("fish", "FesterHead", "Go fish.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The number of things to catch."));
    this.creatorConfigValues
        .add(new ConfigValue(ITEM_KEY, false, "If supplied, the item to catch."));
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
  public void onFishCaught(PlayerFishEvent event) {
    if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) {
      return;
    }

    QPlayer qp = QuestsAPI.getPlayerManager().getPlayer(event.getPlayer().getUniqueId(), true);
    QuestProgressFile qpf = qp.getQuestProgressFile();

    Material incoming = ((Item) event.getCaught()).getItemStack().getType();
    int count = ((Item) event.getCaught()).getItemStack().getAmount();

    processMaterial(incoming, qp, qpf, count);
  }
}

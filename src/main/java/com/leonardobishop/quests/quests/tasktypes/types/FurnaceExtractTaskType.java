package com.leonardobishop.quests.quests.tasktypes.types;

import java.util.ArrayList;
import java.util.List;
import com.leonardobishop.quests.api.QuestsAPI;
import com.leonardobishop.quests.player.QPlayer;
import com.leonardobishop.quests.player.questprogressfile.QuestProgressFile;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.FurnaceExtractEvent;

public final class FurnaceExtractTaskType extends TaskType {

  private List<ConfigValue> creatorConfigValues = new ArrayList<>();

  public FurnaceExtractTaskType() {
    super("furnaceextract", "FesterHead",
        "Extract items from a furnace, blast furnace, or smoker.");
    this.creatorConfigValues
        .add(new ConfigValue(AMOUNT_KEY, true, "The number of items to extract."));
    this.creatorConfigValues
        .add(new ConfigValue(ITEM_KEY, false, "If supplied, a specific item to extract."));
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
  public void onFurnaceExtract(FurnaceExtractEvent event) {

    QPlayer qp = QuestsAPI.getPlayerManager().getPlayer(event.getPlayer().getUniqueId(), true);
    QuestProgressFile qpf = qp.getQuestProgressFile();

    processMaterial(event.getItemType(), qp, qpf, event.getItemAmount());
  }
}

package com.leonardobishop.quests.api;

import java.util.List;
import java.util.Objects;
import com.leonardobishop.quests.QuestsLogger;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.coreprotect.CoreProtectAPI.ParseResult;

public class QuestsCoreProtectAPI {

  private CoreProtectAPI coreProtectAPI = null;
  private QuestsLogger questLogger = QuestsAPI.getQuestManager().getPlugin().getQuestsLogger();

  public QuestsCoreProtectAPI() {
  }

  public void setupCoreProtect() {
    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CoreProtect");

    // Check that CoreProtect is loaded
    if (plugin == null || !(plugin instanceof CoreProtect)) {
      return;
    }

    // Check that the API is enabled
    CoreProtectAPI coreProtect = ((CoreProtect) plugin).getAPI();
    if (coreProtect.isEnabled() == false) {
      return;
    }

    // Check that a compatible version of the API is loaded
    if (coreProtect.APIVersion() < 6) {
      return;
    }

    coreProtectAPI = coreProtect;
  }

  public CoreProtectAPI getCoreProtectAPI() {
    return coreProtectAPI;
  }

  public boolean nonNull() {
    return Objects.nonNull(coreProtectAPI);
  }

  public boolean isPlayerBlock(Block block, int seconds) {
    if (Objects.nonNull(coreProtectAPI)) {
      List<String[]> lookup = coreProtectAPI.blockLookup(block, seconds);
      if (Objects.nonNull(lookup) && !lookup.isEmpty()) {
        questLogger.debug("  CoreProtect check:");

        // Look at the first index of the block from the lookup
        String[] result = lookup.get(0);
        ParseResult parseResult = coreProtectAPI.parseResult(result);
        String player = parseResult.getPlayer();
        boolean isRolledBack = parseResult.isRolledBack();
        String actionString = parseResult.getActionString();
        questLogger.debug("    Block placed by: §8" + player);
        questLogger.debug("  Block rolled back: §8" + isRolledBack);
        questLogger.debug("       Block action: §8" + actionString);

        if (!player.isEmpty() && !isRolledBack && actionString.equals("Placement")) {
          return true;
        }
      }
      questLogger.debug(" CoreProtect passed!");
    }
    return false;
  }
}

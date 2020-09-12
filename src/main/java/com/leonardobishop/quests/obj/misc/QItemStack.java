package com.leonardobishop.quests.obj.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.leonardobishop.quests.QuestsLogger;
import com.leonardobishop.quests.api.QuestsAPI;
import com.leonardobishop.quests.player.questprogressfile.QuestProgress;
import com.leonardobishop.quests.player.questprogressfile.QuestProgressFile;
import com.leonardobishop.quests.quests.Quest;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class QItemStack {

  private String name;
  private List<String> loreNormal;
  private List<String> loreStarted;
  private ItemStack startingItemStack;

  private QuestsLogger questLogger = QuestsAPI.getQuestManager().getPlugin().getQuestsLogger();

  public QItemStack(String name, List<String> loreNormal, List<String> loreStarted,
      ItemStack startingItemStack) {
    this.name = name;
    this.loreNormal = loreNormal;
    this.loreStarted = loreStarted;
    this.startingItemStack = startingItemStack;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getLoreNormal() {
    return loreNormal;
  }

  public void setLoreNormal(List<String> loreNormal) {
    this.loreNormal = loreNormal;
  }

  public List<String> getLoreStarted() {
    return loreStarted;
  }

  public void setLoreStarted(List<String> loreStarted) {
    this.loreStarted = loreStarted;
  }

  public ItemStack getStartingItemStack() {
    return startingItemStack;
  }

  public void setStartingItemStack(ItemStack startingItemStack) {
    this.startingItemStack = startingItemStack;
  }

  public ItemStack toItemStack(Quest quest, QuestProgressFile questProgressFile,
      QuestProgress questProgress) {
    ItemStack is = new ItemStack(startingItemStack);
    ItemMeta ism = is.getItemMeta();
    ism.setDisplayName(name);
    List<String> formattedLore = new ArrayList<>();
    List<String> tempLore = new ArrayList<>(loreNormal);
    if (questProgressFile.hasStartedQuest(quest)) {
      tempLore.addAll(loreStarted);
      ism.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
      try {
        ism.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ism.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
      } catch (Exception ignored) {

      }
    }
    if (questProgress != null) {
      for (String s : tempLore) {
        Matcher m = Pattern.compile("\\{([^}]+)}").matcher(s);
        while (m.find()) {
          String[] parts = m.group(1).split(":");
          if (parts.length > 1) {
            if (questProgress.getTaskProgress(parts[0]) == null) {
              continue;
            }
            String replacement = "";
            if (Objects
                .nonNull(quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId()))) {
              int tempProgress = 0;
              int tempAmount = 0;
              switch (parts[1]) {
                case "progress":
                  if (Objects.nonNull(questProgress.getTaskProgress(parts[0]).getProgress())) {
                    try {
                      tempProgress = Integer.parseInt(
                          String.valueOf(questProgress.getTaskProgress(parts[0]).getProgress()));
                    } catch (NumberFormatException ex) {
                    }
                  }
                  if (Objects
                      .nonNull(quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId())
                          .getConfigValue("amount"))) {
                    try {
                      tempAmount = Integer.parseInt(String.valueOf(
                          quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId())
                              .getConfigValue("amount")));
                    } catch (NumberFormatException ex) {
                    }
                  }
                  replacement =
                      String.valueOf((tempProgress > tempAmount) ? tempAmount : tempProgress);
                  break;
                case "item":
                  if (Objects
                      .nonNull(quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId())
                          .getConfigValue("item"))) {
                    replacement = String
                        .valueOf(quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId())
                            .getConfigValue("item"));
                  } else {
                    questLogger.severe("Unable to determine 'item' for task: "
                        + questProgress.getTaskProgress(parts[0]).getTaskId());
                    replacement = "---null---";
                  }
                  break;
                case "amount":
                  if (Objects
                      .nonNull(quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId())
                          .getConfigValue("amount"))) {
                    replacement = String
                        .valueOf(quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId())
                            .getConfigValue("amount"));
                  } else {
                    questLogger.severe("Unable to determine 'amount' for task: "
                        + questProgress.getTaskProgress(parts[0]).getTaskId());
                    replacement = "---null---";
                  }
                  break;
                case "togo":
                  if (Objects.nonNull(questProgress.getTaskProgress(parts[0]).getProgress())) {
                    try {
                      tempProgress = Integer.parseInt(
                          String.valueOf(questProgress.getTaskProgress(parts[0]).getProgress()));
                    } catch (NumberFormatException ex) {
                    }
                  }
                  if (Objects
                      .nonNull(quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId())
                          .getConfigValue("amount"))) {
                    try {
                      tempAmount = Integer.parseInt(String.valueOf(
                          quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId())
                              .getConfigValue("amount")));
                    } catch (NumberFormatException ex) {
                    }
                  }
                  int tempReplacement = tempAmount - tempProgress;
                  replacement = String.valueOf((tempReplacement < 0) ? 0 : tempReplacement);
                  break;
                case "present":
                  if (Objects.nonNull(
                      quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId()))) {
                    replacement = String
                        .valueOf(quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId())
                            .getConfigValue("present"));
                  } else {
                    questLogger.severe("Unable to determine 'present' for task: "
                        + questProgress.getTaskProgress(parts[0]).getTaskId());
                    replacement = "---null---";
                  }
                  break;
                case "past":
                  if (Objects
                      .nonNull(quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId())
                          .getConfigValue("past"))) {
                    replacement = String
                        .valueOf(quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId())
                            .getConfigValue("past"));
                  } else {
                    questLogger.severe("Unable to determine 'past' for task: "
                        + questProgress.getTaskProgress(parts[0]).getTaskId());
                    replacement = "---null---";
                  }
                  break;
                case "complete":
                  s = s.replace("{" + m.group(1) + "}",
                      String.valueOf(questProgress.getTaskProgress(parts[0]).isCompleted()));
                  break;
              }
            } else {
              questLogger.severe("Unable to find task id " + parts[0] + " in your quest "
                  + quest.getId() + ".yml file!");
              replacement = "---null---";
            }
            s = s.replace("{" + m.group(1) + "}", replacement);
          }
        }
        formattedLore.add(s);
      }
    }
    ism.setLore(formattedLore);
    is.setItemMeta(ism);
    return is;
  }
}

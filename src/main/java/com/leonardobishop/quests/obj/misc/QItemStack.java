package com.leonardobishop.quests.obj.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  public QItemStack(String name, List<String> loreNormal, List<String> loreStarted, ItemStack startingItemStack) {
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

  public ItemStack toItemStack(Quest quest, QuestProgressFile questProgressFile, QuestProgress questProgress) {
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
            String tempString = "";
            switch (parts[1]) {
            case "progress":
              tempString = String.valueOf(questProgress.getTaskProgress(parts[0]).getProgress());
              s = s.replace("{" + m.group(1) + "}", (tempString.equals("null") ? String.valueOf(0) : tempString));
              break;

            case "item":
              tempString = String
                  .valueOf(quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId()).getConfigValue("item"));
              tempString = tempString.replaceAll("_", " ").toLowerCase();
              s = s.replace("{" + m.group(1) + "}", (tempString.equals("null") ? String.valueOf(0) : tempString));
              break;

            case "amount":
              tempString = String
                  .valueOf(quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId()).getConfigValue("amount"));
              s = s.replace("{" + m.group(1) + "}", (tempString.equals("null") ? String.valueOf(0) : tempString));
              break;
            case "present":
              tempString = String.valueOf(
                  quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId()).getConfigValue("present"));
              s = s.replace("{" + m.group(1) + "}", (tempString.equals("null") ? String.valueOf(0) : tempString));
              break;
            case "past":
              tempString = String
                  .valueOf(quest.getTask(questProgress.getTaskProgress(parts[0]).getTaskId()).getConfigValue("past"));
              s = s.replace("{" + m.group(1) + "}", (tempString.equals("null") ? String.valueOf(0) : tempString));
              break;
            case "complete":
              s = s.replace("{" + m.group(1) + "}",
                  String.valueOf(questProgress.getTaskProgress(parts[0]).isCompleted()));
              break;
            }
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

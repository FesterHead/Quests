package com.leonardobishop.quests.obj.misc;

import com.leonardobishop.quests.player.QPlayer;
import com.leonardobishop.quests.quests.Quest;
import com.leonardobishop.quests.obj.Options;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QMenuDaily implements QMenu {

  private final HashMap<Integer, String> slotsToQuestIds = new HashMap<>();
  // private int backButtonLocation = -1;
  // private boolean backButtonEnabled = true;
  private final QMenuCategory superMenu;
  private String categoryName;
  private final int pageSize = 45;
  private final QPlayer owner;

  public QMenuDaily(QPlayer owner, QMenuCategory superMenu) {
    this.owner = owner;
    this.superMenu = superMenu;
  }

  public void populate(List<Quest> quests) {
    int slot = 11;
    for (Quest quest : quests) {
      slotsToQuestIds.put(slot, quest.getId());
      slot++;
      if (slot == 16) {
        break;
      }
    }
  }

  @Override
  public HashMap<Integer, String> getSlotsToMenu() {
    return slotsToQuestIds;
  }

  @Override
  public QPlayer getOwner() {
    return owner;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public Inventory toInventory(int page) {
    // int pageMin = pageSize * (page - 1);
    // int pageMax = pageSize * page;
    String title = Options.GUITITLE_DAILY_QUESTS.toString();
    return Bukkit.createInventory(null, 27, title);
  }

  public ItemStack replaceItemStack(ItemStack is, Map<String, String> placeholders) {
    ItemStack newItemStack = is.clone();
    List<String> lore = newItemStack.getItemMeta().getLore();
    List<String> newLore = new ArrayList<>();
    for (String s : lore) {
      for (Map.Entry<String, String> entry : placeholders.entrySet()) {
        s = s.replace(entry.getKey(), entry.getValue());
      }
      newLore.add(s);
    }
    ItemMeta ism = newItemStack.getItemMeta();
    ism.setLore(newLore);
    newItemStack.setItemMeta(ism);
    return newItemStack;
  }

  // Implement too
  public QMenuCategory getSuperMenu() {
    return this.superMenu;
  }

  public int getPageSize() {
    return this.pageSize;
  }
}

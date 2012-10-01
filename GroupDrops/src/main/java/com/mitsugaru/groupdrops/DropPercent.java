package com.mitsugaru.groupdrops;

import java.util.Random;

import org.bukkit.inventory.ItemStack;

public class DropPercent {
   private int itemId = 1;
   private short durability = 0;
   private double percent = 0;
   private final Random r = new Random(System.currentTimeMillis());

   public DropPercent(int item, short dur, double percent) {
      itemId = item;
      durability = dur;
      this.percent = percent;
   }

   private boolean dropOccurs() {
      if(percent <= 0) {
         return false;
      } else if(percent % 100 == 0) {
         return true;
      }
      return ((percent % 100) >= r.nextDouble() * 100);
   }

   public ItemStack getItemByChance() {
      final ItemStack item = new ItemStack(itemId, 1, durability);
      final boolean drop = dropOccurs();
      if(percent <= 0) {
         return null;
      } else if(percent >= 100 && (percent % 100 != 0) && drop) {
         item.setAmount((int) (percent / 100) + 1);
      } else if(percent >= 100 && !drop) {
         item.setAmount((int) (percent / 100));
      } else if(percent >= 100 && drop) {
         item.setAmount((int) (percent / 100));
      } else if(!drop) {
         return null;
      }
      return item;
   }
}

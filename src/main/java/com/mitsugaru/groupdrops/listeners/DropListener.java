package com.mitsugaru.groupdrops.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.mitsugaru.groupdrops.DropPercent;
import com.mitsugaru.groupdrops.GroupDrops;
import com.mitsugaru.groupdrops.config.RootConfigNode;
import com.mitsugaru.groupdrops.permissions.PermissionHandler;
import com.mitsugaru.groupdrops.permissions.PermissionNode;

/**
 * Listener for item drops on block break.
 * 
 * @author Tokume
 * 
 */
public class DropListener implements Listener {
   /**
    * Plugin.
    */
   private GroupDrops plugin;

   /**
    * Constructor.
    * 
    * @param plugin
    *           - Base plugin.
    */
   public DropListener(final GroupDrops plugin) {
      this.plugin = plugin;
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   public void onItemDrop(final BlockBreakEvent event) {
      if(event.isCancelled()) {
         return;
      }
      final Player player = event.getPlayer();
      // Check if they're ignored.
      if(PermissionHandler.has(player, PermissionNode.IGNORE)) {
         return;
      }
      final Block block = event.getBlock();
      final Location location = block.getLocation();
      final int id = block.getTypeId();
      final short durability = Short.valueOf(block.getData() + "");
      if(plugin.getRootConfig().getBoolean(RootConfigNode.DEBUG_EVENTS)) {
         plugin.getLogger().info("Break: " + id + " & " + durability);
      }
      final List<DropPercent> extras = new ArrayList<DropPercent>();
      // Grab all groups for the specified item and grab all drop percentages
      // that the player is applied to.
      final List<String> appliedGroups = plugin.getDropConfig().getGroups(id,
            durability);
      if(appliedGroups.isEmpty()) {
         return;
      }
      // Iterate through groups.
      boolean clear = false;
      for(final String group : appliedGroups) {
         if(PermissionHandler.hasDropGroup(player, group)) {
            if(plugin.getDropConfig().clearDropsOfItemAndGroup(id, durability,
                  group)) {
               clear = true;
            }
            extras.addAll(plugin.getDropConfig().getDropsOfItemAndGroup(id,
                  durability, group));
         }
      }
      //Clear block drop if necessary.
      if(clear) {
         event.setCancelled(true);
         block.setType(Material.AIR);
      }
      //Drop all valid items.
      for(final DropPercent extra : extras) {
         final ItemStack item = extra.getItemByChance();
         if(item != null) {
            if(plugin.getRootConfig().getBoolean(RootConfigNode.DEBUG_EVENTS)) {
               plugin.getLogger().info("Added extra drop: " + item.toString());
            }
            player.getWorld().dropItemNaturally(location, item);
         }
      }
   }
}

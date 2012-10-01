package com.mitsugaru.groupdrops.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.mitsugaru.groupdrops.GroupDrops;
import com.mitsugaru.groupdrops.permissions.PermissionHandler;
import com.mitsugaru.groupdrops.permissions.PermissionNode;

public class GroupDropsCommander implements CommandExecutor {

   private GroupDrops plugin;

   public GroupDropsCommander(GroupDrops plugin) {
      this.plugin = plugin;
   }

   @Override
   public boolean onCommand(CommandSender sender, Command cmd,
         String commandLabel, String[] args) {
      if(args.length == 0) {
         showHelp(sender);
      } else {
         try {
            final GDCommand com = GDCommand.valueOf(args[0].toUpperCase());
            switch(com) {
            case RELOAD: {
               if(!PermissionHandler.has(sender, PermissionNode.ADMIN_RELOAD)) {
                  sender.sendMessage(ChatColor.translateAlternateColorCodes(
                        '&', GroupDrops.TAG + " &4Lack permission: "
                              + PermissionNode.ADMIN_RELOAD.getNode()));
                  break;
               }
               plugin.saveAllConfigs();
               sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                     GroupDrops.TAG + " &aReloaded configs"));
            }
            default: {

            }
            }
         } catch(IllegalArgumentException e) {
            sender.sendMessage("Unknown command: " + args[0]);
         }
      }
      return true;
   }

   public void showHelp(CommandSender sender) {
      sender.sendMessage(ChatColor.GREEN + "=====" + ChatColor.WHITE
            + "GroupDrops" + ChatColor.GREEN + "=====");
      sender.sendMessage(ChatColor.AQUA + "/gd " + ChatColor.YELLOW + "reload "
            + ChatColor.GREEN + "- Reloads configuration files.");
   }
}

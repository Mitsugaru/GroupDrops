package com.mitsugaru.groupdrops;

import org.bukkit.plugin.java.JavaPlugin;

import com.mitsugaru.groupdrops.commands.GroupDropsCommander;
import com.mitsugaru.groupdrops.config.DropConfig;
import com.mitsugaru.groupdrops.config.RootConfig;
import com.mitsugaru.groupdrops.listeners.DropListener;

public class GroupDrops extends JavaPlugin {

   private RootConfig rootConfig;
   private DropConfig dropConfig;
   
   public static final String TAG = "[GD]";

   @Override
   public void onEnable() {
      // Get configs
      rootConfig = new RootConfig(this);
      dropConfig = new DropConfig(this);
      // register commander
      this.getCommand("gd").setExecutor(new GroupDropsCommander(this));
      // Register listener
      this.getServer().getPluginManager()
            .registerEvents(new DropListener(this), this);
   }

   @Override
   public void onDisable() {
      saveAllConfigs();
   }

   public void saveAllConfigs() {
      rootConfig.reload();
      rootConfig.save();
      dropConfig.reload();
      dropConfig.save();
   }

   /**
    * Gets the root config.
    * 
    * @return Root config.
    */
   public RootConfig getRootConfig() {
      return rootConfig;
   }

   public DropConfig getDropConfig() {
      return dropConfig;
   }

}

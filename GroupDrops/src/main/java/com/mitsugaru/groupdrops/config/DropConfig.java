package com.mitsugaru.groupdrops.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mitsugaru.groupdrops.DropPercent;
import com.mitsugaru.groupdrops.GroupDrops;

public class DropConfig {
   // Class variables
   private GroupDrops plugin;
   private File file;
   private YamlConfiguration config;

   public DropConfig(GroupDrops jplugin) {
      plugin = jplugin;
      file = new File(plugin.getDataFolder().getAbsolutePath() + "/drops.yml");
      if(!file.exists()) {
         try {
            file.createNewFile();
         } catch(IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not create drops.yml",
                  e);
         }
      }
      config = YamlConfiguration.loadConfiguration(file);
   }

   public List<String> getGroups(int itemId, short durability) {
      final List<String> groups = new ArrayList<String>();
      String path = itemId + "&" + durability;
      if(durability == 0) {
         path = "" + itemId;
      }
      if(config.contains(path)) {
         final ConfigurationSection section = config
               .getConfigurationSection(path);
         groups.addAll(section.getKeys(false));
      }
      if(plugin.getRootConfig().getBoolean(RootConfigNode.DEBUG_CONFIG)) {
         plugin.getLogger().info("Found groups: " + groups.toString());
      }
      return groups;
   }

   public boolean clearDropsOfItemAndGroup(int itemId, short durability,
         String group) {
      boolean clear = false;
      String path = itemId + "&" + durability + "." + group;
      if(durability == 0) {
         path = itemId + "." + group;
      }
      if(config.contains(path)) {
         clear = config.getBoolean(path + ".clear", false);
      }
      if(plugin.getRootConfig().getBoolean(RootConfigNode.DEBUG_CONFIG)) {
         plugin.getLogger().info("Clear for group " + group + ": " + clear);
      }
      return clear;
   }

   public List<DropPercent> getDropsOfItemAndGroup(int itemId,
         short durability, String group) {
      final List<DropPercent> drops = new ArrayList<DropPercent>();
      String path = itemId + "&" + durability + "." + group + ".drops";
      if(durability == 0) {
         path = itemId + "." + group + ".drops";
      }
      if(config.contains(path)) {
         final ConfigurationSection section = config
               .getConfigurationSection(path);
         for(Map.Entry<String, Object> entry : section.getValues(false)
               .entrySet()) {
            try {
               final String key = entry.getKey();
               double percent = 0;
               if(entry.getValue() instanceof Double) {
                  percent = Double.valueOf((Double) entry.getValue());
               } else if(entry.getValue() instanceof Integer) {
                  percent = Double.valueOf((Integer) entry.getValue());
               } else {
                  throw new ClassCastException();
               }
               if(key.contains("&")) {
                  final String[] split = key.split("&");
                  drops.add(new DropPercent(Integer.parseInt(split[0]), Short
                        .parseShort(split[1]), percent));
               } else {
                  drops.add(new DropPercent(Integer.parseInt(key), (short) 0,
                        percent));
               }
            } catch(NumberFormatException e) {
               plugin.getLogger()
                     .warning(
                           "Error in drops for: " + path + " with: "
                                 + entry.getKey());
               e.printStackTrace();
            } catch(ClassCastException e) {
               plugin.getLogger()
                     .warning(
                           "Error in drops for: " + path + " with: "
                                 + entry.getKey());
               e.printStackTrace();
            }
         }
      }
      return drops;
   }

   /**
    * Save the localization config.
    */
   public void save() {
      try {
         // Save the file
         config.save(file);
      } catch(IOException e1) {
         plugin.getLogger().warning(
               "File I/O Exception on saving localization config");
         e1.printStackTrace();
      }
   }

   /**
    * Reload the localization config.
    */
   public void reload() {
      try {
         config.load(file);
      } catch(FileNotFoundException e) {
         e.printStackTrace();
      } catch(IOException e) {
         e.printStackTrace();
      } catch(InvalidConfigurationException e) {
         e.printStackTrace();
      }
   }

   public void set(String path, Object o) {
      config.set(path, o);
      save();
   }

}

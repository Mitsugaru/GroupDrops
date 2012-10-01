package com.mitsugaru.groupdrops.config;

/**
 * Represents a configuration node from the config.yml.
 * 
 * @author Mitsugaru
 * 
 */
public enum RootConfigNode implements ConfigNode {
   /**
    * Debug.
    */
   DEBUG_EVENTS("debug.events", VarType.BOOLEAN, false),
   DEBUG_CONFIG("debug.config", VarType.BOOLEAN, false),
   /**
    * Version.
    */
   VERSION("version", VarType.STRING, "0.01");

   /**
    * Config path.
    */
   private String path;
   /**
    * Default value.
    */
   private Object def;
   /**
    * Variable type.
    */
   private VarType vartype;

   /**
    * Private constructor
    * 
    * @param path
    *           - Config path.
    * @param vartype
    *           - Variable type.
    * @param def
    *           - Default value.
    */
   private RootConfigNode(String path, VarType vartype, Object def) {
      this.path = path;
      this.vartype = vartype;
      this.def = def;
   }

   /**
    * Get the config path.
    * 
    * @return Config path.
    */
   public String getPath() {
      return this.path;
   }

   /**
    * Get the variable type.
    * 
    * @return Variable type.
    */
   public VarType getVarType() {
      return this.vartype;
   }

   /**
    * Get the default value.
    * 
    * @return Default value.
    */
   public Object getDefaultValue() {
      return this.def;
   }
}

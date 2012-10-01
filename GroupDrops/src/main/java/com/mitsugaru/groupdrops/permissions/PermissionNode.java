package com.mitsugaru.groupdrops.permissions;

/**
 * Static permission nodes.
 * 
 * @author Tokume
 * 
 */
public enum PermissionNode {
   IGNORE(".ignore"),
   /**
    * Admin
    */
   ADMIN_RELOAD(".admin.reload");

   public static final String prefix = "GroupDrops";
   private String node;

   private PermissionNode(String node) {
      this.node = prefix + node;
   }

   public String getNode() {
      return node;
   }
}

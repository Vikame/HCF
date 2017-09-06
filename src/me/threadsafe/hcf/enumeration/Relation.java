package me.threadsafe.hcf.enumeration;

import org.bukkit.ChatColor;

public enum Relation {

    NEUTRAL(true, ChatColor.WHITE), ENEMY(true, ChatColor.RED), ALLY(false, ChatColor.AQUA), MEMBER(false, ChatColor.GREEN);

    public boolean damageable;
    public ChatColor color;

    Relation(boolean damageable, ChatColor color){
        this.damageable = damageable;
        this.color = color;
    }

}

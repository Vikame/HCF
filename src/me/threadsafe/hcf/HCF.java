package me.threadsafe.hcf;

import me.threadsafe.hcf.api.*;
import me.threadsafe.hcf.backend.*;
import me.threadsafe.hcf.backend.listener.ClaimProtectionListener;
import me.threadsafe.hcf.commands.AdminFactionCommand;
import me.threadsafe.hcf.enumeration.Relation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.UUID;

public class HCF extends JavaPlugin {

    private static HCF instance;

    private FactionCache factionCache;
    private UserCache userCache;

    public void onEnable(){
        instance = this;

        this.factionCache = new HCFFactionCache();
        this.userCache = new HCFUserCache();

        YamlConfiguration users = ConfigurationHelper.create("users");
        Set<String> userKeys = users.getKeys(false);

        log("&eLoading " + userKeys.size() + " user" + (userKeys.size() != 1 ? "s" : "") + "...");

        for(String s : userKeys){
            userCache.create(UUID.fromString(s), users.getString(s));
        }

        log("&eFinished loading all users.");

        YamlConfiguration playerFactions = ConfigurationHelper.create("playerfactions");
        Set<String> playerFactionKeys = playerFactions.getKeys(false);

        log("&eLoading " + playerFactionKeys.size() + " player faction" + (playerFactionKeys.size() != 1 ? "s" : "") + "...");

        for(String s : playerFactionKeys){
            ConfigurationSection section = playerFactions.getConfigurationSection(s);

            PlayerFaction faction = new PlayerFaction(UUID.fromString(s), section.getString("tag"));
            faction.dtr = section.getDouble("dtr");
            faction.freeze = section.getLong("freeze");

            for(String str : section.getStringList("members")){
                faction.members.add(this.userCache.search(UUID.fromString(str)));
            }

            ConfigurationSection relations = section.getConfigurationSection("relations");
            for(String str : relations.getKeys(false)){
                faction.relations.put(UUID.fromString(str), Relation.valueOf(relations.getString(str)));
            }

            if(section.contains("claim")){
                ConfigurationSection sec = section.getConfigurationSection("claim");

                HCFClaim claim = new HCFClaim(sec.getInt("minx"), sec.getInt("minz"), sec.getInt("maxx"), sec.getInt("maxz"));

                if(sec.contains("home")){
                    ConfigurationSection home = sec.getConfigurationSection("home");

                    claim.home = new Location(claim.getWorld(), home.getInt("x"), home.getInt("y"), home.getInt("z"));
                }

                faction.claim = claim;
            }

            factionCache.getFactions().add(faction);
        }

        log("&eFinished loading all player factions.");

        YamlConfiguration systemFactions = ConfigurationHelper.create("systemfactions");
        Set<String> systemFactionKeys = systemFactions.getKeys(false);

        log("&eLoading " + systemFactionKeys.size() + " system faction" + (systemFactionKeys.size() != 1 ? "s" : "") + "...");

        for(String s : systemFactionKeys){
            ConfigurationSection section = systemFactions.getConfigurationSection(s);

            SystemFaction faction = new SystemFaction(UUID.fromString(s), section.getString("internal"), section.getString("tag"), section.getBoolean("safezone"), section.getBoolean("auto_created"));

            for(String str : section.getStringList("members")){
                faction.members.add(this.userCache.search(UUID.fromString(str)));
            }

            if(section.contains("claim")){
                ConfigurationSection sec = section.getConfigurationSection("claim");

                HCFClaim claim = new HCFClaim(sec.getInt("minx"), sec.getInt("minz"), sec.getInt("maxx"), sec.getInt("maxz"));

                if(sec.contains("home")){
                    ConfigurationSection home = sec.getConfigurationSection("home");

                    claim.home = new Location(claim.getWorld(), home.getInt("x"), home.getInt("y"), home.getInt("z"));
                }

                faction.claim = claim;
            }

            factionCache.getFactions().add(faction);
        }

        log("&eFinished loading all system factions.");

        log("&eAuto-creating needed factions...");
        int count = 0;

        if(factionCache.searchInternal("system_wilderness") == null){
            factionCache.create(UUID.randomUUID(), "system_wilderness", "&2Wilderness", false, true);
            count++;
        }
        if(factionCache.searchInternal("system_warzone") == null){
            factionCache.create(UUID.randomUUID(), "system_warzone", "&4Warzone", false, true);
            count++;
        }
        if(factionCache.searchInternal("system_spawn") == null){
            factionCache.create(UUID.randomUUID(), "system_spawn", "&aSpawn", true, true);
            count++;
        }

        log("&eAuto-created " + count + " faction" + (count != 1 ? "s" : "") + ".");

        Faction wild = factionCache.searchInternal("system_wilderness");
        if(wild != null && wild instanceof SystemFaction){
            factionCache.setWilderness((SystemFaction)wild);
        }else{
            Bukkit.shutdown();
            return;
        }

        int index = 0;

        for(Faction faction : factionCache.getFactions()){
            log("DEBUG {FACTION}", " [" + faction.getId().toString() + "] " + faction.getInternalName() + ": " + faction.getTag());

            if(faction instanceof SystemFaction){
                index++;
                log("DEBUG {LIST}", Messages.createListMessage(index, (SystemFaction)faction));
            }
        }

        Bukkit.getPluginManager().registerEvents(new ClaimProtectionListener(), this);

        getCommand("adminfaction").setExecutor(new AdminFactionCommand());
    }

    public void onDisable(){
        factionCache.save();
        userCache.save();
    }

    private void log(String extra, String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[HCF | " + extra + "] &f" + message));
    }

    private void log(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[HCF] &f" + message));
    }

    public FactionCache getFactionCache(){
        return factionCache;
    }

    public UserCache getUserCache(){
        return userCache;
    }

    public static HCF getInstance(){
        return instance;
    }

}

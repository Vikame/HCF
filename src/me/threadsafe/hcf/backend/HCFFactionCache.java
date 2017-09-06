package me.threadsafe.hcf.backend;

import me.threadsafe.hcf.api.ConfigurationHelper;
import me.threadsafe.hcf.api.Faction;
import me.threadsafe.hcf.api.FactionCache;
import me.threadsafe.hcf.api.User;
import me.threadsafe.hcf.enumeration.Relation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HCFFactionCache implements FactionCache {

    public List<Faction> factions;
    public Faction wilderness;

    public HCFFactionCache(){
        factions = new ArrayList<>();
    }

    @Override
    public List<Faction> getFactions() {
        return factions;
    }

    @Override
    public Faction create(UUID id, String tag) {
        Faction faction = new PlayerFaction(id, tag);
        if(!factions.contains(faction)) factions.add(faction);

        return faction;
    }

    @Override
    public Faction create(UUID id, String internal, String tag) {
        Faction faction = new SystemFaction(id, internal, tag, false);
        if(!factions.contains(faction)) factions.add(faction);

        return faction;
    }

    @Override
    public Faction create(UUID id, String internal, String tag, boolean safezone) {
        Faction faction = new SystemFaction(id, internal, tag, safezone);
        if(!factions.contains(faction)) factions.add(faction);

        return faction;
    }

    @Override
    public Faction create(UUID id, String internal, String tag, boolean safezone, boolean auto_created) {
        Faction faction = new SystemFaction(id, internal, tag, safezone, auto_created);
        if(!factions.contains(faction)) factions.add(faction);

        return faction;
    }

    @Override
    public void disband(Faction faction) {
        factions.remove(faction);
    }

    @Override
    public Faction searchInternal(String internal) {
        for(Faction faction : factions){
            if(faction.getInternalName().equalsIgnoreCase(internal)) return faction;
        }

        return null;
    }

    @Override
    public Faction searchTag(String tag) {
        for(Faction faction : factions){
            if(ChatColor.stripColor(faction.getTag()).equalsIgnoreCase(tag)) return faction;
        }

        return null;
    }

    @Override
    public Faction searchId(UUID id) {
        for(Faction faction : factions){
            if(faction.getId().equals(id)) return faction;
        }

        return null;
    }

    @Override
    public Faction searchUser(User user) {
        for(Faction faction : factions){
            if(faction.getMembers().contains(user)) return faction;
        }

        return null;
    }

    @Override
    public Faction searchLocation(Location location){
        for(Faction faction : factions){
            if(faction.getClaim() != null && faction.getClaim().isInsideClaim(location)) return faction;
        }

        return null;
    }

    @Override
    public void setWilderness(SystemFaction faction) {
        wilderness = faction;
    }

    @Override
    public Faction getWilderness() {
        if(wilderness == null) throw new NullPointerException("Wilderness cannot be null");

        return wilderness;
    }

    @Override
    public void save() {
        YamlConfiguration player = ConfigurationHelper.create("playerfactions");
        YamlConfiguration system = ConfigurationHelper.create("systemfactions");

        for(Faction faction : factions){
            boolean sys = faction instanceof SystemFaction;

            YamlConfiguration use = faction instanceof PlayerFaction ? player : sys ? system : null;
            if(use == null) continue;

            ConfigurationSection section = use.contains(faction.getId().toString()) ? use.getConfigurationSection(faction.getId().toString()) : use.createSection(faction.getId().toString());

            section.set("internal", faction.getInternalName());
            section.set("tag", faction.getTag());
            if(!sys) section.set("dtr", faction.getDtr());
            if(!sys) section.set("freeze", faction.getFreezeTime());
            if(sys) section.set("safezone", ((SystemFaction)faction).safezone);
            if(sys) section.set("auto_created", ((SystemFaction)faction).auto_created);

            List<String> members = new ArrayList<>();
            for(User user : faction.getMembers()){
                members.add(user.getId().toString());
            }

            section.set("members", members);

            if(!sys){
                ConfigurationSection relations = section.contains("relations") ? section.getConfigurationSection("relations") : section.createSection("relations");

                for(Map.Entry<UUID, Relation> entry : faction.getRelationMap().entrySet()){
                    relations.set(entry.getKey().toString(), entry.getValue().name());
                }
            }
        }

        try {
            player.save(ConfigurationHelper.createFile("playerfactions"));
            system.save(ConfigurationHelper.createFile("systemfactions"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

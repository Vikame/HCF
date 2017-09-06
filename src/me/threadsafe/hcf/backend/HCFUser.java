package me.threadsafe.hcf.backend;

import me.threadsafe.hcf.HCF;
import me.threadsafe.hcf.api.Faction;
import me.threadsafe.hcf.api.RelationParticipator;
import me.threadsafe.hcf.api.User;
import me.threadsafe.hcf.enumeration.Relation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HCFUser implements User{

    public UUID uuid;
    public String name;

    public HCFUser(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public UUID getId() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isOnline() {
        return Bukkit.getPlayer(uuid) != null;
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public void message(String message) {
        Player player = Bukkit.getPlayer(uuid);
        if(player == null) return;

        player.sendMessage(message);
    }

    @Override
    public Faction getFaction() {
        return HCF.getInstance().getFactionCache().searchUser(this);
    }

    @Override
    public Relation getRelationTo(RelationParticipator participator) {
        return getFaction() == null ? Relation.NEUTRAL : getFaction().getRelationTo(participator);
    }

    @Override
    public ChatColor getColorOf(RelationParticipator participator) {
        return getRelationTo(participator).color;
    }
}

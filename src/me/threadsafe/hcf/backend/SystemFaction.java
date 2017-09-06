package me.threadsafe.hcf.backend;

import me.threadsafe.hcf.api.Claim;
import me.threadsafe.hcf.api.Faction;
import me.threadsafe.hcf.api.RelationParticipator;
import me.threadsafe.hcf.api.User;
import me.threadsafe.hcf.enumeration.Relation;
import org.bukkit.ChatColor;

import java.util.*;

public class SystemFaction implements Faction{

    public UUID id;
    public String internal, tag;
    public boolean safezone, auto_created;
    public List<User> members;
    public Map<UUID, Relation> relations; // UNUSED: System factions do not have relations.
    public Claim claim;

    public SystemFaction(UUID id, String internal, String tag, boolean safezone) {
        this.id = id;
        this.internal = internal;
        this.tag = tag;
        this.safezone = safezone;
        this.auto_created = false;
        this.members = new ArrayList<>();
        this.relations = new HashMap<>();
    }

    public SystemFaction(UUID id, String internal, String tag, boolean safezone, boolean auto_created) {
        this.id = id;
        this.internal = internal;
        this.tag = tag;
        this.safezone = safezone;
        this.auto_created = auto_created;
        this.members = new ArrayList<>();
        this.relations = new HashMap<>();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setInternalName(String internal) {
        this.internal = internal;
    }

    @Override
    public String getInternalName() {
        return internal;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public List<User> getMembers() {
        return members;
    }

    @Override
    public Map<UUID, Relation> getRelationMap() {
        return relations;
    }

    @Override
    public void setDtr(double dtr) {}

    @Override
    public double getDtr() {
        return 1;
    }

    @Override
    public double getMaxDtr() {
        return 1;
    }

    @Override
    public double getDtrRegenPerMinute() {
        return 0;
    }

    @Override
    public void setFreezeTime(long time) {}

    @Override
    public boolean isFrozen() {
        return false;
    }

    @Override
    public long getFreezeTime() {
        return 0;
    }

    @Override
    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    @Override
    public Claim getClaim() {
        return claim;
    }

    @Override
    public void addMember(User user) {
        members.add(user);
    }

    @Override
    public void removeMember(User user) {
        members.remove(user);
    }

    @Override
    public boolean isMember(User user) {
        return members.contains(user);
    }

    @Override
    public void message(String message) {
        for(User user : members){
            user.message(message);
        }
    }

    @Override
    public Relation getRelationTo(RelationParticipator participator) {
        return Relation.NEUTRAL;
    }

    @Override
    public ChatColor getColorOf(RelationParticipator participator) {
        return safezone ? ChatColor.GREEN : ChatColor.RED;
    }

    @Override
    public boolean equals(Object o){
        return o instanceof Faction && ((Faction)o).getInternalName().equalsIgnoreCase(this.getInternalName());
    }

}

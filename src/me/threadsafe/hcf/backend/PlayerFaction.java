package me.threadsafe.hcf.backend;

import me.threadsafe.hcf.api.Claim;
import me.threadsafe.hcf.api.Faction;
import me.threadsafe.hcf.api.RelationParticipator;
import me.threadsafe.hcf.api.User;
import me.threadsafe.hcf.enumeration.Relation;
import org.bukkit.ChatColor;

import java.util.*;

public class PlayerFaction implements Faction{

    public UUID id;
    public String tag;
    public List<User> members;
    public Map<UUID, Relation> relations;
    public double dtr;
    public long freeze;
    public Claim claim;

    public PlayerFaction(UUID id, String tag) {
        this.id = id;
        this.tag = tag;
        this.members = new ArrayList<>();
        this.relations = new HashMap<>();
        this.relations.put(id, Relation.MEMBER);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setInternalName(String internal) {
        throw new NoSuchElementException("An internal tag for [PlayerFaction] cannot be modified.");
    }

    @Override
    public String getInternalName() {
        return tag;
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
    public void setDtr(double dtr) {
        this.dtr = dtr;
    }

    @Override
    public double getDtr() {
        return dtr;
    }

    @Override
    public double getMaxDtr() {
        return members.size() <= 1 ? 1.01 : members.size() * 0.51;
    }

    @Override
    public double getDtrRegenPerMinute() {
        return getMaxDtr() / 10;
    }

    @Override
    public void setFreezeTime(long time) {
        this.freeze = System.currentTimeMillis()+time;
    }

    @Override
    public boolean isFrozen() {
        return freeze-System.currentTimeMillis() > 0;
    }

    @Override
    public long getFreezeTime() {
        return freeze-System.currentTimeMillis();
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
        Faction target = null;
        if(participator instanceof User){
            target = ((User)participator).getFaction();
        }else if(participator instanceof Faction){
            target = (Faction)participator;
        }else throw new IllegalArgumentException("Participator not supported.");

        if(target == null || !relations.containsKey(target.getId())) return Relation.NEUTRAL;

        return relations.get(target.getId());
    }

    @Override
    public ChatColor getColorOf(RelationParticipator participator) {
        return getRelationTo(participator).color;
    }

    @Override
    public boolean equals(Object o){
        return o instanceof Faction && ((Faction)o).getInternalName().equalsIgnoreCase(this.getInternalName());
    }

}

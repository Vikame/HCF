package me.threadsafe.hcf.api;

import me.threadsafe.hcf.enumeration.Relation;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Faction extends RelationParticipator {

    UUID getId();

    void setInternalName(String internal);
    String getInternalName();

    void setTag(String tag);
    String getTag();

    List<User> getMembers();

    Map<UUID, Relation> getRelationMap();

    void setDtr(double dtr);
    double getDtr();

    double getMaxDtr();
    double getDtrRegenPerMinute();

    void setFreezeTime(long time);
    boolean isFrozen();
    long getFreezeTime();

    void setClaim(Claim claim);
    Claim getClaim();

    void addMember(User user);
    void removeMember(User user);
    boolean isMember(User user);

    void message(String message);

}

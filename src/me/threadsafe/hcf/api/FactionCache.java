package me.threadsafe.hcf.api;

import me.threadsafe.hcf.backend.SystemFaction;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public interface FactionCache {

    List<Faction> getFactions();

    Faction create(UUID id, String tag);
    Faction create(UUID id, String internal, String tag);
    Faction create(UUID id, String internal, String tag, boolean safezone);
    Faction create(UUID id, String internal, String tag, boolean safezone, boolean auto_created);

    void disband(Faction faction);

    Faction searchInternal(String internal);
    Faction searchTag(String tag);
    Faction searchId(UUID id);
    Faction searchUser(User user);
    Faction searchLocation(Location location);

    void setWilderness(SystemFaction faction);
    Faction getWilderness();

    void save();
}

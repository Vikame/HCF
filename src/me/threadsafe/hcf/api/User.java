package me.threadsafe.hcf.api;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface User extends RelationParticipator{

    UUID getId();
    String getName();

    boolean isOnline();
    Player getPlayer();

    void message(String message);

    Faction getFaction();

}

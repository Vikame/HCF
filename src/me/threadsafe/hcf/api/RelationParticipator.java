package me.threadsafe.hcf.api;

import me.threadsafe.hcf.enumeration.Relation;
import org.bukkit.ChatColor;

public interface RelationParticipator {

    Relation getRelationTo(RelationParticipator participator);
    ChatColor getColorOf(RelationParticipator participator);

}

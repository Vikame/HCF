package me.threadsafe.hcf.api;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public interface Claim {

    int getMinimumX();
    int getMinimumZ();
    int getMaximumX();
    int getMaximumZ();

    World getWorld();

    Location getMinimumPoint();
    Location getMaximumPoint();

    Location getHomePoint();

    boolean isInsideClaim(Block block);
    boolean isInsideClaim(Location location);

    boolean isOnEdgeOfClaim(Block block);
    boolean isOnEdgeOfClaim(Location location);

    boolean isOnCornerOfClaim(Block block);
    boolean isOnCornerOfClaim(Location location);

}

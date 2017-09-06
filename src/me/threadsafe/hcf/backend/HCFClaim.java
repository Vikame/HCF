package me.threadsafe.hcf.backend;

import me.threadsafe.hcf.api.Claim;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class HCFClaim implements Claim {

    private static World OVERWORLD = Bukkit.getWorld("world");

    public int minx, minz, maxx, maxz;
    public Location home;

    public HCFClaim(int minx, int minz, int maxx, int maxz){
//        if(OVERWORLD == null){
//            for(World world : Bukkit.getWorlds()){
//                if(world.getEnvironment() == World.Environment.NORMAL){
//                    OVERWORLD = world;
//                    break;
//                }
//            }
//        }

        this.minx = minx;
        this.minz = minz;
        this.maxx = maxx;
        this.maxz = maxz;
    }

    @Override
    public int getMinimumX() {
        return minx;
    }

    @Override
    public int getMinimumZ() {
        return minz;
    }

    @Override
    public int getMaximumX() {
        return maxx;
    }

    @Override
    public int getMaximumZ() {
        return maxz;
    }

    @Override
    public World getWorld() {
        return OVERWORLD;
    }

    @Override
    public Location getMinimumPoint() {
        return new Location(OVERWORLD, minx, 0, minz);
    }

    @Override
    public Location getMaximumPoint() {
        return new Location(OVERWORLD, maxx, OVERWORLD.getMaxHeight(), maxz);
    }

    @Override
    public Location getHomePoint() {
        return home;
    }

    @Override
    public boolean isInsideClaim(Block block) {
        return isInsideClaim(block.getLocation());
    }

    @Override
    public boolean isInsideClaim(Location location) {
        return location.getBlockX() >= minx && location.getBlockX() <= maxx && location.getBlockZ() >= minz && location.getBlockZ() <= maxx;
    }

    @Override
    public boolean isOnEdgeOfClaim(Block block) {
        return isOnEdgeOfClaim(block.getLocation());
    }

    @Override
    public boolean isOnEdgeOfClaim(Location location) {
        int x = location.getBlockX();
        int z = location.getBlockZ();

        return (x == minx || x == maxx || z == minz || z == maxz) && (x >= minx && x <= maxx && z >= minz && z <= maxz);
    }

    @Override
    public boolean isOnCornerOfClaim(Block block){
        return isOnCornerOfClaim(block.getLocation());
    }

    @Override
    public boolean isOnCornerOfClaim(Location location){
        int x = location.getBlockX();
        int z = location.getBlockZ();

        return (x == maxx && z == maxz) || (x == maxx && z == minz) || (x == minx && z == maxz) || (x == minx && z == minz);
    }

}

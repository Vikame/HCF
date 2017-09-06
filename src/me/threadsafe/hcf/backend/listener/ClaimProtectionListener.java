package me.threadsafe.hcf.backend.listener;

import me.threadsafe.hcf.HCF;
import me.threadsafe.hcf.api.Faction;
import me.threadsafe.hcf.api.FactionCache;
import me.threadsafe.hcf.api.User;
import me.threadsafe.hcf.backend.SystemFaction;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.inventory.ItemStack;

public class ClaimProtectionListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if(e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockZ() == e.getFrom().getBlockZ()) return;

        Player p = e.getPlayer();
        User user = HCF.getInstance().getUserCache().create(p.getUniqueId(), p.getName());

        FactionCache cache = HCF.getInstance().getFactionCache();
        Faction from = cache.searchLocation(e.getFrom());
        Faction to = cache.searchLocation(e.getTo());
        Faction wilderness = cache.getWilderness();

        if(from == to) return;

        if(from != null){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eNow leaving " +
                    user.getColorOf(from)
                    + from.getTag() + "&e. "
                    + (from instanceof SystemFaction && ((SystemFaction)from).safezone ? "&a(Safezone)" : "&c(PvP Allowed)")));
        }else{
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eNow leaving " +
                    user.getColorOf(wilderness)
                    + wilderness.getTag() + "&e. "
                    + (wilderness instanceof SystemFaction && ((SystemFaction)wilderness).safezone ? "&a(Safezone)" : "&c(PvP Allowed)")));
        }
        if(to != null){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eNow entering " +
                    user.getColorOf(to)
                    + to.getTag() + "&e. "
                    + (to instanceof SystemFaction && ((SystemFaction)to).safezone ? "&a(Safezone)" : "&c(PvP Allowed)")));
        }else{
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eNow entering " + user.getColorOf(wilderness) + wilderness.getTag() + "&e. "
                    + (wilderness instanceof SystemFaction && ((SystemFaction)wilderness).safezone ? "&a(Safezone)" : "&c(PvP Allowed)")));
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        cancelIfApplicable(e, e.getPlayer(), e.getBlock().getLocation());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Block b = e.getBlockPlaced();
        if(b.getType() == Material.BED_BLOCK){
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou are unable to place beds."));
            return;
        }

        cancelIfApplicable(e, e.getPlayer(), e.getBlock().getLocation());
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent e){
        cancelIfApplicable(e, e.getPlayer(), e.getBlockClicked().getLocation());
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent e){
        cancelIfApplicable(e, e.getPlayer(), e.getBlockClicked().getLocation());
    }

    // Disallow the dispensing of a liquid into a claim from outside.
    @EventHandler
    public void onBlockDispense(BlockDispenseEvent e){
        Block b = e.getBlock();

        if(b.getState() instanceof Dispenser){
            if(e.getItem().getType() == Material.WATER_BUCKET || e.getItem().getType() == Material.LAVA_BUCKET){
                FactionCache cache = HCF.getInstance().getFactionCache();

                BlockFace face = null;

                if(e.getBlock().getData() == 8) face = BlockFace.DOWN;
                else if(e.getBlock().getData() == 9) face = BlockFace.UP;
                else if(e.getBlock().getData() == 10) face = BlockFace.NORTH;
                else if(e.getBlock().getData() == 11) face = BlockFace.SOUTH;
                else if(e.getBlock().getData() == 12) face = BlockFace.WEST;
                else face = BlockFace.EAST;

                if(!cache.searchLocation(b.getLocation()).equals(cache.searchLocation(b.getRelative(face).getLocation()))) e.setCancelled(true);
            }
        }
    }

    // Disallow the usage of hopper minecarts, could take items out of chests from within the claim.
    @EventHandler
    public void onMinecartPlace(VehicleCreateEvent e){
        if(e.getVehicle() instanceof HopperMinecart){
            HopperMinecart minecart = (HopperMinecart)e.getVehicle();

            minecart.remove();

            Location l = minecart.getLocation();

            l.getWorld().dropItem(l, new ItemStack(Material.MINECART, 1));
            l.getWorld().dropItem(l, new ItemStack(Material.HOPPER, 1));
        }
    }

    // Disallow pistons to push into a claim from outside.
    @EventHandler
    public void onPistonPush(BlockPistonExtendEvent e){
        FactionCache cache = HCF.getInstance().getFactionCache();

        Block block = e.getBlock();
        BlockFace dir = e.getDirection();

        Faction atPiston = cache.searchLocation(block.getLocation());

        Faction extension = cache.searchLocation(block.getRelative(dir).getLocation());

        if(!atPiston.equals(extension)){
            e.setCancelled(true);
            return;
        }

        for(Block b : e.getBlocks()){
            Faction at = cache.searchLocation(b.getRelative(dir).getLocation());

            if(!at.equals(atPiston)){
                e.setCancelled(true);
                break;
            }
        }
    }

    // Disallow pistons to pull from a claim from outside.
    @EventHandler
    public void onPistonPull(BlockPistonRetractEvent e){
        Block piston = e.getBlock();
        if(!e.isSticky()) return;

        FactionCache cache = HCF.getInstance().getFactionCache();

        if(!cache.searchLocation(piston.getLocation()).equals(cache.searchLocation(e.getRetractLocation()))) e.setCancelled(true);
    }

    // Disallow liquid flow into a claim from outside.
    @EventHandler
    public void onLiquidFlow(BlockFromToEvent e) {
        Block from = e.getBlock();
        Block to = e.getToBlock();

        FactionCache cache = HCF.getInstance().getFactionCache();

        if (from.isLiquid()) {
            if(!cache.searchLocation(from.getLocation()).equals(cache.searchLocation(to.getLocation()))){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onMinecartExplode(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof TNTPrimed || e.getDamager() instanceof ExplosiveMinecart) e.setCancelled(true);
    }

    @EventHandler
    public void onFireSpread(BlockIgniteEvent e){
        if(e.getCause() == BlockIgniteEvent.IgniteCause.SPREAD || e.getCause() == BlockIgniteEvent.IgniteCause.LAVA) e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e){
        e.setCancelled(true);
    }

    private boolean cancelIfApplicable(Cancellable cancellable, Player p, Location location){
        if(p.hasPermission("hcf.bypass")) return false;

        FactionCache cache = HCF.getInstance().getFactionCache();
        Faction at = cache.searchLocation(location);
        User user = HCF.getInstance().getUserCache().create(p.getUniqueId(), p.getName());

        if(at != null && !at.equals(user.getFaction())){
            cancellable.setCancelled(true);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou are unable to build in the territory of " + user.getColorOf(at) + at.getTag() + "&e."));
        }

        return cancellable.isCancelled();
    }

}

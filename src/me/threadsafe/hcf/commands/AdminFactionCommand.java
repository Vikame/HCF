package me.threadsafe.hcf.commands;

import me.threadsafe.hcf.HCF;
import me.threadsafe.hcf.Messages;
import me.threadsafe.hcf.api.Faction;
import me.threadsafe.hcf.api.FactionCache;
import me.threadsafe.hcf.api.TypeHelper;
import me.threadsafe.hcf.api.User;
import me.threadsafe.hcf.backend.PlayerFaction;
import me.threadsafe.hcf.backend.SystemFaction;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AdminFactionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length <= 0){
            sender.sendMessage(Messages.createAdminFactionHelpMessage(label));
            return true;
        }

        FactionCache cache = HCF.getInstance().getFactionCache();

        String sub = args[0];
        if(sub.equalsIgnoreCase("create")){
            if(args.length <= 1){
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + " create <internal_name> [tag] [safezone]");
                return true;
            }

            String internal = args[1];
            String tag = (args.length <= 2 ? internal : args[2]);
            boolean safezone = false;
            if(args.length > 3){
                Boolean val = TypeHelper.BooleanTypeHelper.getInstance().parse(args[3]);
                if(val == null){
                    sender.sendMessage(Messages.createNotOfTypeMessage(args[3], TypeHelper.BooleanTypeHelper.getInstance().keys()));
                    return true;
                }else safezone = val;
            }

            if(cache.searchInternal(internal) != null){
                sender.sendMessage(Messages.createFactionAlreadyExistsInternalMessage(internal));
                return true;
            }

            cache.create(UUID.randomUUID(), internal, tag, safezone, false);
            sender.sendMessage(Messages.createSystemFactionCreatedMessage(internal, tag));
        }else if(sub.equalsIgnoreCase("disband")){
            if(args.length <= 1){
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + " disband <internal_name>");
                return true;
            }

            Faction target = cache.searchInternal(args[1]);
            if(target == null){
                sender.sendMessage(Messages.createFactionDoesntExistInteralMessage(args[1]));
                return true;
            }

            sender.sendMessage(Messages.createAdminFactionDisbandMessage(args[1]));
            cache.disband(target);
        }else if(sub.equalsIgnoreCase("tag")){
            if(args.length <= 2){
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + " tag <internal_name> <tag>");
                return true;
            }

            Faction target = cache.searchInternal(args[1]);
            if(target == null){
                sender.sendMessage(Messages.createFactionDoesntExistInteralMessage(args[1]));
                return true;
            }

            if(target instanceof PlayerFaction) target.message(Messages.createAdminRenamedFactionMessage(target.getTag(), args[2]));
            sender.sendMessage(Messages.createAdminFactionRenameMessage(target.getInternalName(), args[2]));

            target.setTag(args[2]);
        }else if(sub.equalsIgnoreCase("safezone")){
            if(args.length <= 2){
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + " safezone <internal_name> <safezone>");
                return true;
            }

            Faction target = cache.searchInternal(args[1]);
            if(target == null){
                sender.sendMessage(Messages.createFactionDoesntExistInteralMessage(args[1]));
                return true;
            }

            if(target instanceof PlayerFaction){
                sender.sendMessage(Messages.createNotSystemFactionMessage(args[1]));
                return true;
            }

            SystemFaction faction = (SystemFaction)target;

            Boolean val = TypeHelper.BooleanTypeHelper.getInstance().parse(args[2]);
            if(val == null){
                sender.sendMessage(Messages.createNotOfTypeMessage(args[2], TypeHelper.BooleanTypeHelper.getInstance().keys()));
                return true;
            }else faction.safezone = val;

            sender.sendMessage(Messages.createAdminFactionSafezoneMessage(target.getInternalName(), val));
        }else if(sub.equalsIgnoreCase("list")){
            int index = 0;

            for(Faction faction : cache.getFactions()){
                if(faction instanceof SystemFaction) {
                    index++;

                    sender.sendMessage(Messages.createListMessage(index, (SystemFaction)faction));
                }
            }

            if(index == 0) sender.sendMessage(ChatColor.RED + "No system factions have been created.");
        }else if(sub.equalsIgnoreCase("join")){
            if(!(sender instanceof Player)){
                sender.sendMessage(Messages.createPlayerOnlyMessage());
                return true;
            }

            if(args.length <= 0){
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + " join <internal_name>");
                return true;
            }

            Player p = (Player)sender;
            User user = HCF.getInstance().getUserCache().create(p.getUniqueId(), p.getName());

            if(user.getFaction() != null){
                sender.sendMessage(Messages.createMustLeaveFactionMessage());
                return true;
            }

            Faction target = cache.searchInternal(args[1]);
            if(target == null){
                sender.sendMessage(Messages.createFactionDoesntExistInteralMessage(args[1]));
                return true;
            }

            if(target instanceof PlayerFaction) target.message(Messages.createAdminJoinedFactionMessage(p.getName()));
            target.addMember(user);
        }else{
            sender.sendMessage(ChatColor.RED + "Invalid sub-command.");
        }
        return true;
    }

}

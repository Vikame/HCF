package me.threadsafe.hcf;

import me.threadsafe.hcf.api.Faction;
import me.threadsafe.hcf.backend.SystemFaction;
import org.bukkit.ChatColor;

public class Messages {

    private static final String AF_LIST = "&8[&f{NUMBER}&8] {AUTO_CREATED} &e{INTERNAL} &6{TAG} {PVP}";
    private static final String AF_HELP_MESSAGE =
            "&7&m-------&7 [ &fAdmin Faction Help &7] &m-------\n" +
            "&6/{LABEL} create <internal_name> <tag> [safezone]&7: &fCreate a system faction\n" +
            "&6/{LABEL} disband <internal_name>&7: &fDisband a system faction\n" +
            "&6/{LABEL} tag <internal_name> <tag>&7: &fModify a system faction's tag value\n" +
            "&6/{LABEL} safezone <internal_name> <safezone>&7: &fModify a system faction's safezone value\n" +
            "&6/{LABEL} list&7: &fView all system factions\n" +
            "&6/{LABEL} join <internal_name>&7: &fJoin a system faction";

    private static final String NOT_OF_TYPE = "&c{ARGUMENT} must be of type [{TYPES}].";

    private static final String FACTION_ALREADY_EXISTS_INTERNAL = "&cA faction with the internal identifier '{INTERNAL}' already exists.";
    private static final String FACTION_DOESNT_EXIST_INTERNAL = "&cA faction with the internal identifier '{INTERNAL}' doesn't exist.";

    private static final String NOT_SYSTEM_FACTION = "&cThe faction '{INTERNAL}' is not a system faction.";

    private static final String SYSTEM_FACTION_CREATED = "&6You have created the system faction '&f{INTERNAL}&6' with the tag '&f{TAG}&6'.";

    private static final String ADMIN_RENAMED_FACTION = "&6Your faction has been renamed to '&f{NEW}&6' (was '&f{OLD}&6') by an administrator.";
    private static final String ADMIN_FACTION_RENAME = "&6You have renamed the faction '&f{INTERNAL}&6' to '&f{TAG}&6'.";

    private static final String ADMIN_FACTION_SAFEZONE = "&6You have edited the faction '&f{INTERNAL}&6' safezone value&7: &f{SAFEZONE}&6.";

    private static final String PLAYER_ONLY = "&cOnly a player can use that command.";

    private static final String MUST_LEAVE_FACTION = "&cYou must leave your current faction before doing this.";

    private static final String ADMIN_JOINED_FACTION = "&f{NAME} &6has forcefully joined your faction.";

    private static final String ADMIN_FACTION_DISBAND = "&6You have disbanded the faction '&f{INTERNAL}&6'.";

    public static String createListMessage(int id, SystemFaction faction){
        return ChatColor.translateAlternateColorCodes('&', AF_LIST.replace("{NUMBER}", "" + id).replace("{AUTO_CREATED} ", faction.auto_created ? ChatColor.RED + "{Auto-created} " : "")
                .replace("{INTERNAL}", faction.getInternalName()).replace("{TAG}", faction.getTag().replace(ChatColor.COLOR_CHAR, '&'))
                .replace("{PVP}", faction.safezone ? ChatColor.GREEN + "(Safezone)" : ChatColor.RED + "(PvP Allowed)").trim());
    }

    public static String createAdminFactionHelpMessage(String label){
        return ChatColor.translateAlternateColorCodes('&', AF_HELP_MESSAGE.replace("{LABEL}", label));
    }

    public static String createNotOfTypeMessage(String argument, String... values) {
        String types = "";
        for(String s : values) types += s + ",";
        if(types.endsWith(",")) types = types.substring(0, types.length()-1);
        else types = "ERROR";

        return ChatColor.translateAlternateColorCodes('&', NOT_OF_TYPE.replace("{ARGUMENT}", argument).replace("{TYPES}", types));
    }

    public static String createFactionAlreadyExistsInternalMessage(String internal) {
        return ChatColor.translateAlternateColorCodes('&', FACTION_ALREADY_EXISTS_INTERNAL.replace("{INTERNAL}", internal));
    }

    public static String createSystemFactionCreatedMessage(String internal, String tag) {
        return ChatColor.translateAlternateColorCodes('&', SYSTEM_FACTION_CREATED.replace("{INTERNAL}", internal).replace("{TAG}", tag));
    }

    public static String createFactionDoesntExistInteralMessage(String internal) {
        return ChatColor.translateAlternateColorCodes('&', FACTION_DOESNT_EXIST_INTERNAL.replace("{INTERNAL}", internal));
    }

    public static String createAdminRenamedFactionMessage(String oldTag, String newTag) {
        return ChatColor.translateAlternateColorCodes('&', ADMIN_RENAMED_FACTION.replace("{OLD}", oldTag).replace("{NEW}", newTag));
    }

    public static String createAdminFactionRenameMessage(String internal, String tag) {
        return ChatColor.translateAlternateColorCodes('&', ADMIN_FACTION_RENAME.replace("{INTERNAL}", internal).replace("{TAG}", tag));
    }

    public static String createAdminFactionSafezoneMessage(String internal, boolean val) {
        return ChatColor.translateAlternateColorCodes('&', ADMIN_FACTION_SAFEZONE.replace("{INTERNAL}", internal).replace("{SAFEZONE}", "" + val));
    }

    public static String createNotSystemFactionMessage(String internal) {
        return ChatColor.translateAlternateColorCodes('&', NOT_SYSTEM_FACTION.replace("{INTERNAL}", internal));
    }

    public static String createPlayerOnlyMessage() {
        return ChatColor.translateAlternateColorCodes('&', PLAYER_ONLY);
    }

    public static String createMustLeaveFactionMessage() {
        return ChatColor.translateAlternateColorCodes('&', MUST_LEAVE_FACTION);
    }

    public static String createAdminJoinedFactionMessage(String name) {
        return ChatColor.translateAlternateColorCodes('&', ADMIN_JOINED_FACTION.replace("{NAME}", name));
    }

    public static String createAdminFactionDisbandMessage(String internal) {
        return ChatColor.translateAlternateColorCodes('&', ADMIN_FACTION_DISBAND.replace("{INTERNAL}", internal));
    }
}

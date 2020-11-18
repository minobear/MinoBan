package mino.minoban.commands;

import mino.minoban.MinoBAN;
import mino.minoban.events.ChatMessageEvent;
import mino.minoban.utils.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommands implements CommandExecutor {
    private final MinoBAN plugin = MinoBAN.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player != null){
            if (!player.hasPermission("minoban.admin")){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("no-permission")));
                return false;
            }
        }

        if(args.length > 0){
            if (args[0].equalsIgnoreCase("ban")){
                if (args.length >= 3){
                    Player target = Bukkit.getPlayer(args[1]);
                    StringBuilder banReason = new StringBuilder();
                    for (int i=2; i<args.length; i++){
                        banReason.append(args[i]).append(" ");
                    }
                    if (target != null){
                        if (player == null)
                            MinoBAN.ban(target, plugin.getConfig().getString("console-display"), banReason.toString());
                        else
                            MinoBAN.ban(target, player.getName(), banReason.toString());
                    }else{
                        OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[1]);
                        if (player == null)
                            MinoBAN.ban(offlineTarget, plugin.getConfig().getString("console-display"), banReason.toString());
                        else
                            MinoBAN.ban(offlineTarget, player.getName(), banReason.toString());
                    }
                }else{
                    commandHelp(player, 1);
                }

            }else if(args[0].equalsIgnoreCase("tban")){
                if (args.length >= 4){
                    Player target = Bukkit.getPlayer(args[1]);
                    String banTime = args[2];
                    StringBuilder banReason = new StringBuilder();
                    for (int i=3; i<args.length; i++){
                        banReason.append(args[i]).append(" ");
                    }
                    if (banTime.substring(banTime.length()-1).equalsIgnoreCase("s") || banTime.substring(banTime.length()-1).equalsIgnoreCase("m") || banTime.substring(banTime.length()-1).equalsIgnoreCase("h") || banTime.substring(banTime.length()-1).equalsIgnoreCase("d")){
                        if (target != null){
                            if (player == null)
                                MinoBAN.tban(target, plugin.getConfig().getString("console-display"), banTime, banReason.toString());
                            else
                                MinoBAN.tban(target, player.getName(), banTime, banReason.toString());
                        }else{
                            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[1]);
                            if (player == null)
                                MinoBAN.tban(offlineTarget, plugin.getConfig().getString("console-display"), banTime, banReason.toString());
                            else
                                MinoBAN.tban(offlineTarget, player.getName(), banTime, banReason.toString());
                        }
                    }else{
                        commandHelp(player, 2);
                    }

                }else{
                    commandHelp(player, 2);
                }

            }else if (args[0].equalsIgnoreCase("unban")) {
                if (args.length >= 2) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if (player == null)
                        MinoBAN.unban(target, plugin.getConfig().getString("console-display"));
                    else
                        MinoBAN.unban(target, player.getName());
                } else {
                    commandHelp(player, 3);
                }
            }else if (args[0].equalsIgnoreCase("mute")) {
                if (args.length >= 3) {
                    Player target = Bukkit.getPlayer(args[1]);
                    StringBuilder muteReason = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        muteReason.append(args[i]).append(" ");
                    }
                    if (player == null)
                        MinoBAN.mute(target, plugin.getConfig().getString("console-display"), muteReason.toString());
                    else
                        MinoBAN.mute(target, player.getName(), muteReason.toString());
                }else{
                    commandHelp(player, 4);
                }
            }else if (args[0].equalsIgnoreCase("tmute")) {
                if (args.length >= 4) {
                    Player target = Bukkit.getPlayer(args[1]);
                    String muteTime = args[2];
                    StringBuilder muteReason = new StringBuilder();
                    for (int i=3; i<args.length; i++){
                        muteReason.append(args[i]).append(" ");
                    }
                    if (muteTime.substring(muteTime.length() - 1).equalsIgnoreCase("s") || muteTime.substring(muteTime.length() - 1).equalsIgnoreCase("m") || muteTime.substring(muteTime.length() - 1).equalsIgnoreCase("h") || muteTime.substring(muteTime.length() - 1).equalsIgnoreCase("d")) {
                        if (target != null) {
                            if (player == null)
                                MinoBAN.tmute(target, plugin.getConfig().getString("console-display"), muteTime, muteReason.toString());
                            else
                                MinoBAN.tmute(target, player.getName(), muteTime, muteReason.toString());
                        } else {
                            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[1]);
                            if (player == null)
                                MinoBAN.tmute(offlineTarget, plugin.getConfig().getString("console-display"), muteTime, muteReason.toString());
                            else
                                MinoBAN.tmute(offlineTarget, player.getName(), muteTime, muteReason.toString());
                        }
                    } else {
                        commandHelp(player, 5);
                    }

                } else {
                    commandHelp(player, 5);
                }
            }else if (args[0].equalsIgnoreCase("unmute")){
                if (args.length >= 2) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if (player == null)
                        MinoBAN.unmute(target, plugin.getConfig().getString("console-display"));
                    else
                        MinoBAN.unmute(target, player.getName());
                }else{
                    commandHelp(player, 6);
                }
            }else if (args[0].equalsIgnoreCase("reload")) {
                FileUtil.loadBanAndMute();
                plugin.reloadConfig();
                if (player == null)
                    System.out.println(plugin.getConfig().getString("reload-complete"));
                else
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("reload-complete")));
            }else if (args[0].equalsIgnoreCase("togglechat")){
                ChatMessageEvent.toggleChat = !ChatMessageEvent.toggleChat;
                if (player == null){
                    if (ChatMessageEvent.toggleChat)
                        Bukkit.broadcastMessage(ChatColor.YELLOW+plugin.getConfig().getString("console-display")+" "+ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("chat-open-message")));
                    else
                        Bukkit.broadcastMessage(ChatColor.YELLOW+plugin.getConfig().getString("console-display")+" "+ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("chat-close-message")));
                } else
                if (ChatMessageEvent.toggleChat)
                    Bukkit.broadcastMessage(ChatColor.YELLOW+player.getName()+" "+ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("chat-open-message")));
                else
                    Bukkit.broadcastMessage(ChatColor.YELLOW+player.getName()+" "+ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("chat-close-message")));
            }else{
                commandHelp(player, 0);
            }

        }else{
            commandHelp(player, 0);
        }

        return false;
    }

    private void commandHelp(Player player, int type){
        if (player == null){
            switch (type){
                case 0:
                    plugin.getLogger().info("USAGE: mb ban <player> <reason>");
                    plugin.getLogger().info("USAGE: mb ban <player> <[number]s/m/h/d> <reason>");
                    plugin.getLogger().info("USAGE: mb unban <player>");
                    plugin.getLogger().info("USAGE: mb mute <player> <reason>");
                    plugin.getLogger().info("USAGE: mb tmute <player> <[number]s/m/h/d> <reason>");
                    plugin.getLogger().info("USAGE: mb unmute <player>");
                    plugin.getLogger().info("USAGE: mb togglechat");
                    plugin.getLogger().info("USAGE: mb reload");
                    break;
                case 1:
                    plugin.getLogger().info("USAGE: mb ban <player> <reason>");
                    break;
                case 2:
                    plugin.getLogger().info("USAGE: mb tban <player> <[number]s/m/h/d> <reason>");
                    break;
                case 3:
                    plugin.getLogger().info("USAGE: mb unban <player>");
                    break;
                case 4:
                    plugin.getLogger().info("USAGE: mb mute <player> <reason>");
                    break;
                case 5:
                    plugin.getLogger().info("USAGE: mb tmute <player> <[number]s/m/h/d> <reason>");
                    break;
                case 6:
                    plugin.getLogger().info("USAGE: mb unmute <player>");
                    break;
            }
        }else {
            switch (type){
                case 0:
                    player.sendMessage(ChatColor.RED+"/mb ban <player> <reason>");
                    player.sendMessage(ChatColor.RED+"/mb tban <player> <[number]s/m/h/d> <reason>");
                    player.sendMessage(ChatColor.RED+"/mb unban <player>");
                    player.sendMessage(ChatColor.RED+"/mb mute <player> <reason>");
                    player.sendMessage(ChatColor.RED+"/mb tmute <player> <[number]s/m/h/d> <reason>");
                    player.sendMessage(ChatColor.RED+"/mb unmute <player>");
                    player.sendMessage(ChatColor.RED+"/mb togglechat");
                    player.sendMessage(ChatColor.RED+"/mb reload");
                    break;
                case 1:
                    player.sendMessage(ChatColor.RED+"/mb ban <player> <reason>");
                    break;
                case 2:
                    player.sendMessage(ChatColor.RED+"/mb tban <player> <[number]s/m/h/d> <reason>");
                    break;
                case 3:
                    player.sendMessage(ChatColor.RED+"/mb unban <player>");
                    break;
                case 4:
                    player.sendMessage(ChatColor.RED+"/mb mute <player> <reason>");
                    break;
                case 5:
                    player.sendMessage(ChatColor.RED+"/mb tmute <player> <[number]s/m/h/d> <reason>");
                    break;
                case 6:
                    player.sendMessage(ChatColor.RED+"/mb unmute <player>");
                    break;
            }
        }
    }
}

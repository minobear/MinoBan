package mino.minoban;

import mino.minoban.commands.BanCommands;
import mino.minoban.events.ChatMessageEvent;
import mino.minoban.events.GuiMenuEvent;
import mino.minoban.events.JoinServerEvent;
import mino.minoban.utils.FileUtil;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;

public final class MinoBAN extends JavaPlugin {

    public static MinoBAN plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        registerCommands();
        registerEvents();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        FileUtil.createBansFile(getConfig().getString("ban-file"));
        FileUtil.createBansFile(getConfig().getString("mute-file"));
        FileUtil.loadBanAndMute();
    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new GuiMenuEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinServerEvent(), this);
        getServer().getPluginManager().registerEvents(new ChatMessageEvent(), this);
    }

    private void registerCommands(){
        getCommand("mb").setExecutor(new BanCommands());
    }

    public static MinoBAN getInstance(){
        return plugin;
    }

    public static void ban(Player banTarget, String executor, String banReason){
        final String banBroadcast = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("ban-broadcast").replace("<executor>", executor).replace("<banned player>", banTarget.getName()).replace("<time>", plugin.getConfig().getString("permanent")).replace("<reason>", banReason));
        String banMessage = getBanMessage(banReason, -1);
        FileUtil.addBan(banTarget, banReason);
        banTarget.kickPlayer(banMessage);
        Bukkit.broadcastMessage(banBroadcast);
    }

    public static void ban(OfflinePlayer banTarget, String executor, String banReason){
        final String banBroadcast = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("ban-broadcast").replace("<executor>", executor).replace("<banned player>", banTarget.getName()).replace("<time>", plugin.getConfig().getString("permanent")).replace("<reason>", banReason));
        FileUtil.addBan(banTarget, banReason);
        Bukkit.broadcastMessage(banBroadcast);
    }

    public static void tban(Player banTarget, String executor, String timeStr, String banReason){
        final String banBroadcast = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("ban-broadcast").replace("<executor>", executor).replace("<banned player>", banTarget.getName()).replace("<time>", timeStr).replace("<reason>", banReason));
        String date = timeStr.substring(timeStr.length()-1);
        long timeNum = Long.parseLong(timeStr.substring(0, timeStr.length()-1));
        long time = date.equalsIgnoreCase("s") ? timeNum*1000 : date.equalsIgnoreCase("m") ? timeNum*60000 : date.equalsIgnoreCase("h") ? timeNum*3600000 : date.equalsIgnoreCase("d") ? timeNum*86400000 : -1;
        time+=System.currentTimeMillis();
        FileUtil.addTBan(banTarget, time, banReason);
        String banMessage = getBanMessage(banReason, time);
        banTarget.kickPlayer(banMessage);
        Bukkit.broadcastMessage(banBroadcast);
    }

    public static void tban(OfflinePlayer banTarget, String executor, String timeStr, String banReason){
        final String banBroadcast = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("ban-broadcast").replace("<executor>", executor).replace("<banned player>", banTarget.getName()).replace("<time>", timeStr).replace("<reason>", banReason));
        String date = timeStr.substring(timeStr.length()-1);
        long timeNum = Long.parseLong(timeStr.substring(0, timeStr.length()-1));
        long time = date.equalsIgnoreCase("s") ? timeNum*1000 : date.equalsIgnoreCase("m") ? timeNum*60000 : date.equalsIgnoreCase("h") ? timeNum*3600000 : date.equalsIgnoreCase("d") ? timeNum*86400000 : -1;
        time+=System.currentTimeMillis();
        FileUtil.addTBan(banTarget, time, banReason);
        Bukkit.broadcastMessage(banBroadcast);
    }

    public static void unban(OfflinePlayer target, String executor){
        final String unbanBroadcast = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("unban-broadcast").replace("<executor>", executor).replace("<unban player>", target.getName()));
        boolean success = FileUtil.removeBan(target);
        if (success)
            Bukkit.broadcastMessage(unbanBroadcast);
        else
            try {
                Bukkit.getPlayer(executor).sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("unban-error")));
            }catch (NullPointerException e){
                System.out.println(plugin.getConfig().getString("unban-error"));
            }
    }

    public static void mute(Player target, String executor, String banReason){
        final String muteBroadcast = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("mute-broadcast").replace("<executor>", executor).replace("<muted player>", target.getName()).replace("<time>", plugin.getConfig().getString("permanent")).replace("<reason>", banReason));
        FileUtil.addMute(target, -1L, banReason);
        Bukkit.broadcastMessage(muteBroadcast);
    }

    public static void tmute(Player target, String executor, String timeStr, String banReason){
        final String muteBroadcast = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("mute-broadcast").replace("<executor>", executor).replace("<muted player>", target.getName()).replace("<time>", timeStr).replace("<reason>", banReason));
        String date = timeStr.substring(timeStr.length()-1);
        long timeNum = Long.parseLong(timeStr.substring(0, timeStr.length()-1));
        long time = date.equalsIgnoreCase("s") ? timeNum*1000 : date.equalsIgnoreCase("m") ? timeNum*60000 : date.equalsIgnoreCase("h") ? timeNum*3600000 : date.equalsIgnoreCase("d") ? timeNum*86400000 : -1;
        time+=System.currentTimeMillis();
        FileUtil.addMute(target, time, banReason);
        Bukkit.broadcastMessage(muteBroadcast);
    }

    public static void tmute(OfflinePlayer target, String executor, String timeStr, String banReason){
        final String muteBroadcast = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("mute-broadcast").replace("<executor>", executor).replace("<muted player>", target.getName()).replace("<time>", plugin.getConfig().getString("permanent")).replace("<reason>", banReason));
        String date = timeStr.substring(timeStr.length()-1);
        long timeNum = Long.parseLong(timeStr.substring(0, timeStr.length()-1));
        long time = date.equalsIgnoreCase("s") ? timeNum*1000 : date.equalsIgnoreCase("m") ? timeNum*60000 : date.equalsIgnoreCase("h") ? timeNum*3600000 : date.equalsIgnoreCase("d") ? timeNum*86400000 : -1;
        time+=System.currentTimeMillis();
        FileUtil.addMute(target, time, banReason);
        Bukkit.broadcastMessage(muteBroadcast);
    }

    public static void unmute(OfflinePlayer target, String executor){
        final String unmuteBroadcast = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("unmute-broadcast").replace("<executor>", executor).replace("<unmute player>", target.getName()));
        boolean success = FileUtil.removeMute(target);
        if (success)
            Bukkit.broadcastMessage(unmuteBroadcast);
        else
            try {
                Bukkit.getPlayer(executor).sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("unmute-error")));
            }catch (NullPointerException e){
                System.out.println(plugin.getConfig().getString("unmute-error"));
            }
    }

    public static String getBanMessage(String banReason, long time){
        final ConfigurationSection config = plugin.getConfig();
        int day = 0, hour = 0, min = 0, sec = 0;
        long currTime = System.currentTimeMillis();
        sec = Math.toIntExact((time - currTime) / 1000);
        if (sec >= 60){
            min = sec / 60;
            sec %= 60;
        }
        if (min >= 60){
            hour = min / 60;
            min %= 60;
        }
        if (hour >= 24){
            day = hour / 24;
            hour %= 24;
        }
        String timeRemaining = day>0 ? day + config.getString("day") : "";
        timeRemaining = hour>0 ? timeRemaining+hour+config.getString("hour") : timeRemaining;
        timeRemaining = min>0 ? timeRemaining+min+config.getString("min") : timeRemaining;
        timeRemaining = sec>=0 ? timeRemaining+sec+config.getString("sec") : timeRemaining;
        if (time == -1) timeRemaining = config.getString("permanent");

        return ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("ban-title")
                        +"\n\n"+plugin.getConfig().getString("ban-message")
                        +"\n\n"+plugin.getConfig().getString("ban-reason").replace("<reason>", banReason)
                        +"\n"+plugin.getConfig().getString("ban-remaining").replace("<time>", timeRemaining)
        );
    }

    public void openMainMenu(Player player){
        HashSet<Player> onlinePlayers = new HashSet<>(Bukkit.getOnlinePlayers());
        HashSet<OfflinePlayer> bannedPlayers = new HashSet<>(Bukkit.getBannedPlayers());
        OfflinePlayer[] offlinePlayers = Bukkit.getServer().getOfflinePlayers();
        int onlinePlayersCount = onlinePlayers.size();
        int bannedPlayersCount = bannedPlayers.size();
        int offlinePlayersCount = offlinePlayers.length;

        Inventory gui = Bukkit.createInventory(player, 9, "主選單");
        ItemStack onlinePlayerList = new ItemStack(Material.EMERALD_BLOCK, onlinePlayersCount);
        ItemStack bannedPlayerList = new ItemStack(Material.REDSTONE_BLOCK, bannedPlayersCount);
        ItemStack offlinePlayerList = new ItemStack(Material.LAPIS_BLOCK, offlinePlayersCount);
        ItemMeta opList_meta = onlinePlayerList.getItemMeta();
        ItemMeta bpList_meta = bannedPlayerList.getItemMeta();
        ItemMeta ofList_meta = offlinePlayerList.getItemMeta();

        opList_meta.setDisplayName(ChatColor.GREEN + "線上玩家清單");
        bpList_meta.setDisplayName(ChatColor.RED + "已封鎖玩家清單");
        ofList_meta.setDisplayName(ChatColor.BLUE + "離線玩家清單");
        onlinePlayerList.setItemMeta(opList_meta);
        bannedPlayerList.setItemMeta(bpList_meta);
        offlinePlayerList.setItemMeta(ofList_meta);

        gui.setItem(0, onlinePlayerList);
        gui.setItem(1, bannedPlayerList);
        gui.setItem(2, offlinePlayerList);

        player.openInventory(gui);
    }

    public void openPlayerListMenu(Player player){
        Inventory gui = Bukkit.createInventory(player, 54, "線上玩家清單");

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta back_meta = back.getItemMeta();
        back_meta.setDisplayName(ChatColor.RED + "返回");
        back.setItemMeta(back_meta);

        for (Player p : Bukkit.getOnlinePlayers()){
            ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta head_meta = (SkullMeta) playerHead.getItemMeta();
            head_meta.setDisplayName(p.getDisplayName());
            head_meta.setOwner(p.getName());
            ArrayList<String> lores = new ArrayList<>();
            lores.add(ChatColor.LIGHT_PURPLE + "玩家血量: " + ChatColor.WHITE + p.getHealth());
            lores.add(ChatColor.GREEN + "玩家經驗值: " + ChatColor.WHITE + p.getExp());
            lores.add(ChatColor.GOLD + "玩家座標: " + ChatColor.WHITE + "x: "+ p.getLocation().getX() + ", y: " +p.getLocation().getY() + ", z: " + p.getLocation().getZ());
            head_meta.setLore(lores);
            playerHead.setItemMeta(head_meta);
            gui.addItem(playerHead);
        }
        gui.setItem(53, back);

        player.openInventory(gui);
    }

    public void openBannedPlayerList(Player player){
        Inventory gui = Bukkit.createInventory(player, 54, "已封鎖玩家清單");

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta back_meta = back.getItemMeta();
        back_meta.setDisplayName(ChatColor.RED + "返回");
        back.setItemMeta(back_meta);

        for (OfflinePlayer p : Bukkit.getBannedPlayers()){
            ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta head_meta = (SkullMeta) playerHead.getItemMeta();
            head_meta.setDisplayName(p.getName());
            head_meta.setOwner(p.getName());
            playerHead.setItemMeta(head_meta);
            gui.addItem(playerHead);
        }
        gui.setItem(53, back);

        player.openInventory(gui);
    }

    public void openOfflinePlayersList(Player player){
        Inventory gui = Bukkit.createInventory(player, 54, "離線玩家清單");

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta back_meta = back.getItemMeta();
        back_meta.setDisplayName(ChatColor.RED + "返回");
        back.setItemMeta(back_meta);

        for (OfflinePlayer p : Bukkit.getOfflinePlayers()){
            ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta head_meta = (SkullMeta) playerHead.getItemMeta();
            head_meta.setDisplayName(p.getName());
            head_meta.setOwner(p.getName());
            playerHead.setItemMeta(head_meta);
            gui.addItem(playerHead);
        }
        gui.setItem(53, back);

        player.openInventory(gui);

    }

    public void openOfflinePlayerOption(Player player){
        Inventory gui = Bukkit.createInventory(player, 9, "離線玩家設定");


    }

    public void openConfirmMenu(Player player, Player banTarget){
        Inventory confirmGui = Bukkit.createInventory(player, 9, "確定要封鎖該玩家?");

        ItemStack player_head = new ItemStack((Material.SKULL_ITEM), 1, (short) 3);
        ItemStack yes = new ItemStack(Material.BARRIER, 1);
        ItemStack no = new ItemStack(Material.ARROW, 1);
        SkullMeta head_meta = (SkullMeta) player_head.getItemMeta();
        ItemMeta yes_meta = yes.getItemMeta();
        ItemMeta no_meta = no.getItemMeta();

        head_meta.setDisplayName(banTarget.getDisplayName());
        head_meta.setOwner(banTarget.getName());
        yes_meta.setDisplayName(ChatColor.GREEN + "確定封鎖");
        no_meta.setDisplayName(ChatColor.RED + "返回");
        player_head.setItemMeta(head_meta);
        yes.setItemMeta(yes_meta);
        no.setItemMeta(no_meta);

        confirmGui.setItem(0, yes);
        confirmGui.setItem(4, player_head);
        confirmGui.setItem(8, no);

        player.openInventory(confirmGui);
    }

    public void openBannedPlayerOptionMenu(Player player, OfflinePlayer target){
        Inventory gui = Bukkit.createInventory(player, 9, "已封鎖玩家設定");
        ItemStack unban = new ItemStack(Material.ANVIL);
        ItemStack back = new ItemStack(Material.ARROW);
        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta unban_meta = unban.getItemMeta();
        ItemMeta back_meta = back.getItemMeta();
        SkullMeta head_meta = (SkullMeta) playerHead.getItemMeta();
        unban_meta.setDisplayName(ChatColor.GREEN + "解除封鎖");
        back_meta.setDisplayName(ChatColor.RED + "返回");
        head_meta.setDisplayName(target.getName());
        head_meta.setOwner(target.getName());
        unban.setItemMeta(unban_meta);
        back.setItemMeta(back_meta);
        playerHead.setItemMeta(head_meta);

        gui.addItem(unban);
        gui.setItem(8, back);
        gui.setItem(4, playerHead);

        player.openInventory(gui);
    }
}

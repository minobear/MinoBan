package mino.minoban.events;

import mino.minoban.MinoBAN;
import mino.minoban.utils.FileUtil;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiMenuEvent implements Listener {
    private final MinoBAN plugin = MinoBAN.getInstance();

    @EventHandler
    public void onItemClicked(InventoryClickEvent e){
        if(e.getCurrentItem() == null) return;

        switch (e.getView().getTitle()){
            case "主選單":
                e.setCancelled(true);
                switch (e.getCurrentItem().getType()){
                    case EMERALD_BLOCK:
                        plugin.openPlayerListMenu((Player) e.getWhoClicked());
                        break;
                    case REDSTONE_BLOCK:
                        plugin.openBannedPlayerList((Player) e.getWhoClicked());
                        break;
                    case LAPIS_BLOCK:
                        plugin.openOfflinePlayersList((Player) e.getWhoClicked());
                }
                break;
            case "線上玩家清單":
                e.setCancelled(true);
                switch (e.getCurrentItem().getType()) {
                    case SKULL_ITEM:
                        plugin.openConfirmMenu((Player) e.getWhoClicked(), Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName()));
                        break;
                    case ARROW:
                        plugin.openMainMenu((Player) e.getWhoClicked());
                        break;
                }
                break;
            case "已封鎖玩家清單":
                e.setCancelled(true);
                switch (e.getCurrentItem().getType()) {
                    case SKULL_ITEM:
                        plugin.openBannedPlayerOptionMenu((Player) e.getWhoClicked(), Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getDisplayName()));
                        break;
                    case ARROW:
                        plugin.openMainMenu((Player) e.getWhoClicked());
                        break;
                }
                break;
            case "離線玩家清單":
                e.setCancelled(true);
                switch (e.getCurrentItem().getType()){
                    case SKULL_ITEM:
                    break;
                }
                break;
            case "確定要封鎖該玩家?":
                e.setCancelled(true);
                switch (e.getCurrentItem().getType()){
                    case BARRIER:
                        String whoBan = e.getWhoClicked().getName();
                        String targetName = e.getClickedInventory().getItem(4).getItemMeta().getDisplayName();
                        Player target = Bukkit.getPlayer(targetName);
                        Bukkit.getBanList(BanList.Type.NAME).addBan(targetName, "No season, just ban you for fun :D", null, whoBan);
                        target.kickPlayer("你被 " + e.getWhoClicked().getName() + " 封鎖了!!");
                        e.getWhoClicked().sendMessage(ChatColor.GREEN + "你已成功封鎖 " + ChatColor.RED + targetName);
                        e.getWhoClicked().closeInventory();
                        break;
                    case ARROW:
                        plugin.openPlayerListMenu((Player) e.getWhoClicked());
                        break;

                }
                break;
            case "已封鎖玩家設定":
                e.setCancelled(true);
                switch (e.getCurrentItem().getType()) {
                    case ANVIL:
                        Bukkit.getOfflinePlayer(e.getView().getItem(4).getItemMeta().getDisplayName()).setBanned(false);
                        e.getWhoClicked().sendMessage((ChatColor.GREEN + "你成功已將 " + ChatColor.AQUA + e.getView().getItem(4).getItemMeta().getDisplayName() + ChatColor.GREEN + " 解除封鎖"));
                        e.getWhoClicked().closeInventory();
                        break;
                    case ARROW:
                        plugin.openBannedPlayerList((Player) e.getWhoClicked());
                        break;
                }
        }
    }
}

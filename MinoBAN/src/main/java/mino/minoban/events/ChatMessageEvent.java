package mino.minoban.events;

import mino.minoban.MinoBAN;
import mino.minoban.utils.FileUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;

public class ChatMessageEvent implements Listener {
    private final MinoBAN plugin = MinoBAN.getInstance();
    private final HashMap<Player, Long> playerChatTime = new HashMap<>();
    public static boolean toggleChat = true;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSendMessage(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();

        if (!toggleChat) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("chat-has-been-closed")));
            e.setCancelled(true);
            return;
        }

        for (String info : FileUtil.getMutePlayers()){
            String[] infoArgs = info.split(", ");
            String reason = infoArgs[3].trim();
            long banTime = Long.parseLong(infoArgs[2].trim());
            if (info.contains(player.getUniqueId().toString()) || info.contains(player.getName())){
                if (banTime == -1L || System.currentTimeMillis() - banTime < 0){
                    final ConfigurationSection config = plugin.getConfig();
                    int day = 0, hour = 0, min = 0, sec = 0;
                    long currTime = System.currentTimeMillis();
                    sec = Math.toIntExact((banTime - currTime) / 1000);
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
                    if (banTime == -1) timeRemaining = config.getString("permanent");

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("mute-message")
                            +"\n"+plugin.getConfig().getString("mute-reason").replace("<reason>", reason)
                            +"\n"+plugin.getConfig().getString("mute-remaining").replace("<time>", timeRemaining)
                    ));
                    e.setCancelled(true);
                }else{
                    FileUtil.removeMute(player);
                }
                return;
            }
        }

        if (playerChatTime.containsKey(player)){
            if (System.currentTimeMillis() - playerChatTime.get(player) < (plugin.getConfig().getLong("chat-cooldown")*1000)){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("cooldown-message")));
                e.setCancelled(true);
            }else{
                playerChatTime.put(player, System.currentTimeMillis());
            }
        }else{
            playerChatTime.put(player, System.currentTimeMillis());
        }
    }
}

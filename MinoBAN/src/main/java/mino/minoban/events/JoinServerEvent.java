package mino.minoban.events;

import mino.minoban.MinoBAN;
import mino.minoban.utils.FileUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;

public class JoinServerEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent e){
        Player player = e.getPlayer();
        ArrayList<String> bansPlayer = FileUtil.getBanPlayers();
        bansPlayer.forEach(text -> {
            String[] playerInfo = text.split(",");
            String playerName = playerInfo[0].trim();
            String playerUUID = playerInfo[1].trim();
            long banTime = Long.parseLong(playerInfo[2].trim());
            String banReason = playerInfo[3].trim();
            final String banMessage = MinoBAN.getBanMessage(banReason, banTime);

            if (player.getUniqueId().toString().contains(playerUUID) || player.getName().equalsIgnoreCase(playerName)) {
                if (banTime == -1L || System.currentTimeMillis() - banTime < 0){
                    e.disallow(PlayerLoginEvent.Result.KICK_OTHER, banMessage);
                }else{
                    FileUtil.removeBan(player);
                }
            }
        });
    }
}

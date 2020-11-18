package mino.minoban.events;

import mino.minoban.utils.FileUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class QuitServerEvent implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        ArrayList<String> bansPlayer = FileUtil.getBanPlayers();
        for (String s : bansPlayer) {
            if (s.contains(player.getUniqueId().toString()) || s.contains(player.getName())) {
                e.setQuitMessage(null);
                break;
            }
        }
    }
}

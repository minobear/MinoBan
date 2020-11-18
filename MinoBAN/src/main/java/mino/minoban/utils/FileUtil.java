package mino.minoban.utils;

import mino.minoban.MinoBAN;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;

public class FileUtil {
    private static final MinoBAN plugin = MinoBAN.getInstance();

    public static ArrayList<String> banPlayers = new ArrayList<>();

    public static ArrayList<String> mutePlayers = new ArrayList<>();

    public static void loadBanAndMute(){
        refreshBanPlayers();
        refreshMutePlayers();
    }

    public static ArrayList<String> getBanPlayers() {
        return banPlayers;
    }

    public static ArrayList<String> getMutePlayers() {
        return mutePlayers;
    }

    public static void createBansFile(String fileName){
        File bansFile = new File(fileName);
        try {
            boolean result = bansFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addBan(Player player, String reason){
        String playerName = player.getName();
        String playerUUID = player.getUniqueId().toString();
        addBanProgram(playerName, playerUUID, -1L, reason);
    }

    public static void addBan(OfflinePlayer player, String reason){
        String playerName = player.getName();
        String playerUUID = player.getUniqueId().toString();
        addBanProgram(playerName, playerUUID, -1L, reason);
    }

    public static void addTBan(Player player, Long time, String reason){
        String playerName = player.getName();
        String playerUUID = player.getUniqueId().toString();
        addBanProgram(playerName, playerUUID, time, reason);
    }

    public static void addTBan(OfflinePlayer player, Long time, String reason){
        String playerName = player.getName();
        String playerUUID = player.getUniqueId().toString();
        addBanProgram(playerName, playerUUID, time, reason);
    }

    public static boolean removeBan(OfflinePlayer player){
        String playerName = player.getName();
        String playerUUID = player.getUniqueId().toString();
        return removeBanProgram(playerName, playerUUID);
    }

    public static void removeBan(Player player){
        String playerName = player.getName();
        String playerUUID = player.getUniqueId().toString();
        removeBanProgram(playerName, playerUUID);
    }

    public static void addMute(Player player, Long time, String reason){
        String playerName = player.getName();
        String playerUUID = player.getUniqueId().toString();
        addMuteProgram(playerName, playerUUID, time, reason);
    }

    public static void addMute(OfflinePlayer player, Long time, String reason){
        String playerName = player.getName();
        String playerUUID = player.getUniqueId().toString();
        addMuteProgram(playerName, playerUUID, time, reason);
    }

    public static boolean removeMute(OfflinePlayer player){
        String playerName = player.getName();
        String playerUUID = player.getUniqueId().toString();
        return removeMuteProgram(playerName, playerUUID);
    }

    private static void addBanProgram(String playerName, String playerUUID, Long time, String reason){
        FileWriter bansFile = null;

        while (banPlayers.toString().contains(playerUUID) || banPlayers.toString().contains(playerName)){
            for (int i=0; i<banPlayers.size(); i++){
                if (banPlayers.get(i).contains(playerUUID) || banPlayers.get(i).contains(playerName)){
                    banPlayers.remove(i);
                    break;
                }
            }
        }

        banPlayers.add(playerName+", "+playerUUID+", "+time+", "+reason);
        try {
            bansFile = new FileWriter(plugin.getConfig().getString("ban-file"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert bansFile != null;
        BufferedWriter bw = new BufferedWriter(bansFile);
        try {
            for (String info : banPlayers){
                bw.write(info);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        refreshBanPlayers();
    }

    private static boolean removeBanProgram(String playerName, String playerUUID){
        FileWriter fw = null;
        try {
            fw = new FileWriter(plugin.getConfig().getString("ban-file"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert fw != null;
        BufferedWriter bw = new BufferedWriter(fw);
        boolean findPlayer = false;

        while (banPlayers.toString().contains(playerUUID) || banPlayers.toString().contains(playerName)){
            for (int i=0; i<banPlayers.size(); i++){
                if (banPlayers.get(i).contains(playerUUID) || banPlayers.get(i).contains(playerName)){
                    banPlayers.remove(i);
                    findPlayer = true;
                    break;
                }
            }
        }

        for (String info : banPlayers){
            try {
                bw.write(info);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        refreshBanPlayers();
        return findPlayer;
    }

    private static void addMuteProgram(String playerUUID, String playerName, Long time, String reason){
        FileWriter fw = null;

        while (mutePlayers.toString().contains(playerUUID) || mutePlayers.toString().contains(playerName)){
            for (int i=0; i<mutePlayers.size(); i++){
                if (mutePlayers.get(i).contains(playerUUID) || mutePlayers.get(i).contains(playerName)){
                    mutePlayers.remove(i);
                    break;
                }
            }
        }

        mutePlayers.add(playerName+", "+playerUUID+", "+time+", "+reason);
        try {
            fw = new FileWriter(plugin.getConfig().getString("mute-file"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert fw != null;
        BufferedWriter bw = new BufferedWriter(fw);
        for (String info : mutePlayers){
            try {
                bw.write(info);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        refreshBanPlayers();
    }

    private static boolean removeMuteProgram(String playerName, String playerUUID){
        FileWriter fw = null;
        try {
            fw = new FileWriter(plugin.getConfig().getString("mute-file"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert fw != null;
        BufferedWriter bw = new BufferedWriter(fw);
        boolean findPlayer = false;

        while (mutePlayers.toString().contains(playerUUID) || mutePlayers.toString().contains(playerName)){
            for (int i=0; i<mutePlayers.size(); i++){
                if (mutePlayers.get(i).contains(playerUUID) || mutePlayers.get(i).contains(playerName)){
                    mutePlayers.remove(i);
                    findPlayer = true;
                    break;
                }
            }
        }

        for (String info : banPlayers){
            try {
                bw.write(info);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        refreshMutePlayers();
        return findPlayer;
    }

    public static void refreshBanPlayers(){
        FileReader fr = null;
        try {
            createBansFile(plugin.getConfig().getString("ban-file"));
            fr = new FileReader(new File(plugin.getConfig().getString("ban-file")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert fr != null;
        BufferedReader br = new BufferedReader(fr);
        ArrayList<String> banInfo = new ArrayList<>();
        String tmp;
        try {
            while((tmp = br.readLine()) != null) {
                banInfo.add(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        banPlayers = banInfo;
    }

    public static void refreshMutePlayers(){
        FileReader fr = null;
        try {
            createBansFile(plugin.getConfig().getString("mute-file"));
            fr = new FileReader(new File(plugin.getConfig().getString("mute-file")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert fr != null;
        BufferedReader br = new BufferedReader(fr);
        ArrayList<String> muteInfo = new ArrayList<>();
        String tmp;
        try {
            while((tmp = br.readLine()) != null) {
                muteInfo.add(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mutePlayers = muteInfo;
    }

}

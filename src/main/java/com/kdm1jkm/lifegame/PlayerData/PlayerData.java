package com.kdm1jkm.lifegame.PlayerData;

import com.google.gson.Gson;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerData {
    private Map<UUID, Player> playerMap;

    public PlayerData() {
        playerMap = new HashMap<>();
    }

    public static PlayerData Load(String filePath) {
        try {
            PlayerData result;
            File file = new File(filePath);
            if (!file.exists()) {
                Bukkit.getLogger().warning("[LifeGame] Error to load playerData.");
                return new PlayerData();
            }
            Gson gson = new Gson();
            result = gson.fromJson(new FileReader(file), PlayerData.class);
            return result;
        } catch (Exception ignored) {
        }

        Bukkit.getLogger().warning("[LifeGame] Error to load playerData.");
        return new PlayerData();
    }

    public boolean isPlayerRegistered(UUID uuid) {
        return playerMap.containsKey(uuid);
    }

    public void addPlayer(UUID uuid) {
        playerMap.put(uuid, new Player());
    }

    public void addPlayer(org.bukkit.entity.Player p) {
        addPlayer(p.getUniqueId());
    }

    public Player getPlayer(UUID uuid) {
        if (!playerMap.containsKey(uuid)) {
            addPlayer(uuid);
        }
        return playerMap.get(uuid);
    }

    public Set<UUID> getKeySet() {
        return playerMap.keySet();
    }

    public Player getPlayer(org.bukkit.entity.Player p) {
        return getPlayer(p.getUniqueId());
    }

    public void Save(String dir, String fileName) {
        try {
            Gson gson = new Gson();

            File d = new File(dir);
            d.mkdirs();
//            if (!d.mkdirs()) throw new FileLockInterruptionException();

            File file = new File(dir + File.separator + fileName);
            FileWriter fw = new FileWriter(file);
            fw.write(gson.toJson(this));
            fw.flush();
            fw.close();
        } catch (Exception ignored) {
        }
    }
}

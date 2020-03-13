package com.kdm1jkm.lifegame;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private Map<UUID, Player> playerMap;

    public PlayerData() {
        playerMap = new HashMap<>();
    }

    public static PlayerData Load(String filePath) {
        PlayerData result = new PlayerData();

        try {
            JSONParser parser = new JSONParser();
            JSONObject main;

            FileReader fr = new FileReader(new File(filePath));

            Bukkit.getLogger().info("[LifeGame] Successfully Read Player Data File.");

            main = (JSONObject) parser.parse(fr);

            Iterator iterator = main.keySet().iterator();

            while (iterator.hasNext()) {

                String key = iterator.next().toString();
                JSONObject playerInfo = (JSONObject) main.get(key);

                int money = Integer.parseInt(playerInfo.get("money").toString());

                result.addPlayer(UUID.fromString(key));
                result.getPlayer(UUID.fromString(key)).money.set(money);

                Bukkit.getLogger().info("[LifeGame] UUID: " + key);
                Bukkit.getLogger().info("[LifeGame] money: " + money);
            }
            fr.close();
        } catch (Exception e) {
        }

        return result;
    }

    public boolean isPlayerRegistered(UUID uuid) {
        return playerMap.containsKey(uuid);
    }

    public void addPlayer(UUID uuid) {
        playerMap.put(uuid, new Player());
    }

    public Player getPlayer(UUID uuid) {
        if (!playerMap.containsKey(uuid)) {
            addPlayer(uuid);
        }
        return playerMap.get(uuid);
    }

    public void Save(String dir, String fileName) {
        JSONObject main = new JSONObject();

        Iterator iterator = playerMap.keySet().iterator();

        while (iterator.hasNext()) {
            JSONObject playerInfo = new JSONObject();
            UUID key = (UUID) iterator.next();

            playerInfo.put("money", playerMap.get(key).money.get());

            main.put(key.toString(), playerInfo);
        }

        try {
            File d = new File(dir);
            d.mkdirs();

            File file = new File(dir + File.separator + fileName);
            FileWriter fw = new FileWriter(file);
            fw.write(main.toJSONString());
            fw.flush();
            fw.close();
        } catch (Exception e) {
        }
    }

    public class Player {
        public Money money;

        public Player() {
            money = new Money(0);
        }

        public class Money {
            private int num;

            public Money(int money) {
                num = money;
            }

            public int get() {
                return num;
            }

            public void set(int money) {
                num = money;
            }

            public void add(int money) {
                num += money;
            }

            public void sub(int money) {
                num -= money;
            }

            public void send(Player player, int num) {
                player.money.add(num);
                this.sub(num);
            }
        }
    }
}

package com.kdm1jkm.lifegame.Region;

import com.kdm1jkm.lifegame.KeyWord;
import com.kdm1jkm.lifegame.LifeGame;
import com.kdm1jkm.lifegame.PlayerData.PlayerData;
import com.kdm1jkm.lifegame.PlayerData.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class RegionManager {
    private PlayerData data;
    private World flatWorld;
    private Map<UUID, RegionInfo> waitingPlayers;

    public RegionManager(PlayerData data, World flatWorld, final LifeGame lifeGame) {
        this.data = data;
        this.flatWorld = flatWorld;
        waitingPlayers = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(new RegionEventListener(this), lifeGame);
    }

    public void teleportRegionWorld(Player p) {
        p.sendMessage(KeyWord.PREFIX_NORMAL + "부동산 월드로 이동합니다.");
        p.teleport(flatWorld.getSpawnLocation());
    }

    public void teleportBasicWorld(Player p) {
        p.sendMessage(KeyWord.PREFIX_NORMAL + "야생 월드로 이동합니다.");
        p.teleport(Bukkit.getWorld("world").getSpawnLocation());
    }

    public void createRegionStart(Player p) {
        waitingPlayers.put(p.getUniqueId(), new RegionInfo());
        p.sendMessage(KeyWord.PREFIX_NORMAL + "막대기로 범위를 설정하십시오.");
    }

    public boolean setRegionLoc1(Player player, Location loc) {
        if (waitingPlayers.containsKey(player.getUniqueId())) {
            Location temp = waitingPlayers.get(player.getUniqueId()).loc1;
            if (temp == null || temp.getBlockX() != loc.getBlockX() || temp.getBlockZ() != loc.getBlockZ()) {
                player.sendMessage(KeyWord.PREFIX_NORMAL + "X: " + loc.getBlockX() + ", Z: " + loc.getBlockZ() + "위치에 Pos1을 설정하였습니다.");
                waitingPlayers.get(player.getUniqueId()).loc1 = loc.clone();
            }
            return true;
        }
        return false;
    }

    public boolean setRegionLoc2(Player player, Location loc) {
        if (waitingPlayers.containsKey(player.getUniqueId())) {
            Location temp = waitingPlayers.get(player.getUniqueId()).loc2;
            if (temp == null || temp.getBlockX() != loc.getBlockX() || temp.getBlockZ() != loc.getBlockZ()) {
                player.sendMessage(KeyWord.PREFIX_NORMAL + "X: " + loc.getBlockX() + ", Z: " + loc.getBlockZ() + "위치에 Pos2를 설정하였습니다.");
                waitingPlayers.get(player.getUniqueId()).loc2 = loc.clone();
            }
            return true;
        }
        return false;
    }

    public void createRegion(Player p, String name) {
        if (waitingPlayers.containsKey(p.getUniqueId()) && waitingPlayers.get(p.getUniqueId()).isSetAll()) {
            Region newRegion = new Region(waitingPlayers.get(p.getUniqueId()).loc1, waitingPlayers.get(p.getUniqueId()).loc2, name);
            waitingPlayers.remove(p.getUniqueId());

            for (UUID other : data.getKeySet()) {
                if (data.getPlayer(other).Regions().isCollide(newRegion)) {
                    p.sendMessage(KeyWord.PREFIX_WARNING + "다른 사유지와 겹칩니다.");
                    return;
                }
            }
            p.sendMessage(KeyWord.PREFIX_NORMAL + "사유지를 생성했습니다.");

            data.getPlayer(p.getUniqueId()).Regions().add(newRegion);
        }
        else {
            p.sendMessage(KeyWord.PREFIX_WARNING + "Pos1과 Pos2를 모두 선택해 주십시오.");
        }
    }

    public void regionList(Player p){
        p.sendMessage(KeyWord.PREFIX_NORMAL + "--------지역 목록--------");
        for(Region r : data.getPlayer(p).Regions().get()){
            p.sendMessage(KeyWord.PREFIX_NORMAL + r.getName() + " X: " + r.getX1() + ", Z: " + r.getZ1() + " ~ X: " + r.getX2() + ", Z: " + r.getZ2());
        }
    }

    public boolean checkCollide(Player p, Location location) {
        for (UUID other : data.getKeySet()) {
            if (other == p.getUniqueId()) continue;
//            p.sendMessage(KeyWord.PREFIX_NORMAL + "검사중");

            if (data.getPlayer(other).Regions().isIn(location)) {
                p.sendMessage(KeyWord.PREFIX_WARNING + "사유지입니다.");
                return true;
            }
        }
        return false;
    }

    private class RegionInfo {
        public Location loc1, loc2;

        public boolean isSetAll() {
            return loc1 != null && loc2 != null;
        }
    }
}

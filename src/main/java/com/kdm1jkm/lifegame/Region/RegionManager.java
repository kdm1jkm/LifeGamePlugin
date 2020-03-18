package com.kdm1jkm.lifegame.Region;

import com.kdm1jkm.lifegame.KeyWord;
import com.kdm1jkm.lifegame.LifeGame;
import com.kdm1jkm.lifegame.PlayerData.PlayerData;
import com.kdm1jkm.lifegame.PlayerData.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class RegionManager {
    private PlayerData data;
    private World flatWorld;
    private Map<UUID, RegionInfo> waitingPlayers;

    public RegionManager(PlayerData data, World flatWorld, final LifeGame lifeGame) {
        this.data = data;
        this.flatWorld = flatWorld;
        waitingPlayers = new HashMap<>();
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
        createRegionStart(p, p);
    }

    public void createRegionStart(Player subject, Player target) {
        if(waitingPlayers.containsKey(subject.getUniqueId()))return;
        waitingPlayers.put(subject.getUniqueId(), new RegionInfo(target));
        subject.sendMessage(KeyWord.PREFIX_NORMAL + "나무 호미로 범위를 설정하십시오.");
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
        if (!waitingPlayers.containsKey(p.getUniqueId()) || !waitingPlayers.get(p.getUniqueId()).isSetAll()) {
            p.sendMessage(KeyWord.PREFIX_WARNING + "Pos1과 Pos2를 모두 선택해 주십시오.");
            return;
        }

        Region newRegion = new Region(waitingPlayers.get(p.getUniqueId()).loc1, waitingPlayers.get(p.getUniqueId()).loc2, name);
        Player target = waitingPlayers.get(p.getUniqueId()).targetPlayer;
        waitingPlayers.remove(p.getUniqueId());

        for (UUID other : data.getKeySet()) {
            if (data.getPlayer(other).Regions().isCollide(newRegion)) {
                p.sendMessage(KeyWord.PREFIX_WARNING + "다른 사유지와 겹칩니다.");
                return;
            }
        }

        if (p == target) {
            p.sendMessage(KeyWord.PREFIX_NORMAL + "사유지를 생성했습니다.");
        } else {
            p.sendMessage(KeyWord.PREFIX_NORMAL + target.getName() + "의 사유지를 생성했습니다.");
            target.sendMessage(KeyWord.PREFIX_NORMAL + p.getName() + "이(가) 당신의 사유지를 생성했습니다.");
        }

        data.getPlayer(target.getUniqueId()).Regions().add(newRegion);
    }

    public void regionList(Player p) {
        regionList(p, p);
    }

    public void regionList(Player check, Player send) {
        send.sendMessage(KeyWord.PREFIX_NORMAL + "--------지역 목록--------");
        for (Region r : data.getPlayer(check).Regions().get()) {
            send.sendMessage(KeyWord.PREFIX_NORMAL + r.getName() + " X: " + r.getX1() + ", Z: " + r.getZ1() + " ~ X: " + r.getX2() + ", Z: " + r.getZ2());
        }
    }

    public boolean checkCollide(Player p, Location location) {
        for (UUID other : data.getKeySet()) {
            if (other.equals(p.getUniqueId())) continue;

            if (data.getPlayer(other).Regions().isIn(location)) {
                p.sendMessage(KeyWord.PREFIX_WARNING + "사유지입니다.");
                return true;
            }
        }
        return false;
    }

    public void checkEnterRegion(Player p, Location from, Location to) {
        for (UUID other : data.getKeySet()) {
            if (data.getPlayer(other).Regions().isIn(to) && !data.getPlayer(other).Regions().isIn(from)) {
                p.sendMessage(KeyWord.PREFIX_NORMAL + Bukkit.getPlayer(other).getName() + "님의 지역에 입장하셨습니다.");
            }
        }
    }

    public void teleportRegion(Player p, String regionName) {
        for (Region r : data.getPlayer(p).Regions().get()) {
            if (r.getName().equals(regionName)) {
                p.teleport(new Location(flatWorld, r.getX1() - 1, 4, r.getZ1() - 1));
                p.sendMessage(KeyWord.PREFIX_NORMAL + r.getName() + "(으)로 이동합니다.");
                return;
            }
        }

        p.sendMessage(KeyWord.PREFIX_WARNING + regionName + "은(는) 존재하지 않습니다.");
    }

    public void deleteRegion(Player p, String regionName) {
        for (Region r : data.getPlayer(p).Regions().get()) {
            if (r.getName().equals(regionName)) {
                data.getPlayer(p).Regions().remove(r);
                p.sendMessage(KeyWord.PREFIX_NORMAL + r.getName() + "을(를) 삭제합니다.");
                return;
            }
        }

        p.sendMessage(KeyWord.PREFIX_WARNING + regionName + "은(는) 존재하지 않습니다.");
    }

    public List<String> getRegions(Player p) {
        List<String> result = new ArrayList<>();

        for (Region r : data.getPlayer(p).Regions().get()) {
            result.add(r.getName());
        }

        return result;
    }

    public boolean checkPermissionDeny(Player p) {
        if (p.isOp()) {
            return false;
        } else {
            p.sendMessage(KeyWord.PERMISSION_DENIED);
            return true;
        }
    }

    private class RegionInfo {
        public Location loc1, loc2;
        public Player targetPlayer;

        public RegionInfo(Player targetPlayer) {
            this.targetPlayer = targetPlayer;
        }

        public boolean isSetAll() {
            return loc1 != null && loc2 != null && targetPlayer != null;
        }
    }
}

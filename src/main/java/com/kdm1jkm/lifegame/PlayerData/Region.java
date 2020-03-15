package com.kdm1jkm.lifegame.PlayerData;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class Region {
    private int x1, z1, x2, z2;

    public int getX1() {
        return x1;
    }

    public int getZ1() {
        return z1;
    }

    public int getX2() {
        return x2;
    }

    public int getZ2() {
        return z2;
    }

    private UUID worldUUID;
    private String regionName;

    public Region(Location location1, Location location2, String regionName) {
        x1 = (int) Math.min(location1.getX(), location2.getX());
        z1 = (int) Math.min(location1.getZ(), location2.getZ());
        x2 = (int) Math.max(location1.getX(), location2.getX());
        z2 = (int) Math.max(location1.getZ(), location2.getZ());
        worldUUID = location1.getWorld().getUID();
        this.regionName = regionName;
    }

    public String getName() {
        return regionName;
    }

    public void setName(String regionName) {
        this.regionName = regionName;
    }

    public World getWorld() {
        return Bukkit.getWorld(worldUUID);
    }

    public boolean isIn(Location location) {
        if (location.getWorld() != getWorld()) {
            return false;
        }

        return location.getBlockX() >= x1 &&
                location.getBlockX() <= x2 &&
                location.getBlockZ() >= z1 &&
                location.getBlockZ() <= z2;
    }

    public boolean isCollide(Region region) {
        return !(region.x1 >= x2 ||
                region.x2 <= x1 ||
                region.z1 >= z2 ||
                region.z2 <= z1);
    }

    public Location getLocation1() {
        return new Location(Bukkit.getWorld(worldUUID), x1, 0, z1);
    }

    public Location getLocation2() {
        return new Location(Bukkit.getWorld(worldUUID), x2, 0, z2);
    }
}
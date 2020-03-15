package com.kdm1jkm.lifegame.PlayerData;

import org.bukkit.Location;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Regions {
    Set<Region> data;

    public Regions() {
        data = new HashSet<>();
    }

    public void add(Region region) {
        data.add(region);
    }

    public void remove(Region region) {
        data.remove(region);
    }

    public Iterator<Region> iterator() {
        return data.iterator();
    }

    public boolean isIn(Location location) {
        for (Region r : data) {
            if (r.isIn(location)) return true;
        }
        return false;
    }

    public boolean isCollide(Region region) {
        for (Region r : data) {
            if (r.isCollide(region)) return true;
        }
        return false;
    }
}
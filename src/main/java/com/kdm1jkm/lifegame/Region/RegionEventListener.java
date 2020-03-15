package com.kdm1jkm.lifegame.Region;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class RegionEventListener implements Listener {

    private RegionManager manager;

    public RegionEventListener(RegionManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.WOODEN_HOE) {
            if (manager.checkCollide(event.getPlayer(), event.getClickedBlock().getLocation())) return;
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                event.setCancelled(manager.setRegionLoc1(event.getPlayer(), event.getClickedBlock().getLocation()));
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(manager.setRegionLoc2(event.getPlayer(), event.getClickedBlock().getLocation()));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (manager.checkCollide(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (manager.checkCollide(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        manager.checkEnterRegion(event.getPlayer(), event.getFrom(), event.getTo());
    }
}

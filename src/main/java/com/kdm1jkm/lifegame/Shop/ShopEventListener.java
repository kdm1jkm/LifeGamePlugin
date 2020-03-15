package com.kdm1jkm.lifegame.Shop;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ShopEventListener implements Listener {
    private ShopManager manager;

    public ShopEventListener(ShopManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onClickBlock(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();

                manager.chooseShop(event.getPlayer(), sign.getLines());
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        if (manager.isPlayerWaiting(p)) {
            manager.sell(p, event.getMessage());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlaceBlock(SignChangeEvent event) {
        Player p = event.getPlayer();

        if (event.getLine(0).equals("[SHOP]")) {
            event.setCancelled(manager.makeShop(p, event.getLines()));
        }
    }
}

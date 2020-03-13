package com.kdm1jkm.lifegame.shop;

import com.kdm1jkm.lifegame.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShopEventListener implements Listener {

    private Map<UUID, SellInfo> waitingList = new HashMap<>();
    private PlayerData data;

    public ShopEventListener(PlayerData data) {
        this.data = data;
    }

    @EventHandler
    public void onPlace(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();

                if (sign.getLine(0).equals("[SHOP]")) {
                    String sellItem = sign.getLine(1).toUpperCase();
                    int sellPrice = Integer.parseInt(sign.getLine(2));

                    Material sellItemMaterial = Material.getMaterial(sellItem);
                    if (sellItemMaterial != null) {
                        Player p = event.getPlayer();
                        int itemCount = 0;

                        for (int i = 0; i < 36; i++) {
                            ItemStack invItem = p.getInventory().getItem(i);
                            if (invItem != null) {
                                if (invItem.getType().equals(sellItemMaterial)) {
                                    itemCount += invItem.getAmount();
                                }
                            }
                        }

                        if (waitingList.containsKey(p.getUniqueId())) {
                            waitingList.remove(p.getUniqueId());
                        }
                        waitingList.put(p.getUniqueId(), new SellInfo(sellItemMaterial, itemCount, sellPrice));
                        p.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.WHITE + sellItem +
                                "아이템을 몇 개 판매하시겠습니까? (현재 보유 갯수: " + itemCount + "개)");

                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        if (waitingList.containsKey(p.getUniqueId())) {
            event.setCancelled(true);

            SellInfo info = waitingList.get(p.getUniqueId());

            try {
                int amount = Integer.parseInt(event.getMessage());
                if (amount > info.maxAmount) {
                    p.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.RED + "당신은 " + amount + "개의 " + info.sellItem.toString() + "아이템을 가지고 있지 않습니다.");
                } else if (amount < 0) {
                    p.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.RED + "음수는 허용되지 않습니다.");
                } else {
                    p.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.WHITE + info.sellItem.toString() + "아이템 " + amount + "개를 팔아 " + info.price * amount + "원을 지급합니다.");
                    waitingList.remove(p.getUniqueId());

                    data.getPlayer(p.getUniqueId()).money.add(info.price * amount);

                    //인벤토리에서 아이템 제거
                    int leftAmount = amount;
                    while (leftAmount > 0) {
                        int inventoryIndex = 0;

                        //일치하는 아이템 찾기
                        while (p.getInventory().getItem(inventoryIndex) != null || inventoryIndex < 36) {
                            if (p.getInventory().getItem(inventoryIndex) != null) {
                                if (p.getInventory().getItem(inventoryIndex).getType().equals(info.sellItem)) {
                                    break;
                                }
                            }
                            inventoryIndex++;
                        }

                        if (leftAmount - p.getInventory().getItem(inventoryIndex).getAmount() > 0) {
                            leftAmount -= p.getInventory().getItem(inventoryIndex).getAmount();
                            p.getInventory().getItem(inventoryIndex).setAmount(0);
                        } else {
                            p.getInventory().getItem(inventoryIndex).setAmount(p.getInventory().getItem(inventoryIndex).getAmount() - leftAmount);
                            leftAmount = 0;
                        }
                    }
                }
            } catch (NumberFormatException e) {
                p.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.RED + event.getMessage() + "은(는) 유효한 값이 아닙니다.");
            }
        }
    }

    @EventHandler
    public void onPlaceBlock(SignChangeEvent event) {
        Player p = event.getPlayer();

        if (event.getLine(0).equals("[SHOP]")) {
            if (!p.isOp()) {
                event.setCancelled(true);
                p.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.RED + "권한이 부족해 상점을 만들 수 없습니다.");
                return;
            }

            Material sellMaterial = Material.getMaterial(event.getLine(1).toUpperCase());
            if (sellMaterial == null) {
                event.setCancelled(true);
                p.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.RED + "유효하지 않은 아이템 이름입니다.");
                return;
            }

            try {
                int price = Integer.parseInt(event.getLine(2));
                if (price < 0) throw new Exception("가격은 음수가 될 수 없습니다.");
                p.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.WHITE + sellMaterial.toString() + "상품을 " + event.getLine(2) + "원으로 판매합니다.");
            } catch (Exception e) {
                p.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.RED + "유효하지 않은 가격입니다.");
                event.setCancelled(true);
            }
        }
    }

    private class SellInfo {
        public Material sellItem;
        public int maxAmount;
        public int price;

        public SellInfo(Material sellItem, int maxAmount, int price) {
            this.sellItem = sellItem;
            this.maxAmount = maxAmount;
            this.price = price;
        }
    }
}

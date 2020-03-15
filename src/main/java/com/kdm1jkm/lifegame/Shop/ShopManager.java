package com.kdm1jkm.lifegame.Shop;

import com.kdm1jkm.lifegame.KeyWord;
import com.kdm1jkm.lifegame.PlayerData.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShopManager {

    private Map<UUID, SellInfo> waitingList = new HashMap<>();
    private PlayerData data;

    public ShopManager(PlayerData data) {
        this.data = data;
    }

    /**
     * 플레이어가 상점을 클릭했을때 처리. waitingList에 플레이어를 추가하고 안내메세지를 띄운다.
     *
     * @param p     거래를 진행할 플레이어
     * @param lines 표지판 내용
     */
    public void chooseShop(Player p, String[] lines) {
        if (lines[0].equals("[SHOP]")) {
            String sellItem = lines[1].toUpperCase();
            int sellPrice = Integer.parseInt(lines[2]);

            Material sellItemMaterial = Material.getMaterial(sellItem);

            if (sellItemMaterial == null) return;

            int itemCount = 0;

            for (int i = 0; i < 36; i++) {
                ItemStack invItem = p.getInventory().getItem(i);
                if (invItem != null) {
                    if (invItem.getType().equals(sellItemMaterial)) {
                        itemCount += invItem.getAmount();
                    }
                }
            }

            waitingList.remove(p.getUniqueId());
            waitingList.put(p.getUniqueId(), new SellInfo(sellItemMaterial, itemCount, sellPrice));

            p.sendMessage(KeyWord.PREFIX_NORMAL + sellItem + "아이템을 몇 개 판매하시겠습니까? (현재 보유 갯수: " + itemCount + "개)");
        }
    }

    /**
     * 채팅이 유효한 값인지 검사 후 판매 진행
     *
     * @param p    판매를 진행할 플레이어
     * @param chat 플레이어가 친 채팅
     */
    public void sell(Player p, String chat) {
        SellInfo info = waitingList.get(p.getUniqueId());
        int amount;

        try {
            amount = Integer.parseInt(chat);
        } catch (NumberFormatException e) {
            p.sendMessage(KeyWord.PREFIX_WARNING + chat + "은(는) 유효한 값이 아닙니다.");
            return;
        }

        if (amount > info.maxAmount || amount < 0) {
            p.sendMessage(KeyWord.INVALID_AMOUNT);
            return;
        }

        p.sendMessage(KeyWord.PREFIX_NORMAL + info.sellItem.toString() + "아이템 " + amount + "개를 팔아 " + info.price * amount + "원을 지급합니다.");

        waitingList.remove(p.getUniqueId());

        data.getPlayer(p.getUniqueId()).money().add(info.price * amount);

        //인벤토리에서 아이템 제거
        int leftAmount = amount;
        int i = 0;

        while (leftAmount > 0 && i < 36) {
            ItemStack curItem = p.getInventory().getItem(i);

            //일치하는 아이템 찾기
            while (curItem == null || !curItem.getType().equals(info.sellItem)) {
                curItem = p.getInventory().getItem(++i);
            }

            if (leftAmount - curItem.getAmount() > 0) {
                leftAmount -= curItem.getAmount();
                curItem.setAmount(0);
            } else {
                curItem.setAmount(curItem.getAmount() - leftAmount);
                leftAmount = 0;
            }
        }
    }

    /**
     * 상점이 유효한가 검사후 플레이어에게 관련 내용을 메세지로 전달.
     *
     * @param p     상점을 만드는 플레이어
     * @param lines 표지판 내용
     * @return 이벤트 취소 여부
     */
    public boolean makeShop(Player p, String[] lines) {
        if (!p.isOp()) {
            p.sendMessage(KeyWord.PREFIX_WARNING + "권한이 부족해 상점을 만들 수 없습니다.");
            return true;
        }

        Material sellMaterial = Material.getMaterial(lines[1].toUpperCase());
        if (sellMaterial == null) {
            p.sendMessage(KeyWord.PREFIX_WARNING + "유효하지 않은 아이템 이름입니다.");
            return true;
        }

        int price;
        try {
            price = Integer.parseInt(lines[2]);
        } catch (Exception e) {
            p.sendMessage(KeyWord.INVALID_AMOUNT);
            return true;
        }

        if (price < 0) {
            p.sendMessage(KeyWord.INVALID_AMOUNT);
            return true;
        }

        p.sendMessage(KeyWord.PREFIX_NORMAL + sellMaterial.toString() + "상품을 " + price + "원으로 판매합니다.");
        return false;
    }

    /**
     * 해당 플레이어가 waitingList목록에 있는지를 검사
     *
     * @param p 검사할 플레이어
     * @return 목록 존재 여부
     */
    public boolean isPlayerWaiting(Player p) {
        return waitingList.containsKey(p.getUniqueId());
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

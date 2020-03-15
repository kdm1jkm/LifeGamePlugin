package com.kdm1jkm.lifegame.Money;

import com.kdm1jkm.lifegame.KeyWord;
import com.kdm1jkm.lifegame.PlayerData.PlayerData;
import org.bukkit.entity.Player;

public class MoneyManager {
    private PlayerData data;

    public MoneyManager(PlayerData data) {
        this.data = data;
    }

    public void displayMoney(Player p) {
        p.sendMessage(KeyWord.PREFIX_NORMAL + "Money: " + data.getPlayer(p).money().get() + "원");
    }

    public void addMoney(Player player, Player otherPlayer, int amount) {
        if (data.getPlayer(otherPlayer).money().get() + amount < 0) {
            player.sendMessage(KeyWord.INVALID_AMOUNT);
            return;
        }

        data.getPlayer(otherPlayer).money().add(amount);

        player.sendMessage(KeyWord.PREFIX_NORMAL + otherPlayer.getName() + "에게 " + amount + "원을 지급하였습니다.");
        otherPlayer.sendMessage(KeyWord.PREFIX_NORMAL + player.getName() + "에게 " + amount + "원을 지급받았습니다.");
    }

    public void setMoney(Player player, Player otherPlayer, int amount) {
        if (amount < 0) {
            player.sendMessage(KeyWord.INVALID_AMOUNT);
            return;
        }

        data.getPlayer(otherPlayer).money().set(amount);

        player.sendMessage(KeyWord.PREFIX_NORMAL + otherPlayer.getName() + "의 돈을 " + amount + "원으로 설정했습니다.");
        otherPlayer.sendMessage(KeyWord.PREFIX_NORMAL + player.getName() + "이 당신의 돈을 " + amount + "원으로 설정했습니다.");
    }

    public void sendMoney(Player player, Player otherPlayer, int amount) {
        if (amount < 0) {
            player.sendMessage(KeyWord.INVALID_AMOUNT);
            return;
        }
        if(data.getPlayer(player).money().get() < amount){
            player.sendMessage(KeyWord.LACK_OF_MONEY(data.getPlayer(player).money().get()));
            return;
        }

        data.getPlayer(player).money().send(data.getPlayer(otherPlayer), amount);

        player.sendMessage(KeyWord.PREFIX_NORMAL + otherPlayer.getName() + "에게 " + amount + "원을 보냈습니다.");
        otherPlayer.sendMessage(KeyWord.PREFIX_NORMAL + player.getName() + "에게서 " + amount + "원을 받았습니다.");
    }
}


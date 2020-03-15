package com.kdm1jkm.lifegame.PlayerData;

public class Money {
    private int mMoney;

    public Money(int money) {
        mMoney = money;
    }

    public int get() {
        return mMoney;
    }

    public void set(int money) {
        mMoney = money;
    }

    public void add(int money) {
        mMoney += money;
    }

    public void send(Player player, int num) {
        player.money().add(num);
        this.add(-num);
    }
}
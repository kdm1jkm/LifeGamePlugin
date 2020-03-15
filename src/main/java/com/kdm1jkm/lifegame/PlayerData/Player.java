package com.kdm1jkm.lifegame.PlayerData;

public class Player {
    private Money money;
    private Regions regions;

    public Player(Money money, Regions regions) {
        this.money = money;
        this.regions = regions;
    }

    public Player() {
        money = new Money(0);
        regions = new Regions();
    }

    public Money money() {
        return money;
    }

    public Regions Regions() {
        return regions;
    }
}
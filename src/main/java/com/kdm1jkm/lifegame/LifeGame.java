package com.kdm1jkm.lifegame;

import com.kdm1jkm.lifegame.money.MoneyCommand;
import com.kdm1jkm.lifegame.money.MoneyCommandTapCompletion;
import com.kdm1jkm.lifegame.shop.ShopEventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class LifeGame extends JavaPlugin {

    public PlayerData data;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("[LifeGame] Loading Data.");
        LoadData();

        Bukkit.getLogger().info("[LifeGame] Registering Commands.");
        getCommand("money").setExecutor(new MoneyCommand(data));
        getCommand("money").setTabCompleter(new MoneyCommandTapCompletion());

        Bukkit.getLogger().info("[LifeGame] Enabling Shop System.");
        getServer().getPluginManager().registerEvents(new ShopEventListener(data), this);

        Bukkit.getLogger().info("[LifeGame] Plugin Enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SaveData();

        Bukkit.getLogger().info("[LifeGame] Plugin Disabled");
    }

    public void LoadData() {
        Bukkit.getLogger().info("[LifeGame] Loading Player Money Data.");
        data = PlayerData.Load(getDataFolder().getAbsolutePath() + File.separator + "playerData.json");
    }

    public void SaveData() {
        Bukkit.getLogger().info("[LifeGame] Saving Player Money Data.");
        data.Save(getDataFolder().getAbsolutePath(), "playerData.json");
    }
}

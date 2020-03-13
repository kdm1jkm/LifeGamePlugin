package com.kdm1jkm.lifegame;

import com.kdm1jkm.lifegame.commands.MoneyCommand;
import com.kdm1jkm.lifegame.commands.MoneyCommandTapCompletion;
import com.kdm1jkm.lifegame.shop.ShopEventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class LifeGame extends JavaPlugin {

    public PlayerData data;

    @Override
    public void onEnable() {
        // Plugin startup logic
        LoadData();

        Bukkit.getLogger().info("[LifeGame] Plugin Enabled");

        getCommand("money").setExecutor(new MoneyCommand(data));
        getCommand("money").setTabCompleter(new MoneyCommandTapCompletion());

        getServer().getPluginManager().registerEvents(new ShopEventListener(data), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SaveData();
        Bukkit.getLogger().info("[LifeGame] Plugin Disabled");
    }

    public void LoadData() {
        Bukkit.getLogger().info("[LifeGame] Reading " + getDataFolder().getAbsolutePath() + File.separator + "playerData.json");
        data = PlayerData.Load(getDataFolder().getAbsolutePath() + File.separator + "playerData.json");
    }

    public void SaveData() {
        data.Save(getDataFolder().getAbsolutePath(), "playerData.json");
    }
}

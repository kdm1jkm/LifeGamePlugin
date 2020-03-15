package com.kdm1jkm.lifegame;

import com.kdm1jkm.lifegame.Money.MoneyCommand;
import com.kdm1jkm.lifegame.Money.MoneyCommandTapCompletion;
import com.kdm1jkm.lifegame.PlayerData.PlayerData;
import com.kdm1jkm.lifegame.PlayerData.Region;
import com.kdm1jkm.lifegame.Region.*;
import com.kdm1jkm.lifegame.Shop.ShopEventListener;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class LifeGame extends JavaPlugin {

    public PlayerData data;
    private RegionManager regionManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("[LifeGame] Loading Data.");
        LoadData();

        Bukkit.getLogger().info("[LifeGame] Loading LifeGame Flat World.");
        WorldCreator wc = new WorldCreator("world_lifegame_flatworld").
                type(WorldType.FLAT).
                generateStructures(false);
        World world = wc.createWorld();
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setAnimalSpawnLimit(0);
        world.setTime(6000);

        Bukkit.getLogger().info("[LifeGame] Enabling Shop System.");
        getServer().getPluginManager().registerEvents(new ShopEventListener(data), this);

        Bukkit.getLogger().info("[LifeGame] Enabling Region System.");
        regionManager = new RegionManager(data, world, this);
        getServer().getPluginManager().registerEvents(new RegionEventListener(regionManager), this);

        Bukkit.getLogger().info("[LifeGame] Registering Commands.");
        getCommand("money").setExecutor(new MoneyCommand(data));
        getCommand("money").setTabCompleter(new MoneyCommandTapCompletion());

        getCommand("region").setExecutor(new RegionCommand(regionManager));
        getCommand("region").setTabCompleter(new RegionCommandTapCompletion());

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

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull  String[] args) {
        if(label.equals("rrr")){
            LoadData();
        }
        return true;
    }
}

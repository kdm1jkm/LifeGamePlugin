package com.kdm1jkm.lifegame.Region;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RegionCommandTapCompletion implements TabCompleter {
    private RegionManager manager;

    public RegionCommandTapCompletion(RegionManager manager) {
        this.manager = manager;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> result = new ArrayList<>();

        switch (args.length) {
            case 1:
                result.add("tp");
                result.add("back");
                result.add("create");
                result.add("list");
                break;

            case 2:
                switch (args[0]) {
                    case "create":
                        result.add("start");
                        result.add("confirm");
                        break;

                    case "tp":
                        Player p = (Player)sender;
                        result.addAll(manager.getRegions(p));
                        break;
                }
                break;
        }

//        result.removeIf(s -> !args[args.length - 1].equals(s.substring(0, args[args.length].length() - 1)));

        return result;
    }
}

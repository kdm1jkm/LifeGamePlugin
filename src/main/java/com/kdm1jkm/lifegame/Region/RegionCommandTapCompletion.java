package com.kdm1jkm.lifegame.Region;

import org.bukkit.Bukkit;
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
                result.add("list");
                result.add("del");
                if(sender.isOp()) {
                    result.add("create");
                }
                break;

            case 2:
                switch (args[0]) {
                    case "create":
                        if(!sender.isOp())break;
                        result.add("start");
                        result.add("confirm");
                        break;

                    case "tp":
                    case "del":
                        result.addAll(manager.getRegions((Player)sender));
                        break;

                    case "list":
                        if(!sender.isOp())break;
                        for(Player p : Bukkit.getOnlinePlayers()){
                            result.add(p.getName());
                        }
                        break;
                }
                break;

            case 3:
                if(args[0].equals("create") && args[1].equals("start")){
                    if(!sender.isOp())break;
                    for(Player p:Bukkit.getOnlinePlayers())
                        result.add(p.getName());
                }
                break;
        }

//        result.removeIf(s -> !args[args.length - 1].equals(s.substring(0, args[args.length].length() - 1)));

        return result;
    }
}

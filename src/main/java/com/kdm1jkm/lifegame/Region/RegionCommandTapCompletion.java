package com.kdm1jkm.lifegame.Region;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RegionCommandTapCompletion implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> result= new ArrayList<>();

        switch(args.length){
            case 1:
                result.add("tp");
                result.add("back");
                result.add("create");
                result.add("set");
                break;
        }

        return result;
    }
}

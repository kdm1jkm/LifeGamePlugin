package com.kdm1jkm.lifegame.money;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MoneyCommandTapCompletion implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();

        switch (args.length) {
            case 1:
                if (sender.isOp()) {
                    result.add("add");
                    result.add("set");
                }
                result.add("send");
                break;

            case 2:
                switch (args[0]) {
                    case "add":
                    case "set":
                    case "send":
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            result.add(p.getName());
                        }
                        break;
                }
                break;
        }


        return result;
    }
}

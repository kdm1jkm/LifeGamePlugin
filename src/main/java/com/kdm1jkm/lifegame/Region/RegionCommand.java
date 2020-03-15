package com.kdm1jkm.lifegame.Region;

import com.kdm1jkm.lifegame.KeyWord;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RegionCommand implements CommandExecutor {

    private RegionManager manager;

    public RegionCommand(RegionManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(KeyWord.CONSOLE_DENIED);
            return false;

        } else {
            Player p = (Player) sender;
            switch (args.length) {
                case 1:
                    switch (args[0]) {
                        case "tp":
                            manager.teleportRegionWorld(p);
                            break;

                        case "back":
                            manager.teleportBasicWorld(p);
                            break;

                        case "create":
                            manager.createRegionStart(p);
                            break;

                        default:
                            sendErrorCommandMessage(p);
                            break;
                    }
                    break;

                case 2:

                    if(args[0].equals("set")){
                            manager.createRegion(p, args[1]);
                    }
                    else{
                        sendErrorCommandMessage(p);
                    }


                default:
                    sendErrorCommandMessage(p);
                    break;
            }
        }
        return true;
    }

    private void sendErrorCommandMessage(Player p) {
        p.sendMessage(KeyWord.PREFIX_WARNING + "/region tp");
        p.sendMessage(KeyWord.PREFIX_WARNING + "/region back");
        p.sendMessage(KeyWord.PREFIX_WARNING + "/region create");
        p.sendMessage(KeyWord.PREFIX_WARNING + "/region set <RegionName>");
    }
}

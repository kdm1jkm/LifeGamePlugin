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

                        case "list":
                            manager.regionList(p);
                            break;

                        default:
                            sendErrorCommandMessage(p);
                            break;
                    }
                    break;

                case 2:
                    switch (args[0]) {
                        case "create":
                            switch (args[1]) {
                                case "start":
                                    if (manager.checkPermissionDeny(p))
                                        break;
                                    manager.createRegionStart(p);

                                    break;

                                default:
                                    sendErrorCommandMessage(p);
                                    break;

                            }
                            break;

                        case "tp":
                            manager.teleportRegion(p, args[1]);
                            break;

                        case "del":
                            manager.deleteRegion(p, args[1]);
                            break;

                        case "list":
                            if (manager.checkPermissionDeny(p))
                                break;
                            Player other = Bukkit.getPlayer(args[1]);
                            if (other == null) {
                                p.sendMessage(KeyWord.INVALID_PLAYER);
                                break;
                            }
                            manager.regionList(other, p);
                            break;

                        default:
                            sendErrorCommandMessage(p);
                            break;
                    }
                    break;

                case 3:
                    switch (args[0]) {
                        case "create":
                            switch (args[1]) {
                                case "confirm":
                                    if (manager.checkPermissionDeny(p))
                                        break;
                                    manager.createRegion(p, args[2]);
                                    break;

                                case "start":
                                    if(manager.checkPermissionDeny(p))break;
                                    Player target = Bukkit.getPlayer(args[2]);
                                    if(target==null){
                                        p.sendMessage(KeyWord.INVALID_PLAYER);
                                        break;
                                    }
                                    manager.createRegionStart(p, target);
                                    break;

                                default:
                                    sendErrorCommandMessage(p);
                                    break;
                            }
                            break;

                        default:
                            sendErrorCommandMessage(p);
                            break;
                    }
                    break;

                default:
                    sendErrorCommandMessage(p);
                    break;
            }
        }
        return true;
    }

    private void sendErrorCommandMessage(Player p) {
        p.sendMessage(KeyWord.PREFIX_WARNING + "/region tp");
        p.sendMessage(KeyWord.PREFIX_WARNING + "/region tp <RegionName>");
        p.sendMessage(KeyWord.PREFIX_WARNING + "/region back");
        p.sendMessage(KeyWord.PREFIX_WARNING + "/region list");
        if (p.isOp()) {
            p.sendMessage(KeyWord.PREFIX_WARNING + "/region create start");
            p.sendMessage(KeyWord.PREFIX_WARNING + "/region create start <Player>");
            p.sendMessage(KeyWord.PREFIX_WARNING + "/region create confirm <RegionName>");
            p.sendMessage(KeyWord.PREFIX_WARNING + "/region create confirm <RegionName>");
            p.sendMessage(KeyWord.PREFIX_WARNING + "/region del <RegionName>");
            p.sendMessage(KeyWord.PREFIX_WARNING + "/region list <Player>");
        }
    }
}

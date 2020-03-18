package com.kdm1jkm.lifegame.Money;

import com.kdm1jkm.lifegame.KeyWord;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MoneyCommand implements CommandExecutor {
    private MoneyManager manager;

    public MoneyCommand(MoneyManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            UUID playerUuid = p.getUniqueId();

            switch (args.length) {
                case 0:
                    manager.displayMoney(p);
                    break;

                case 1:
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        p.sendMessage(KeyWord.INVALID_PLAYER);
                        break;
                    }
                    manager.displayMoney(p, target);
                    break;

                case 3:
                    Player otherPlayer = Bukkit.getPlayer(args[1]);
                    if (otherPlayer == null) {
                        sender.sendMessage(KeyWord.INVALID_PLAYER);
                        break;
                    }

                    int num;
                    try {
                        num = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(KeyWord.INVALID_VALUE);
                        break;
                    }

                    switch (args[0]) {
                        case "add":
                            if (p.isOp()) {
                                manager.addMoney(p, otherPlayer, num);
                            } else {
                                p.sendMessage(KeyWord.PERMISSION_DENIED);
                            }
                            break;

                        case "set":
                            if (p.isOp()) {
                                manager.setMoney(p, otherPlayer, num);
                            } else {
                                p.sendMessage(KeyWord.PERMISSION_DENIED);
                            }
                            break;

                        case "send":
                            manager.sendMoney(p, otherPlayer, num);
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
            return true;
        } else {
            sender.sendMessage(KeyWord.CONSOLE_DENIED);
        }

        return true;
    }

    private void sendErrorCommandMessage(Player p) {
        p.sendMessage(KeyWord.PREFIX_WARNING + "/money");
        p.sendMessage(KeyWord.PREFIX_WARNING + "/money send <player> <amount>");
        if (p.isOp()) {
            p.sendMessage(KeyWord.PREFIX_WARNING + "/money <Player>");
            p.sendMessage(KeyWord.PREFIX_WARNING + "/money add <player> <amount>");
            p.sendMessage(KeyWord.PREFIX_WARNING + "/money set <player> <amount>");
        }
    }
}

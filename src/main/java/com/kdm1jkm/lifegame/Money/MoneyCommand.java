package com.kdm1jkm.lifegame.Money;

import com.kdm1jkm.lifegame.KeyWord;
import com.kdm1jkm.lifegame.PlayerData.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MoneyCommand implements CommandExecutor {

    private PlayerData data;
    private MoneyManager manager;

    public MoneyCommand(PlayerData d) {
        data = d;
        manager = new MoneyManager(data);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            UUID playerUuid = p.getUniqueId();

            if (!data.isPlayerRegistered(playerUuid)) {
                data.addPlayer(playerUuid);
            }

            switch (args.length) {
                case 0:
                    manager.displayMoney(p);
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

                    if (!data.isPlayerRegistered(otherPlayer.getUniqueId())) {
                        data.addPlayer(otherPlayer);
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
            sender.sendMessage(KeyWord.PREFIX_WARNING + "콘솔에서는 이 명령어를 실행할 수 없습니다.");
        }

        return true;
    }

    private void sendErrorCommandMessage(Player p) {
        p.sendMessage(KeyWord.PREFIX_WARNING + "/money : 현재 자신이 가진 돈을 확인합니다.");
        p.sendMessage(KeyWord.PREFIX_WARNING + "/money send <player> <amount> : <player>에게 <amount>만큼 자신의 돈을 보냅니다.");
    }
}

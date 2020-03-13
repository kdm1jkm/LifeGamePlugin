package com.kdm1jkm.lifegame.commands;

import com.kdm1jkm.lifegame.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MoneyCommand implements CommandExecutor {

    private PlayerData data;

    public MoneyCommand(PlayerData d) {
        data = d;
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
                    sender.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.WHITE + "Money: " + data.getPlayer(playerUuid).money.get() + "원");
                    break;

                case 3:
                    Player otherPlayer = Bukkit.getPlayer(args[1]);
                    if (otherPlayer == null) {
                        sender.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.RED + "유효하지 않은 플레이어명 입니다.");
                        break;
                    }
                    UUID otherPlayerUuid = otherPlayer.getUniqueId();
                    String otherPlayerName = otherPlayer.getName();

                    int num = Integer.parseInt(args[2]);

                    if (!data.isPlayerRegistered(otherPlayerUuid)) {
                        data.addPlayer(otherPlayerUuid);
                    }


                    switch (args[0]) {
                        case "add":
                            if (p.isOp()) {
                                data.getPlayer(otherPlayerUuid).money.add(num);
                                sender.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.WHITE + otherPlayerName + "에게 " + args[2] + "원을 지급하였습니다.");

                                otherPlayer.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.WHITE + sender.getName() + "에게 " + args[2] + "원을 지급받았습니다.");
                            }
                            break;
                        case "set":
                            if (p.isOp()) {
                                data.getPlayer(otherPlayerUuid).money.set(num);
                                sender.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.WHITE + otherPlayerName + "의 돈을 " + args[2] + "원으로 설정하였습니다.");

                                otherPlayer.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.WHITE + sender.getName() + "이 당신의 돈을 " + args[2] + "원으로 설정하였습니다.");
                            }
                            break;

                        case "send":
                            if (num > data.getPlayer(playerUuid).money.get()) {
                                sender.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.RED + "잔액이 부족합니다. (현재 잔액 " + data.getPlayer(playerUuid).money.get() + "원)");
                            } else if (num < 0) {
                                sender.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.RED + "마이너스 금액은 보낼 수 없습니다!");
                            } else {
                                data.getPlayer(playerUuid).money.send(data.getPlayer(otherPlayerUuid), num);

                                sender.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.WHITE + otherPlayerName + "에게 " + args[2] + "원을 보냈습니다.");

                                otherPlayer.sendMessage(ChatColor.YELLOW + "[LifeGame] " + ChatColor.WHITE + sender.getName() + "에게서 " + args[2] + "원을 받았습니다.");
                            }
                    }

                default:
                    return false;
            }

            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "콘솔에서는 이 명령어를 실행할 수 없습니다.");
        }

        return false;
    }
}

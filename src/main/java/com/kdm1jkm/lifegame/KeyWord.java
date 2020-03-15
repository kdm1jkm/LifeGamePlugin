package com.kdm1jkm.lifegame;

import org.bukkit.ChatColor;

public class KeyWord {
    public static final String PREFIX_NORMAL = ChatColor.YELLOW + "[LifeGame] " + ChatColor.WHITE;
    public static final String PREFIX_WARNING = ChatColor.YELLOW + "[LifeGame] " + ChatColor.RED;
    public static final String INVALID_PLAYER = PREFIX_WARNING + "유효하지 않은 플레이어명 입니다.";
    public static final String INVALID_AMOUNT = PREFIX_WARNING + "유효하지 않은 금액입니다.";
    public static final String INVALID_VALUE = PREFIX_WARNING + "유효하지 않은 값입니다.";
    public static final String PERMISSION_DENIED = PREFIX_WARNING + "권한이 부족합니다.";
    public static final String CONSOLE_DENIED = PREFIX_WARNING + "콘솔에서는 이 명령어를 실행하실 수 없습니다.";

    public static String LACK_OF_MONEY(int num) {
        return PREFIX_WARNING + "잔액이 부족합니다. (현재 잔액: " + num + "원)";
    }
}

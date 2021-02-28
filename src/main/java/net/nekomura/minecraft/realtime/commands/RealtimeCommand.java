package net.nekomura.minecraft.realtime.commands;

import net.nekomura.minecraft.realtime.Main;
import net.nekomura.minecraft.realtime.TimezoneUtils;
import net.nekomura.minecraft.realtime.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.*;

public class RealtimeCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("realtime.admin")) {  //如果沒有realtime.admin權限
            sender.sendMessage(Message.getTranslatedString("no-permission"));
            return true;
        }else {  //有realtime.admin權限
            if (args.length == 0) {  //如果args長度為零，也就是指令只有/realtime
                //發給sender簡介
                sender.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "RealTime"
                        + ChatColor.GOLD + ">>>" + ChatColor.AQUA + "\nMADE BY: 貓村幻影");
                return true;
            }else if (args[0].equals("reload")) {  //如果指令是/realtime reload
                String oldTimezone = Main.plugin.getConfig().getString("timezone");
                //重新載入config檔案
                Main.plugin.reloadConfig();
                String newTimezone = Main.plugin.getConfig().getString("timezone");
                if (TimezoneUtils.isLowerDay(oldTimezone, newTimezone) || TimezoneUtils.isGreaterDay(oldTimezone, newTimezone)) {
                    for (String worldName: Main.plugin.getConfig().getStringList("world")) {
                        World world = Bukkit.getWorld(worldName);
                        //如果沒有這個世界，則為null，剩下直接跳到下一個迴圈
                        if (world == null)
                            continue;
                        world.setFullTime(world.getFullTime() + TimezoneUtils.dayBetween(oldTimezone, newTimezone) * 24000L);
                    }
                }
                sender.sendMessage(Message.getTranslatedString("reloaded"));
                return true;
            }else if (args[0].equals("timezone")) {
                if (args.length >= 2) {
                    if (args[1].equals("set")) {
                        if (args.length >= 3) {  //如果args(指令參數)大於等於3
                            String oldTimezone = Main.plugin.getConfig().getString("timezone");  //獲取config裡的舊時區
                            String newTimezone = args[2];  //獲取欲設定的新時區
                            //雙重驗證是否為可用時區
                            String[] availableTimezone = TimeZone.getAvailableIDs();
                            boolean isAvailableTimezone;

                            try {
                                ZoneId.of(newTimezone);
                                isAvailableTimezone = true;
                            }catch (DateTimeException e) {
                                isAvailableTimezone = false;
                            }

                            if (isAvailableTimezone || Arrays.asList(availableTimezone).contains(newTimezone)) {  //是可用時區
                                //設定config的新的timezone
                                Main.plugin.getConfig().set("timezone", newTimezone);
                                //儲存config
                                Main.plugin.saveConfig();
                                if (TimezoneUtils.isLowerDay(oldTimezone, newTimezone) || TimezoneUtils.isGreaterDay(oldTimezone, newTimezone)) {
                                    for (String worldName: Main.plugin.getConfig().getStringList("world")) {
                                        World world = Bukkit.getWorld(worldName);
                                        //如果沒有這個世界，則為null，剩下直接跳到下一個迴圈
                                        if (world == null)
                                            continue;
                                        world.setFullTime(world.getFullTime() + TimezoneUtils.dayBetween(oldTimezone, newTimezone) * 24000L);
                                    }
                                }
                                //發送message給sender
                                sender.sendMessage(Message.getTranslatedString("timezone-changed", oldTimezone, newTimezone));
                            }else {  //不是可用時區
                                sender.sendMessage(Message.getTranslatedString("invalid-timezone"));
                            }
                        }else {  //args(指令參數)沒有大於等於2(相當於小於2 )
                            sender.sendMessage(Message.getTranslatedString("timezone-set-usage"));
                        }
                        return true;
                    }else if (args[1].equals("get")) {
                        sender.sendMessage(Message.getTranslatedString("current-timezone", Main.plugin.getConfig().getString("timezone")));
                        return true;
                    }
                }else {
                    sender.sendMessage(Message.getTranslatedString("timezone-usage"));
                    return true;
                }
            }else if (args[0].equals("world")) {
                if (args.length >= 2) {
                    switch (args[1]) {
                        case "add":
                            if (args.length >= 3) {  //如果args(指令參數)大於等於3
                                List<String> worlds = Main.plugin.getConfig().getStringList("world");  //獲取啟用RealTime功能的世界

                                if (Bukkit.getWorld(args[2]) == null) {
                                    sender.sendMessage(Message.getTranslatedString("world-not-exist", args[2]));
                                    return true;
                                }

                                if (!worlds.contains(args[2])) {
                                    worlds.add(args[2]);
                                    Main.plugin.getConfig().set("world", worlds);
                                    Main.plugin.saveConfig();
                                    sender.sendMessage(Message.getTranslatedString("enabled-world", args[2]));
                                }else {
                                    sender.sendMessage(Message.getTranslatedString("world-already-enabled", args[2]));
                                }
                            } else {  //args(指令參數)沒有大於等於3(相當於小於3 )
                                sender.sendMessage(Message.getTranslatedString("timezone-world-set-usage", args[2]));
                            }
                            return true;
                        case "remove":
                            if (args.length >= 3) {  //如果args(指令參數)大於等於3
                                List<String> worlds = Main.plugin.getConfig().getStringList("world");  //獲取啟用RealTime功能的世界
                                if (worlds.contains(args[2])) {
                                    worlds.remove(args[2]);
                                    Main.plugin.getConfig().set("world", worlds);
                                    Main.plugin.saveConfig();
                                    Bukkit.getWorld(args[2]).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
                                    sender.sendMessage(Message.getTranslatedString("disabled-world", args[2]));
                                } else {
                                    sender.sendMessage(Message.getTranslatedString("world-had-not-been-enabled", args[2]));
                                }
                                return true;
                            }
                            break;
                        case "addthisworld":
                            if (sender instanceof Player) {
                                Player player = (Player) sender;
                                World locateWorld = player.getWorld(); //獲取玩家所在世界

                                List<String> worlds = Main.plugin.getConfig().getStringList("world");  //獲取啟用RealTime功能的世界

                                if (!worlds.contains(locateWorld.getName())) {
                                    worlds.add(locateWorld.getName());
                                    Main.plugin.getConfig().set("world", worlds);
                                    Main.plugin.saveConfig();
                                    sender.sendMessage(Message.getTranslatedString("enabled-world", locateWorld.getName()));
                                }else {
                                    sender.sendMessage(Message.getTranslatedString("world-already-enabled", locateWorld.getName()));
                                }
                            } else {
                                sender.sendMessage(Message.getTranslatedString("player-only-command"));
                            }
                            return true;
                        case "removethisworld":
                            if (sender instanceof Player) {
                                Player player = (Player) sender;
                                World locateWorld = player.getWorld(); //獲取玩家所在世界

                                List<String> worlds = Main.plugin.getConfig().getStringList("world");  //獲取啟用RealTime功能的世界

                                if (worlds.contains(locateWorld.getName())) {
                                    worlds.remove(locateWorld.getName());
                                    Main.plugin.getConfig().set("world", worlds);
                                    Main.plugin.saveConfig();
                                    locateWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
                                    sender.sendMessage(Message.getTranslatedString("disabled-world", locateWorld.getName()));
                                } else {
                                    sender.sendMessage(Message.getTranslatedString("world-had-not-been-enabled", locateWorld.getName()));
                                }
                            } else {
                                sender.sendMessage(Message.getTranslatedString("player-only-command"));
                            }
                            return true;
                    }
                }else {
                    return false;
                }
            }else if (args[0].equals("language")) {
                if (args.length == 2) {
                    Main.plugin.getConfig().set("lang", args[1]);
                    sender.sendMessage(Message.getTranslatedString("language-set", args[1]));
                    return true;
                }else {
                    sender.sendMessage(Message.getTranslatedString("language-cmd-usage"));
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 3) {
            if (args[1].equals("add") && args[0].equals("world")) {
                List<String> worldNames = new ArrayList<>();
                List<World> worlds = Bukkit.getWorlds();
                for (World w: worlds) {
                    worldNames.add(w.getName());
                }
                return worldNames;
            }else if (args[1].equals("remove") && args[0].equals("world")) {
                return Main.plugin.getConfig().getStringList("world");
            }
            return Collections.emptyList();
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("timezone")) {
                String[] list = {"set", "get"};
                return Arrays.asList(list);
            }else if (args[0].equalsIgnoreCase("reload")) {
                return Collections.emptyList();
            }else if (args[0].equalsIgnoreCase("world")) {
                String[] list = {"add", "remove", "addthisworld", "removethisworld"};
                return Arrays.asList(list);
            }else if (args[0].equalsIgnoreCase("language")) {
                File langDir = new File(Main.plugin.getDataFolder(), "langs");
                String[] files = langDir.list((dir1, name) -> name.toLowerCase().endsWith(".yml"));
                String[] lang = new String[files.length];
                for (int i = 0; i < files.length; i++) {
                    lang[i]  = files[i].substring(0, files[i].length() - 4);
                }
                return Arrays.asList(lang);
            }
        }
        if (args.length == 1) {
            String[] list = {"reload", "timezone", "world", "language"};
            return Arrays.asList(list);
        }
        return null;
    }
}

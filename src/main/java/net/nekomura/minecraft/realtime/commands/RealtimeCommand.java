package net.nekomura.minecraft.realtime.commands;

import net.nekomura.minecraft.realtime.Main;
import net.nekomura.minecraft.realtime.TimezoneUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public class RealtimeCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("realtime.admin")) {  //如果沒有realtime.admin權限
            sender.sendMessage(ChatColor.RED + "你沒有使用此指令的權限。");
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
                        world.setFullTime(world.getFullTime() + TimezoneUtils.dayBetween(oldTimezone, newTimezone) * 24000);
                    }
                }
                sender.sendMessage("reload完畢！");
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
                                        world.setFullTime(world.getFullTime() + TimezoneUtils.dayBetween(oldTimezone, newTimezone) * 24000);
                                    }
                                }
                                //發送message給sender
                                sender.sendMessage(String.format("已將時區由%s改為%s！", oldTimezone, newTimezone));
                            }else {  //不是可用時區
                                sender.sendMessage(ChatColor.RED + "錯誤的時區！");
                            }
                        }else {  //args(指令參數)沒有大於等於2(相當於小於2 )
                            sender.sendMessage(ChatColor.RED + "用法: /realtime timezone set <時區>");
                        }
                        return true;
                    }else if (args[1].equals("get")) {
                        sender.sendMessage("現在的時區為" + Main.plugin.getConfig().getString("timezone"));
                        return true;
                    }
                }else {
                    sender.sendMessage("用法: /realtime timezone set <args> 或 /realtime timezone get");
                    return true;
                }
            }else if (args[0].equals("world")) {
                if (args.length >= 2) {
                    switch (args[1]) {
                        case "add":
                            if (args.length >= 3) {  //如果args(指令參數)大於等於3
                                List<String> worlds = Main.plugin.getConfig().getStringList("world");  //獲取啟用RealTime功能的世界

                                if (Bukkit.getWorld(args[2]) == null) {
                                    sender.sendMessage(ChatColor.RED + "世界 " + args[2] + " 不存在");
                                    return true;
                                }

                                worlds.add(args[2]);
                                Main.plugin.getConfig().set("world", worlds);
                                Main.plugin.saveConfig();
                                sender.sendMessage("已啟用世界 " + args[2]);
                            } else {  //args(指令參數)沒有大於等於3(相當於小於3 )
                                sender.sendMessage(ChatColor.RED + "用法: /realtime world set <世界名>");
                            }
                            return true;
                        case "remove":
                            if (args.length >= 3) {  //如果args(指令參數)大於等於3
                                List<String> worlds = Main.plugin.getConfig().getStringList("world");  //獲取啟用RealTime功能的世界
                                if (worlds.contains(args[2])) {
                                    worlds.remove(args[2]);
                                    Main.plugin.getConfig().set("world", worlds);
                                    Main.plugin.saveConfig();
                                    sender.sendMessage("已禁用世界 " + args[2]);
                                } else {
                                    sender.sendMessage(ChatColor.RED + "世界 " + args[2] + " 未啟用");
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
                                    sender.sendMessage("已啟用世界 " + locateWorld.getName());
                                } else {
                                    sender.sendMessage(ChatColor.RED + "世界 " + locateWorld.getName() + " 已啟用");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "必須是玩家才能使用此指令！");
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
                                    sender.sendMessage("已禁用世界 " + locateWorld.getName());
                                } else {
                                    sender.sendMessage(ChatColor.RED + "世界 " + locateWorld.getName() + " 未啟用");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "必須是玩家才能使用此指令！");
                            }
                            return true;
                    }
                }else {
                    return false;
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 3) {
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
            }
        }
        if (args.length == 1) {
            String[] list = {"reload", "timezone", "world"};
            return Arrays.asList(list);
        }
        return null;
    }
}

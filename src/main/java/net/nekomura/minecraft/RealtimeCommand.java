package net.nekomura.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.TimeZone;

public class RealtimeCommand implements CommandExecutor {
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
                if (TimezoneUtils.isLowerDay(oldTimezone, newTimezone)) {
                    for (String worldName: Main.plugin.getConfig().getStringList("world")) {
                        World world = Bukkit.getWorld(worldName);
                        //如果沒有這個世界，則為null，剩下直接跳到下一個迴圈
                        if (world == null)
                            continue;
                        world.setFullTime(world.getFullTime() + TimezoneUtils.dayBetween(oldTimezone, newTimezone) * 24000);
                    }
                }else if (TimezoneUtils.isGreaterDay(oldTimezone, newTimezone)) {
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
                        if (args.length >= 3) {  //如果args(指令參數)大於等於2
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
                                if (TimezoneUtils.isLowerDay(oldTimezone, newTimezone)) {
                                    for (String worldName: Main.plugin.getConfig().getStringList("world")) {
                                        World world = Bukkit.getWorld(worldName);
                                        //如果沒有這個世界，則為null，剩下直接跳到下一個迴圈
                                        if (world == null)
                                            continue;
                                        world.setFullTime(world.getFullTime() + TimezoneUtils.dayBetween(oldTimezone, newTimezone) * 24000);
                                    }
                                }else if (TimezoneUtils.isGreaterDay(oldTimezone, newTimezone)) {
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
                    }
                }else {
                    sender.sendMessage("用法: /realtime timezone set <args> 或 /realtime timezone get");
                }
            }
        }
        return false;
    }
}

package net.nekomura.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.*;

public class Main extends JavaPlugin {

    static Map<World, Boolean> addDay = new HashMap<World, Boolean>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equals("realtime")) {  // 指令 realtime
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
                    //重新載入config檔案
                    this.reloadConfig();
                    sender.sendMessage("reload完畢！");
                    return true;
                }else if (args[0].equals("settimezone")) {  //如果指令是/realtime timezone ...
                    if (args.length >= 2) {  //如果args(指令參數)大於等於2
                        String oldTimezone = this.getConfig().getString("timezone");  //獲取config裡的舊時區
                        String newTimezone = args[1];  //獲取欲設定的新時區
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
                            this.getConfig().set("timezone", newTimezone);
                            //儲存config
                            this.saveConfig();
                            //發送message給sender
                            sender.sendMessage(String.format("已將時區由%s改為%s！", oldTimezone, newTimezone));
                        }else {  //不是可用時區
                            sender.sendMessage(ChatColor.RED + "錯誤的時區！");
                        }
                    }else {  //args(指令參數)沒有大於等於2(相當於小於2 )
                        sender.sendMessage(ChatColor.RED + "用法: /realtime settimezone <時區>");
                    }
                    return true;
                }else if (args[0].equals("gettimezone")) {
                    sender.sendMessage("現在的時區為" + this.getConfig().getString("timezone"));
                }
            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();  //如果config文件不存在則儲存預設config.yml
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            //獲取config文件中的需要同步時間的世界名稱
            List<String> worlds = Main.super.getConfig().getStringList("world");
            for (String worldName: worlds) {  //用foreach循環設定時間
                //獲取和世界名稱相同的世界
                World world = Bukkit.getWorld(worldName);
                //如果沒有這個世界，則為null，剩下直接跳到下一個迴圈
                if (world == null)
                    continue;
                //先設定遊戲時間停止
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                //得到config裡的時區
                String timezone = Main.super.getConfig().getString("timezone");

                if (!addDay.containsKey(world))
                    addDay.put(world, true);

                //獲取當前時間
                Calendar c = Calendar.getInstance();
                //設定時區
                c.setTimeZone(TimeZone.getTimeZone(timezone));

                int hour = c.get(Calendar.HOUR_OF_DAY);  //獲取現在時
                int minute = c.get(Calendar.MINUTE);  //獲取現在分
                int second = c.get(Calendar.SECOND);  //獲取現在秒

                //Minecraft 0點相當於現實6點，故給hour - 6
                if (hour < 6)
                    hour += 18;  //hour = hour + 24 - 6
                else
                    hour -= 6;

                //在Minecraft時間中一天的流逝為0~24000，可以看成千位數以上為小時，而百位數以下為分鐘與秒
                //假想一個時間單位，1000時間單位=1小時，1小時有3600秒，故時間單位=秒/3.6
                double milliHour = (minute * 60 + second) / 3.6;

                //將小時*1000加上剛剛的假想時間單位
                int time = (int) (hour * 1000 + milliHour);

                if (time == 0 && addDay.get(world)) {
                    time = 24000;
                    addDay.put(world, false);
                }else if (time != 0)
                    addDay.put(world, true);

                //獲取現在時間
                long nowTime = world.getTime();
                //獲取現在完整時間，包含日的變化
                long nowFullTime = world.getFullTime();

                //將新的時間設定為現在完整時間(可慮日)加上現在相對時間(不可慮日)的時間變化
                long newFullTime = nowFullTime + time - nowTime;

                if (newFullTime - nowFullTime > 18000) {
                    newFullTime -= 24000;
                }else if (newFullTime - nowFullTime < -18000) {
                    newFullTime += 24000;
                }

                //設定時間
                world.setFullTime(newFullTime);
            }
        }, 0L, 1L);  //20L相當於20tick，也就是每秒執行一次
    }
}
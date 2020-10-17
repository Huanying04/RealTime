package net.nekomura.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Main extends JavaPlugin {

    public static JavaPlugin plugin;
    static Map<World, Boolean> addDay = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equals("gettime")) {
            Player player = (Player)sender;
            player.sendMessage("time: " + player.getWorld().getTime() + "\n" +
                    "full time: " + player.getWorld().getFullTime());
        }
        getCommand("realtime").setExecutor(new RealtimeCommand());
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("realtime") || command.getName().equalsIgnoreCase("rt")) {
            if (args.length == 1) {
                String[] list = {"reload", "timezone"};
                return Arrays.asList(list);
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("timezone")) {
                    String[] list = {"set", "get"};
                    return Arrays.asList(list);
                }
            }
        }
        return null;
    }

    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();  //如果config文件不存在則儲存預設config.yml
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {

        }
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

                //設定時間
                world.setFullTime(newFullTime);
            }
        }, 0L, 1L);  //20L相當於20tick，也就是每秒執行一次
    }
}
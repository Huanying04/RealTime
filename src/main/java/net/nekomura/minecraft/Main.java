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
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

public class Main extends JavaPlugin {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equals("realtime")) {
            if (!sender.hasPermission("realtime.admin")) {
                sender.sendMessage(ChatColor.RED + "你沒有使用此指令的權限。");
                return true;
            }else {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "RealTime"
                    + ChatColor.GOLD + ">>>" + ChatColor.AQUA + "\nMADE BY: 貓村幻影");
                    return true;
                }else if (args[0].equals("reload")) {
                    this.reloadConfig();
                    sender.sendMessage("reload完畢！");
                    return true;
                }else if (args[0].equals("settimezone")) {
                    if (args.length >= 2) {
                        String oldTimezone = this.getConfig().get("timezone").toString();
                        String newTimezone = args[1];
                        String[] availableTimezone = TimeZone.getAvailableIDs();
                        boolean isAvailableTimezone;

                        try {
                            ZoneId.of(newTimezone);
                            isAvailableTimezone = true;
                        }catch (DateTimeException e) {
                            isAvailableTimezone = false;
                        }

                        if (isAvailableTimezone || Arrays.stream(availableTimezone).anyMatch(newTimezone::equals)) {
                            this.getConfig().set("timezone", newTimezone);
                            this.saveConfig();
                            sender.sendMessage(String.format("已將時區由%s改為%s！", oldTimezone, newTimezone));
                        }else {
                            sender.sendMessage(ChatColor.RED + "錯誤的時區！");
                        }
                    }else {
                        sender.sendMessage(ChatColor.RED + "用法: /realtime settimezone <時區>");
                    }
                    return true;
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
        this.saveDefaultConfig();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (World world: Bukkit.getWorlds()) {
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                String timezone = Main.super.getConfig().get("timezone").toString();

                Calendar c = Calendar.getInstance();
                c.setTimeZone(TimeZone.getTimeZone(timezone));

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                int second = c.get(Calendar.SECOND);

                if (hour < 6)
                    hour += 18;  //hour = hour + 24 - 6
                else
                    hour -= 6;

                double centSecond = (minute * 60 + second) / 3.6;

                int time = (int) (hour * 1000 + centSecond);

                world.setTime(time);
            }
        }, 0L, 20L);
    }
}
package net.nekomura.minecraft.realtime.lang;

import net.nekomura.minecraft.realtime.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Message {
    private static FileConfiguration getLanguageFile() {
        String currentLang = Main.plugin.getConfig().getString("lang");

        File f = new File(Main.plugin.getDataFolder(), "langs/" + currentLang + ".yml");
        if (!f.exists()) {
            f = new File(Main.plugin.getDataFolder(), "langs/en_us.yml");
        }
        return YamlConfiguration.loadConfiguration(f);
    }

    public static String getTranslatedString(String path, String... formatted) {
        FileConfiguration f = getLanguageFile();
        String translated = f.getString(path);
        for (int i = 0; i < formatted.length; i++) {
            translated = translated.replaceAll("%" + (i + 1), formatted[i]);
        }
        return translated;
    }
}
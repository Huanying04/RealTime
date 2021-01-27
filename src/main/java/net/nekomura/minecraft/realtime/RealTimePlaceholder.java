package net.nekomura.minecraft.realtime;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class RealTimePlaceholder extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "realtime";
    }

    @Override
    public String getAuthor() {
        return "貓村幻影";
    }

    @Override
    public String getVersion() {
        return "1.0.1.4";
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        if(identifier.equalsIgnoreCase("timezone")){
            return Main.plugin.getConfig().getString("timezone");
        }

        if(player == null){
            return "";
        }

        return null;
    }
}

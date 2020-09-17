package cn.iqianye.mc.zmusic.papi;

import cn.iqianye.mc.zmusic.papi.api.AdvancementAPI;
import cn.iqianye.mc.zmusic.papi.data.PlayerData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.charset.StandardCharsets;

public class PluginMessage implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        bytes[0] = 0;
        String message = new String(bytes, StandardCharsets.UTF_8).substring(1);
        Gson gson = new GsonBuilder().create();
        JsonObject json = gson.fromJson(message, JsonObject.class);
        Bukkit.getServer().getLogger().info(message);
        if (Addon.isSupportAdv) {
            try {
                boolean isAdv = json.get("isAdv").getAsBoolean();
                if (isAdv) {
                    String title = json.get("title").getAsString();
                    new AdvancementAPI(title).sendAdvancement(player);
                }
            } catch (Exception e) {
            }
        }
        if (Addon.isPapi) {
            try {
                String name = json.get("name").getAsString();
                PlayerData.setPlayerMusicName(player, name);
            } catch (Exception e) {
                PlayerData.setPlayerMusicName(player, null);
            }
            try {
                String singer = json.get("singer").getAsString();
                PlayerData.setPlayerMusicSinger(player, singer);
            } catch (Exception e) {
                PlayerData.setPlayerMusicSinger(player, null);
            }
            try {
                String lyric = json.get("lyric").getAsString();
                PlayerData.setPlayerLyric(player, lyric);
            } catch (Exception e) {
                PlayerData.setPlayerLyric(player, null);
            }
            try {
                Long currentTime = json.get("currentTime").getAsLong();
                PlayerData.setPlayerCurrentTime(player, currentTime);
            } catch (Exception e) {
                PlayerData.setPlayerCurrentTime(player, null);
            }
            try {
                Long maxTime = json.get("maxTime").getAsLong();
                PlayerData.setPlayerMaxTime(player, maxTime);
            } catch (Exception e) {
                PlayerData.setPlayerMaxTime(player, null);
            }
            try {
                String platform = json.get("platform").getAsString();
                PlayerData.setPlayerPlatform(player, platform);
            } catch (Exception e) {
                PlayerData.setPlayerPlatform(player, null);
            }
            try {
                String src = json.get("src").getAsString();
                PlayerData.setPlayerPlaySource(player, src);
            } catch (Exception e) {
                PlayerData.setPlayerPlaySource(player, null);
            }
        }
    }
}

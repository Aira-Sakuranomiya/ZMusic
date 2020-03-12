package cn.iqianye.MinecraftPlugins.ZMusic.PApi;

import cn.iqianye.MinecraftPlugins.ZMusic.Player.PlayerStatus;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * PlaceholderAPI 扩展类
 */
public class PApiExp extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "zmusic";
    }

    @Override
    public String getAuthor() {
        return "真心";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        // 播放状态
        if (identifier.equalsIgnoreCase("status_play")) {
            boolean status = PlayerStatus.getPlayerPlayStatus(player);
            if (status) {
                return "播放中";
            } else {
                return "未播放";
            }
        }
        // 循环状态
        if (identifier.equalsIgnoreCase("status_loop")) {
            boolean status = PlayerStatus.getPlayerLoopPlay(player);
            if (status) {
                return "开";
            } else {
                return "关";
            }
        }
        // 音乐名称
        if (identifier.equalsIgnoreCase("playing_name")) {
            String musicName = PlayerStatus.getPlayerMusicName(player);
            if (musicName != null) {
                return musicName;
            } else {
                return "无";
            }
        }
        // 歌词
        if (identifier.equalsIgnoreCase("playing_lyric")) {
            String lyric = PlayerStatus.getPlayerLyric(player);
            if (lyric != null) {
                return lyric;
            } else {
                return "无";
            }
        }
        // 当前播放时间
        if (identifier.equalsIgnoreCase("time_current")) {
            Integer currentTime = PlayerStatus.getPlayerCurrentTime(player);
            if (currentTime != null) {
                if (currentTime < 60) {
                    return "00" + ":" + String.format("%02d", currentTime);
                } else if (currentTime > 60 && currentTime < 3600) {
                    int m = currentTime / 60;
                    int s = currentTime % 60;
                    return String.format("%02d", m) + ":" + String.format("%02d", s);
                } else {
                    int h = currentTime / 3600;
                    int m = (currentTime % 3600) / 60;
                    int s = (currentTime % 3600) % 60;
                    return String.format("%02d", h) + ":" + String.format("%02d", m) + ":" + String.format("%02d", s);
                }
            } else {
                return "--:--";
            }
        }
        // 最大播放时间
        if (identifier.equalsIgnoreCase("time_max")) {
            Integer maxTime = PlayerStatus.getPlayerMaxTime(player);
            if (maxTime != null) {
                if (maxTime < 60) {
                    return "00" + ":" + String.format("%02d", maxTime);
                } else if (maxTime > 60 && maxTime < 3600) {
                    int m = maxTime / 60;
                    int s = maxTime % 60;
                    return String.format("%02d", m) + ":" + String.format("%02d", s);
                } else {
                    int h = maxTime / 3600;
                    int m = (maxTime % 3600) / 60;
                    int s = (maxTime % 3600) % 60;
                    return String.format("%02d", h) + ":" + String.format("%02d", m) + ":" + String.format("%02d", s);
                }
            } else {
                return "--:--";
            }
        }
        return null;
    }
}

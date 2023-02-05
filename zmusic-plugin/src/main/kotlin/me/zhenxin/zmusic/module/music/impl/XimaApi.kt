package me.zhenxin.zmusic.module.music.impl

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.parseObject
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.entity.LyricRaw
import me.zhenxin.zmusic.entity.MusicInfo
import me.zhenxin.zmusic.entity.PlaylistInfo
import me.zhenxin.zmusic.module.music.MusicApi
import me.zhenxin.zmusic.utils.get
import java.net.URLEncoder

/**
 * 喜马拉雅实现
 *
 * @author 真心
 * @since 2021/9/8 10:32
 * @email qgzhenxin@qq.com
 */
@Suppress("SpellCheckingInspection")
class XimaApi : MusicApi {
    override val name: String = Lang.PLATFORM_XIMA

    override fun searchPage(keyword: String, page: Int, count: Int): MutableList<MusicInfo> {
        val musics = mutableListOf<MusicInfo>()
        val search =
            get(
                "https://www.ximalaya.com/revision/search/main?kw=${
                    URLEncoder.encode(keyword, "UTF-8")
                }&core=track&page=$page&rows=$count"
            )
        val data = search.parseObject()
        val result = data.getJSONObject("data")
        val track = result.getJSONObject("track")
        val songs = track.getJSONArray("docs")
        songs.forEach {
            it as JSONObject

            val id = it.getString("id")
            val name = it.getString("title")
            val albumName = it.getString("albumTitle")
            val albumImage = it.getString("albumCoverPath")
            val duration = it.getIntValue("duration")

            musics.add(
                MusicInfo(
                    id,
                    name,
                    "",
                    albumName,
                    albumImage,
                    duration
                )
            )
        }
        return musics
    }

    override fun getPlaylist(id: String): PlaylistInfo {
        TODO("Not yet implemented")
    }

    override fun getAlbum(id: String) {
        TODO("Not yet implemented")
    }

    override fun getPlayUrl(id: String): String {
        val result =
            get("https://mobile.ximalaya.com/mobile-playpage/playpage/tabs/$id/ts-${System.currentTimeMillis()}")
        val json = result.parseObject()
        val data = json.getJSONObject("data")
        val page = data.getJSONObject("playpage")
        val track = page.getJSONObject("trackInfo")
        return track.getString("playUrl64")
    }

    override fun getLyric(id: String): MutableList<LyricRaw> {
        TODO("Not yet implemented")
    }

    override fun getMusicInfo(id: String): MusicInfo {
        TODO("Not yet implemented")
    }
}
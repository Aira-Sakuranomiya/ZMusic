package me.zhenxin.zmusic.music

import me.zhenxin.zmusic.bossbar.BossBar
import me.zhenxin.zmusic.config.config
import me.zhenxin.zmusic.entity.LyricRaw
import me.zhenxin.zmusic.enums.PlayMode
import me.zhenxin.zmusic.enums.PlayMode.*
import me.zhenxin.zmusic.logger
import me.zhenxin.zmusic.status.*
import me.zhenxin.zmusic.taboolib.extend.sendMsg
import me.zhenxin.zmusic.utils.colored
import me.zhenxin.zmusic.utils.playMusic
import me.zhenxin.zmusic.utils.stopMusic
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.submit
import java.util.*

/**
 * 音乐播放器
 *
 * @author 真心
 * @since 2022/1/24 10:20
 */
class MusicPlayer(
    private val player: ProxyPlayer,
    private val api: MusicApi,
    private val musicList: MutableList<MusicInfo>,
    private val mode: PlayMode = SINGLE
) : TimerTask() {
    private var currentIndex = 0
    private lateinit var currentMusic: MusicInfo
    private lateinit var bossBar: BossBar
    private var currentLyric: MutableList<LyricRaw> = mutableListOf()

    private var currentTime = 0L

    private fun play() {
        val url = api.getPlayUrl(currentMusic.id)
        player.playMusic(url)
        bossBar.start()
    }

    override fun run() {
        if (!player.isOnline()) {
            logger.debug("[Thread:${Thread.currentThread().id}]玩家离线, 线程终止")
            cancel()
        }
        currentTime += 1
        sendLyric()
        updateState()
        checkMode()
    }

    fun start() {
        player.stopMusic()
        currentMusic = musicList[currentIndex]
        currentLyric = api.getLyric(currentMusic.id)
        player.createBossBar()
        bossBar = player.getState().bossBar!!
        bossBar.setTitle(config.LYRIC_COLOR.colored() + currentMusic.fullName)
        bossBar.setTime(currentMusic.duration.toFloat() / 1000)
        play()
        Timer().schedule(this, 1000, 1000)
    }

    private fun sendLyric() {
        submit(async = true) {
            val lyric = currentLyric.find { it.time == currentTime } ?: return@submit
            val content = "${config.LYRIC_COLOR.colored()}${lyric.content}"
            if (config.LYRIC_BOSS_BAR) {
                bossBar.setTitle(content)
            }
            if (config.LYRIC_ACTION_BAR) {
                player.sendActionBar(content)
            }
            if (config.LYRIC_CHAT) {
                player.sendMsg(content)
            }
        }
    }

    private fun updateState() {

    }

    private fun checkMode() {
        if (currentTime == currentMusic.duration) {
            when (mode) {
                SINGLE -> {
                    player.removeBossBar()
                    player.stopMusic()
                    player.setState(playing = true)
                    player.setState(player = null)
                    cancel()
                }
                SINGLE_LOOP -> {
                    currentTime = 0
                    play()
                }
                LIST -> {
                    if (currentIndex == musicList.size - 1) {
                        player.removeBossBar()
                        player.stopMusic()
                        player.setState(playing = false)
                        player.setState(player = null)
                        cancel()
                    } else {
                        currentIndex++
                    }
                    currentMusic = musicList[currentIndex]
                    currentLyric = api.getLyric(currentMusic.id)
                    bossBar.setTitle(currentMusic.fullName)
                    bossBar.setTime(currentMusic.duration.toFloat())
                    currentTime = 0
                    play()
                }
                LIST_LOOP -> {
                    if (currentIndex == musicList.size - 1) {
                        currentIndex = 0
                    } else {
                        currentIndex++
                    }
                    currentMusic = musicList[currentIndex]
                    currentLyric = api.getLyric(currentMusic.id)
                    bossBar.setTitle(currentMusic.fullName)
                    bossBar.setTime(currentMusic.duration.toFloat())
                    currentTime = 0
                    play()
                }
                LIST_RANDOM -> {
                    currentIndex = Random().nextInt(musicList.size)
                    currentMusic = musicList[currentIndex]
                    currentLyric = api.getLyric(currentMusic.id)
                    bossBar.setTitle(currentMusic.fullName)
                    bossBar.setTime(currentMusic.duration.toFloat())
                    currentTime = 0
                    play()
                }
            }
        }
    }

}

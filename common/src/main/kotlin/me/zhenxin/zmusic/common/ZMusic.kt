package me.zhenxin.zmusic.common

import me.zhenxin.zmusic.common.config.Config
import me.zhenxin.zmusic.common.i18n.I18n
import me.zhenxin.zmusic.common.modules.logger.Logger
import java.io.File

/**
 * 主入口
 *
 * @author 真心
 * @since 2021/7/15 1:33
 * @email qgzhenxin@qq.com
 */
object ZMusic {
    fun onEnable(dataFolder: File) {
        logger.info("&aLoading...")
        Config.load(dataFolder)
        logger.info("&aLanguage system loading...")
        I18n.load(dataFolder)
        logger.info(I18n.Init_I18nLoaded)
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }

    }
}

lateinit var logger: Logger
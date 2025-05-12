package org.example.project.core.util

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.core.util.DateUtils.createTiemestamp

internal expect fun platformLog(level: String, tag: String, message: String)

object LoggerUtil {
    enum class Level(val value: Int) {
        VERBOSE(2),
        DEBUG(3),
        INFO(4),
        WARN(5),
        ERROR(6)
    }

    private var minLevel = Level.VERBOSE

    // 出力するログレベルを設定
    fun setMinLevel(level: Level) {
        minLevel = level
    }

    fun v(tag: String, message: String) {
        if (minLevel.value <= Level.VERBOSE.value)
            log(Level.VERBOSE, tag, message)
    }

    fun d(tag: String, message: String) {
        if (minLevel.value <= Level.DEBUG.value)
            log(Level.DEBUG, tag, message)
    }

    fun i(tag: String, message: String) {
        if (minLevel.value <= Level.INFO.value)
            log(Level.INFO, tag, message)
    }

    fun w(tag: String, message: String) {
        if (minLevel.value <= Level.WARN.value)
            log(Level.WARN, tag, message)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (minLevel.value <= Level.ERROR.value) {
            log(Level.ERROR, tag, message)
            throwable?.let {
                log(Level.ERROR, tag, "Exception: ${it.message}\n${it.stackTraceToString()}")
            }
        }
    }

    // 実際のログ出力を行う関数
    private fun log(level: Level, tag: String, message: String) {
        // 共通のログフォーマット
        val formattedLog = "[${createTiemestamp()}][${level.name}][$tag] $message"
        // 実際のプラットフォーム固有の出力はexpect/actualで実装
        platformLog(level.name, tag, formattedLog)
    }
}

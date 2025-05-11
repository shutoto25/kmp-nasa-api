package org.example.project.core.util

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * 日付操作のためのユーティリティクラス
 */
object DateUtils {
    /**
     * 今日の日付を取得（YYYY-MM-DD形式）
     */
    fun getTodayDate(): String {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return "${today.year}-${today.monthNumber.toString().padStart(2, '0')}-${today.dayOfMonth.toString().padStart(2, '0')}"
    }
}
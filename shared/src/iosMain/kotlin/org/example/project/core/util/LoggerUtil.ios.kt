package org.example.project.core.util

import platform.Foundation.NSLog

internal actual fun platformLog(level: String, tag: String, message: String) {
    // iOSではNSLogを使用
    NSLog("[%@] %@", tag, message)
}
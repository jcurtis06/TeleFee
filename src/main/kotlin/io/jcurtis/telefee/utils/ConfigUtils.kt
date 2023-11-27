package io.jcurtis.telefee.utils

import io.jcurtis.telefee.TeleFee.Companion.plugin

object ConfigUtils {
    private val prefix = plugin.messages.get("prefix") as String

    fun getMsg(key: String): String {
        return "$prefix ${plugin.messages.get(key) as String}"
    }
}
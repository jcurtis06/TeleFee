package io.jcurtis.telefee.request

import io.jcurtis.telefee.TeleFee.Companion.plugin
import java.util.UUID

data class TPRequest(
    val sender: UUID,
    val target: UUID,
    val timestamp: Long,
    val expire: Long = (plugin.config.getInt("expiration") * 1000) + timestamp
)
package io.jcurtis.telefee

import io.jcurtis.telefee.command.TPAcceptCMD
import io.jcurtis.telefee.command.TeleFeeCMD
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

@Suppress("unused")
class TeleFee: JavaPlugin() {
    var messages = YamlConfiguration()

    companion object {
        lateinit var plugin: TeleFee
    }

    init {
        plugin = this
    }

    override fun onEnable() {
        createConfigs()

        getCommand("telefee")?.setExecutor(TeleFeeCMD())
        getCommand("tpaccept")?.setExecutor(TPAcceptCMD())
    }

    override fun onDisable() {
        saveConfig()
    }

    private fun createConfigs() {
        saveDefaultConfig()

        val customConfig = File(dataFolder, "messages.yml")
        if (!customConfig.exists()) {
            customConfig.parentFile.mkdirs()
            saveResource("messages.yml", false)
        }

        messages = YamlConfiguration.loadConfiguration(File(dataFolder, "messages.yml"))
    }
}
package io.jcurtis.telefee.command

import io.jcurtis.telefee.request.TPRequestManager
import io.jcurtis.telefee.TeleFee.Companion.plugin
import io.jcurtis.telefee.utils.ConfigUtils
import io.jcurtis.telefee.utils.ExpUtils
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TPAcceptCMD: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage(
                ConfigUtils.getMsg("error")
                    .replace("%error%", "You must be a player!")
                    .replace("%remedy%", "(try running it in game)")
            )
            return true
        }

        val req = TPRequestManager.getRecentRequest(sender.uniqueId)
        if (req == null) {
            sender.sendMessage(
                ConfigUtils.getMsg("error")
                    .replace("%error%", "You have no pending requests!")
                    .replace("%remedy%", "")
            )
            return true
        }

        if (req.expire < System.currentTimeMillis()) {
            sender.sendMessage(
                ConfigUtils.getMsg("error")
                    .replace("%error%", "That request expired!")
                    .replace("%remedy%", "(have them resend it)")
            )

            TPRequestManager.removeRequest(req)
            return true
        }

        val requester = sender.server.getPlayer(req.sender)
        if (requester == null) {
            sender.sendMessage(
                ConfigUtils.getMsg("error")
                    .replace("%error%", "That request expired!")
                    .replace("%remedy%", "")
            )
            TPRequestManager.removeRequest(req)
            return true
        }

        val cost = plugin.config.getDouble("cost")
        val expCost = ExpUtils.levelToPoints(sender.level, cost)
        if (ExpUtils.getPlayerExp(requester) < expCost) {
            sender.sendMessage(
                ConfigUtils.getMsg("error")
                    .replace("%error%", "That player no longer has enough XP!")
                    .replace("%remedy%", "")
            )
            TPRequestManager.removeRequest(req)
            return true
        }

        plugin.server.scheduler.scheduleSyncDelayedTask(plugin, {
            requester.teleport(sender)

            sender.sendMessage(
                ConfigUtils.getMsg("request-accepted")
                    .replace("%player%", requester.name)
            )

            sender.playSound(sender.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)

            requester.sendMessage(
                ConfigUtils.getMsg("teleported")
                    .replace("%player%", requester.name)
            )
            requester.playSound(requester.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)

            ExpUtils.changePlayerExp(requester, -expCost)

            TPRequestManager.removeRequest(req)
        }, 1)

        return true
    }
}
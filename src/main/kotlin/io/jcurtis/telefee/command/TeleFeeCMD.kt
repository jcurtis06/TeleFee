package io.jcurtis.telefee.command

import io.jcurtis.telefee.TeleFee.Companion.plugin
import io.jcurtis.telefee.request.TPRequest
import io.jcurtis.telefee.request.TPRequestManager
import io.jcurtis.telefee.utils.ConfigUtils
import io.jcurtis.telefee.utils.ExpUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class TeleFeeCMD: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage(
                ConfigUtils.getMsg("error")
                    .replace("%error%", "You must be a player!")
                    .replace("%remedy%", "(try running it in game)")
            )
            return true
        }

        if (args == null || args.size != 1) {
            sender.sendMessage(
                ConfigUtils.getMsg("error")
                    .replace("%error%", "Invalid syntax!")
                    .replace("%remedy%", "(/telefee <player>)")
            )
            return true
        }

        val target = sender.server.getPlayer(args[0])
        if (target == null) {
            sender.sendMessage(
                ConfigUtils.getMsg("error")
                    .replace("%error%", "Player not found!")
                    .replace("%remedy%", "(are they online?)")
            )
            return true
        }

        if (target == sender) {
            sender.sendMessage(
                ConfigUtils.getMsg("error")
                    .replace("%error%", "You can't teleport to yourself!")
                    .replace("%remedy%", "(try a different player's name)")
            )
            return true
        }

        val cost = plugin.config.getDouble("cost")
        val expCost = ExpUtils.levelToPoints(sender.level, cost)

        if (ExpUtils.getPlayerExp(sender) < expCost) {
            sender.sendMessage(
                ConfigUtils.getMsg("error")
                    .replace("%error%", "You do not have enough levels!")
                    .replace("%remedy%", "(you need $expCost)")
            )
            return true
        }

        val req = TPRequest(
            sender.uniqueId,
            target.uniqueId,
            System.currentTimeMillis()
        )

        TPRequestManager.addRequest(req)

        sender.sendMessage(
            ConfigUtils.getMsg("request-sent")
                .replace("%player%", target.name)
        )

        sender.sendMessage(
            ConfigUtils.getMsg("request-cost")
                .replace("%cost%", "$cost levels")
        )

        target.sendMessage(
            ConfigUtils.getMsg("request-received")
                .replace("%player%", sender.name)
        )

        target.sendMessage(
            ConfigUtils.getMsg("request-accept")
        )

        target.sendMessage(
            ConfigUtils.getMsg("request-expiry")
                .replace("%expiration%", ((req.expire - req.timestamp) / 1000).toString())
        )

        return true
    }
}
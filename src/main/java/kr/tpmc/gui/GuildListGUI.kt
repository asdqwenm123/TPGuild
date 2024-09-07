package kr.tpmc.gui

import io.github.asdqwenm123.SimplePlayer
import io.github.monun.invfx.InvFX
import io.github.monun.invfx.openFrame
import kr.tpmc.TPGuild
import kr.tpmc.command.KommandGuild.isMember
import kr.tpmc.model.Guild
import kr.tpmc.model.GuildRequest
import kr.tpmc.model.GuildRequestStatus
import kr.tpmc.model.Rank
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

object GuildListGUI {
    private val previousItem =
        ItemStack(Material.END_CRYSTAL).apply {
            itemMeta = itemMeta.apply {
                displayName(
                    text().color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                        .decorate(TextDecoration.BOLD).content("←").build()
                )
            }
        }
    private val nextItem =
        ItemStack(Material.END_CRYSTAL).apply {
            itemMeta = itemMeta.apply {
                displayName(
                    text().color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                        .decorate(TextDecoration.BOLD).content("→").build()
                )
            }
        }
    fun open(player: Player) {
//        var isClosed = false
//
//        TPGuild.plugin.server.scheduler.runTaskLater(TPGuild.plugin, { bukkitTask ->
//                if (isClosed) {
//                    bukkitTask.cancel()
//                } else {
//                    open(player)
//                }
//            }, 20L
//        )

//        val guild = Guild.getGuildList().getGuild(player.uniqueId)
        val guildListGUI = InvFX.frame(6, text("길드 리스트")) {
            list(0, 0, 8, 4, false, { Guild.getGuildList().guilds }) {
                transform { guild ->
                    ItemStack(Material.PLAYER_HEAD).apply {
                        itemMeta = (itemMeta as SkullMeta).apply {
                            owningPlayer = SimplePlayer.getPlayer(guild.owner)
                            displayName(guild.name.toMiniMessage().decoration(TextDecoration.ITALIC, false))
                            lore((lore() ?: mutableListOf()).plus(text("왼쪽 클릭으로 가입 신청").decoration(TextDecoration.ITALIC, false)))
                            lore(lore()!!.plus(text("[길드장]").decoration(TextDecoration.ITALIC, false)))
                            lore(lore()!!.plus(text(SimplePlayer.getName(guild.owner)).decoration(TextDecoration.ITALIC, false)))
                            if (guild.memberList.getMembers(Rank.CO_OWNER).isNotEmpty()) {
                                lore(lore()!!.plus(text("[부길드장]").decoration(TextDecoration.ITALIC, false)))
                                lore(lore()!!.plus(text(guild.memberList.getMembers(Rank.CO_OWNER)
                                    .map { member -> SimplePlayer.getName(member.uuid) }
                                    .joinToString(", ")).decoration(TextDecoration.ITALIC, false)))

                            }
                            if (guild.memberList.getMembers(Rank.MEMBER).isNotEmpty()) {
                                lore(lore()!!.plus(text("[멤버]").decoration(TextDecoration.ITALIC, false)))
                                lore(lore()!!.plus(text(guild.memberList.getMembers(Rank.MEMBER)
                                    .map { member -> SimplePlayer.getName(member.uuid) }
                                    .joinToString(", ")).decoration(TextDecoration.ITALIC, false)))

                            }
                        }
                    }
                }
                onClickItem { _, _, item, _ ->
                    val guild = item.first
                    if (!player.isMember() && (GuildRequest.getGuildRequests()[player] == null || GuildRequest.getGuildRequests()[player]?.any { request -> request.guild == guild } == false)) {
                        val req = GuildRequest(player, guild, GuildRequestStatus.TO_GUILD)
                        req.addGuildRequest()
                        req.sendMessage()
                        player.sendMessage(text("길드 >> ${guild.name}에 요청했습니다."))
                    }
                }
            }.let { invList ->
                slot(0, 5) {
                    item = previousItem
                    onClick { invList.page-- }
                }

                slot(8, 5) {
                    item = nextItem
                    onClick { invList.page++ }
                }
            }
//        slot(7, 5) {
//            item = ItemStack(Material.COMMAND_BLOCK).apply {
//                itemMeta = itemMeta.apply {
//                    itemMeta.displayName(text("길드 생성"))
//                }
//            }
//            onClick {
////                Guild.createGuild(player)
//                player.sendMessage(text("기능 구현 중. '/길드 생성 [이름]'을 사용해주세요."))
//            }
//        }
//            onClose { isClosed = true }
        }

        player.openFrame(guildListGUI)

    }


}
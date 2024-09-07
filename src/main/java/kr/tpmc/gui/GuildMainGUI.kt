package kr.tpmc.gui

import io.github.asdqwenm123.SimplePlayer
import io.github.monun.invfx.InvFX
import io.github.monun.invfx.openFrame
import kr.tpmc.TPGuild
import kr.tpmc.manager.PluginManager
import kr.tpmc.model.Guild
import kr.tpmc.model.Money
import kr.tpmc.model.Money.Companion.INTEREST_RATE
import kr.tpmc.model.Money.Companion.MAINTENANCE_COST
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.function.Consumer

object GuildMainGUI {
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
        var isClosed = false

        TPGuild.plugin.server.scheduler.runTaskLater(TPGuild.plugin, { bukkitTask ->
                if (isClosed) {
                    bukkitTask.cancel()
                } else {
                    try {
                        open(player)
                    } catch (e: Exception) {
                        bukkitTask.cancel()
                    }
                }
            }, 20L
        )

        val guild = Guild.getGuildList().getGuild(player.uniqueId)

        val guildMainGUI = InvFX.frame(6, guild.name.toMiniMessage()) {
//            slot(0, 0) {
//                item = ItemStack(Material.GOLDEN_APPLE).apply {
//                    val meta = this.itemMeta
//                    meta.displayName(Component.text(SimplePlayer.getPlayer("Aslagon").toString()))
//                    this.itemMeta = meta
//                }
//            }
            slot(2, 1) {
                item = ItemStack(Material.PLAYER_HEAD).apply {
                    val meta = this.itemMeta
                    val skull = meta as SkullMeta
                    skull.setOwningPlayer(SimplePlayer.getPlayer(guild.owner))
                    skull.displayName(text(SimplePlayer.getName(guild.owner)).decoration(TextDecoration.ITALIC, false))
                    skull.lore((skull.lore() ?: mutableListOf()).plus(text("길드장").decoration(TextDecoration.ITALIC, false)))
                    this.itemMeta = meta
                }
            }

            slot(6, 1) {
                item = ItemStack(Material.PLAYER_HEAD).apply {
                    val meta = this.itemMeta
                    val skull = meta as SkullMeta
                    skull.setOwningPlayer(player)
                    skull.displayName(text(player.name).decoration(TextDecoration.ITALIC, false))
                    skull.lore((skull.lore() ?: mutableListOf()).plus(text("당신").decoration(TextDecoration.ITALIC, false)))
                    this.itemMeta = meta
                }
            }

            slot(8, 0) {
                item = ItemStack(Material.EMERALD).apply {
                    val meta = this.itemMeta
                    meta.displayName(text("길드 자본").decoration(TextDecoration.ITALIC, false))
                    meta.lore(arrayListOf(text("보유금: ${guild.money.getMoney()} 골드").decoration(TextDecoration.ITALIC, false), text("대출금: ${guild.money.getLoan()} 골드").decoration(TextDecoration.ITALIC, false)))
                    meta.lore(meta.lore()!!.plus(text("예상 다음 유지금: ${guild.memberList.members.count() * MAINTENANCE_COST} 골드").decoration(TextDecoration.ITALIC, false)))
                    if (guild.money.getLoan() > 0) {
                        meta.lore(meta.lore()!!.plus(text("이자: ${(guild.money.getLoan() * INTEREST_RATE)} 골드").decoration(TextDecoration.ITALIC, false)))
                        val year = guild.money.getNextInterestDate().year
                        val month = guild.money.getNextInterestDate().monthValue
                        val day = guild.money.getNextInterestDate().dayOfMonth
                        val hour = guild.money.getNextInterestDate().hour
                        val minute = guild.money.getNextInterestDate().minute
                        val second = guild.money.getNextInterestDate().second
                        meta.lore(meta.lore()!!.plus(text("다음 이자 적용 날짜: ${year}년 ${month}월 ${day}일 ${hour}시 ${minute}분 ${second}초").decoration(TextDecoration.ITALIC, false)))
                    }
                    this.itemMeta = meta
                }
            }

            list(0, 2, 8, 4, false, { guild.memberList.members }) {
                transform { member ->
                    ItemStack(Material.PLAYER_HEAD).apply {
                        val meta = this.itemMeta
                        val skull = meta as SkullMeta
                        skull.setOwningPlayer(SimplePlayer.getPlayer(member.uuid))
                        skull.displayName(text(SimplePlayer.getName(member.uuid)).decoration(TextDecoration.ITALIC, false))
                        skull.lore((skull.lore() ?: mutableListOf()).plus(text("[${member.rank}]").decoration(TextDecoration.ITALIC, false)))
                        this.itemMeta = meta
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

            onClose { isClosed = true }

        }

        player.openFrame(guildMainGUI)

    }
}
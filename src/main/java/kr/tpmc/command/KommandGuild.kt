package kr.tpmc.command

import io.github.asdqwenm123.SimplePlayer
import io.github.monun.kommand.*
import kr.tpmc.TPGuild.plugin
import kr.tpmc.gui.GuildListGUI
import kr.tpmc.gui.GuildMainGUI
import kr.tpmc.manager.PluginManager
import kr.tpmc.model.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.entity.Player

object KommandGuild {
    fun register() {
        val rankArgument = KommandArgument.dynamic(StringType.GREEDY_PHRASE) { _, input ->
            val member = Guild.getGuildList().getGuild(player.uniqueId).memberList.getMember(player.uniqueId)

            when (member.rank) {
                Rank.OWNER -> {
                    Rank.values()
                        .map {rank -> rank.toString() }
                        .filter { s -> s != Rank.OWNER.toString() }
                        .plus("전체")
                        .firstOrNull { s -> s == input }
                }

                Rank.CO_OWNER -> {
                    Rank.values()
                        .map {rank -> rank.toString() }
                        .filter { s -> s != Rank.OWNER.toString() }
                        .plus("전체")
                        .firstOrNull { s -> s == input }

                }

                Rank.MEMBER -> {
                    Rank.values().asSequence().map { rank -> rank.toString() }
                        .filter { s -> s != Rank.OWNER.toString() }
                        .filter { s -> s != Rank.CO_OWNER.toString() }
                        .plus("전체")
                        .firstOrNull { s -> s == input }
                }

                null -> {}
            }
//            Rank.values().map { it.toString() }.filter { s -> s != Rank.OWNER.toString() }.plus("전체").firstOrNull { s -> s == input }
        }.apply {
            suggests {
                val member = Guild.getGuildList().getGuild(it.source.player.uniqueId).memberList.getMember(it.source.player.uniqueId)

                when (member.rank) {
                    Rank.OWNER -> {
                        suggest(Rank.values().map {rank -> rank.toString() }.filter { s -> s != Rank.OWNER.toString() }.plus("전체"))
                    }

                    Rank.CO_OWNER -> {
                        suggest(Rank.values().map {rank -> rank.toString() }.filter { s -> s != Rank.OWNER.toString() }.plus("전체"))
                    }

                    Rank.MEMBER -> {
                        suggest(Rank.values().map {rank -> rank.toString() }
                            .filter { s -> s != Rank.OWNER.toString() }
                            .filter { s -> s != Rank.CO_OWNER.toString() }
                            .plus("전체"))
                    }

                    null -> {}
                }
            }
        }

        val moneyPlayerMoneyArgument = KommandArgument.dynamic { _, input ->
//            println(if (input.toLongOrNull() !in 0..PluginManager.getEconomy().getBalance(player).toLong()) null else input.toLong())
            return@dynamic if (input.toLongOrNull() !in 0..PluginManager.getEconomy().getBalance(player).toLong()) null else input.toLong()
        }/*.apply {
            suggests {
                suggestDefault()
            }
        }*/

        val moneyGuildMoneyArgument = KommandArgument/*long(minimum = 0, maximum = Guild.getGuildList().getGuild(player.uniqueId).money.getMoney())*/.dynamic { _, input ->
//            println(cx.input)
//            println("인풋: $input")
//            println(if (input.toLongOrNull() == null) null else if (input.toLong() !in 0..Guild.getGuildList().getGuild(player.uniqueId).money.getMoney()) null else input.toLong())
            return@dynamic if (input.toLongOrNull() !in 0..Guild.getGuildList().getGuild(player.uniqueId).money.getMoney()) null else input.toLong()
        }/*.apply {
            suggests {
                suggestDefault()
            }
        }*/
        
        val loanPlayerMoneyArgument = KommandArgument.dynamic { _, input ->
            if (input.toLongOrNull() == null) {
                return@dynamic null
            }

            if (input.toLong() < 0) {
                return@dynamic null
            }
            val guild = Guild.getGuildList().getGuild(player.uniqueId)

            if (guild.money.getMoney() - input.toLong() < 0) {
                return@dynamic null
            }

            if (guild.money.getLoan() - input.toLong() < 0) {
                return@dynamic null
            }
            return@dynamic input.toLong()
        }

        val loanGuildMoneyArgument = KommandArgument.dynamic { _, input ->
            if (input.toLongOrNull() == null) {
                return@dynamic null
            }

            if (input.toLong() < 0) {
                return@dynamic null
            }

            val guild = Guild.getGuildList().getGuild(player.uniqueId)

            if (guild.money.getLoan() + input.toLong() > Money.MAXIMUM_LIMIT) {
                return@dynamic null
            }

            return@dynamic input.toLong()
        }

        val playersArgument = KommandArgument.dynamic { _, input ->
            SimplePlayer.getNames().firstOrNull {s -> s == input}
        }.apply {
            suggests {
                suggest(SimplePlayer.getNames())
            }
        }

        plugin.kommand {
            register("guild", "길드", "팀") {
                requires { isPlayer/* && isMember()*/ }
                executes {
                    if (isMember()) {
                        GuildMainGUI.open(player)
                    } else {
                        player.sendMessage(text("길드 >> 아직 길드에 가입하지 않았습니다."))
                        player.sendMessage(text("길드 >> 길드 리스트를 보여드립니다."))

                        GuildListGUI.open(player)
//                        Guild.getGuildList().getGuild("hello").memberList.addMember(Member(Rank.MEMBER, player.uniqueId))

                    }
                }
//                then("테스트") {
//                    then("arg" to dynamic(StringType.GREEDY_PHRASE) { _, input ->
//                        if (input == "a") "a" else null
//                    }.apply {
//                        suggests {
//                            suggest(hashMapOf("a" to 1)) {
//                                text(it)
//                            }
//                        }
//                    }) {
//                        executes {
//                            val arg: String = it["arg"]
//                            println(arg)
//                        }
//                    }
//                }
                then("관리자") {
                    requires { isOp }
                    then("길드삭제") {
                        then("guild" to dynamic(StringType.GREEDY_PHRASE) { context, input ->
                            Guild.getGuildList().guilds.firstOrNull {guild -> guild.name.textComponent.getContent() == input }?.name?.toString()
//                            if (GuildRequest.getGuildRequests()[player] == null) {
//                                return@dynamic null
//                            }
//                            GuildRequest.getGuildRequests()[player]!!.map { guildRequest -> guildRequest.guild.name.toString() }.firstOrNull { s -> s == input }
                        }.apply {
                            suggests {
                                suggest(Guild.getGuildList().guilds.map { guild -> guild.name.textComponent.getContent() }
                                )
                            }
                        }) {
                            executes {
                                val argGuild: String = it["guild"]
                                val guild = Guild.getGuildList().getGuild(argGuild)

                                guild.removeGuild()
                                player.sendMessage(text("길드 >> ").append(guild.name.toMiniMessage()).append(text("를 강제 삭제했습니다.")))
                            }
                        }
                    }
                    then("이름재설정") {
                        then("guild" to dynamic(StringType.QUOTABLE_PHRASE) { context, input ->
                            Guild.getGuildList().guilds.firstOrNull {guild -> guild.name.textComponent.getContent() == input }?.name?.toString()
//                            if (GuildRequest.getGuildRequests()[player] == null) {
//                                return@dynamic null
//                            }
//                            GuildRequest.getGuildRequests()[player]!!.map { guildRequest -> guildRequest.guild.name.toString() }.firstOrNull { s -> s == input }
                        }.apply {
                            suggests {
                                suggest(Guild.getGuildList().guilds.map { guild -> guild.name.textComponent.getContent() }
                                )
                            }
                        }) {
                            then("name" to string(StringType.QUOTABLE_PHRASE)) {
                                executes {
                                    val argGuild: String = it["guild"]
                                    val argName: String = it["name"]
                                    val name = MiniMessage.miniMessage().deserialize(argName)
                                    val guild = Guild.getGuildList().getGuild(argGuild)

                                    guild.name.displayComponent = name as TextComponent
                                    guild.name.textComponent = name.toPlainString()

                                    player.sendMessage(
                                        text("길드 >> ").append(guild.name.toMiniMessage()).append(text("로 강제 설정되었습니다."))
                                    )

                                }
                            }
                        }
                    }
                }

                then("해임") {
                    requires { isMember() && isOwner() }
                    then("member" to dynamic { context, input ->
//                        if (Guild.getGuildList().getGuild(player.uniqueId).memberList.getMember(SimplePlayer.getPlayer(input).uniqueId).rank != Rank.MEMBER) {
//                            return@dynamic null
//                        } else {
//                            return@dynamic SimplePlayer.getPlayer(input)
//                        }
                        Guild.getGuildList().getGuild(context.source.player.uniqueId).memberList.getMembers(Rank.CO_OWNER)
                            .firstOrNull { s -> s.uuid == SimplePlayer.getUUID(input) }
                    }.apply {
                        suggests {
                            suggest(Guild.getGuildList().getGuild(it.source.player.uniqueId).memberList.getMembers(Rank.CO_OWNER)
                                .associateBy { member -> SimplePlayer.getName(member.uuid) })
                        }
                    }) {
                        executes {
                            val member: Member = it["member"]

                            member.rank = Rank.MEMBER
                            member.chatStatus.rank = Rank.MEMBER
                            player.sendMessage(text("길드 >> ${SimplePlayer.getName(member.uuid)}를 멤버로 해임했습니다."))
                        }
                    }
                }

                then("임명") {
                    requires { isMember() && isOwner() }
                    then("member" to dynamic { context, input ->
//                        if (Guild.getGuildList().getGuild(player.uniqueId).memberList.getMember(SimplePlayer.getPlayer(input).uniqueId).rank != Rank.MEMBER) {
//                            return@dynamic null
//                        } else {
//                            return@dynamic SimplePlayer.getPlayer(input)
//                        }
                        Guild.getGuildList().getGuild(context.source.player.uniqueId).memberList.getMembers(Rank.MEMBER)
                            .firstOrNull { s -> s.uuid == SimplePlayer.getUUID(input) }
                    }.apply {
                        suggests {
                            suggest(Guild.getGuildList().getGuild(it.source.player.uniqueId).memberList.getMembers(Rank.MEMBER)
                                .associateBy { member -> SimplePlayer.getName(member.uuid) })
                        }
                    }) {
                        executes {
                            val member: Member = it["member"]

                            member.rank = Rank.CO_OWNER
                            player.sendMessage(text("길드 >> ${SimplePlayer.getName(member.uuid)}를 부길드장으로 임명했습니다."))
                        }
                    }
                }

                then("탈퇴") {
                    requires { isMember() && !isOwner() }
                    //TODO 탈퇴 만들기
                    executes {
                        player.sendMessage(text("길드 >> '/길드 탈퇴 확인'을 입력하여 완전히 탈퇴해주세요."))
                    }
                    then("확인") {
                        executes {
                            val guild = Guild.getGuildList().getGuild(player.uniqueId)
                            guild.memberList.removeMember(player.uniqueId)
                            player.sendMessage(text("길드 >> ").append(guild.name.toMiniMessage()).append(text("에서 탈퇴되었습니다.")))
                        }
                    }
                }
                then("초대") {
                    then("가입요청") {
                        requires { isMember() /*&& (isOwner() || isCoOwner())*/ }

                        //TODO /길드 초대 요청 [플레이어 이름] (길드에 있으면 자동완성에 안뜨게) //자동완성 해결
                        then("player" to dynamic { _, input ->
                            plugin.server.onlinePlayers
                                .filter { player -> !player.isMember() }
                                .firstOrNull { player -> player.name == input }
                        }.apply {
                            suggests {
                                suggest(
                                    plugin.server.onlinePlayers
                                    .filter { player -> !player.isMember() }
                                    .associateBy { player -> player.name }
                                )
                            }
                        }) {
                            executes {
                                val argPlayer: Player = it["player"]
                                if (GuildRequest.getGuildRequests()[argPlayer] == null || GuildRequest.getGuildRequests()[argPlayer]?.any { request -> request.guild == Guild.getGuildList().getGuild(player.uniqueId) } == false) {
                                    val req = GuildRequest(
                                        argPlayer,
                                        Guild.getGuildList().getGuild(player.uniqueId),
                                        GuildRequestStatus.TO_PLAYER
                                    )
                                    req.addGuildRequest()
                                    req.sendMessage()
                                    player.sendMessage(text("길드 >> ${argPlayer.name}에게 요청했습니다."))
                                }
                            }
                        }
                    }

                    then("가입신청수락") {
                        requires { isMember() && (isOwner() || isCoOwner()) }

                        //TODO /길드 초대 수락 [플레이어 이름] //자동완성 해결
                        then("player" to dynamic(StringType.GREEDY_PHRASE) { context, input ->
                            GuildRequest.getGuildRequests()
                                .filter { entry ->
                                    entry.value.any { request -> request.guild == Guild.getGuildList().getGuild(context.source.player.uniqueId) && request.status == GuildRequestStatus.TO_GUILD }
                                }
                                .map { entry -> entry.key }
                                .firstOrNull { key -> key.name == input }
                        }.apply {
                            suggests {
                                suggest(
                                    GuildRequest.getGuildRequests()
                                        .filter { entry ->
                                            entry.value.any { request -> request.guild == Guild.getGuildList().getGuild(it.source.player.uniqueId) && request.status == GuildRequestStatus.TO_GUILD }
                                        }
                                        .map { entry -> entry.key }
                                        .associateBy { player -> player.name }
                                )

                            }
                        }) { //널포인터 고치기
                            executes {
                                val argPlayer: Player = it["player"]
                                val request = GuildRequest.getGuildRequests()[argPlayer]?.first { guildRequest -> guildRequest.guild == Guild.getGuildList().getGuild(player.uniqueId) }!!

//                                println(player)
                                request.guild.memberList.addMember(Member(Rank.MEMBER, argPlayer.uniqueId))

                                GuildRequest.removeGuildRequest(argPlayer)

//                                player.sendMessage(text("길드 >> ${Guild.getGuildList().getGuild(player.uniqueId).name}를 수락했습니다."))
                                player.sendMessage(text("길드 >> ${argPlayer.name}를 수락했습니다."))
//                                request.player.sendMessage(text("길드 >> ${Guild.getGuildList().getGuild(player.uniqueId).name}에서 수락했습니다."))
                                argPlayer.sendMessage(text("길드 >> ").append(Guild.getGuildList().getGuild(player.uniqueId).name.toMiniMessage()).append(text("에서 수락했습니다.")))
                            }
                        }
                    }
                    then("가입신청거절") {
                        requires { isMember() && (isOwner() || isCoOwner()) }

                        //TODO /길드 초대 거절 [플레이어 이름]
                        then("player" to dynamic(StringType.GREEDY_PHRASE) { context, input ->
                            GuildRequest.getGuildRequests()
                                .filter { entry ->
                                    entry.value.any { request -> request.guild == Guild.getGuildList().getGuild(context.source.player.uniqueId) && request.status == GuildRequestStatus.TO_GUILD }
                                }
                                .map { entry -> entry.key }
                                .firstOrNull { key -> key.name == input }
                        }.apply {
                            suggests {
                                suggest(
                                    GuildRequest.getGuildRequests()
                                        .filter { entry ->
                                            entry.value.any { request -> request.guild == Guild.getGuildList().getGuild(it.source.player.uniqueId) && request.status == GuildRequestStatus.TO_GUILD }
                                        }
                                        .map { entry -> entry.key }
                                        .associateBy { player -> player.name }
                                )

                            }
                        }) { //널포인터 고치기
                            executes {
                                val argPlayer: Player = it["player"]
                                val request = GuildRequest.getGuildRequests()[argPlayer]?.first { guildRequest -> guildRequest.guild == Guild.getGuildList().getGuild(player.uniqueId) }!!

//                                println(player)
//                                request.guild.memberList.addMember(Member(Rank.MEMBER, player.uniqueId))

//                                GuildRequest.getGuildRequests()[player]!!.remove(request)

                                GuildRequest.removeGuildRequest(player, request)
//                                player.sendMessage(text("길드 >> ${Guild.getGuildList().getGuild(player.uniqueId).name}를 거절했습니다."))
                                player.sendMessage(text("길드 >> ${argPlayer.name}를 거절했습니다."))
//                                request.player.sendMessage(text("길드 >> ${Guild.getGuildList().getGuild(player.uniqueId).name}에서 거절했습니다."))
                                argPlayer.sendMessage(text("길드 >> ").append(Guild.getGuildList().getGuild(player.uniqueId).name.toMiniMessage()).append(text("에서 거절했습니다.")))
                            }
                        }
                    }
                    then("가입신청") {
                        requires { !isMember() }

                        then("guild" to dynamic(StringType.GREEDY_PHRASE) { context, input ->
                            Guild.getGuildList().guilds.firstOrNull {guild -> guild.name.textComponent.getContent() == input }?.name?.toString()
//                            if (GuildRequest.getGuildRequests()[player] == null) {
//                                return@dynamic null
//                            }
//                            GuildRequest.getGuildRequests()[player]!!.map { guildRequest -> guildRequest.guild.name.toString() }.firstOrNull { s -> s == input }
                        }.apply {
                            suggests {
                                suggest(Guild.getGuildList().guilds.map { guild -> guild.name.textComponent.getContent() }
                                )
                            }
                        }) {
                            executes {
                                val argGuild: String = it["guild"]
                                val guild = Guild.getGuildList().getGuild(argGuild)
                                if (GuildRequest.getGuildRequests()[player] == null || GuildRequest.getGuildRequests()[player]?.any { request -> request.guild == guild } == false) {
                                    val req = GuildRequest(player, guild, GuildRequestStatus.TO_GUILD)
                                    req.addGuildRequest()
                                    req.sendMessage()
//                                    player.sendMessage(text("길드 >> ${guild.name}에 요청했습니다."))
                                    player.sendMessage(text("길드 >> ").append(guild.name.toMiniMessage()).append(text("에 요청했습니다.")))
                                }
                            }
                        }
                    }

                    then("가입요청수락") {
                        requires { !isMember() }

                        then("guild" to dynamic(StringType.GREEDY_PHRASE) { context, input ->
                            GuildRequest.getGuildRequests()[context.source.player]
                                ?.filter { request -> request.status == GuildRequestStatus.TO_PLAYER }
                                ?.firstOrNull { guildRequest -> guildRequest.guild.name.textComponent.getContent() == input }
                        }.apply {
                            suggests {
                                if (GuildRequest.getGuildRequests()[it.source.player] == null) {
                                    suggestDefault()
                                } else {
                                    suggest(
                                        GuildRequest.getGuildRequests()[it.source.player]
                                            ?.filter { request ->
                                                request.status == GuildRequestStatus.TO_PLAYER
                                            }
                                        !!.map { guildRequest -> guildRequest.guild.name.textComponent.getContent() }
                                    )
                                }
                            }
                        }) {
                            executes {
                                val guildRequest: GuildRequest = it["guild"]
//                                val request: GuildRequest =
//                                    GuildRequest.getGuildRequests()[player]?.first { guildRequest ->
//                                        guildRequest.guild == Guild.getGuildList().getGuild(guild)
//                                    }!!

                                GuildRequest.removeGuildRequest(player)

                                guildRequest.guild.memberList.addMember(Member(Rank.MEMBER, player.uniqueId))

//                                GuildRequest.getGuildRequests().remove(player)
//                                SimplePlayer.getPlayer(guildRequest.guild.owner).sendMessage(text("길드 >> ${guildRequest.guild.name}가 참가하셨습니다."))
                                SimplePlayer.getPlayer(guildRequest.guild.owner).sendMessage(text("길드 >> ").append(guildRequest.guild.name.toMiniMessage()).append(text("가 참가하셨습니다.")))
//                                player.sendMessage(text("길드 >> ${guildRequest.guild.name}에 참가하셨습니다."))
                                player.sendMessage(text("길드 >> ").append(guildRequest.guild.name.toMiniMessage()).append(text("에 참가하셨습니다.")))
                            }
                        }
                    }

                    then("가입요청거절") {
                        requires { !isMember() }

                        then("guild" to dynamic(StringType.GREEDY_PHRASE) { context, input ->
                            GuildRequest.getGuildRequests()[context.source.player]
                                ?.filter { request -> request.status == GuildRequestStatus.TO_PLAYER }
                                ?.firstOrNull { guildRequest -> guildRequest.guild.name.textComponent.getContent() == input }
                        }.apply {
                            suggests {
                                if (GuildRequest.getGuildRequests()[it.source.player] == null) {
                                    suggestDefault()
                                } else {
                                    suggest(
                                        GuildRequest.getGuildRequests()[it.source.player]
                                            ?.filter { request ->
                                                request.status == GuildRequestStatus.TO_PLAYER
                                            }
                                        !!.map { guildRequest -> guildRequest.guild.name.textComponent.getContent() }
                                    )
                                }
                            }
                        }) {
                            executes {
                                val guildRequest: GuildRequest = it["guild"]
//                                val request: GuildRequest =
//                                    GuildRequest.getGuildRequests()[player]?.first { guildRequest ->
//                                        guildRequest.guild == Guild.getGuildList().getGuild(guild)
//                                    }!!

//                                GuildRequest.getGuildRequests()[player]!!.remove(request)
                                GuildRequest.removeGuildRequest(player, guildRequest)
//                                SimplePlayer.getPlayer(guildRequest.guild.owner).sendMessage(text("길드 >> ${guildRequest.guild.name}가 거절하셨습니다."))
                                SimplePlayer.getPlayer(guildRequest.guild.owner).sendMessage(text("길드 >> ").append(guildRequest.guild.name.toMiniMessage()).append(text("가 거절하셨습니다.")))
//                                player.sendMessage(text("길드 >> ${guildRequest.guild.name}의 초대를 거절하셨습니다."))
                                player.sendMessage(text("길드 >> ").append(guildRequest.guild.name.toMiniMessage()).append(text("의 초대를 거절하셨습니다.")))
                            }
                        }
                    }
                }

//                then("길드초대") {
//                    requires { isMember() && (isOwner() || isCoOwner()) }
//                    then("요청") {
//                        //TODO /길드 초대 요청 [플레이어 이름] (길드에 있으면 자동완성에 안뜨게)
//                        then("player" to player().apply {
//                            suggests {
//                                suggest(plugin.server.onlinePlayers.filter { player -> Guild.getGuildList().contains(player.uniqueId) }
//                                    .associateBy { player -> player.name })
//                            }
//                        }) {
//                            executes {
//                                val argPlayer: Player = it["player"]
//
//                                val req = GuildRequest(argPlayer, Guild.getGuildList().getGuild(player.uniqueId), GuildRequestStatus.TO_PLAYER)
//                                req.addGuildRequest()
//                                req.sendMessage()
//                                player.sendMessage(text("길드 >> ${argPlayer.name}에 요청했습니다."))
//                            }
//                        }
//                    }
//
//                    then("수락") {
//                        //TODO /길드 초대 수락 [플레이어 이름]
//                        then("player" to dynamic(StringType.GREEDY_PHRASE) { context, input ->
//                            GuildRequest.getGuildRequests().filter { guildRequest ->
//                                guildRequest.value.map { request -> request.guild }.contains(Guild.getGuildList().getGuild(context.source.player.uniqueId))
//                            }.map { entry -> entry.key }.firstOrNull {s -> s.name == input }
//                        }.apply {
//                            suggests {
//                                suggest(GuildRequest.getGuildRequests().filter { guildRequest ->
//                                    guildRequest.value.map { request -> request.guild }.contains(Guild.getGuildList().getGuild(it.source.player.uniqueId))
//                                }.map { entry -> entry.key.name })
//
//                            }
//                        }) { //널포인터 고치기
//                            executes {
//                                val argPlayer: Player = it["player"]
//                                val request = GuildRequest.getGuildRequests()[argPlayer]?.first { guildRequest -> guildRequest.guild == Guild.getGuildList().getGuild(player.uniqueId) }!!
//
////                                println(player)
//                                request.guild.memberList.addMember(Member(Rank.MEMBER, player.uniqueId))
//
//                                GuildRequest.getGuildRequests().remove(player)
//
//                                request.player.sendMessage(text("길드 >> ${Guild.getGuildList().getGuild(player.uniqueId).name}에서 수락했습니다."))
//                            }
//                        }
//                    }
//
//                    then("거절") {
//                        //TODO /길드 초대 거절 [플레이어 이름]
//                        then("player" to dynamic(StringType.GREEDY_PHRASE) { context, input ->
//                            GuildRequest.getGuildRequests().filter { guildRequest ->
//                                guildRequest.value.map { request -> request.guild }.contains(Guild.getGuildList().getGuild(context.source.player.uniqueId))
//                            }.map { entry -> entry.key }.firstOrNull {s -> s.name == input }
//                        }.apply {
//                            suggests {
//                                suggest(GuildRequest.getGuildRequests().filter { guildRequest ->
//                                    guildRequest.value.map { request -> request.guild }.contains(Guild.getGuildList().getGuild(it.source.player.uniqueId))
//                                }.map { entry -> entry.key.name })
//
//                            }
//                        }) { //널포인터 고치기
//                            executes {
//                                val argPlayer: Player = it["player"]
//                                val request = GuildRequest.getGuildRequests()[argPlayer]?.first { guildRequest -> guildRequest.guild == Guild.getGuildList().getGuild(player.uniqueId) }!!
//
////                                println(player)
////                                request.guild.memberList.addMember(Member(Rank.MEMBER, player.uniqueId))
//
//                                GuildRequest.getGuildRequests()[player]!!.remove(request)
//
//                                request.player.sendMessage(text("길드 >> ${Guild.getGuildList().getGuild(player.uniqueId).name}에서 거절했습니다."))
//                            }
//                        }
//                    }
//                }
//                then("플레이어초대") {
//                    requires { !isMember() }
//                    then("요청") {
//                        then("guild" to dynamic(StringType.GREEDY_PHRASE) { _, input ->
//                            Guild.getGuildList().guilds.firstOrNull {guild -> guild.name.toString() == input }?.name?.toString()
////                            if (GuildRequest.getGuildRequests()[player] == null) {
////                                return@dynamic null
////                            }
////                            GuildRequest.getGuildRequests()[player]!!.map { guildRequest -> guildRequest.guild.name.toString() }.firstOrNull { s -> s == input }
//                        }.apply {
//                            suggests {
//                                suggest(Guild.getGuildList().guilds.map { guild -> guild.name.toString() })
//                            }
//                        }) {
//                            executes {
//                                val argGuild: String = it["guild"]
//                                val guild = Guild.getGuildList().getGuild(argGuild)
//
//                                val req = GuildRequest(player, guild, GuildRequestStatus.TO_GUILD)
//                                req.addGuildRequest()
//                                req.sendMessage()
//                                player.sendMessage(text("길드 >> ${guild.name}에 요청했습니다."))
//                            }
//                        }
//                    }
//
//                    then("수락") {
//                        then("guild" to dynamic(StringType.GREEDY_PHRASE) { _, input ->
//                            if (GuildRequest.getGuildRequests()[player] == null) {
//                                return@dynamic null
//                            }
//                            GuildRequest.getGuildRequests()[player]!!.filter { guildRequest -> guildRequest.status == GuildRequestStatus.TO_PLAYER }.map { guildRequest -> guildRequest.guild.name.toString() }.firstOrNull { s -> s == input }
//                        }.apply {
//                            suggests {
//                                if (GuildRequest.getGuildRequests()[it.source.player] != null)
//                                    suggest(GuildRequest.getGuildRequests()[it.source.player]!!.filter { guildRequest -> guildRequest.status == GuildRequestStatus.TO_PLAYER }.map { guildRequest -> guildRequest.guild.name.toString() })
//                            }
//                        }) {
//                            executes {
//                                val guild: String = it["guild"]
//                                val request: GuildRequest =
//                                    GuildRequest.getGuildRequests()[player]?.first { guildRequest ->
//                                        guildRequest.guild == Guild.getGuildList().getGuild(guild)
//                                    }!!
//
//                                request.guild.memberList.addMember(Member(Rank.MEMBER, player.uniqueId))
//
//                                GuildRequest.getGuildRequests().remove(player)
//
//                                player.sendMessage(text("길드 >> ${guild}에 참가하셨습니다."))
//                            }
//                        }
//                    }
//
//                    then("거절") {
//                        then("guild" to dynamic(StringType.GREEDY_PHRASE) { _, input ->
//                            if (GuildRequest.getGuildRequests()[player] == null) {
//                                return@dynamic null
//                            }
//                            GuildRequest.getGuildRequests()[player]!!.filter { guildRequest -> guildRequest.status == GuildRequestStatus.TO_PLAYER }.map { guildRequest -> guildRequest.guild.name.toString() }.firstOrNull { s -> s == input }
//                        }.apply {
//                            suggests {
//                                if (GuildRequest.getGuildRequests()[it.source.player] != null)
//                                    suggest(GuildRequest.getGuildRequests()[it.source.player]!!.filter { guildRequest -> guildRequest.status == GuildRequestStatus.TO_PLAYER }.map { guildRequest -> guildRequest.guild.name.toString() })
//                            }
//                        }) {
//                            executes {
//                                val guild: String = it["guild"]
//                                val request: GuildRequest =
//                                    GuildRequest.getGuildRequests()[player]?.first { guildRequest ->
//                                        guildRequest.guild == Guild.getGuildList().getGuild(guild)
//                                    }!!
//
//                                GuildRequest.getGuildRequests()[player]!!.remove(request)
//
//                                player.sendMessage(text("길드 >> ${guild}의 초대를 거절하셨습니다."))
//                            }
//                        }
//                    }
//                }

                then("대출금") {
                    requires { isMember() }
                    executes {
                        val guild = Guild.getGuildList().getGuild(player.uniqueId)

                        player.sendMessage(text("길드 >> 대출금: ${guild.money.getLoan()} 골드"))
                    }
                    then("상환") {
                        then("amount" to loanPlayerMoneyArgument) {
                            executes {
                                val amount: Long = it["amount"]
                                val guild = Guild.getGuildList().getGuild(player.uniqueId)

//                                PluginManager.getEconomy().withdrawPlayer(player, amount.toDouble())
                                guild.money.withdraw(amount)
                                guild.money.withdrawLoan(amount)

                                player.sendMessage(text("길드 >> $amount 골드가 상환되었습니다."))
                                player.sendMessage(text("길드 >> 길드 대출금: ${guild.money.getLoan()} 골드"))
                                player.sendMessage(text("길드 >> 길드 보유금: ${guild.money.getMoney()} 골드"))
//                                player.sendMessage(text("길드 >> ${player.name} 보유금: ${PluginManager.getEconomy().getBalance(player).toInt()} 골드"))
                            }
                        }
                    }
                    then("대출") {
                        requires { isOwner() || isCoOwner() }

                        then("amount" to loanGuildMoneyArgument) {
                            executes {
                                val amount: Long = it["amount"]
                                val guild = Guild.getGuildList().getGuild(player.uniqueId)

                                guild.money.deposit(amount)
                                guild.money.depositLoan(amount)

                                player.sendMessage(text("길드 >> $amount 골드가 길드에 지급되었습니다."))
                                player.sendMessage(text("길드 >> 길드 대출금: ${guild.money.getLoan()} 골드"))
                                player.sendMessage(text("길드 >> 길드 보유금: ${guild.money.getMoney()} 골드"))
                                player.sendMessage(text("길드 >> 현재 이자율: ${(Money.INTEREST_RATE * 100).toInt()}%"))
                            }
                        }
                    }
                }

                then("보유금") {
                    requires { isMember() }
                    executes {
                        val guild = Guild.getGuildList().getGuild(player.uniqueId)

                        player.sendMessage(text("길드 >> 보유금: ${guild.money.getMoney()} 골드"))
                    }
                    then("입금") {
                        then("amount" to moneyPlayerMoneyArgument) {
                            executes {
                                val amount: Long = it["amount"]
                                val guild = Guild.getGuildList().getGuild(player.uniqueId)

                                PluginManager.getEconomy().withdrawPlayer(player, amount.toDouble())
                                guild.money.deposit(amount)

                                player.sendMessage(text("길드 >> $amount 골드가 차감되었습니다."))
                                player.sendMessage(text("길드 >> 길드 보유금: ${guild.money.getMoney()} 골드"))
                                player.sendMessage(text("길드 >> ${player.name} 보유금: ${PluginManager.getEconomy().getBalance(player).toLong()} 골드"))
                            }
                        }
                    }

                    then("출금") {
                        requires { isOwner() || isCoOwner() }

                        then("amount" to moneyGuildMoneyArgument) {
                            executes {
                                val amount: Long = it["amount"]
                                val guild = Guild.getGuildList().getGuild(player.uniqueId)
                                guild.money.withdraw(amount)
                                PluginManager.getEconomy().depositPlayer(player, amount.toDouble())
                                player.sendMessage(text("길드 >> $amount 골드가 지급되었습니다."))
                                player.sendMessage(text("길드 >> 길드 보유금: ${guild.money.getMoney()} 골드"))
                                player.sendMessage(text("길드 >> ${player.name} 보유금: ${PluginManager.getEconomy().getBalance(player).toLong()} 골드"))
                            }
                        }
                    }
                }

                then("삭제") {
                    requires { isMember() && isOwner() }
                    executes {
                        player.sendMessage(text("길드 >> '/길드 삭제 확인'을 입력하여 완전히 삭제해주세요."))
                    }
                    then("확인") {
                        executes {
                            val guild = Guild.getGuildList().getGuild(player.uniqueId)

                            PluginManager.getEconomy().depositPlayer(player, guild.money.getMoney().toDouble())
                            PluginManager.getEconomy().withdrawPlayer(player, guild.money.getLoan().toDouble())

                            player.sendMessage(text("길드 >> 보유금 ${guild.money.getMoney()} 골드가 지급되었습니다."))
                            player.sendMessage(text("길드 >> 대출금 ${guild.money.getLoan()} 골드가 차감되었습니다."))

                            guild.removeGuild()
                            player.sendMessage(text("길드 >> ").append(guild.name.toMiniMessage()).append(text("가 삭제되었습니다.")))
//                            player.sendMessage(text("길드 >> ${guild.name}가 삭제되었습니다."))

                        }
                    }
                }

                then("이름재설정") {
                    requires { isMember() && isOwner() }
                    then("name" to string(StringType.GREEDY_PHRASE)) {
                        executes {
                            val argName: String = it["name"]
                            val name = MiniMessage.miniMessage().deserialize(argName)
                            val guild = Guild.getGuildList().getGuild(player.uniqueId)

                            if (guild.money.getMoney() - Money.RENAMING_COST >= 0) {
                                guild.name.displayComponent = name as TextComponent
                                guild.name.textComponent = name.toPlainString()
                                player.sendMessage(text("길드 >> ").append(guild.name.toMiniMessage()).append(text("로 설정되었습니다.")))
                            }  else {
                                player.sendMessage(
                                    text(
                                        "길드 >> 길드 이름 재설정 비용이 부족합니다. (${
                                            Money.RENAMING_COST - guild.money.getMoney()
                                        } 골드 부족)"
                                    )
                                )
                            }
                        }
                    }
                }

                then("생성") {
                    requires { !isMember() }
                    then("name" to string(StringType.GREEDY_PHRASE)) {
                        executes {
                            val argName: String = it["name"]
                            val name = MiniMessage.miniMessage().deserialize(argName)
                            val status = PluginManager.getEconomy()
                                .getBalance(player) - Money.ESTABLISHMENT_FUND.toDouble() >= 0
                            if (status) {
                                PluginManager.getEconomy()
                                    .withdrawPlayer(player, Money.ESTABLISHMENT_FUND.toDouble())

                                createGuild(player, name)

                                val guild = Guild.getGuildList().getGuild(player.uniqueId)

//                                guild.money.deposit(Money.ESTABLISHMENT_FUND / 2)
                                GuildRequest.getGuildRequests().remove(player)
                                player.sendMessage(
                                    text("길드 >> ").append(guild.name.toMiniMessage()).append(text("가 생성되었습니다."))
                                )
//                                player.sendMessage(text("길드 >> 기본금으로 ${Money.ESTABLISHMENT_FUND / 2} 골드 지급되었습니다."))
                            } else {
                                player.sendMessage(
                                    text(
                                        "길드 >> 길드 설립금이 부족합니다. (${
                                            Money.ESTABLISHMENT_FUND - PluginManager.getEconomy().getBalance(player)
                                                .toLong()
                                        } 골드 부족)"
                                    )
                                )
                            }
                        }
                    }
                }

                then("리스트") {
                    executes {
                        GuildListGUI.open(player)
                    }
                }

                then("채팅") {
                    requires { isMember() }
                    executes {
                        val member = Guild.getGuildList().getGuild(player.uniqueId).memberList.getMember(player.uniqueId)

                        if (member.chatStatus.isGuildChat) {
                            member.chatStatus.isGuildChat = false

                            player.sendMessage(text("길드 >> [전체]에서 채팅 중"))
                        } else {
                            member.chatStatus.isGuildChat = true
                            member.chatStatus.rank = if (member.rank == Rank.OWNER) Rank.CO_OWNER else member.rank

                            player.sendMessage(text("길드 >> [${member.chatStatus.rank}]에서 채팅 중"))
                        }
                    }

                    then("rank" to rankArgument) {
                        executes {
                            val rank: String by it
                            val member = Guild.getGuildList().getGuild(player.uniqueId).memberList.getMember(player.uniqueId)

                            if (rank == "전체") {
                                member.chatStatus.isGuildChat = false

                                player.sendMessage(text("길드 >> [전체]에서 채팅 중"))
                            } else {
                                if (member.chatStatus.rank.toString() == rank && member.chatStatus.isGuildChat) {
                                    member.chatStatus.isGuildChat = false

                                    player.sendMessage(text("길드 >> [전체]에서 채팅 중"))
                                } else {
                                    member.chatStatus.isGuildChat = true
                                    member.chatStatus.rank = Rank.values().first { f -> f.toString() == rank }

                                    player.sendMessage(text("길드 >> [${member.chatStatus.rank}]에서 채팅 중"))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createGuild(player: Player, name: Component) {
        if (!Guild.getGuildList().contains(player.uniqueId) && !Guild.getGuildList().contains(name)) {
            Guild(Name((name as TextComponent).toPlainString(), name), player.uniqueId).addGuild()
        }
    }

    fun KommandSource.isOwner(): Boolean {
        return Guild.getGuildList().contains(player.uniqueId) && Guild.getGuildList().getGuild(player.uniqueId).owner == player.uniqueId
    }

    fun KommandSource.isCoOwner(): Boolean {
        return Guild.getGuildList().contains(player.uniqueId) && Guild.getGuildList().getGuild(player.uniqueId).memberList.getMember(player.uniqueId).rank == Rank.CO_OWNER
    }

    fun KommandSource.isMember(): Boolean {
        return Guild.getGuildList().contains(player.uniqueId)
    }

    fun Player.isOwner(): Boolean {
        return Guild.getGuildList().contains(player!!.uniqueId) && Guild.getGuildList().getGuild(player!!.uniqueId).owner == player!!.uniqueId
    }

    fun Player.isCoOwner(): Boolean {
        return Guild.getGuildList().contains(player!!.uniqueId) && Guild.getGuildList().getGuild(player!!.uniqueId).memberList.getMember(player!!.uniqueId).rank == Rank.CO_OWNER
    }

    fun Player.isMember(): Boolean {
        return Guild.getGuildList().contains(player!!.uniqueId)
    }

    private fun TextComponent.toPlainString(): TextComponent {
        return text(content().replace("&#[A-Fa-f0-9]{6}".toRegex(), ""))
    }

    private fun TextComponent.getContent(): String {
        return content()
    }
}
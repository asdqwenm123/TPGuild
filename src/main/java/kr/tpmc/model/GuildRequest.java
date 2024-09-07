package kr.tpmc.model;

import io.github.asdqwenm123.SimplePlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class GuildRequest {
    private static final HashMap<Player, ArrayList<GuildRequest>> guildRequests = new HashMap<>();

    private final Player player;
    private final Guild guild;
    private final GuildRequestStatus status;

    public GuildRequest(Player player, Guild guild, GuildRequestStatus status) {
        this.player = player;
        this.guild = guild;
        this.status = status;
    }

    public void addGuildRequest() {
        guildRequests.computeIfAbsent(player, k -> new ArrayList<>()).add(this);
    }

    public static void removeGuildRequest(Player player) {
        guildRequests.remove(player);
    }

    public static void removeGuildRequest(Player player, GuildRequest guildRequest) {
        guildRequests.get(player).remove(guildRequest);
    }

    public Player getPlayer() {
        return player;
    }

    public Guild getGuild() {
        return guild;
    }

    public GuildRequestStatus getStatus() {
        return status;
    }

    public void sendMessage() {
        switch (status) {
            case TO_GUILD -> {
                for (Member member : guild.getMemberList().getMembers()) {
                    if (member.getRank() != Rank.MEMBER && SimplePlayer.getPlayer(member.getUuid()) != null) {
                        SimplePlayer.getPlayer(member.getUuid()).sendMessage(Component.text("길드 >> ").append(Component.text(player.getName() + "가 가입 요청했습니다.")));
                    }
                }
            }

            case TO_PLAYER -> {
                player.sendMessage(Component.text("길드 >> ").append(guild.getName().toMiniMessage()).append(Component.text("가 초대했습니다.")));
            }

        }
    }

    public static HashMap<Player, ArrayList<GuildRequest>> getGuildRequests() {
        return guildRequests;
    }
}

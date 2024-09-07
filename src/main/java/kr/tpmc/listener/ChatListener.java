package kr.tpmc.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import kr.tpmc.TPGuild;
import kr.tpmc.model.Guild;
import kr.tpmc.model.GuildList;
import kr.tpmc.model.Member;
import kr.tpmc.model.Message;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        if (Guild.getGuildList().contains(player.getUniqueId())) {
            Member member = Guild.getGuildList().getGuild(player.getUniqueId()).getMemberList().getMember(player.getUniqueId());

            if (member.getChatStatus().isGuildChat()) {
                event.setCancelled(true);
                new Message(player, member.getChatStatus().getRank(), (TextComponent) event.originalMessage()).sendMessage();
            }
        }
    }
}

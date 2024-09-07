package kr.tpmc.model;

import kr.tpmc.TPGuild;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.lushplugins.chatcolorhandler.ChatColorHandler;
import org.lushplugins.chatcolorhandler.parsers.custom.HexParser;
import org.lushplugins.chatcolorhandler.parsers.custom.MiniMessageParser;

import java.util.List;

public class Message implements ComponentSerializable {
    private final Player sender;
    private final Rank recipientRank;
    private final TextComponent textComponent;

    public Message(Player sender, Rank recipientRank, TextComponent textComponent) {
        this.sender = sender;
        this.recipientRank = recipientRank;
        this.textComponent = textComponent;
    }

    public void sendMessage() {
        for (Member member : Guild.getGuildList().getGuild(sender.getUniqueId()).getMemberList().getMembers()) {
            switch (recipientRank) {
                case CO_OWNER -> {
                    if (member.getRank() == recipientRank || member.getRank() == Rank.OWNER) {
                        Player p = TPGuild.plugin.getServer().getPlayer(member.getUuid());
                        if (p != null) {
                            p.sendMessage(Component.text("[" + recipientRank + "] " + "<" + sender.getName() + "> ").append(textComponent));
                        }
                    }
                }
                case MEMBER -> {
                    if (member.getRank() == recipientRank || member.getRank() == Rank.OWNER || member.getRank() == Rank.CO_OWNER) {
                        Player p = TPGuild.plugin.getServer().getPlayer(member.getUuid());
                        if (p != null) {
                            p.sendMessage(Component.text("[" + recipientRank + "] " + "<" + sender.getName() + "> ").append(textComponent));
                        }
                    }
                }
            }

        }
    }

    @Override
    public String toJsonString() {
        return JSONComponentSerializer.json().serialize(textComponent);
    }

    @Override
    public String toString() {
        return PlainTextComponentSerializer.plainText().serialize(textComponent);
    }

    @Override
    public String toLagacyString() {
        return null;
    }

    @Override
    public String toMiniMessageString() {
        return null;
    }

    @Override
    public Component toMiniMessage() {
        return null;
    }
}

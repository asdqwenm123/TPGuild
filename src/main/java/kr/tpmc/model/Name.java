package kr.tpmc.model;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.lushplugins.chatcolorhandler.ChatColorHandler;
import org.lushplugins.chatcolorhandler.parsers.custom.HexParser;
import org.lushplugins.chatcolorhandler.parsers.custom.MiniMessageParser;

import java.util.List;

public class Name implements ComponentSerializable {
    private TextComponent textComponent;
    private TextComponent displayComponent;

    public Name(String name, TextComponent display) {
        this.textComponent = Component.text(name);
        this.displayComponent = display;
    }

    public Name(TextComponent name, TextComponent display) {
        this.textComponent = name;
        this.displayComponent = display;
    }

    public TextComponent getDisplayComponent() {
        return displayComponent;
    }

    public void setDisplayComponent(TextComponent displayComponent) {
        this.displayComponent = displayComponent;
    }

    public void setTextComponent(TextComponent textComponent) {
        this.textComponent = textComponent;
    }

    public TextComponent getTextComponent() {
        return textComponent;
    }

    @Override
    public String toString() {
        return PlainTextComponentSerializer.plainText().serialize(displayComponent);
    }

    @Override
    public String toLagacyString() {
//        System.out.println(ChatColorHandler.translate(toMiniMessageString(), List.of(HexParser.class, MiniMessageParser.class)));
        return ChatColorHandler.translate(toMiniMessageString(), List.of(HexParser.class, MiniMessageParser.class));
    }

    @Override
    public String toMiniMessageString() {
//        System.out.println(MiniMessage.miniMessage().serialize(textComponent));
        return HexParser.parseToMiniMessage(toString());
    }

    @Override
    public Component toMiniMessage() {
        return MiniMessage.miniMessage().deserialize(toMiniMessageString());
    }


    @Override
    public String toJsonString() {
        return JSONComponentSerializer.json().serialize(displayComponent);
    }


}

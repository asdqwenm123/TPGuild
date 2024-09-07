package kr.tpmc.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.io.IOException;

public class TextComponentAdapter extends TypeAdapter<TextComponent> {
    @Override
    public void write(JsonWriter out, TextComponent value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        String json = GsonComponentSerializer.gson().serialize(value);
        out.value(json);
    }

    @Override
    public TextComponent read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String json = in.nextString();
        return (TextComponent) GsonComponentSerializer.gson().deserialize(json);
    }
}

package kr.tpmc.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import kr.tpmc.TPGuild;
import kr.tpmc.model.Guild;
import kr.tpmc.model.GuildList;
import net.kyori.adventure.text.TextComponent;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class FileUtil {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(TextComponent.class, new TextComponentAdapter()).registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

    public static void save() {
        File file = new File(TPGuild.plugin.getDataFolder(),"guilds.json");
        try {
            FileWriter fileWriter = new FileWriter(file);
//            System.out.println(gson.toJsonString(Guild.getGuildList()));
            fileWriter.write(gson.toJson(Guild.getGuildList()));
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void load() {
        File file = new File(TPGuild.plugin.getDataFolder(),"guilds.json");
        if (!file.exists()) {
            save();
        }
        try {
            Type type = new TypeToken<GuildList>() {}.getType();
            GuildList guildList = gson.fromJson(new FileReader(file), type);
            if (guildList != null && guildList.getGuilds() != null) {
                for (Guild guild : guildList.getGuilds()) {
                    guild.getMoney().setGuild(guild);
                    Guild.getGuildList().addGuild(guild);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

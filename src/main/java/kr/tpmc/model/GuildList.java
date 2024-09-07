package kr.tpmc.model;

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.UUID;

public class GuildList {
    private final ArrayList<Guild> guilds;

    public GuildList() {
        guilds = new ArrayList<>();
    }

    public boolean contains(String name) {
        for (Guild g : guilds) {
            return g.getName().toString().equals(name);
        }
        return false;
    }

    public boolean contains(Component name) {
        for (Guild g : guilds) {
            return g.getName().getTextComponent().equals(name);
        }
        return false;
    }

//    public boolean contains(Name name) {
//        for (Guild g : guilds) {
//            return g.getName().equals(name);
//        }
//        return false;
//    }

    public boolean contains(UUID uuid) {
        for (Guild g : guilds) {
            if (g.getMemberList().contains(uuid)) {
                return true;
            }
        }
        return false;
    }

    public void addGuild(Guild g) {
        guilds.add(g);
    }

    public void removeGuild(Guild g) {
        guilds.remove(g);
    }

    public ArrayList<Guild> getGuilds() {
        return (ArrayList<Guild>) guilds.clone();
    }



    public Guild getGuild(String name) {
        for (Guild g : guilds) {
            if (g.getName().toString().equals(name)) {
                return g;
            }
        }
        return null;
    }

    public Guild getGuild(UUID uuid) {
        for (Guild g : guilds) {
            if (g.getMemberList().contains(uuid)) {
                return g;
            }
        }
        return null;
    }
}

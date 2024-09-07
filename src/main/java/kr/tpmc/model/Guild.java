package kr.tpmc.model;

import com.google.gson.annotations.Expose;

import java.util.UUID;

public class Guild {
    private static final GuildList guildList = new GuildList();
    private final Name name;
    private final UUID owner;
    private final MemberList memberList;
    private final Money money;

    public Guild(Name name, UUID owner) {
        this.name = name;
        this.owner = owner;
        this.memberList = new MemberList();
        this.money = new Money(this);
        memberList.addMember(new Member(Rank.OWNER, owner));
    }

    public Money getMoney() {
        return this.money;
    }

    public boolean addGuild() {
        if (guildList.contains(name.toString())) {
            return false;
        } else {
            guildList.addGuild(this);
            return true;
        }
    }

    public boolean removeGuild() {
//        if (guildList.contains(name.toString())) {
            guildList.removeGuild(this);
            return true;
//        }
//        return false;
    }

    public MemberList getMemberList() {
        return memberList;
    }

    public Name getName() {
        return name;
    }

    public UUID getOwner() {
        return owner;
    }

//    public static void createGuild(Player player, Name name) {
//        if (!Guild.getGuildList().contains(player.getUniqueId()) && !Guild.getGuildList().contains(name)) {
//            new Guild(name, player.getUniqueId()).addGuild();
//        }
//    }

    public static GuildList getGuildList() {
        return guildList;
    }

}

package kr.tpmc.model;

import java.util.UUID;

public class Member implements Cloneable {
    private Rank rank;
    private UUID uuid;
    private final ChatStatus chatStatus;

    public Member(Rank rank, UUID uuid) {
        this.rank = rank;
        this.uuid = uuid;
        this.chatStatus = new ChatStatus(rank);
    }

    public ChatStatus getChatStatus() {
        return chatStatus;
    }

    public Rank getRank() {
        return rank;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    @Override
    public Member clone() {
        try {
            Member clone = (Member) super.clone();
            clone.rank = this.rank;
            clone.uuid = this.uuid;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

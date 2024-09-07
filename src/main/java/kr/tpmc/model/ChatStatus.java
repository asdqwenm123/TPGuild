package kr.tpmc.model;

public class ChatStatus {
    private Rank rank;
    private boolean isGuildChat;

    public ChatStatus(Rank rank, boolean isGuildChat) {
        this.rank = rank;
        this.isGuildChat = isGuildChat;
    }

    public ChatStatus(Rank rank) {
        this(rank, false);
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public boolean isGuildChat() {
        return isGuildChat;
    }

    public void setGuildChat(boolean guildChat) {
        this.isGuildChat = guildChat;
    }
}

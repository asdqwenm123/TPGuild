package kr.tpmc.model;

import java.util.ArrayList;
import java.util.UUID;

public class MemberList {
    private final ArrayList<Member> members;

    public MemberList() {
        members = new ArrayList<>();
    }

    public void addMember(Member member) {
        members.add(member);
    }

    public void removeMember(Member member) {
        members.remove(member);
    }

    public boolean setRank(UUID uuid, Rank rank) {
        for (Member member : members) {
            if (member.getUuid().equals(uuid)) {
                member.setRank(rank);
                return true;
            }
        }
        return false;
    }

    public boolean contains(Member member) {
        return members.contains(member);
    }

    public boolean contains(UUID uuid) {
        for (Member member : members) {
            if (member.getUuid().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public boolean removeMember(UUID uuid) {
        for (Member member : members) {
            if (member.getUuid().equals(uuid)) {
                members.remove(member);
                return true;
            }
        }
        return false;
    }

    public Member getMember(UUID uuid) {
        for (Member member : members) {
            if (member.getUuid().equals(uuid)) {
                return member;
            }
        }
        return null;
    }

    public ArrayList<Member> getMembers() {
        return (ArrayList<Member>) members.clone();
    }

    public ArrayList<Member> getMembers(Rank rank) {
        ArrayList<Member> result = new ArrayList<>();
        for (Member member : members) {
            if (member.getRank() == rank) {
                result.add(member);
            }
        }
        return result;
    }

    public Rank getRank(UUID uuid) {
        for (Member member : members) {
            if (member.getUuid().equals(uuid)) {
                return member.getRank();
            }
        }
        return null;
    }

}

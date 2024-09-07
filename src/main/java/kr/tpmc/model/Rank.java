package kr.tpmc.model;

public enum Rank {
    OWNER,
    CO_OWNER,
    MEMBER;

    @Override
    public String toString() {
        switch (this) {
            case OWNER -> {
                return "길드장";
            }

            case CO_OWNER -> {
                return "부길드장";
            }

            case MEMBER -> {
                return "멤버";
            }
        }
        return null;
    }
}

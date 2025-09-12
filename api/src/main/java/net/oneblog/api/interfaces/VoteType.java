package net.oneblog.api.interfaces;

public enum VoteType {
    LIKE("LIKE"),
    DISLIKE("DISLIKE");

    private final String value;

    VoteType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}

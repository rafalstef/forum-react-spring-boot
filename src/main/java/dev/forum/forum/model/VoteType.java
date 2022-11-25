package dev.forum.forum.model;

public enum VoteType {
    UPVOTE(1), DOWN_VOTE(-1);

    private int voteValue;

    VoteType(int voteValue) {
    }

    public Integer getDirection() {
        return this.voteValue;
    }
}

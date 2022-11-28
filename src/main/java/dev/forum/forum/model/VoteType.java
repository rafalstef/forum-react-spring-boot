package dev.forum.forum.model;

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1);

    private final int voteValue;

    VoteType(int voteValue) {
        this.voteValue = voteValue;
    }

    public Integer getDirection() {
        return this.voteValue;
    }
}

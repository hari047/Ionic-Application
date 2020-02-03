package com.facebook.share;

public interface Sharer {

    public static class Result {
        final String postId;

        public Result(String postId2) {
            this.postId = postId2;
        }

        public String getPostId() {
            return this.postId;
        }
    }

    boolean getShouldFailOnDataError();

    void setShouldFailOnDataError(boolean z);
}

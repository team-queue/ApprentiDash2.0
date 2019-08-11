package com.example.teamboolean.apprentidash;

import com.example.teamboolean.apprentidash.Models.Comment;
import com.example.teamboolean.apprentidash.Models.Discussion;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class ForumTest {

    @Test
    public void testDiscussionNull() {
        Discussion testDiscussion = new Discussion();
        assertNull("this should be null", testDiscussion.getComments());
        assertNull("this should be null", testDiscussion.getAuthor());
        assertNull("this should be null", testDiscussion.getBody());
        assertNull("this should be null", testDiscussion.getTitle());
        assertNull("this should be null", testDiscussion.getCreatedAt());
    }

    @Test
    public void testCommentNull() {
        Comment testComment = new Comment();
        assertNull("this should be null", testComment.getAuthor());
        assertNull("this should be null", testComment.getParentDiscussion());
        assertNull("this should be null", testComment.getBody());
        assertNull("this should be null", testComment.getCreatedAt());
    }

    @Test
    public void testCommentHasParent() {
        Discussion testDiscussion = new Discussion(null, "test title", "test body");
        Comment testComment = new Comment(null, testDiscussion, "test body");

        assertEquals("The test comment should have testDiscussion as its parent discussion.",
                testDiscussion,
                testComment.getParentDiscussion());
    }
}

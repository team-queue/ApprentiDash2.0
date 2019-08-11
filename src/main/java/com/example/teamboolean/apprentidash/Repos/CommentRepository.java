package com.example.teamboolean.apprentidash.Repos;

import com.example.teamboolean.apprentidash.Models.Comment;
import com.example.teamboolean.apprentidash.Models.Discussion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findAllByParentDiscussion(Discussion parentDiscussion);
}

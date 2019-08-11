package com.example.teamboolean.apprentidash.Repos;

import com.example.teamboolean.apprentidash.Models.Discussion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DiscussionRepository extends CrudRepository<Discussion, Long> {
    Discussion findById(long id);
    List<Discussion> findAllByOrderByCreatedAtDesc();
    List<Discussion> findAllByBody(String body);
}

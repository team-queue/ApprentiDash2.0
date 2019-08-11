package com.example.teamboolean.apprentidash.Controllers;

import com.example.teamboolean.apprentidash.Models.AppUser;
import com.example.teamboolean.apprentidash.Models.Comment;
import com.example.teamboolean.apprentidash.Models.Discussion;
import com.example.teamboolean.apprentidash.Repos.AppUserRepository;
import com.example.teamboolean.apprentidash.Repos.CommentRepository;
import com.example.teamboolean.apprentidash.Repos.DiscussionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Controller
public class ForumController {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    DiscussionRepository discussionRepository;

    //Forum Page
    @GetMapping("/forum")
    public String getForum(Model m, Principal p){
        //Sets the necessary variables for the nav bar
        m.addAttribute("isLoggedIn",true);
        m.addAttribute("userFirstName", appUserRepository.findByUsername(p.getName()).getFirstName());
        m.addAttribute("currentPage", "forum");

        List<Discussion> allDiscussions = discussionRepository.findAllByOrderByCreatedAtDesc();
        m.addAttribute("allDiscussions", allDiscussions);
        return "forum";
    }

    //Single Thread Page
    @GetMapping("/forum/{id}")
    public String getDiscussion(@PathVariable long id, Model m, Principal p){
        //Sets the necessary variables for the nav bar
        // add ID Get of Thread form ThreadRepository
        m.addAttribute("isLoggedIn",true);
        m.addAttribute("userFirstName", appUserRepository.findByUsername(p.getName()).getFirstName());
        m.addAttribute("currentPage", "discussion");

        Discussion discussion = discussionRepository.findById(id);

        m.addAttribute("discussion", discussion);

        return "discussion";
    }

    //POST Mapping to create new Discussion
    @PostMapping("/forum")
    public String createDiscussion(Model m, Principal p, String title, String body) {
        AppUser author = appUserRepository.findByUsername(p.getName());
        Discussion newDiscussion = new Discussion(author, title, body);
        discussionRepository.save(newDiscussion);
        return getForum(m, p);
    }

    @PostMapping("/forum/{id}")
    public String createComment(@PathVariable long id, Model m, Principal p, String body) {
        AppUser author = appUserRepository.findByUsername(p.getName());
        Discussion parentDiscussion = discussionRepository.findById(id);
        Comment newComment = new Comment(author, parentDiscussion, body);

        commentRepository.save(newComment);

        return getDiscussion(id, m, p);
    }

    @DeleteMapping("/forum/{id}/delete")
    public String deleteDiscussion(@PathVariable long id, Model m, Principal p) {

        AppUser currentUser = appUserRepository.findByUsername(p.getName());

        Discussion currentDiscussion = discussionRepository.findById(id);

        if (currentUser.equals(currentDiscussion.getAuthor())) {
            List<Comment> deletedComments = commentRepository.findAllByParentDiscussion(currentDiscussion);
            for (Comment comment : deletedComments) {
                commentRepository.delete(comment);
            }
            discussionRepository.delete(currentDiscussion);
        }

        return getForum(m, p);
    }

}

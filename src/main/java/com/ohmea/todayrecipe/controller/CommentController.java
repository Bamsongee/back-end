package com.ohmea.todayrecipe.controller;
import com.ohmea.todayrecipe.entity.CommentEntity;
import com.ohmea.todayrecipe.entity.UserEntity;
import com.ohmea.todayrecipe.service.CommentService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping
    public String getComments(@PathVariable String recipeId, Model model) {
        List<CommentEntity> comments = commentService.getCommentsByRecipeId(recipeId);
        model.addAttribute("comments", comments);
        return "comments";
    }

    @PostMapping
    public String addComment(@PathVariable String recipeId, @RequestParam String content, @RequestParam UserEntity user) {
        // CommentEntity comment = new CommentEntity(content, user, recipeId);
        commentService.saveComment(content, user, recipeId);
        return "redirect:/recipe/" + recipeId + "/comments";
    }

//    @DeleteMapping("/{commentId}")
//    public String deleteComment(@PathVariable String recipeId, @PathVariable Integer commentId) {
//        commentService.deleteComment(commentId);
//        return "redirect:/posts/" + postId + "/comments";
//    }
}

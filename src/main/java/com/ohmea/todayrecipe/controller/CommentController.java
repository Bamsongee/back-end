package com.ohmea.todayrecipe.controller;
import com.ohmea.todayrecipe.dto.comment.CommentResponseDTO;
import com.ohmea.todayrecipe.dto.comment.CreateCommentDTO;
import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import com.ohmea.todayrecipe.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/posts/{recipeId}/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<CommentResponseDTO>>> getComments(@PathVariable String recipeId) {
        List<CommentResponseDTO> response = commentService.getCommentsByRecipeRanking(recipeId);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<List<CommentResponseDTO>>(200, "댓글을 성공적으로 조회했습니다.", response));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> addComment(@PathVariable String recipeId,  @RequestBody CreateCommentDTO createCommentDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        commentService.saveComment(createCommentDTO.getComment(), recipeId, username);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<List<CommentResponseDTO>>(200, "댓글을 성공적으로 생성했습니다.", null));
    }

//    @DeleteMapping("/{commentId}")
//    public String deleteComment(@PathVariable String recipeId, @PathVariable Integer commentId) {
//        commentService.deleteComment(commentId);
//        return "redirect:/posts/" + postId + "/comments";
//    }
}

package com.ohmea.todayrecipe.controller;

import com.ohmea.todayrecipe.dto.comment.CommentResponseDTO;
import com.ohmea.todayrecipe.dto.recipe.RecipeResponseDTO;
import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import com.ohmea.todayrecipe.dto.user.JoinDTO;
import com.ohmea.todayrecipe.dto.user.UpdateUserDTO;
import com.ohmea.todayrecipe.dto.user.UserResponseDTO;
import com.ohmea.todayrecipe.entity.RefreshEntity;
import com.ohmea.todayrecipe.jwt.JWTUtil;
import com.ohmea.todayrecipe.repository.RefreshRedisRepository;
import com.ohmea.todayrecipe.service.CommentService;
import com.ohmea.todayrecipe.service.UserService;
import com.ohmea.todayrecipe.util.TokenErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final RefreshRedisRepository refreshRedisRepository;
    private final CommentService commentService;

    @GetMapping("/")
    public ResponseDTO<String> init(){
        return new ResponseDTO<String>(HttpStatus.OK.value(), "배포 성공", null);
    }

    @PostMapping("/join")
    public ResponseEntity<ResponseDTO<String>> joinProcess(JoinDTO joinDTO) {

        ResponseDTO<String> response = userService.joinProcess(joinDTO);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @PostMapping("/reissue")
    public ResponseEntity<ResponseDTO> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 헤더에서 refresh키에 담긴 토큰을 꺼냄
        String refreshToken = request.getHeader("refresh");

        if (refreshToken == null) {
            TokenErrorResponse.sendErrorResponse(response, "헤더에서 토큰을 찾을 수 없습니다.");
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            TokenErrorResponse.sendErrorResponse(response, "access 토큰이 만료되었습니다.");
        }

        String type = jwtUtil.getType(refreshToken);
        if (!type.equals("refreshToken")) {
            TokenErrorResponse.sendErrorResponse(response, "refesh token이 아닙니다.");
        }

        Optional<RefreshEntity> isExist = refreshRedisRepository.findById(refreshToken);
        if (isExist.isEmpty()) {
            TokenErrorResponse.sendErrorResponse(response, "토큰이 만료되었습니다.");
        }

        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // 새로운 Access token과 refreshToken 생성
        String newAccessToken = jwtUtil.createJwt("accessToken", username, role, 600000L);
        String newRefreshToken = jwtUtil.createJwt("refreshToken", username, role, 600000L);

        response.setHeader("accessToken", "Bearer " + newAccessToken);
        response.setHeader("refreshToken", "Bearer " + newRefreshToken);

        refreshRedisRepository.deleteById(refreshToken);
        RefreshEntity refreshEntity = new RefreshEntity(newRefreshToken, username);
        refreshRedisRepository.save(refreshEntity);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<>(200, "accessToken 재발급 완료. 헤더를 확인하세요.", null));
    }

    /**
     * 마이페이지 조회
     */
    @GetMapping("/mypage")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> mypage() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponseDTO userResponseDTO = userService.getUserInfo(username);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<UserResponseDTO>(200, "user 조회가 완료되었습니다.", userResponseDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        UserResponseDTO userResponseDTO = userService.updateUser(username, updateUserDTO);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<UserResponseDTO>(200, "user 수정이 완료되었습니다.", userResponseDTO));
    }

    // 유저 별 댓글 조회
    @GetMapping("/mypage/comments")
    public ResponseEntity<ResponseDTO<List<CommentResponseDTO>>> getUserComments() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<CommentResponseDTO> comments = commentService.getCommentsByUser(username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO<>(200, "사용자가 작성한 댓글 조회 완료", comments));
    }

    // 레시피 찜
    @PostMapping("/recipe/like/{id}")
    public ResponseEntity<ResponseDTO> likeRecipe(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String message = userService.likeRecipe(username, id);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO(200, message, null));
    }

    // 레시피 찜 해제
    @DeleteMapping("/recipe/delete/{id}")
    public ResponseEntity unlikeRecipe(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.unlikeRecipe(username, id);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO(200, "레시피 찜 해제가 완료되었습니다.", null));
    }

    // 찜한 레시피 조회
    @GetMapping("/like")
    public ResponseEntity<ResponseDTO<?>> getLikedRecipes() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<RecipeResponseDTO.list> response = userService.getLikedRecipes(username);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<>(200, "찜한 레시피 조회가 완료되었습니다.", response));
    }


    // 찜한 레시피 카테고리별 출력
    @GetMapping("/like/filter")
    public ResponseEntity<ResponseDTO<?>> getLikedRecipesByCategory(@RequestParam("category") String category) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<RecipeResponseDTO.list> response = userService.getLikedRecipesByCategory(username, category);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<>(200, "찜한 레시피 카테고리별 조회가 완료되었습니다.", response));
    }

    // (알고리즘) 찜한 레시피
    @GetMapping("/recommend/category")
    public ResponseEntity<ResponseDTO<List<RecipeResponseDTO.list>>> getTopCategoryRecipes() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<RecipeResponseDTO.list> topCategoryRecipes = userService.getTopCategoryRecipes(username);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<>(200, "가장 많이 찜한 카테고리의 레시피 조회 완료", topCategoryRecipes));
    }

    // (알고리즘) 조회수 레시피
    @GetMapping("/recommend/count")
    public ResponseEntity<ResponseDTO<List<RecipeResponseDTO.list>>> getTopRecipesByGender() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<RecipeResponseDTO.list> topRecipes = userService.getTopRecipesByGender(username);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<>(200, "성별 기반 추천 레시피 조회 완료", topRecipes));
    }

    // (알고리즘) 합치기
    @GetMapping("/algorithm")
    public ResponseEntity<ResponseDTO<?>> getCombinedRecommendations() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<RecipeResponseDTO.list> response = userService.getCombinedRecommendations(username);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<>(200, "레시피 맞춤 알고리즘 조회 완료", response));
    }

}

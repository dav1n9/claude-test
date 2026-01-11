package com.dav1n9.claudetest.controller;

import com.dav1n9.claudetest.dto.request.PostCreateRequest;
import com.dav1n9.claudetest.dto.request.PostUpdateRequest;
import com.dav1n9.claudetest.dto.response.PostResponse;
import com.dav1n9.claudetest.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 관리 API")
public class PostController {

    private final PostService postService;

    @GetMapping
    @Operation(summary = "전체 게시글 조회", description = "등록된 모든 게시글을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.ok(postService.findAllPosts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시글 단건 조회", description = "ID로 특정 게시글을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    public ResponseEntity<PostResponse> getPostById(
            @Parameter(description = "게시글 ID") @PathVariable Long id) {
        return ResponseEntity.ok(postService.findPostById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자별 게시글 조회", description = "특정 사용자가 작성한 모든 게시글을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    public ResponseEntity<List<PostResponse>> getPostsByUserId(
            @Parameter(description = "사용자 ID") @PathVariable Long userId) {
        return ResponseEntity.ok(postService.findPostsByUserId(userId));
    }

    @PostMapping
    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "게시글 수정", description = "기존 게시글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    public ResponseEntity<PostResponse> updatePost(
            @Parameter(description = "게시글 ID") @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest request) {
        return ResponseEntity.ok(postService.updatePost(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "게시글 ID") @PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
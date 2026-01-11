package com.dav1n9.claudetest.service;

import com.dav1n9.claudetest.dto.request.PostCreateRequest;
import com.dav1n9.claudetest.dto.request.PostUpdateRequest;
import com.dav1n9.claudetest.dto.response.PostResponse;
import com.dav1n9.claudetest.entity.Post;
import com.dav1n9.claudetest.entity.User;
import com.dav1n9.claudetest.exception.ResourceNotFoundException;
import com.dav1n9.claudetest.repository.PostRepository;
import com.dav1n9.claudetest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostResponse> findAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
    }

    public PostResponse findPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return PostResponse.from(post);
    }

    public List<PostResponse> findPostsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return postRepository.findByUserId(userId).stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse createPost(PostCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);
        return PostResponse.from(savedPost);
    }

    @Transactional
    public PostResponse updatePost(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        if (request.getTitle() != null) {
            post.updateTitle(request.getTitle());
        }

        if (request.getContent() != null) {
            post.updateContent(request.getContent());
        }

        return PostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post", "id", id);
        }
        postRepository.deleteById(id);
    }
}
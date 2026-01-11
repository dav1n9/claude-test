package com.dav1n9.claudetest.service;

import com.dav1n9.claudetest.dto.request.UserCreateRequest;
import com.dav1n9.claudetest.dto.request.UserUpdateRequest;
import com.dav1n9.claudetest.dto.response.UserResponse;
import com.dav1n9.claudetest.dto.response.UserStatisticsResponse;
import com.dav1n9.claudetest.entity.User;
import com.dav1n9.claudetest.exception.DuplicateResourceException;
import com.dav1n9.claudetest.exception.ResourceNotFoundException;
import com.dav1n9.claudetest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    public UserResponse findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        User user = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .build();

        User savedUser = userRepository.save(user);
        return UserResponse.from(savedUser);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (request.getUserName() != null) {
            user.updateUserName(request.getUserName());
        }

        if (request.getEmail() != null) {
            if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException("User", "email", request.getEmail());
            }
            user.updateEmail(request.getEmail());
        }

        return UserResponse.from(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }

    public UserStatisticsResponse getUserStatistics() {
        List<User> users = userRepository.findAll();

        long totalUsers = users.size();
        long totalPosts = 0;

        List<UserStatisticsResponse.UserPostCount> userPostCounts = new ArrayList<>();

        for (User user : users) {
            int postCount = user.getPosts().size();
            totalPosts = totalPosts + postCount;

            UserStatisticsResponse.UserPostCount upc = new UserStatisticsResponse.UserPostCount();
            upc.setUserId(user.getId());
            upc.setUserName(user.getUserName());
            upc.setPostCount(postCount);
            userPostCounts.add(upc);
        }

        userPostCounts.sort(new Comparator<UserStatisticsResponse.UserPostCount>() {
            @Override
            public int compare(UserStatisticsResponse.UserPostCount o1, UserStatisticsResponse.UserPostCount o2) {
                return o2.getPostCount() - o1.getPostCount();
            }
        });

        List<UserStatisticsResponse.UserPostCount> topPosters = new ArrayList<>();
        for (int i = 0; i < Math.min(5, userPostCounts.size()); i++) {
            topPosters.add(userPostCounts.get(i));
        }

        double avg = 0;
        if (totalUsers > 0) {
            avg = (double) totalPosts / totalUsers;
        }

        UserStatisticsResponse response = new UserStatisticsResponse();
        response.setTotalUsers(totalUsers);
        response.setTotalPosts(totalPosts);
        response.setAveragePostsPerUser(avg);
        response.setTopPosters(topPosters);

        return response;
    }
}
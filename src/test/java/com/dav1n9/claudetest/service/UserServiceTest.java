package com.dav1n9.claudetest.service;

import com.dav1n9.claudetest.dto.response.UserStatisticsResponse;
import com.dav1n9.claudetest.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("should_ReturnCorrectStatistics_When_UsersExist")
    void should_ReturnCorrectStatistics_When_UsersExist() {
        // Given
        long totalUsers = 3L;
        long totalPosts = 10L;

        List<Object[]> postCounts = Arrays.asList(
                new Object[]{1L, "user1", 5L},
                new Object[]{2L, "user2", 3L},
                new Object[]{3L, "user3", 2L}
        );

        given(userRepository.count()).willReturn(totalUsers);
        given(userRepository.countAllPosts()).willReturn(totalPosts);
        given(userRepository.findUserPostCounts()).willReturn(postCounts);

        // When
        UserStatisticsResponse result = userService.getUserStatistics();

        // Then
        assertThat(result.getTotalUsers()).isEqualTo(3);
        assertThat(result.getTotalPosts()).isEqualTo(10);
        assertThat(result.getAveragePostsPerUser()).isEqualTo(10.0 / 3.0);
        assertThat(result.getTopPosters()).hasSize(3);
        assertThat(result.getTopPosters().get(0).getUserName()).isEqualTo("user1");
        assertThat(result.getTopPosters().get(0).getPostCount()).isEqualTo(5);
    }

    @Test
    @DisplayName("should_ReturnEmptyStatistics_When_NoUsersExist")
    void should_ReturnEmptyStatistics_When_NoUsersExist() {
        // Given
        given(userRepository.count()).willReturn(0L);
        given(userRepository.countAllPosts()).willReturn(0L);
        given(userRepository.findUserPostCounts()).willReturn(Collections.emptyList());

        // When
        UserStatisticsResponse result = userService.getUserStatistics();

        // Then
        assertThat(result.getTotalUsers()).isEqualTo(0);
        assertThat(result.getTotalPosts()).isEqualTo(0);
        assertThat(result.getAveragePostsPerUser()).isEqualTo(0);
        assertThat(result.getTopPosters()).isEmpty();
    }

    @Test
    @DisplayName("should_LimitTopPostersToFive_When_MoreThanFiveUsersExist")
    void should_LimitTopPostersToFive_When_MoreThanFiveUsersExist() {
        // Given
        long totalUsers = 7L;
        long totalPosts = 28L;

        List<Object[]> postCounts = Arrays.asList(
                new Object[]{1L, "user1", 10L},
                new Object[]{2L, "user2", 8L},
                new Object[]{3L, "user3", 5L},
                new Object[]{4L, "user4", 3L},
                new Object[]{5L, "user5", 1L},
                new Object[]{6L, "user6", 1L},
                new Object[]{7L, "user7", 0L}
        );

        given(userRepository.count()).willReturn(totalUsers);
        given(userRepository.countAllPosts()).willReturn(totalPosts);
        given(userRepository.findUserPostCounts()).willReturn(postCounts);

        // When
        UserStatisticsResponse result = userService.getUserStatistics();

        // Then
        assertThat(result.getTopPosters()).hasSize(5);
        assertThat(result.getTopPosters().get(0).getUserName()).isEqualTo("user1");
        assertThat(result.getTopPosters().get(4).getUserName()).isEqualTo("user5");
    }

    @Test
    @DisplayName("should_CalculateCorrectAverage_When_UsersHaveNoPosts")
    void should_CalculateCorrectAverage_When_UsersHaveNoPosts() {
        // Given
        long totalUsers = 5L;
        long totalPosts = 0L;

        List<Object[]> postCounts = Arrays.asList(
                new Object[]{1L, "user1", 0L},
                new Object[]{2L, "user2", 0L},
                new Object[]{3L, "user3", 0L},
                new Object[]{4L, "user4", 0L},
                new Object[]{5L, "user5", 0L}
        );

        given(userRepository.count()).willReturn(totalUsers);
        given(userRepository.countAllPosts()).willReturn(totalPosts);
        given(userRepository.findUserPostCounts()).willReturn(postCounts);

        // When
        UserStatisticsResponse result = userService.getUserStatistics();

        // Then
        assertThat(result.getTotalUsers()).isEqualTo(5);
        assertThat(result.getTotalPosts()).isEqualTo(0);
        assertThat(result.getAveragePostsPerUser()).isEqualTo(0);
    }

    // test
}

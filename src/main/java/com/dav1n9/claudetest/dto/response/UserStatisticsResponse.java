package com.dav1n9.claudetest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStatisticsResponse {

    private long totalUsers;
    private long totalPosts;
    private double averagePostsPerUser;
    private List<UserPostCount> topPosters;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserPostCount {
        private Long userId;
        private String userName;
        private int postCount;
    }
}

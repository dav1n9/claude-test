package com.dav1n9.claudetest.repository;

import com.dav1n9.claudetest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u.id, u.userName, COUNT(p) FROM User u LEFT JOIN u.posts p GROUP BY u.id, u.userName ORDER BY COUNT(p) DESC")
    List<Object[]> findUserPostCounts();

    @Query("SELECT COUNT(p) FROM Post p")
    long countAllPosts();
}

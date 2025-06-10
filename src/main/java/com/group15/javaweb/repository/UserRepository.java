package com.group15.javaweb.repository;

import com.group15.javaweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);


    // Đếm số user mới trong khoảng thời gian
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    Long countByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);

    // Lấy thống kê user mới theo ngày
    @Query("SELECT DATE(u.createdAt) as createdDate, COUNT(u) as userCount " +
            "FROM User u WHERE u.createdAt >= :startDate " +
            "GROUP BY DATE(u.createdAt) ORDER BY DATE(u.createdAt)")
    List<Object[]> getDailyUserStats(@Param("startDate") LocalDateTime startDate);

    // Lấy thống kê user mới theo tháng
    @Query("SELECT MONTH(u.createdAt) as month, YEAR(u.createdAt) as year, COUNT(u) as userCount " +
            "FROM User u WHERE u.createdAt >= :startDate " +
            "GROUP BY YEAR(u.createdAt), MONTH(u.createdAt) " +
            "ORDER BY YEAR(u.createdAt), MONTH(u.createdAt)")
    List<Object[]> getMonthlyUserStats(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT DATE(u.createdAt) as createdDate, COUNT(u) as userCount " +
            "FROM User u WHERE u.createdAt >= :startDate AND u.createdAt < :endDate " +
            "GROUP BY DATE(u.createdAt) ORDER BY DATE(u.createdAt)")
    List<Object[]> getDailyUserStatsForRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}

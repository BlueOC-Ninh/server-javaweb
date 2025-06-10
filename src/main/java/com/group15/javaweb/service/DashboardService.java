package com.group15.javaweb.service;

import com.group15.javaweb.dto.response.ChartResponse;
import com.group15.javaweb.dto.response.SeriesData;
import com.group15.javaweb.repository.OrderRepository;
import com.group15.javaweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter ISO_INSTANT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public ChartResponse getChartDataByDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("fromDate không thể lớn hơn toDate");
        }


        List<String> categories = new ArrayList<>();
        List<Integer> salesData = new ArrayList<>();
        List<Integer> revenueData = new ArrayList<>();
        List<Integer> customersData = new ArrayList<>();

        LocalDateTime startDateTime = fromDate.atStartOfDay();
        LocalDateTime endDateTime = toDate.plusDays(1).atStartOfDay();

        Map<LocalDate, Object[]> orderStatsMap = getOrderStatsMapForRange(startDateTime, endDateTime);
        Map<LocalDate, Object[]> userStatsMap = getUserStatsMapForRange(startDateTime, endDateTime);

        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            LocalDateTime localDateTime = date.atStartOfDay();
            OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC);
            categories.add(offsetDateTime.format(ISO_INSTANT_FORMATTER));

            Object[] orderStat = orderStatsMap.get(date);
            if (orderStat != null) {
                salesData.add(((Number) orderStat[1]).intValue());
                BigDecimal revenue = (BigDecimal) orderStat[2];
                revenueData.add(revenue != null ? revenue.divide(BigDecimal.valueOf(1000)).intValue() : 0);
            } else {
                salesData.add(0);
                revenueData.add(0);
            }

            Object[] userStat = userStatsMap.get(date);
            if (userStat != null) {
                customersData.add(((Number) userStat[1]).intValue());
            } else {
                customersData.add(0);
            }
        }

        List<SeriesData> series = new ArrayList<>();
        series.add(new SeriesData("Sales", salesData));
        series.add(new SeriesData("Revenue (x1000)", revenueData));
        series.add(new SeriesData("Customers", customersData));

        return new ChartResponse(series, categories);
    }


    private Map<LocalDate, Object[]> getOrderStatsMapForRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> orderStats = orderRepository.getDailyStatsForRange(startDate, endDate);
        return orderStats.stream()
                .collect(Collectors.toMap(
                        stat -> ((java.sql.Date) stat[0]).toLocalDate(),
                        stat -> stat
                ));
    }

    private Map<LocalDate, Object[]> getUserStatsMapForRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> userStats = userRepository.getDailyUserStatsForRange(startDate, endDate);
        return userStats.stream()
                .collect(Collectors.toMap(
                        stat -> ((java.sql.Date) stat[0]).toLocalDate(),
                        stat -> stat
                ));
    }

}
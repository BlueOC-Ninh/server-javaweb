package com.group15.javaweb.controller;

import com.group15.javaweb.dto.response.ChartResponse;
import com.group15.javaweb.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
@Validated
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Lấy dữ liệu biểu đồ theo một khoảng ngày tùy chọn (fromDate, toDate).
     * Dữ liệu sẽ được nhóm theo từng ngày trong khoảng đó.
     *  http://localhost:8080/api/dashboard/chart-data?fromDate=2025-05-01&toDate=2025-06-07
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/chart-data")
    public ResponseEntity<?> getChartData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        try {
            ChartResponse response = dashboardService.getChartDataByDateRange(fromDate, toDate);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
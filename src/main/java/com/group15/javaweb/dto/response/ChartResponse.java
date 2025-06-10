package com.group15.javaweb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartResponse {
    private List<SeriesData> series;
    private List<String> categories;
}


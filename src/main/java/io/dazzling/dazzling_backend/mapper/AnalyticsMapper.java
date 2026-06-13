package io.dazzling.dazzling_backend.mapper;

import io.dazzling.dazzling_backend.dto.StatCountResponse;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Component
public class AnalyticsMapper {

    public List<StatCountResponse> toStatCountResponses(List<Object[]> rows) {
        return rows.stream()
                .map(row -> new StatCountResponse(formatDate(row[0]), ((Number) row[1]).longValue()))
                .toList();
    }

    private String formatDate(Object value) {
        if (value instanceof LocalDate localDate) {
            return localDate.toString();
        }
        if (value instanceof Date sqlDate) {
            return sqlDate.toLocalDate().toString();
        }
        return value.toString();
    }
}

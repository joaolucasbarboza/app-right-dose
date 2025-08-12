package com.fema.tcc.gateways.http.jsons;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record FoodRecommendationResponseJson(
        String model,
        OffsetDateTime generatedAt,
        Meals meals,
        List<String> alerts,
        List<String> substitutions,
        Map<String, Object> profileApplied
) {
    public record Meals(
            String breakfast,
            String lunch,
            String dinner,
            String snackMorning,
            String snackAfternoon
    ) {}
}

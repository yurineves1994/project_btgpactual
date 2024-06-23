package com.project.btgpactual.orderms.controllers.dto;

import java.util.List;
import java.util.Map;

public record ApiResponse<T>(
        Map<String, Object> sumary,
        List<T> data,
        PaginationResponse pagination) {
}

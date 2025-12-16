package org.lovesoa.calledejb.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieSearchRequest {
    private Map<String, Object> filters;
    private List<String> sort;
    private Integer page = 0;
    private Integer size = 20;
}
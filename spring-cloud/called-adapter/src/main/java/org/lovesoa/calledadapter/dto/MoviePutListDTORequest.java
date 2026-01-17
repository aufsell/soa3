package org.lovesoa.calledadapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MoviePutListDTORequest {
    private List<MovieCreateRequest> movies;
}

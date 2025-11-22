package org.lovesoa.calledejb.dtos;

import lombok.Data;

import java.util.List;

@Data
public class MoviePutListDTORequest {
    private List<MovieCreateRequest> movies;
}

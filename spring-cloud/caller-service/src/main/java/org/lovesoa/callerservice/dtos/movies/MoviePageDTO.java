package org.lovesoa.callerservice.dtos.movies;

import lombok.Data;
import org.lovesoa.callerservice.dtos.pagable.PageableDTO;
import org.lovesoa.callerservice.dtos.pagable.SortDTO;


import java.util.List;

@Data
public class MoviePageDTO {
    private List<MovieResponseDTO> content;
    private PageableDTO pageable;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private int numberOfElements;
    private int size;
    private int number;
    private SortDTO sort;
    private boolean first;
    private boolean empty;
}


package org.lovesoa.callerservice.dtos.movies;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lovesoa.callerservice.dtos.MovieCreateRequest;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoviePutListDTORequest implements Serializable {

    private List<MovieCreateRequest> movies;

}

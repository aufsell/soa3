package org.lovesoa.calledweb.web.soap;

import org.lovesoa.calledejb.dtos.*;
import org.lovesoa.calledweb.web.soap.dto.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Converter between SOAP DTOs and internal DTOs
 */
public class SoapDtoConverter {

    // ========== MovieCreateRequest conversion ==========
    
    public static MovieCreateRequest toMovieCreateRequest(SoapMovieCreateRequest soap) {
        if (soap == null) return null;
        
        MovieCreateRequest request = new MovieCreateRequest();
        request.setId(soap.getId());
        request.setName(soap.getName());
        request.setOscarsCount(soap.getOscarsCount());
        request.setGenre(soap.getGenre());
        request.setMpaaRating(soap.getMpaaRating());
        
        if (soap.getCoordinates() != null) {
            MovieCreateRequest.CoordinatesDto coords = new MovieCreateRequest.CoordinatesDto();
            coords.setX(soap.getCoordinates().getX());
            coords.setY(soap.getCoordinates().getY());
            request.setCoordinates(coords);
        }
        
        if (soap.getOperator() != null) {
            MovieCreateRequest.PersonDto person = new MovieCreateRequest.PersonDto();
            person.setName(soap.getOperator().getName());
            person.setHeight(soap.getOperator().getHeight());
            person.setWeight(soap.getOperator().getWeight());
            
            if (soap.getOperator().getLocation() != null) {
                MovieCreateRequest.PersonDto.LocationDto loc = new MovieCreateRequest.PersonDto.LocationDto();
                loc.setX(soap.getOperator().getLocation().getX());
                loc.setY(soap.getOperator().getLocation().getY());
                loc.setZ(soap.getOperator().getLocation().getZ());
                person.setLocation(loc);
            }
            request.setOperator(person);
        }
        
        return request;
    }

    // ========== MovieUpdateRequest conversion ==========
    
    public static MovieUpdateRequest toMovieUpdateRequest(SoapMovieUpdateRequest soap) {
        if (soap == null) return null;
        
        MovieUpdateRequest request = new MovieUpdateRequest();
        request.setName(soap.getName());
        request.setOscarsCount(soap.getOscarsCount());
        request.setGenre(soap.getGenre());
        request.setMpaaRating(soap.getMpaaRating());
        
        if (soap.getCoordinates() != null) {
            MovieCreateRequest.CoordinatesDto coords = new MovieCreateRequest.CoordinatesDto();
            coords.setX(soap.getCoordinates().getX());
            coords.setY(soap.getCoordinates().getY());
            request.setCoordinates(coords);
        }
        
        if (soap.getOperator() != null) {
            MovieCreateRequest.PersonDto person = new MovieCreateRequest.PersonDto();
            person.setName(soap.getOperator().getName());
            person.setHeight(soap.getOperator().getHeight());
            person.setWeight(soap.getOperator().getWeight());
            
            if (soap.getOperator().getLocation() != null) {
                MovieCreateRequest.PersonDto.LocationDto loc = new MovieCreateRequest.PersonDto.LocationDto();
                loc.setX(soap.getOperator().getLocation().getX());
                loc.setY(soap.getOperator().getLocation().getY());
                loc.setZ(soap.getOperator().getLocation().getZ());
                person.setLocation(loc);
            }
            request.setOperator(person);
        }
        
        return request;
    }

    // ========== MovieSearchRequest conversion ==========
    
    public static MovieSearchRequest toMovieSearchRequest(SoapMovieSearchRequest soap) {
        if (soap == null) return new MovieSearchRequest();
        
        MovieSearchRequest request = new MovieSearchRequest();
        request.setPage(soap.getPage() != null ? soap.getPage() : 0);
        request.setSize(soap.getSize() != null ? soap.getSize() : 20);
        request.setSort(soap.getSortFields());
        
        // Convert filters
        Map<String, Object> filters = new HashMap<>();
        if (soap.getFilterName() != null) {
            filters.put("name", soap.getFilterName());
        }
        if (soap.getFilterGenre() != null) {
            filters.put("genre", soap.getFilterGenre());
        }
        if (soap.getFilterMpaaRating() != null) {
            filters.put("mpaaRating", soap.getFilterMpaaRating());
        }
        if (soap.getFilterOscarsCountMin() != null) {
            filters.put("oscarsCountMin", soap.getFilterOscarsCountMin());
        }
        if (soap.getFilterOscarsCountMax() != null) {
            filters.put("oscarsCountMax", soap.getFilterOscarsCountMax());
        }
        if (!filters.isEmpty()) {
            request.setFilters(filters);
        }
        
        return request;
    }

    // ========== MoviePutListRequest conversion ==========
    
    public static MoviePutListDTORequest toMoviePutListRequest(SoapMoviePutListRequest soap) {
        if (soap == null) return null;
        
        MoviePutListDTORequest request = new MoviePutListDTORequest();
        if (soap.getMovies() != null) {
            List<MovieCreateRequest> movies = soap.getMovies().stream()
                    .map(SoapDtoConverter::toMovieCreateRequest)
                    .collect(Collectors.toList());
            request.setMovies(movies);
        }
        return request;
    }

    // ========== Response conversion ==========
    
    public static SoapMovieResponseDTO toSoapMovieResponseDTO(MovieResponseDTO dto) {
        if (dto == null) return null;
        
        SoapMovieResponseDTO soap = new SoapMovieResponseDTO();
        soap.setId(dto.getId());
        soap.setName(dto.getName());
        soap.setOscarsCount(dto.getOscarsCount());
        soap.setGenre(dto.getGenre());
        soap.setMpaaRating(dto.getMpaaRating());
        soap.setCreationDate(dto.getCreationDate());
        
        if (dto.getCoordinates() != null) {
            SoapCoordinatesDTO coords = new SoapCoordinatesDTO();
            coords.setId(dto.getCoordinates().getId());
            coords.setX(dto.getCoordinates().getX());
            coords.setY(dto.getCoordinates().getY());
            soap.setCoordinates(coords);
        }
        
        if (dto.getOperator() != null) {
            SoapPersonDTO person = new SoapPersonDTO();
            person.setId(dto.getOperator().getId());
            person.setName(dto.getOperator().getName());
            person.setHeight(dto.getOperator().getHeight());
            person.setWeight(dto.getOperator().getWeight());
            
            if (dto.getOperator().getLocation() != null) {
                SoapLocationDTO loc = new SoapLocationDTO();
                loc.setId(dto.getOperator().getLocation().getId());
                loc.setX(dto.getOperator().getLocation().getX());
                loc.setY(dto.getOperator().getLocation().getY());
                loc.setZ(dto.getOperator().getLocation().getZ());
                person.setLocation(loc);
            }
            soap.setOperator(person);
        }
        
        return soap;
    }

    public static List<SoapMovieResponseDTO> toSoapMovieResponseDTOList(List<MovieResponseDTO> dtos) {
        if (dtos == null) return new ArrayList<>();
        return dtos.stream()
                .map(SoapDtoConverter::toSoapMovieResponseDTO)
                .collect(Collectors.toList());
    }

    public static SoapPageDTO toSoapPageDTO(PageDTO<MovieResponseDTO> page) {
        if (page == null) return null;
        
        SoapPageDTO soap = new SoapPageDTO();
        soap.setPage(page.getPage());
        soap.setSize(page.getSize());
        soap.setTotalElements(page.getTotalElements());
        soap.setTotalPages(page.getTotalPages());
        soap.setContent(toSoapMovieResponseDTOList(page.getContent()));
        return soap;
    }

    // ========== Auth conversion ==========
    
    public static LoginRequest toLoginRequest(SoapLoginRequest soap) {
        if (soap == null) return null;
        LoginRequest request = new LoginRequest();
        request.setEmail(soap.getEmail());
        request.setPassword(soap.getPassword());
        return request;
    }

    public static RegisterRequest toRegisterRequest(SoapRegisterRequest soap) {
        if (soap == null) return null;
        RegisterRequest request = new RegisterRequest();
        request.setEmail(soap.getEmail());
        request.setPassword(soap.getPassword());
        request.setName(soap.getName());
        return request;
    }

    public static SoapAuthResponse toSoapAuthResponse(AuthResponse response) {
        if (response == null) return null;
        return new SoapAuthResponse(response.getToken());
    }
}

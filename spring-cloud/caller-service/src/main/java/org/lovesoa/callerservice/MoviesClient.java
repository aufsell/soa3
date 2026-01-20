package org.lovesoa.callerservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lovesoa.callerservice.dtos.MovieCreateRequest;
import org.lovesoa.callerservice.dtos.movies.*;

import org.lovesoa.callerservice.dtos.pagable.PageableDTO;
import org.lovesoa.callerservice.dtos.pagable.SortDTO;
import org.lovesoa.callerservice.exception.ExternalServiceException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class MoviesClient {


    @PostConstruct
    public void logSslProps() {
        System.out.println("### javax.net.ssl.trustStore = " + System.getProperty("javax.net.ssl.trustStore"));
        System.out.println("### javax.net.ssl.trustStoreType = " + System.getProperty("javax.net.ssl.trustStoreType"));
    }

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private String gatewayBaseUrl = "https://api-gateway:8443";

    public MoviesClient(
            RestTemplate restTemplate,
            ObjectMapper objectMapper
    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    private String baseUrl() {
        return gatewayBaseUrl + "/called/api/movies/";
    }

    public MoviesClient() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    private Long parseLong(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).longValue();
        if (value instanceof String) return Long.parseLong((String) value);
        return null;
    }

    private Integer parseInt(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        if (value instanceof String) return Integer.parseInt((String) value);
        return null;
    }

    private Double parseDouble(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).doubleValue();
        if (value instanceof String) return Double.parseDouble((String) value);
        return null;
    }

    public MoviePageDTO searchMovies(MovieSearchRequest request, String bearerToken) {
        if (request.getFilters() == null) request.setFilters(new HashMap<>());
        if (request.getSort() == null) request.setSort(new ArrayList<>());
        if (request.getPage() == null) request.setPage(0);
        if (request.getSize() == null) request.setSize(20);

        String url = baseUrl() + "/search";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);

        HttpEntity<MovieSearchRequest> entity = new HttpEntity<>(request, headers);

        System.out.println(entity.getBody().toString());

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(entity);
            System.out.println("JSON ENtity to be sent: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String json = mapper.writeValueAsString(request);
            System.out.println("JSON request to be sent: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }


        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ExternalServiceException(response.getStatusCodeValue(),
                    "Ошибка первого сервиса", List.of(response.getBody()));
        }

        try {
            Map<String, Object> pageMap = objectMapper.readValue(response.getBody(), Map.class);
            List<Map<String, Object>> contentList = pageMap.get("content") != null
                    ? (List<Map<String, Object>>) pageMap.get("content")
                    : Collections.emptyList();

            List<MovieResponseDTO> movies = new ArrayList<>();
            for (Map<String, Object> item : contentList) {
                MovieResponseDTO dto = new MovieResponseDTO();
                dto.setId(parseLong(item.get("id")));
                dto.setName((String) item.getOrDefault("name", ""));
                dto.setGenre((String) item.getOrDefault("genre", ""));
                dto.setOscarsCount(item.get("oscarsCount") != null ? parseInt(item.get("oscarsCount")) : 0);
                dto.setCreationDate(item.getOrDefault("creationDate", null) != null ? item.get("creationDate").toString() : null);
                dto.setMpaaRating(item.getOrDefault("mpaaRating", null) != null ? item.get("mpaaRating").toString() : null);

                Map<String, Object> coordinatesMap = (Map<String, Object>) item.get("coordinates");
                if (coordinatesMap != null) {
                    CoordinatesDTO coords = new CoordinatesDTO();
                    coords.setId(parseLong(coordinatesMap.get("id")));
                    coords.setX(parseDouble(coordinatesMap.get("x")));
                    coords.setY(parseDouble(coordinatesMap.get("y")));
                    dto.setCoordinates(coords);
                }

                Map<String, Object> operatorMap = (Map<String, Object>) item.get("operator");
                if (operatorMap != null) {
                    OperatorDTO operator = new OperatorDTO();
                    operator.setId(parseLong(operatorMap.get("id")));
                    operator.setName((String) operatorMap.getOrDefault("name", ""));
                    operator.setHeight(parseDouble(operatorMap.get("height")));
                    operator.setWeight(parseDouble(operatorMap.get("weight")));

                    Map<String, Object> locationMap = (Map<String, Object>) operatorMap.get("location");
                    if (locationMap != null) {
                        LocationDTO loc = new LocationDTO();
                        loc.setId(parseLong(locationMap.get("id")));
                        loc.setX(parseInt(locationMap.get("x")));
                        loc.setY(parseInt(locationMap.get("y")));
                        loc.setZ(parseLong(locationMap.get("z")));
                        operator.setLocation(loc);
                    }
                    dto.setOperator(operator);
                }

                movies.add(dto);
            }

            int pageNumber = pageMap.get("page") != null ? parseInt(pageMap.get("page")) : 0;
            int pageSize = pageMap.get("size") != null ? parseInt(pageMap.get("size")) : movies.size();
            long totalElements = pageMap.get("totalElements") != null ? parseLong(pageMap.get("totalElements")) : movies.size();
            int totalPages = pageMap.get("totalPages") != null ? parseInt(pageMap.get("totalPages")) : (int) Math.ceil((double) totalElements / pageSize);

            SortDTO sortDTO = new SortDTO();
            sortDTO.setSorted(false);
            sortDTO.setUnsorted(true);
            sortDTO.setEmpty(true);

            PageableDTO pageableDTO = new PageableDTO();
            pageableDTO.setPageNumber(pageNumber);
            pageableDTO.setPageSize(pageSize);
            pageableDTO.setOffset((long) (pageNumber * pageSize));
            pageableDTO.setPaged(true);
            pageableDTO.setUnpaged(false);
            pageableDTO.setSort(sortDTO);

            MoviePageDTO pageDTO = new MoviePageDTO();
            pageDTO.setContent(movies);
            pageDTO.setPageable(pageableDTO);
            pageDTO.setTotalElements(totalElements);
            pageDTO.setTotalPages(totalPages);
            pageDTO.setNumberOfElements(movies.size());
            pageDTO.setSize(pageSize);
            pageDTO.setNumber(pageNumber);
            pageDTO.setSort(sortDTO);
            pageDTO.setFirst(pageNumber == 0);
            pageDTO.setLast(pageNumber == totalPages - 1);
            pageDTO.setEmpty(movies.isEmpty());

            return pageDTO;

        } catch (Exception e) {
            throw new RuntimeException("Ошибка обработки JSON-ответа: " + e.getMessage(), e);
        }
    }

    public void updateMoviesBatch(List<MovieResponseDTO> movies, String bearerToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);

        List<MovieCreateRequest> requests = new ArrayList<>();
        for (MovieResponseDTO movie : movies) {
            requests.add(buildMovieCreateRequest(movie, movie.getOscarsCount()));
        }

        MoviePutListDTORequest wrapper = new MoviePutListDTORequest();
        wrapper.setMovies(requests);

        HttpEntity<MoviePutListDTORequest> entity = new HttpEntity<>(wrapper, headers);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(wrapper);
            System.out.println("JSON request to be sent НА АПДЕЙТ: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl(),
                HttpMethod.PUT,
                entity,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ExternalServiceException(response.getStatusCodeValue(),
                    "Ошибка первого сервиса", List.of(response.getBody()));
        }
    }

    private MovieCreateRequest buildMovieCreateRequest(MovieResponseDTO movie, int oscarsCount) {
        MovieCreateRequest request = new MovieCreateRequest();
        request.setId(movie.getId());
        request.setName(movie.getName());
        request.setGenre(movie.getGenre());
        request.setOscarsCount(oscarsCount);
        request.setMpaaRating(movie.getMpaaRating());

        MovieCreateRequest.CoordinatesDto coords = new MovieCreateRequest.CoordinatesDto();
        if (movie.getCoordinates() != null) {
            coords.setX((int) movie.getCoordinates().getX());
            coords.setY((float) movie.getCoordinates().getY());
        }
        request.setCoordinates(coords);

        MovieCreateRequest.PersonDto operator = new MovieCreateRequest.PersonDto();
        if (movie.getOperator() != null) {
            operator.setName(movie.getOperator().getName());
            operator.setHeight(movie.getOperator().getHeight().floatValue());
            operator.setWeight(movie.getOperator().getWeight().intValue());

            MovieCreateRequest.PersonDto.LocationDto location = new MovieCreateRequest.PersonDto.LocationDto();
            if (movie.getOperator().getLocation() != null) {
                location.setX(movie.getOperator().getLocation().getX().intValue());
                location.setY(movie.getOperator().getLocation().getY().intValue());
                location.setZ(movie.getOperator().getLocation().getZ().longValue());
            }
            operator.setLocation(location);
        }
        request.setOperator(operator);
        return request;
    }




}

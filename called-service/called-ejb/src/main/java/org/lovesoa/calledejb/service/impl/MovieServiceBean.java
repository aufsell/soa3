package org.lovesoa.calledejb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import org.lovesoa.calledejb.dtos.*;
import org.lovesoa.calledejb.models.*;
import org.lovesoa.calledejb.service.api.MovieServiceRemote;
import org.lovesoa.calledejb.service.util.ParserDTO;

import java.time.LocalDate;
import java.util.*;

import static org.lovesoa.calledejb.service.util.ParserDTO.MovietoDTO;

@Stateless
public class MovieServiceBean  implements MovieServiceRemote {

    @PersistenceContext(unitName = "calledPU")
    private EntityManager em;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Transactional
    public MovieResponseDTO createMovie(MovieCreateRequest request){

        if (request.getId() < 1) {
            throw new BadRequestException("id must be >= 1");
        }

        Location location = Location.builder()
                .x(request.getOperator().getLocation().getX())
                .y(request.getOperator().getLocation().getY())
                .z(request.getOperator().getLocation().getZ())
                .build();
        em.persist(location);

        Person operator = Person.builder()
                .name(request.getOperator().getName())
                .height(request.getOperator().getHeight())
                .weight(request.getOperator().getWeight())
                .location(location)
                .build();
        em.persist(operator);

        Coordinates coordinates = Coordinates.builder()
                .x(request.getCoordinates().getX())
                .y(request.getCoordinates().getY())
                .build();
        em.persist(coordinates);

        Movie movie = Movie.builder()
                .name(request.getName())
                .coordinates(coordinates)
                .oscarsCount(Long.valueOf(request.getOscarsCount()))
                .genre(request.getGenre() != null ? MovieGenre.valueOf(request.getGenre()) : null)
                .mpaaRating(MpaaRating.valueOf(request.getMpaaRating()))
                .operator(operator)
                .creationDate(LocalDate.now())
                .build();

        em.persist(movie);
        return MovietoDTO(movie);
    }

    public Movie getMovie(Long id){
        if (id == null || id < 1) {
            throw new BadRequestException("id must be >= 1");
        }

        Movie movie = em.find(Movie.class, id);

        if (movie == null) {
            throw new NullPointerException("movie with id " + id + " not found");
        }
        return movie;
    }

    public MovieResponseDTO getMovieById(Long id){
        Movie movie = getMovie(id);

        return MovietoDTO(movie);
    }

    @Transactional
    public MovieResponseDTO singleMovieUpdate(Long id, MovieUpdateRequest request){
        System.out.println("singleMovieUpdate");
        MovieCreateRequest movieCreateRequest = new MovieCreateRequest();
        movieCreateRequest.setName(request.getName());
        movieCreateRequest.setOscarsCount(request.getOscarsCount());
        movieCreateRequest.setMpaaRating(request.getMpaaRating());
        movieCreateRequest.setCoordinates(request.getCoordinates());
        movieCreateRequest.setGenre(request.getGenre());
        movieCreateRequest.setId(id);
        movieCreateRequest.setOperator(request.getOperator());

        return MovietoDTO(updateMovie(id,movieCreateRequest)) ;
    }

    @Transactional
    public Movie updateMovie(Long id, MovieCreateRequest request){

        Movie movie = getMovie(id);

        if (request.getOperator() != null && request.getOperator().getLocation() != null) {
            Location location = movie.getOperator().getLocation();
            if (location == null) {
                location = Location.builder().build();
                movie.getOperator().setLocation(location);
            }
            location.setX(request.getOperator().getLocation().getX());
            location.setY(request.getOperator().getLocation().getY());
            location.setZ(request.getOperator().getLocation().getZ());
            em.persist(location);
        }

        if (request.getOperator() != null) {
            Person operator = movie.getOperator();
            if (operator == null) {
                operator = Person.builder().build();
                movie.setOperator(operator);
            }
            operator.setName(request.getOperator().getName());
            operator.setHeight(request.getOperator().getHeight());
            operator.setWeight(request.getOperator().getWeight());
            operator.setLocation(movie.getOperator().getLocation());
            em.persist(operator);
        }

        if (request.getCoordinates() != null) {
            Coordinates coordinates = movie.getCoordinates();
            if (coordinates == null) {
                coordinates = Coordinates.builder().build();
                movie.setCoordinates(coordinates);
            }
            coordinates.setX(request.getCoordinates().getX());
            coordinates.setY(request.getCoordinates().getY());
            em.persist(coordinates);
        }

        movie.setName(request.getName());
        movie.setOscarsCount(request.getOscarsCount());
        movie.setGenre(request.getGenre() != null ? MovieGenre.valueOf(request.getGenre()) : null);
        movie.setMpaaRating(MpaaRating.valueOf(request.getMpaaRating()));
        em.persist(movie);
        return movie;

    }

    @Transactional
    public List<MovieResponseDTO> updateMovies(MoviePutListDTORequest request) {

        if (request.getMovies() == null || request.getMovies().isEmpty()) {
            throw new BadRequestException("movies array cannot be empty");
        }

        List<MovieResponseDTO> result = new ArrayList<>();

        for (MovieCreateRequest req : request.getMovies()) {

            if (req.getId() == null || req.getId() < 1) {
                throw new BadRequestException("Each movie must contain id >= 1");
            }

            Movie updated = updateMovie(req.getId(), req);
            result.add(MovietoDTO(updated));
        }

        return result;
    }


    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = getMovie(id);
        if (movie == null) {
            throw new NotFoundException("Movie with id " + id + " not found");
        }

        em.remove(movie);
    }

    @Override
    public PageDTO<MovieResponseDTO> searchMovies(MovieSearchRequest request) {

        Map<String, Object> filters =
                request.getFilters() != null ? request.getFilters() : Map.of();

        List<String> sort =
                request.getSort() != null ? request.getSort() : List.of();

        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 20;

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
        Root<Movie> root = cq.from(Movie.class);

        Map<String, From<?, ?>> joins = new HashMap<>();
        joins.put("root", root);

        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            int i1 = key.indexOf("[");
            int i2 = key.indexOf("]");
            if (i1 < 0 || i2 < 0) continue;

            String fieldPath = key.substring(0, i1);
            String op = key.substring(i1 + 1, i2);

            Path<?> path = getPath(root, joins, fieldPath);
            Predicate p = buildPredicate(cb, path, op, value);
            if (p != null) predicates.add(p);
        }

        cq.where(predicates.toArray(new Predicate[0]));

        if (!sort.isEmpty()) {
            List<Order> orders = new ArrayList<>();
            for (String s : sort) {
                String[] parts = s.split(":");
                if (parts.length != 2) continue;

                Path<?> path = getPath(root, joins, parts[0]);
                boolean desc = "desc".equalsIgnoreCase(parts[1]);
                orders.add(desc ? cb.desc(path) : cb.asc(path));
            }
            cq.orderBy(orders);
        }

        int pageNumber = Math.max(page, 0);
        int pageSize = Math.min(Math.max(size, 1), 100);

        List<Movie> result = em.createQuery(cq)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Movie> countRoot = countQuery.from(Movie.class);
        countQuery.select(cb.count(countRoot));

        Map<String, From<?, ?>> joins2 = new HashMap<>();
        joins2.put("root", countRoot);

        List<Predicate> preds2 = new ArrayList<>();
        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            int i1 = key.indexOf("[");
            int i2 = key.indexOf("]");
            if (i1 < 0 || i2 < 0) continue;

            String fieldPath = key.substring(0, i1);
            String op = key.substring(i1 + 1, i2);

            Path<?> path = getPath(countRoot, joins2, fieldPath);
            Predicate p = buildPredicate(cb, path, op, value);
            if (p != null) preds2.add(p);
        }

        countQuery.where(preds2.toArray(new Predicate[0]));

        long total = em.createQuery(countQuery).getSingleResult();

        PageDTO<MovieResponseDTO> dto = new PageDTO<>();
        List<MovieResponseDTO> contentList = new ArrayList<>();
        for (Movie m : result) {
            contentList.add(ParserDTO.MovietoDTO(m));
        }
        dto.setContent(contentList);
        dto.setPage(pageNumber);
        dto.setSize(pageSize);
        dto.setTotalElements(total);
        dto.setTotalPages((int) Math.ceil((double) total / pageSize));

        return dto;
    }

    private Path<?> getPath(From<?, ?> root, Map<String, From<?, ?>> joins, String fieldPath) {
        String[] parts = fieldPath.split("\\.");
        From<?, ?> current = root;
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < parts.length - 1; i++) {
            key.append(parts[i]);
            String joinKey = key.toString();

            if (!joins.containsKey(joinKey)) {
                current = current.join(parts[i], JoinType.LEFT);
                joins.put(joinKey, current);
            } else {
                current = joins.get(joinKey);
            }

            key.append(".");
        }

        return current.get(parts[parts.length - 1]);
    }

    private Predicate buildPredicate(
            CriteriaBuilder cb, Path<?> path, String operator, Object value
    ) {
        Class<?> javaType = path.getJavaType();

        if (javaType.isEnum() && value instanceof String) {
            @SuppressWarnings("unchecked")
            Object enumValue = Enum.valueOf((Class<Enum>) javaType, ((String) value).toUpperCase());
            value = enumValue;
        }

        if ("eq".equalsIgnoreCase(operator)) {
            return cb.equal(path, value);
        } else if ("ne".equalsIgnoreCase(operator)) {
            return cb.notEqual(path, value);
        } else if ("gt".equalsIgnoreCase(operator)) {
            return numberOp(cb, path, value, "gt");
        } else if ("lt".equalsIgnoreCase(operator)) {
            return numberOp(cb, path, value, "lt");
        } else if ("gte".equalsIgnoreCase(operator)) {
            return numberOp(cb, path, value, "gte");
        } else if ("lte".equalsIgnoreCase(operator)) {
            return numberOp(cb, path, value, "lte");
        } else if ("in".equalsIgnoreCase(operator)) {
            if (value instanceof Collection) {
                return path.in((Collection<?>) value);
            } else if (value.getClass().isArray()) {
                return path.in(Arrays.asList((Object[]) value));
            }
        }
        throw new IllegalArgumentException("Unsupported operator: " + operator);
    }

    @SuppressWarnings("unchecked")
    private Predicate numberOp(CriteriaBuilder cb, Path<?> path, Object value, String op) {
        Number num = (Number) value;
        @SuppressWarnings("unchecked")
        Path<? extends Number> n = (Path<? extends Number>) path;

        if ("gt".equals(op)) {
            return cb.gt(n, num);
        } else if ("lt".equals(op)) {
            return cb.lt(n, num);
        } else if ("gte".equals(op)) {
            return cb.ge(n, num);
        } else if ("lte".equals(op)) {
            return cb.le(n, num);
        } else {
            return null;
        }
    }



}

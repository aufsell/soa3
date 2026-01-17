package org.lovesoa.calledadapter.soap;

import jakarta.xml.ws.Service;
import org.lovesoa.calledadapter.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.List;

@Component
public class MovieSoapClient {

    @Value("${soap.service.url}")
    private String soapServiceUrl;

    private static final String NAMESPACE_URI = "http://soap.calledejb.lovesoa.org/";
    private static final String SERVICE_NAME = "MovieService";

    public MovieResponseDTO createMovie(MovieCreateRequest request) {
        try {
            MovieSoapServiceInterface port = getPort();
            org.lovesoa.calledadapter.soap.model.MovieResponseDTO soapResponse = 
                port.createMovie(toSoapMovieCreateRequest(request));
            return fromSoapMovieResponse(soapResponse);
        } catch (Exception e) {
            throw new RuntimeException("Error calling SOAP service: " + e.getMessage(), e);
        }
    }

    public MovieResponseDTO getMovieById(Long id) {
        try {
            MovieSoapServiceInterface port = getPort();
            org.lovesoa.calledadapter.soap.model.MovieResponseDTO soapResponse = 
                port.getMovieById(id);
            return fromSoapMovieResponse(soapResponse);
        } catch (Exception e) {
            throw new RuntimeException("Error calling SOAP service: " + e.getMessage(), e);
        }
    }

    public List<MovieResponseDTO> updateMovies(MoviePutListDTORequest request) {
        try {
            MovieSoapServiceInterface port = getPort();
            org.lovesoa.calledadapter.soap.model.MoviePutListDTORequest soapRequest = 
                toSoapMoviePutListRequest(request);
            List<org.lovesoa.calledadapter.soap.model.MovieResponseDTO> soapResponses = 
                port.updateMovies(soapRequest);
            return soapResponses.stream()
                .map(this::fromSoapMovieResponse)
                .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error calling SOAP service: " + e.getMessage(), e);
        }
    }

    public MovieResponseDTO singleMovieUpdate(Long id, MovieUpdateRequest request) {
        try {
            MovieSoapServiceInterface port = getPort();
            org.lovesoa.calledadapter.soap.model.MovieUpdateRequest soapRequest = 
                toSoapMovieUpdateRequest(request);
            org.lovesoa.calledadapter.soap.model.MovieResponseDTO soapResponse = 
                port.singleMovieUpdate(id, soapRequest);
            return fromSoapMovieResponse(soapResponse);
        } catch (Exception e) {
            throw new RuntimeException("Error calling SOAP service: " + e.getMessage(), e);
        }
    }

    public void deleteMovie(Long id) {
        try {
            MovieSoapServiceInterface port = getPort();
            port.deleteMovie(id);
        } catch (Exception e) {
            throw new RuntimeException("Error calling SOAP service: " + e.getMessage(), e);
        }
    }

    public PageDTO<MovieResponseDTO> searchMovies(MovieSearchRequest request) {
        try {
            MovieSoapServiceInterface port = getPort();
            org.lovesoa.calledadapter.soap.model.MovieSearchRequest soapRequest = 
                toSoapMovieSearchRequest(request);
            org.lovesoa.calledadapter.soap.model.PageDTO soapPage = 
                port.searchMovies(soapRequest);
            return fromSoapPageDTO(soapPage);
        } catch (Exception e) {
            throw new RuntimeException("Error calling SOAP service: " + e.getMessage(), e);
        }
    }

    private MovieSoapServiceInterface getPort() throws Exception {
        URL wsdlLocation = new URL(soapServiceUrl + "/MovieService/MovieService?wsdl");
        QName serviceName = new QName(NAMESPACE_URI, SERVICE_NAME);
        Service service = Service.create(wsdlLocation, serviceName);
        return service.getPort(MovieSoapServiceInterface.class);
    }

    // Conversion methods
    private org.lovesoa.calledadapter.soap.model.MovieCreateRequest toSoapMovieCreateRequest(MovieCreateRequest dto) {
        org.lovesoa.calledadapter.soap.model.MovieCreateRequest soap = 
            new org.lovesoa.calledadapter.soap.model.MovieCreateRequest();
        soap.setId(dto.getId());
        soap.setName(dto.getName());
        soap.setOscarsCount(dto.getOscarsCount());
        soap.setGenre(dto.getGenre());
        soap.setMpaaRating(dto.getMpaaRating());
        
        if (dto.getCoordinates() != null) {
            org.lovesoa.calledadapter.soap.model.CoordinatesDto coords = 
                new org.lovesoa.calledadapter.soap.model.CoordinatesDto();
            coords.setX(dto.getCoordinates().getX());
            coords.setY(dto.getCoordinates().getY());
            soap.setCoordinates(coords);
        }
        
        if (dto.getOperator() != null) {
            org.lovesoa.calledadapter.soap.model.PersonDto person = 
                new org.lovesoa.calledadapter.soap.model.PersonDto();
            person.setName(dto.getOperator().getName());
            person.setHeight(dto.getOperator().getHeight());
            person.setWeight(dto.getOperator().getWeight());
            
            if (dto.getOperator().getLocation() != null) {
                org.lovesoa.calledadapter.soap.model.LocationDto loc = 
                    new org.lovesoa.calledadapter.soap.model.LocationDto();
                loc.setX(dto.getOperator().getLocation().getX());
                loc.setY(dto.getOperator().getLocation().getY());
                loc.setZ(dto.getOperator().getLocation().getZ());
                person.setLocation(loc);
            }
            soap.setOperator(person);
        }
        
        return soap;
    }

    private org.lovesoa.calledadapter.soap.model.MovieUpdateRequest toSoapMovieUpdateRequest(MovieUpdateRequest dto) {
        org.lovesoa.calledadapter.soap.model.MovieUpdateRequest soap = 
            new org.lovesoa.calledadapter.soap.model.MovieUpdateRequest();
        soap.setName(dto.getName());
        soap.setOscarsCount(dto.getOscarsCount());
        soap.setGenre(dto.getGenre());
        soap.setMpaaRating(dto.getMpaaRating());
        
        if (dto.getCoordinates() != null) {
            org.lovesoa.calledadapter.soap.model.CoordinatesDto coords = 
                new org.lovesoa.calledadapter.soap.model.CoordinatesDto();
            coords.setX(dto.getCoordinates().getX());
            coords.setY(dto.getCoordinates().getY());
            soap.setCoordinates(coords);
        }
        
        if (dto.getOperator() != null) {
            org.lovesoa.calledadapter.soap.model.PersonDto person = 
                new org.lovesoa.calledadapter.soap.model.PersonDto();
            person.setName(dto.getOperator().getName());
            person.setHeight(dto.getOperator().getHeight());
            person.setWeight(dto.getOperator().getWeight());
            
            if (dto.getOperator().getLocation() != null) {
                org.lovesoa.calledadapter.soap.model.LocationDto loc = 
                    new org.lovesoa.calledadapter.soap.model.LocationDto();
                loc.setX(dto.getOperator().getLocation().getX());
                loc.setY(dto.getOperator().getLocation().getY());
                loc.setZ(dto.getOperator().getLocation().getZ());
                person.setLocation(loc);
            }
            soap.setOperator(person);
        }
        
        return soap;
    }

    private org.lovesoa.calledadapter.soap.model.MoviePutListDTORequest toSoapMoviePutListRequest(MoviePutListDTORequest dto) {
        org.lovesoa.calledadapter.soap.model.MoviePutListDTORequest soap = 
            new org.lovesoa.calledadapter.soap.model.MoviePutListDTORequest();
        if (dto.getMovies() != null) {
            soap.setMovies(dto.getMovies().stream()
                .map(this::toSoapMovieCreateRequest)
                .toList());
        }
        return soap;
    }

    private org.lovesoa.calledadapter.soap.model.MovieSearchRequest toSoapMovieSearchRequest(MovieSearchRequest dto) {
        org.lovesoa.calledadapter.soap.model.MovieSearchRequest soap = 
            new org.lovesoa.calledadapter.soap.model.MovieSearchRequest();
        soap.setFilters(dto.getFilters());
        soap.setSort(dto.getSort());
        soap.setPage(dto.getPage());
        soap.setSize(dto.getSize());
        return soap;
    }

    private MovieResponseDTO fromSoapMovieResponse(org.lovesoa.calledadapter.soap.model.MovieResponseDTO soap) {
        MovieResponseDTO dto = new MovieResponseDTO();
        dto.setId(soap.getId());
        dto.setName(soap.getName());
        dto.setOscarsCount(soap.getOscarsCount());
        dto.setGenre(soap.getGenre());
        dto.setMpaaRating(soap.getMpaaRating());
        dto.setCreationDate(soap.getCreationDate());
        
        if (soap.getCoordinates() != null) {
            CoordinatesDTO coords = new CoordinatesDTO();
            coords.setId(null);
            coords.setX(soap.getCoordinates().getX());
            coords.setY(soap.getCoordinates().getY());
            dto.setCoordinates(coords);
        }
        
        if (soap.getOperator() != null) {
            PersonDTO person = new PersonDTO();
            person.setId(null);
            person.setName(soap.getOperator().getName());
            person.setHeight(soap.getOperator().getHeight());
            person.setWeight(soap.getOperator().getWeight());
            
            if (soap.getOperator().getLocation() != null) {
                LocationDTO loc = new LocationDTO();
                loc.setId(null);
                loc.setX(soap.getOperator().getLocation().getX());
                loc.setY(soap.getOperator().getLocation().getY());
                loc.setZ(soap.getOperator().getLocation().getZ());
                person.setLocation(loc);
            }
            dto.setOperator(person);
        }
        
        return dto;
    }

    private PageDTO<MovieResponseDTO> fromSoapPageDTO(org.lovesoa.calledadapter.soap.model.PageDTO soap) {
        PageDTO<MovieResponseDTO> dto = new PageDTO<>();
        if (soap.getContent() != null) {
            dto.setContent(soap.getContent().stream()
                .map(obj -> fromSoapMovieResponse((org.lovesoa.calledadapter.soap.model.MovieResponseDTO) obj))
                .toList());
        }
        dto.setPage(soap.getPage());
        dto.setSize(soap.getSize());
        dto.setTotalElements(soap.getTotalElements());
        dto.setTotalPages(soap.getTotalPages());
        return dto;
    }
}

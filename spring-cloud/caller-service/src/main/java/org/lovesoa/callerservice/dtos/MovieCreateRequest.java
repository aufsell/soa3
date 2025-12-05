package org.lovesoa.callerservice.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieCreateRequest {

    private Long id;

    private String name;

    private CoordinatesDto coordinates;

    private Integer oscarsCount;

    private String genre;

    private String mpaaRating;

    private PersonDto operator;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CoordinatesDto {
        private Integer x;

        private Float y;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PersonDto {
        private String name;

        private Float height;

        private Integer weight;

        private LocationDto location;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class LocationDto {
            private Integer x;

            private Integer y;

            private Long z;
        }
    }
}

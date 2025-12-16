package org.lovesoa.calledejb.dtos;

import lombok.*;

import javax.validation.constraints.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieCreateRequest {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private CoordinatesDto coordinates;

    @NotNull
    @Min(0)
    private Long oscarsCount;

    private String genre;

    @NotBlank
    private String mpaaRating;

    @NotNull
    private PersonDto operator;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CoordinatesDto {
        @NotNull
        private Integer x;

        @NotNull
        @Max(102)
        private Float y;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PersonDto {
        @NotBlank
        private String name;

        @NotNull
        @Positive
        private Float height;

        @Positive
        private Integer weight;

        @NotNull
        private LocationDto location;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class LocationDto {
            @NotNull
            private Integer x;

            private int y;

            @NotNull
            private Long z;
        }
    }
}

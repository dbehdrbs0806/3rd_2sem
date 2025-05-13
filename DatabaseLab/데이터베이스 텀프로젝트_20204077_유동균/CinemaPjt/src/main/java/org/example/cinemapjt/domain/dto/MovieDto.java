package org.example.cinemapjt.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {
    private String movieId;
    private String title;
    private String genre;
    private String director;
    private String leadActor;
    private String posterPath;


}
package bg.softuni.cinevault.service;


import bg.softuni.cinevault.dto.movie.MovieAddDto;
import bg.softuni.cinevault.entities.Movie;

import java.util.List;
import java.util.UUID;

public interface MovieService {
    Movie add(MovieAddDto dto);

    List<Movie> findAll();

    Movie findById(UUID id);
}

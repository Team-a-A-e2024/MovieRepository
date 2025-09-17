package app.utils;

import app.dtos.GenreDTO;
import app.entities.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreMapper {

    public static List<Genre> mapGenreDTOtoGenreEntity(GenreDTO genreDTO) {
        List<Genre> genres = new ArrayList<>();
        for(GenreDTO.Genres genre : genreDTO.getGenres()){
            genres.add(Genre.builder().
                    id(genre.getId()).
                    genre(genre.getName()).
                    build()
            );
        }
        return genres;
    }
}

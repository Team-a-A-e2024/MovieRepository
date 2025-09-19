package app;

import app.config.HibernateConfig;
import app.daos.GenreDAO;
import app.daos.MovieCastDAO;
import app.daos.MovieDAO;
import app.daos.PersonDAO;
import app.dtos.CastDTO;
import app.dtos.CreditsDTO;
import app.dtos.GenreDTO;
import app.dtos.MovieDTO;
import app.entities.Genre;
import app.entities.Movie;
import app.entities.MovieCast;
import app.entities.Person;
import app.services.CreditsService;
import app.services.FetchTools;
import app.services.GenreService;
import app.services.MovieService;
import app.utils.GenreMapper;
import app.utils.MovieCastMapper;
import app.utils.MovieMapper;
import app.utils.PersonMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();


        GenreDAO genreDAO = new GenreDAO(emf);
        MovieCastDAO movieCastDAO = new MovieCastDAO(emf);
        MovieDAO movieDAO = new MovieDAO(emf);
        PersonDAO personDAO = new PersonDAO(emf);

        FetchTools fetchTools = new FetchTools();
        GenreService genreService = new GenreService(fetchTools);
        CreditsService creditsService = new CreditsService(fetchTools);
        MovieService movieService = new MovieService(fetchTools);

        GenreDTO genreDTO = genreService.getGenresInfo();
        List<MovieDTO.Movie> movieDTOs = movieService.getRecentDanishMoviesInfo();
        List<CreditsDTO> creditsDTOs = creditsService.getAllCreditsInfo(movieDTOs);

        List<Genre> genres = GenreMapper.mapGenreDTOtoGenreEntity(genreDTO);
        List<Movie> movies = MovieMapper.mapMovieDTOtoMovieEntity(movieDTOs, genres);
        Set<Person> persons = PersonMapper.mapCreditsDTOsToPersonSet(creditsDTOs);
        List<MovieCast> casts = MovieCastMapper.creditsDTOListToMovieCastList(creditsDTOs, movies);

        genreDAO.createAll(genres);
        movieDAO.createAll(movies);
        persons.forEach(personDAO::create);
        movieCastDAO.createAll(casts);

        emf.close();
    }
}
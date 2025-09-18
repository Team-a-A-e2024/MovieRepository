package app.services;

import app.dtos.MovieDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private FetchTools fetchTools;

    @InjectMocks
    private MovieService movieService;

    @Test
    void getRecentDanishMoviesInfo() {
        // Arrange
        int totalPages = 3;

        MovieDTO firstPageInfo = mock(MovieDTO.class);
        when(firstPageInfo.getTotalPages()).thenReturn(totalPages);
        when(fetchTools.getFromApi(anyString(), eq(MovieDTO.class))).thenReturn(firstPageInfo);

        MovieDTO.Movie movie1 = mock(MovieDTO.Movie.class);
        MovieDTO.Movie movie2 = mock(MovieDTO.Movie.class);
        MovieDTO.Movie movie3 = mock(MovieDTO.Movie.class);
        MovieDTO.Movie movie4 = mock(MovieDTO.Movie.class);

        MovieDTO page1 = mock(MovieDTO.class);
        MovieDTO page2 = mock(MovieDTO.class);
        MovieDTO page3 = mock(MovieDTO.class);

        when(page1.getResults()).thenReturn(List.of(movie1, movie2));
        when(page2.getResults()).thenReturn(List.of(movie3));
        when(page3.getResults()).thenReturn(List.of(movie4));

        when(fetchTools.getFromApiList(anyList(), eq(MovieDTO.class))).thenReturn(List.of(page1, page2, page3));

        // Act
        List<MovieDTO.Movie> actual = movieService.getRecentDanishMoviesInfo();

        // Assert
        assertTrue(actual.contains(movie1));
        assertTrue(actual.contains(movie2));
        assertTrue(actual.contains(movie3));
        assertTrue(actual.contains(movie4));
    }

    @Test
    void getRecentDanishMoviesNegative() {
        // Arrange
        when(fetchTools.getFromApi(anyString(), eq(MovieDTO.class))).thenThrow(new RuntimeException("boom"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> movieService.getRecentDanishMoviesInfo());
    }
}
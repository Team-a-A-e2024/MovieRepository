package app.services;

import app.dtos.GenreDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {
    @Mock
    private FetchTools fetchTools;

    @InjectMocks
    private GenreService genreService;

    @Test
    void getGenresInfo() {
        // Arrange
        GenreDTO expected = new GenreDTO();
        when(fetchTools.getFromApi(anyString(), eq(GenreDTO.class))).thenReturn(expected);

        // Act
        GenreDTO actual = genreService.getGenresInfo();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void getGenresInfoNegative() {
        // Arrange
        when(fetchTools.getFromApi(anyString(), eq(GenreDTO.class))).thenThrow(new RuntimeException("boom"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> genreService.getGenresInfo());
    }
}
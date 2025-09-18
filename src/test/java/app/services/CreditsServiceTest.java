package app.services;

import app.dtos.CreditsDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditsServiceTest {
    @Mock
    private FetchTools fetchTools;

    @InjectMocks
    private CreditsService creditsService;

    @Test
    void getCreditsInfo() {
        // Arrange
        CreditsDTO expected = new CreditsDTO();
        when(fetchTools.getFromApi(anyString(), eq(CreditsDTO.class))).thenReturn(expected);

        // Act
        CreditsDTO actual = creditsService.getCreditsInfoByMovieId(139);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void getCreditsInfoNegative() {
        // Arrange
        when(fetchTools.getFromApi(anyString(), eq(CreditsDTO.class))).thenThrow(new RuntimeException("boom"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> creditsService.getCreditsInfoByMovieId(0));
    }
}
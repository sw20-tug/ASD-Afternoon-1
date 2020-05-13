package at.tugraz.sw20asd.lang.server.controllers;

import at.tugraz.sw20asd.lang.dto.EntryDto;
import at.tugraz.sw20asd.lang.server.services.IEntryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static at.tugraz.sw20asd.lang.util.TestUtilities.getRandomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class EntryControllerTests {
    @Mock
    IEntryService entryService;

    @InjectMocks
    EntryController entryController;

    @ParameterizedTest
    @ValueSource(strings = {"", "non-numeric-value", "1.2", "2,3"})
    public void testGetEntry_ControllerDetectsMalformedId(String idString) {
        ResponseEntity<?> response = entryController.getEntryById(idString);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testGetEntryById_ReturnsNotFound() {
        ResponseEntity<?> responseEntity = entryController.getEntryById("1");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetEntryById_ReturnsEntry() {
        EntryDto e = new EntryDto(getRandomString(15), getRandomString(15));

        when(entryService.findById(anyLong())).thenReturn(e);

        ResponseEntity<?> responseEntity = entryController.getEntryById("1");
        assertAll(
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(responseEntity.getBody()).isEqualTo(e)
        );
    }
}

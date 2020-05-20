package at.tugraz.sw20asd.lang.server.controllers;

import at.tugraz.sw20asd.lang.dto.EntryDto;
import at.tugraz.sw20asd.lang.server.services.IEntryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static at.tugraz.sw20asd.lang.util.TestUtilities.getRandomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
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
        EntryDto e = getRandomEntry();

        when(entryService.findById(anyLong())).thenReturn(e);

        ResponseEntity<?> responseEntity = entryController.getEntryById("1");
        assertAll(
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(responseEntity.getBody()).isEqualTo(e)
        );
    }

    @Test
    public void testEditEntry_RejectsEntryWithNullId() {
        EntryDto e = getRandomEntry();
        e.setId(null);

        ResponseEntity<?> responseEntity = entryController.editEntry(e);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testEditEntry_RejectsEntryWithNullPhrase() {
        EntryDto e = getRandomEntry();
        e.setPhrase(null);

        ResponseEntity<?> responseEntity = entryController.editEntry(e);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testEditEntry_RejectsEntryWithNullTranslation() {
        EntryDto e = getRandomEntry();
        e.setTranslation(null);

        ResponseEntity<?> responseEntity = entryController.editEntry(e);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testEditEntry_ReturnsNotFoundWhenServiceReturnsFalse() {
        ResponseEntity<?> responseEntity = entryController.editEntry(getRandomEntry());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testEditEntry_ReturnsOk() {
        when(entryService.updateEntry(any(EntryDto.class))).thenReturn(true);

        ResponseEntity<?> responseEntity = entryController.editEntry(getRandomEntry());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testDeleteEntry_ReturnsOk() {
        when(entryService.deleteEntry(anyLong())).thenReturn(true);

        ResponseEntity<?> responseEntity = entryController.deleteEntry("1");

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testDeleteEntry_ReturnsNotFound() {
        ResponseEntity<?> responseEntity = entryController.deleteEntry("1");

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "non-numeric-value", "1.2", "2,3"})
    public void testDeleteEntry_ReturnsBadRequest(String str) {
        ResponseEntity<?> response = entryController.deleteEntry(str);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private EntryDto getRandomEntry() {
        EntryDto tmp = new EntryDto();
        tmp.setId(4L); // chosen by fair dice roll. guaranteed to be random. (https://xkcd.com/221/)
        tmp.setPhrase(getRandomString(15));
        tmp.setTranslation(getRandomString(12));

        return tmp;
    }
}

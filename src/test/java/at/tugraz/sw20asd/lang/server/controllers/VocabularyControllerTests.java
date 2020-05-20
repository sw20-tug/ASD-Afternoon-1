package at.tugraz.sw20asd.lang.server.controllers;

import at.tugraz.sw20asd.lang.dto.EntryDto;
import at.tugraz.sw20asd.lang.dto.VocabularyBaseDto;
import at.tugraz.sw20asd.lang.dto.VocabularyDetailDto;
import at.tugraz.sw20asd.lang.server.services.IVocabularyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static at.tugraz.sw20asd.lang.util.TestUtilities.getRandomString;
import static java.util.Locale.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VocabularyControllerTests {

	@Mock
	IVocabularyService vocabularyService;

	@InjectMocks
	VocabularyController vocabularyController;

	@Test
	public void testGetOverview()
	{
		String vocabularyNameToCheck = "Meine Französisch-Vokabel";

		VocabularyBaseDto v1 = new VocabularyBaseDto(1L, "My 1st Vocabs", GERMAN, ENGLISH);
		VocabularyBaseDto v2 = new VocabularyBaseDto(2L, vocabularyNameToCheck, GERMAN, FRENCH);

		when(vocabularyService.list()).thenReturn(Arrays.asList(v1, v2));

		List<VocabularyBaseDto> overview = vocabularyController.overview();

		assertAll(
				() -> assertThat(overview.size()).isEqualTo(2),
				() -> assertThat(overview.get(0).getId()).isEqualTo(1L),
				() -> assertThat(overview.get(1).getName()).isEqualTo(vocabularyNameToCheck)
		);
	}

	@ParameterizedTest
	@MethodSource("invalidIdSource")
	public void testGetVocabularyById_ControllerDetectsMalformedId(String idString) {
		ResponseEntity<?> response = vocabularyController.getVocabularyById(idString);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void testGetVocabularyById_ReturnsNotFound() {
		ResponseEntity<?> responseEntity = vocabularyController.getVocabularyById("1");

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void testAddVocabulary_ReturnsExpectedLocation() {
		long expectedId = 123L;

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		when(vocabularyService.add(any(VocabularyDetailDto.class)))
				.thenReturn(expectedId);

		VocabularyDetailDto v = new VocabularyDetailDto();
		ResponseEntity<?> responseEntity = vocabularyController.addVocabulary(v);

		assertAll(
				() -> assertThat(responseEntity.getStatusCode())
						.isEqualTo(HttpStatus.CREATED),
				() -> assertThat(responseEntity.getHeaders())
						.containsKey("Location"),
				() -> assertThat(responseEntity.getHeaders().getLocation().getPath())
						.isEqualTo(String.format("/%d", expectedId))
		);
	}

	@ParameterizedTest
	@MethodSource("invalidIdSource")
	public void testAddEntry_ControllerDetectsMalformedId(String idString) {
		ResponseEntity<?> response = vocabularyController.getVocabularyById(idString);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void testAddEntry_ReturnsNotFound() {
		ResponseEntity<?> responseEntity = vocabularyController.addEntryToVocabulary("1", getRandomEntry());

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void testAddEntry_ReturnsOk() {
		when(vocabularyService.addToVocabulary(anyLong(), any(EntryDto.class))).thenReturn(true);

		ResponseEntity<?> responseEntity = vocabularyController.addEntryToVocabulary("1", getRandomEntry());

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void testDeleteVocabulary_ReturnsOk() {
		when(vocabularyService.deleteVocabulary(anyLong())).thenReturn(true);

		ResponseEntity<?> responseEntity = vocabularyController.deleteVocabulary("1");

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void testDeleteVocabulary_ReturnsNotFound() {
		ResponseEntity<?> responseEntity = vocabularyController.deleteVocabulary("1");

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@ParameterizedTest
	@MethodSource("invalidIdSource")
	public void testDeleteVocabulary_ReturnsBadRequest(String str) {
		ResponseEntity<?> response = vocabularyController.deleteVocabulary(str);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	private EntryDto getRandomEntry() {
		return new EntryDto(getRandomString(15), getRandomString(15));
	}

	private static Collection<String> invalidIdSource() {
		return Arrays.asList(
				"",
				"non-numeric-input",
				"1.3",
				"2,4");
	}
}

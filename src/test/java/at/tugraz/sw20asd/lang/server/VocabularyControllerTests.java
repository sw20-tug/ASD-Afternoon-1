package at.tugraz.sw20asd.lang.server;

import at.tugraz.sw20asd.lang.model.Entry;
import at.tugraz.sw20asd.lang.model.Vocabulary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import static at.tugraz.sw20asd.lang.TestUtilities.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VocabularyControllerTests {
	@LocalServerPort
	private int randomServerPort;

	private RestTemplate restTemplate;

	@BeforeEach
	public void init() {
		restTemplate = new RestTemplate();
	}

	@Test
	public void testAddVocabulary() throws URISyntaxException {
		HttpEntity<Vocabulary> request = createVocabularyEntity(getRandomString());

		ResponseEntity<Object> result = restTemplate.postForEntity(vocabBaseURI(), request, Object.class);

		assertAll(
				() -> assertEquals(201, result.getStatusCodeValue()),
				() -> assertTrue(result.getHeaders().containsKey("Location")));
	}

	@Test void testAddVocabularyDoesNotOverwriteExisting() throws URISyntaxException {
		ResponseEntity<Vocabulary> result = restTemplate.getForEntity(vocabularyWithId(3), Vocabulary.class);
		Vocabulary vocabularyBefore = result.getBody();

		HttpEntity<Vocabulary> request = createVocabularyEntity(getRandomString());

		restTemplate.postForEntity(vocabBaseURI(), request, Object.class);

		result = restTemplate.getForEntity(vocabularyWithId(3), Vocabulary.class);
		Vocabulary vocabularyAfter = result.getBody();

		assertEquals(vocabularyBefore, vocabularyAfter);
	}

	@Test
	public void testGetAllVocabularies() throws URISyntaxException {
		ResponseEntity<List<Vocabulary>> response = restTemplate.exchange(
				vocabBaseURI(),
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Vocabulary>>() {
				});

		List<Vocabulary> list = response.getBody();

		assertNotNull(list);
		assertAll(
				() -> assertFalse(list.isEmpty()),
				() -> assertEquals(2, list.size()));
	}

	@Test
	public void testGetVocabularyById_ExistingId() throws URISyntaxException {
		ResponseEntity<Vocabulary> result = restTemplate.getForEntity(vocabularyWithId(2), Vocabulary.class);

		assertAll(
				() -> assertEquals(HttpStatus.OK, result.getStatusCode()),
				() -> assertTrue(result.hasBody()));

		Vocabulary vocab = result.getBody();
		assertAll(
				() -> assertNotNull(vocab),
				() -> assertEquals(2, vocab.getID()));
	}

	@Test
	public void testGetVocabularyById_NonExistingId() {
		assertThrows(HttpClientErrorException.NotFound.class,
				() -> restTemplate.getForEntity(vocabularyWithId(42), Vocabulary.class));
	}

	@Test
	public void testGetVocabularyById_MalformedId() throws URISyntaxException {
		URI uri = new URI(String.format("http://localhost:%d/vocab/foo", randomServerPort));

		assertThrows(HttpClientErrorException.BadRequest.class,
				() -> restTemplate.getForEntity(uri, Vocabulary.class));
	}

	@Test
	public void testAddPhraseToVocabulary() throws URISyntaxException {
		int vocabularyId = 2;

		URI uri = new URI(String.format("http://localhost:%d/vocab/%d/add", randomServerPort, vocabularyId));
		HttpEntity<Entry> e = createRandomEntryEntity();

		restTemplate.postForEntity(uri, e, Object.class);

		ResponseEntity<Vocabulary> result = restTemplate.getForEntity(vocabularyWithId(vocabularyId), Vocabulary.class);
		Vocabulary vocab = result.getBody();

		assertAll(
				() -> assertNotNull(vocab),
				() -> assertTrue(vocabularyContainsEntry(vocab, e.getBody())));
	}

	@Test
	public void testAddPhraseToVocabulary_MalformedId() throws URISyntaxException {

		URI uri = new URI(String.format("http://localhost:%d/vocab/%s/add", randomServerPort, "nonsense-id"));

		assertThrows(HttpClientErrorException.BadRequest.class,
				() ->restTemplate.postForEntity(uri, createRandomEntryEntity(), Object.class));
	}

	private HttpEntity<Vocabulary> createVocabularyEntity(String vocabularyName) {
		Vocabulary vocab = new Vocabulary(
				null,
				vocabularyName,
				Locale.GERMAN,
				Locale.ENGLISH);
		vocab.addPhrase(new Entry("foo", "bar"));

		return new HttpEntity<>(vocab);
	}

	private HttpEntity<Entry> createRandomEntryEntity() {
		Entry e = new Entry(getRandomString(), getRandomString());
		return new HttpEntity<>(e);
	}

	private URI vocabularyWithId(int id) throws URISyntaxException {
		return new URI(String.format("http://localhost:%d/vocab/%d", randomServerPort, id));
	}

	private URI vocabBaseURI() throws URISyntaxException {
		return new URI(String.format("http://localhost:%d/vocab/", randomServerPort));
	}
}

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
		URI uri = new URI(String.format("http://localhost:%d/vocab/", randomServerPort));

		HttpEntity<Vocabulary> request = CreateVocabularyEntity("MyVocabulary");

		ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);

		assertAll(
				() -> assertEquals(201, result.getStatusCodeValue()),
				() -> assertTrue(result.getHeaders().containsKey("Location")));
	}

	@Test
	public void testGetAllVocabularies() throws URISyntaxException {
		URI uri = new URI(String.format("http://localhost:%d/vocab/", randomServerPort));

		ResponseEntity<List<Vocabulary>> response = restTemplate.exchange(
				uri,
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
		URI uri = new URI(String.format("http://localhost:%d/vocab/1", randomServerPort));

		ResponseEntity<Vocabulary> result = restTemplate.getForEntity(uri, Vocabulary.class);

		assertAll(
				() -> assertEquals(HttpStatus.OK, result.getStatusCode()),
				() -> assertTrue(result.hasBody()));

		Vocabulary vocab = result.getBody();
		assertAll(
				() -> assertNotNull(vocab),
				() -> assertEquals(1, vocab.getID()));
	}

	@Test
	public void testGetVocabularyById_NonExistingId() throws URISyntaxException {
		URI uri = new URI(String.format("http://localhost:%d/vocab/42", randomServerPort));

		assertThrows(HttpClientErrorException.NotFound.class,
				() -> restTemplate.getForEntity(uri, Vocabulary.class));
	}

	@Test
	public void testGetVocabularyById_MalformedId() throws URISyntaxException {
		URI uri = new URI(String.format("http://localhost:%d/vocab/foo", randomServerPort));

		assertThrows(HttpClientErrorException.BadRequest.class,
				() -> restTemplate.getForEntity(uri, Vocabulary.class));
	}

	private HttpEntity<Vocabulary> CreateVocabularyEntity(String vocabularyName) {
		Vocabulary vocab = new Vocabulary(null, vocabularyName, Locale.GERMAN, Locale.ENGLISH);
		vocab.addPhrase(new Entry("foo", "bar"));

		return new HttpEntity<>(vocab);
	}
}

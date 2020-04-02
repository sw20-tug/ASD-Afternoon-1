package at.tugraz.sw20asd.lang.server;

import at.tugraz.sw20asd.lang.model.Entry;
import at.tugraz.sw20asd.lang.model.Vocabulary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
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

	private HttpEntity<Vocabulary> CreateVocabularyEntity(String vocabularyName) {
		Vocabulary vocab = new Vocabulary(null, vocabularyName, Locale.GERMAN, Locale.ENGLISH);
		vocab.addPhrase(new Entry("foo", "bar"));

		return new HttpEntity<>(vocab);
	}
}

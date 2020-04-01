package at.tugraz.sw20asd.lang.server;
import at.tugraz.sw20asd.lang.model.Entry;
import at.tugraz.sw20asd.lang.model.Vocabulary;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VocabularyControllerTests {
	@LocalServerPort
	int randomServerPort;

	@Test
	public void testAddVocabulary() throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();

		final String baseURL = "http://localhost:" + randomServerPort + "/vocab/";
		URI uri = new URI(baseURL);

		Vocabulary vocab = new Vocabulary(null, "MyVocabulary", Locale.GERMAN, Locale.ENGLISH);
		vocab.addPhrase(new Entry("foo", "bar"));

		HttpEntity<Vocabulary> request = new HttpEntity<>(vocab);

		ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);

		Assertions.assertEquals(201, result.getStatusCodeValue());
	}
}

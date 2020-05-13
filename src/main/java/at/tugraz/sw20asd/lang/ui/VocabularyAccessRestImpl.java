package at.tugraz.sw20asd.lang.ui;

import at.tugraz.sw20asd.lang.dto.VocabularyBaseDto;
import at.tugraz.sw20asd.lang.dto.VocabularyDetailDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class VocabularyAccessRestImpl implements VocabularyAccess {

    private RestTemplate restTemplate = new RestTemplate();
    private URI uri;

    public VocabularyAccessRestImpl(String host, int port){
        try {
            uri = new URI(String.format("http://%s:%d/vocab/", host, port));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer addVocabulary(VocabularyDetailDto vocabulary) {

        int id = -1;
        String buffer = null;
        String parse = null;

        HttpEntity<VocabularyDetailDto> request = new HttpEntity<>(vocabulary);

        ResponseEntity<Object> result = restTemplate.postForEntity(uri, request, Object.class);

        if(!(result.getHeaders().containsKey("Location")))
            return id;

        parse = Objects.requireNonNull(result.getHeaders().getLocation()).getPath();

        if(!parse.isEmpty()){
            buffer = parse.substring(parse.lastIndexOf('/'));
            parse = buffer.split("/")[1];
            id = Integer.parseInt(parse);
        }
        return id;
    }

    @Override
    public VocabularyDetailDto getVocabulary(long id) {
        try
        {
            ResponseEntity<VocabularyDetailDto> response = restTemplate.getForEntity(vocabularyWithId(id), VocabularyDetailDto.class);

            if(!response.getStatusCode().equals(HttpStatus.OK)
                    || !response.hasBody()) {
                return null;
            }

            return response.getBody();
        } catch(URISyntaxException ex) {
            return null;
        }
    }

    private URI vocabularyWithId(long id) throws URISyntaxException {
        return new URI(String.format("%s%d", uri.toString(), id));
    }

    @Override
    public List<VocabularyBaseDto> getAllVocabularies() {
        ResponseEntity<List<VocabularyBaseDto>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<VocabularyBaseDto>>() {
                });
        if(!response.getStatusCode().equals(HttpStatus.OK)
                || !response.hasBody()) {
            return null;
        }
        return response.getBody();
    }
}

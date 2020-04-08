package at.tugraz.sw20asd.lang.ui;

import at.tugraz.sw20asd.lang.model.Vocabulary;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class VocabularyAccessRestImpl implements VocabularyAccess {

    private RestTemplate restTemplate = new RestTemplate();
    private URI uri;

    public VocabularyAccessRestImpl(String host, int port){

        try {
            uri = new URI(String.format("http://%s:%d/vocab", host, port));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer addVocabulary(Vocabulary vocabulary) {

        int id = 0;
        String buffer = null;

        HttpEntity<Vocabulary> request = new HttpEntity<>(vocabulary);

        ResponseEntity<Object> result = restTemplate.postForEntity(uri, request, Object.class);

        if(!(result.getHeaders().containsKey("Location")))
            return null;

        List<String> list = result.getHeaders().getOrEmpty("Location");
        Optional<String> firstElement = list.stream().findFirst();


        if(firstElement.isEmpty())
            return null;
        else{
            buffer = firstElement.get();
            String temp = buffer.substring(buffer.lastIndexOf('/'+1));
            id = Integer.parseInt(temp);
            return id;
        }

    }

    @Override
    public Vocabulary getVocabulary(int id) {
        return null;
    }

    @Override
    public Collection<Vocabulary> getAllVocabularies() {
        return null;
    }
}

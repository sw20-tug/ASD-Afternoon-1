package at.tugraz.sw20asd.lang.server;

import at.tugraz.sw20asd.lang.model.Vocabulary;
import at.tugraz.sw20asd.lang.service.VocabularyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/vocab")
public class VocabularyController {

    private VocabularyDAO _vocabularyDao;

    @Autowired
    public VocabularyController(VocabularyDAO vocabularyDAO) {
        _vocabularyDao = vocabularyDAO;
    }

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addVocabulary(@RequestBody Vocabulary vocabulary) throws Exception {
        Integer id = _vocabularyDao.findAll().size() + 1;
        vocabulary.setId(id);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(vocabulary.getID())
                .toUri();

        if(_vocabularyDao.addVocabulary(vocabulary)) {
            return ResponseEntity.created(location).build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }
}

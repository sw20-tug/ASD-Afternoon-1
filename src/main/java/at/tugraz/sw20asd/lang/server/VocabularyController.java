package at.tugraz.sw20asd.lang.server;

import at.tugraz.sw20asd.lang.model.Entry;
import at.tugraz.sw20asd.lang.model.Vocabulary;
import at.tugraz.sw20asd.lang.service.VocabularyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Object> addVocabulary(@RequestBody Vocabulary vocabulary) {

        try {
            int insertedIndex = _vocabularyDao.addVocabulary(vocabulary);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(insertedIndex)
                    .toUri();

            return ResponseEntity.created(location).build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/")
    public ResponseEntity<Object> getAllVocabularies() {
        return ResponseEntity.ok().body(_vocabularyDao.findAll());
    }

    @GetMapping(path = "/{idString}")
    public ResponseEntity<Object> getVocabularyById(@PathVariable String idString) {
        int id;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().build();
        }
        Vocabulary result = _vocabularyDao.findById(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(path="/{idString}/add")
    public ResponseEntity<Object> addEntryToVocabulary(@PathVariable String idString, @RequestBody Entry entry) {
        int id;
        try {
            id = Integer.parseInt(idString);
        } catch(NumberFormatException ex) {
            return ResponseEntity.badRequest().build();
        }
        if(_vocabularyDao.addEntryToVocabulary(id, entry)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

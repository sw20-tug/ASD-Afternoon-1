package at.tugraz.sw20asd.lang.server.controllers;

import at.tugraz.sw20asd.lang.dto.EntryDto;
import at.tugraz.sw20asd.lang.dto.VocabularyBaseDto;
import at.tugraz.sw20asd.lang.dto.VocabularyDetailDto;
import at.tugraz.sw20asd.lang.server.services.IVocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/vocab")
public class VocabularyController {

    private final IVocabularyService _vocabularyService;

    @Autowired
    public VocabularyController(IVocabularyService vocabularyService) {
        this._vocabularyService = vocabularyService;
    }

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addVocabulary(@RequestBody VocabularyDetailDto vocabulary) {
        try {
            Long insertedIndex = _vocabularyService.add(vocabulary);
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
    public List<VocabularyBaseDto> overview() {
        return _vocabularyService.list();
    }

    @GetMapping(path = "/{idString}")
    public ResponseEntity<VocabularyDetailDto> getVocabularyById(@PathVariable String idString) {
        long id;
        try {
            id = Long.parseLong(idString);
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().build();
        }
        VocabularyDetailDto result = _vocabularyService.findById(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(path = "/{idString}")
    public ResponseEntity<?> addEntryToVocabulary(@PathVariable String idString, @RequestBody EntryDto entry) {
        long id;
        try {
            id = Long.parseLong(idString);
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().build();
        }

        if (_vocabularyService.addToVocabulary(id, entry)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

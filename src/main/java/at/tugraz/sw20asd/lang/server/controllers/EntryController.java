package at.tugraz.sw20asd.lang.server.controllers;

import at.tugraz.sw20asd.lang.dto.EntryDto;
import at.tugraz.sw20asd.lang.server.services.IEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entry")
public class EntryController {
    private final IEntryService _entryService;

    @Autowired
    public EntryController(IEntryService entryService) {
        this._entryService = entryService;
    }

    @GetMapping(path = "/{idString}")
    public ResponseEntity<EntryDto> getEntryById(@PathVariable String idString) {
        long id;

        try {
            id = Long.parseLong(idString);
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().build();
        }

        EntryDto result = _entryService.findById(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(path = "/")
    public ResponseEntity<?> editEntry(@RequestBody EntryDto entry) {
        if (!validateDto(entry)) {
            return ResponseEntity.badRequest().build();
        }

        if (!_entryService.updateEntry(entry)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{idString}")
    public ResponseEntity<?> deleteEntry(@PathVariable String idString) {
        Long id = parseFromString(idString);
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        if (_entryService.deleteEntry(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    private boolean validateDto(EntryDto e) {
        return e.getId() != null
                && e.getPhrase() != null
                && e.getTranslation() != null;
    }

    private Long parseFromString(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}

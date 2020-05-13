package at.tugraz.sw20asd.lang.server.controllers;

import at.tugraz.sw20asd.lang.dto.EntryDto;
import at.tugraz.sw20asd.lang.server.services.IEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

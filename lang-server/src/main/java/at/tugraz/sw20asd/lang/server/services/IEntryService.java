package at.tugraz.sw20asd.lang.server.services;

import at.tugraz.sw20asd.lang.EntryDto;

import java.util.List;

public interface IEntryService {

    EntryDto findById(Long id);

    List<EntryDto> findEntriesForVocabulary(Long id);

    boolean updateEntry(EntryDto entry);

    boolean deleteEntry(Long id);
}

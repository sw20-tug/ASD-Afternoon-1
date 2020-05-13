package at.tugraz.sw20asd.lang.server.services;

import at.tugraz.sw20asd.lang.dto.EntryDto;
import at.tugraz.sw20asd.lang.server.domain.Entry;
import at.tugraz.sw20asd.lang.server.domain.Vocabulary;
import at.tugraz.sw20asd.lang.server.repositories.EntryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntryService implements IEntryService {

    private final EntryRepository _entryRepository;

    private final ModelMapper _modelMapper;

    @Autowired
    public EntryService(EntryRepository entryRepository, ModelMapper modelMapper) {
        _entryRepository = entryRepository;
        _modelMapper = modelMapper;
    }

    @Override
    public EntryDto findById(Long id) {
        return null;
    }

    @Override
    public List<EntryDto> findEntriesForVocabulary(Long id) {
        return _entryRepository.findByVocabularyId(id)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateEntry(EntryDto entry) {
        return false;
    }

    private EntryDto convertToDto(Entry e) {
        return _modelMapper.map(e, EntryDto.class);
    }
}

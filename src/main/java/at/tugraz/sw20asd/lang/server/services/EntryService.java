package at.tugraz.sw20asd.lang.server.services;

import at.tugraz.sw20asd.lang.dto.EntryDto;
import at.tugraz.sw20asd.lang.server.domain.Entry;
import at.tugraz.sw20asd.lang.server.domain.Vocabulary;
import at.tugraz.sw20asd.lang.server.repositories.EntryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
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
        Optional<Entry> optVocab = _entryRepository.findById(id);
        return optVocab.map(this::convertToDto).orElse(null);
    }

    @Override
    public List<EntryDto> findEntriesForVocabulary(Long id) {
        return _entryRepository.findByVocabularyId(id)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean updateEntry(EntryDto entry) {
        Optional<Entry> entryOpt = _entryRepository.findById(entry.getId());
        if(entryOpt.isEmpty()) {
            return false;
        }
        Entry entity = entryOpt.get();
        entity.setPhrase(entry.getPhrase());
        entity.setTranslation(entry.getTranslation());

        _entryRepository.save(entity);
        return true;
    }

    private EntryDto convertToDto(Entry e) {
        return _modelMapper.map(e, EntryDto.class);
    }
}

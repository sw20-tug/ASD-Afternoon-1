package at.tugraz.sw20asd.lang.server.services;

import at.tugraz.sw20asd.lang.EntryDto;
import at.tugraz.sw20asd.lang.VocabularyBaseDto;
import at.tugraz.sw20asd.lang.VocabularyDetailDto;
import at.tugraz.sw20asd.lang.server.domain.Entry;
import at.tugraz.sw20asd.lang.server.domain.Vocabulary;
import at.tugraz.sw20asd.lang.server.repositories.VocabularyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VocabularyService implements IVocabularyService {

    private final VocabularyRepository _vocabularyRepository;

    private final ModelMapper _modelMapper;

    @Autowired
    public VocabularyService(VocabularyRepository vocabularyRepository, ModelMapper modelMapper) {
        _vocabularyRepository = vocabularyRepository;
        _modelMapper = modelMapper;
    }

    @Override
    public List<VocabularyBaseDto> list() {
        return _vocabularyRepository.findAll()
                .stream()
                .map(this::convertToOverviewDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long add(VocabularyDetailDto vocabulary) {
        return _vocabularyRepository.save(convertToVocabulary(vocabulary)).getId();
    }

    @Override
    @Transactional
    public VocabularyDetailDto findById(Long id) {
        Optional<Vocabulary> optVocab = _vocabularyRepository.findById(id);
        return optVocab.map(this::convertToDetailDto).orElse(null);
    }

    @Override
    @Transactional
    public boolean addToVocabulary(Long id, EntryDto e) {
        Optional<Vocabulary> optVocab = _vocabularyRepository.findById(id);
        if (optVocab.isEmpty()) {
            return false;
        }
        optVocab.get().addEntry(convertToEntry(e));
        return true;
    }

    @Override
    public boolean deleteVocabulary(Long id) {
        if (_vocabularyRepository.existsById(id)) {
            _vocabularyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private VocabularyBaseDto convertToOverviewDto(Vocabulary v) {
        return _modelMapper.map(v, VocabularyBaseDto.class);
    }

    private VocabularyDetailDto convertToDetailDto(Vocabulary v) {
        return _modelMapper.map(v, VocabularyDetailDto.class);
    }

    private Vocabulary convertToVocabulary(VocabularyDetailDto v) {
        return _modelMapper.map(v, Vocabulary.class);
    }

    private Entry convertToEntry(EntryDto e) {
        return _modelMapper.map(e, Entry.class);
    }
}

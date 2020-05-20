package at.tugraz.sw20asd.lang.server.services;

import at.tugraz.sw20asd.lang.dto.EntryDto;
import at.tugraz.sw20asd.lang.dto.VocabularyBaseDto;
import at.tugraz.sw20asd.lang.dto.VocabularyDetailDto;

import java.util.List;

public interface IVocabularyService {
    List<VocabularyBaseDto> list();

    Long add(VocabularyDetailDto vocabulary);

    VocabularyDetailDto findById(Long id);

    boolean addToVocabulary(Long id, EntryDto e);
}

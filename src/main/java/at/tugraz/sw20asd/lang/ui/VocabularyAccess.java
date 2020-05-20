package at.tugraz.sw20asd.lang.ui;


import at.tugraz.sw20asd.lang.dto.VocabularyBaseDto;
import at.tugraz.sw20asd.lang.dto.VocabularyDetailDto;

import java.util.Collection;
import java.util.List;

public interface VocabularyAccess {

    Integer addVocabulary(VocabularyDetailDto vocabulary);

    VocabularyDetailDto getVocabulary(long id);

    List<VocabularyBaseDto> getAllVocabularies();
}

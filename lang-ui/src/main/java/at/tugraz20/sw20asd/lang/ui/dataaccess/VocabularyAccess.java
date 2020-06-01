package at.tugraz20.sw20asd.lang.ui.dataaccess;


import at.tugraz.sw20asd.lang.VocabularyBaseDto;
import at.tugraz.sw20asd.lang.VocabularyDetailDto;

import java.util.List;

public interface VocabularyAccess {

    Integer addVocabulary(VocabularyDetailDto vocabulary);

    void deleteVocabulary(long id);

    VocabularyDetailDto getVocabulary(long id);

    List<VocabularyBaseDto> getAllVocabularies();
}

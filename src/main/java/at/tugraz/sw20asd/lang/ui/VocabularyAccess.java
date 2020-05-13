package at.tugraz.sw20asd.lang.ui;


import at.tugraz.sw20asd.lang.model.Vocabulary;

import java.util.Collection;
import java.util.List;

public interface VocabularyAccess {

    Integer addVocabulary(Vocabulary vocabulary);

    Vocabulary getVocabulary(long id);

    List<Vocabulary> getAllVocabularies();

}

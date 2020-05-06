package at.tugraz.sw20asd.lang.ui;


import at.tugraz.sw20asd.lang.model.Vocabulary;

import java.util.Collection;

public interface VocabularyAccess {

    Integer addVocabulary(Vocabulary vocabulary);

    Vocabulary getVocabulary(long id);

    Collection<Vocabulary> getAllVocabularies();
}

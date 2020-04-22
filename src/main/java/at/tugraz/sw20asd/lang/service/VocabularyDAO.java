package at.tugraz.sw20asd.lang.service;

import at.tugraz.sw20asd.lang.model.Vocabulary;

import java.util.Collection;

public interface VocabularyDAO {
    Collection<Vocabulary> findAll();

    Vocabulary findById(int id);

    int addVocabulary(Vocabulary vocabulary);
}

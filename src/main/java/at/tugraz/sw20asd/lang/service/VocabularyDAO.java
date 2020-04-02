package at.tugraz.sw20asd.lang.service;

import at.tugraz.sw20asd.lang.model.Vocabulary;

import java.util.List;

public interface VocabularyDAO {
    List<Vocabulary> findAll();

    boolean addVocabulary(Vocabulary vocabulary);
}

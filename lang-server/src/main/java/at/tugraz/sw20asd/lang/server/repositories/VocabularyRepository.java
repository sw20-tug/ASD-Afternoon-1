package at.tugraz.sw20asd.lang.server.repositories;


import at.tugraz.sw20asd.lang.server.domain.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
}

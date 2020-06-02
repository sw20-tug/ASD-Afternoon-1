package at.tugraz.sw20asd.lang.server.repositories;

import at.tugraz.sw20asd.lang.server.domain.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    @Query("select e from Entry e where e.vocabulary.id =:id")
    List<Entry> findByVocabularyId(@Param("id") Long id);
}

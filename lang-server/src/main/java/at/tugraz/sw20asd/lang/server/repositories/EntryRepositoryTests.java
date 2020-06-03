package at.tugraz.sw20asd.lang.server.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql("/import_entries.sql")
public class EntryRepositoryTests {

    @Autowired
    private EntryRepository entryRepository;

    @Test
    public void testFindByVocabularyId_ReturnsAllEntries() {
        assertThat(entryRepository.findByVocabularyId(1L).size()).isEqualTo(3);
    }

    @Test
    public void testFindByVocabularyId_ReturnsNoEntries() {
        assertThat(entryRepository.findByVocabularyId(900L).size()).isEqualTo(0);
    }
}

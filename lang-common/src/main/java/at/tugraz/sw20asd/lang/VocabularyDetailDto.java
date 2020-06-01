package at.tugraz.sw20asd.lang;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class VocabularyDetailDto extends VocabularyBaseDto {
    public VocabularyDetailDto() {
        super();
    }

    public VocabularyDetailDto(Long id, String name, Locale src, Locale dst) {
        super(id, name, src, dst);
    }

    private Set<EntryDto> _entries = new HashSet<>();

    public Set<EntryDto> getEntries() {
        return _entries;
    }

    public void setEntries(Set<EntryDto> entries) {
        _entries = entries;
    }

    public void addEntry(EntryDto e) {
        _entries.add(e);
    }
}

package at.tugraz.sw20asd.lang.service;

import at.tugraz.sw20asd.lang.model.Vocabulary;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class VocabularyDAOFileImpl implements VocabularyDAO {

    private ObjectMapper _mapper;
    private String _vocabFolder;
    private Map<Integer, Vocabulary> _vocabularies;
    private int _nextFreeIndex = 0;

    public VocabularyDAOFileImpl(String workingDirectory) {
        _vocabFolder = workingDirectory;
        _mapper = new ObjectMapper();
        _mapper.enable(SerializationFeature.INDENT_OUTPUT);

        _vocabularies = new HashMap<>();
        File folder = new File(_vocabFolder);

        File[] listOfFiles = folder.listFiles();
        if(listOfFiles == null) {
            new File(_vocabFolder).mkdirs();
            return;
        }

        for (File f : listOfFiles) {
            if (f.isFile()) {
                Vocabulary vocab = readVocabularyFromFile(f);
                if (vocab != null) {
                    _vocabularies.put(vocab.getID(), vocab);
                    if(vocab.getID() >= _nextFreeIndex) {
                        _nextFreeIndex = vocab.getID() + 1;
                    }
                }
            }
        }
    }

    @Override
    public Collection<Vocabulary> findAll() {
        return _vocabularies.values();
    }

    @Override
    public Vocabulary findById(int id) {
        return _vocabularies.get(id);
    }

    @Override
    public int addVocabulary(Vocabulary vocabulary) {
        int vocabularyIndex = _nextFreeIndex;

        vocabulary.setId(vocabularyIndex);
        serializeVocabularyToFile(vocabulary);
        _vocabularies.put(vocabulary.getID(), vocabulary);

        _nextFreeIndex++;
        return vocabularyIndex;
    }

    private Vocabulary readVocabularyFromFile(File file) {
        Vocabulary vocab = null;
        try {
            vocab = _mapper.readValue(Files.readString(file.toPath()), Vocabulary.class);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
        return vocab;
    }

    private void serializeVocabularyToFile(Vocabulary vocab) {
        try {
            Integer vocabId = vocab.getID();
            if(vocabId == null) {
                throw new Exception("Vocabulary has uninitialized ID");
            }
            String filename = vocabId + ".vocab";

            File file = Paths.get(_vocabFolder, filename).toFile();

            _mapper.writeValue(file, vocab);
        } catch(Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}

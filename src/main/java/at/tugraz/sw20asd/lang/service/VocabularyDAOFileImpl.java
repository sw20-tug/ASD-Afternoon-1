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
import java.util.ArrayList;
import java.util.List;

@Repository
public class VocabularyDAOFileImpl implements VocabularyDAO {

    private ObjectMapper _mapper;
    private String _vocabFolder;
    private List<Vocabulary> _vocabularies;

    public VocabularyDAOFileImpl(String workingDirectory) {
        _vocabFolder = workingDirectory;
        _mapper = new ObjectMapper();
        _mapper.enable(SerializationFeature.INDENT_OUTPUT);

        _vocabularies = new ArrayList<>();
        File folder = new File(_vocabFolder);

        File[] listOfFiles = folder.listFiles();
        if(listOfFiles == null) {
            new File(_vocabFolder).mkdirs();
            return;
        }

        for(int i = 0; i < listOfFiles.length; ++i) {
            File file = listOfFiles[i];
            if(file.isFile()) {
                Vocabulary vocab = readVocabularyFromFile(file);
                if(vocab != null) {
                    _vocabularies.add(vocab);
                }
            }
        }
    }

    @Override
    public List<Vocabulary> findAll() {
        return _vocabularies;
    }

    @Override
    public boolean addVocabulary(Vocabulary vocabulary) {
        serializeVocabularyToFile(vocabulary);
        return true;
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

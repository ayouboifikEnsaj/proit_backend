package ma.ismail.spring_sec_jwt.services;

import ma.ismail.spring_sec_jwt.dto.DtoProbleme;
import ma.ismail.spring_sec_jwt.entites.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProblemService {
    Probleme addNewProbleme(String libelle);
    List<Probleme> getAllProblemes();

    boolean deleteProbleme(Long problemeId);
    Probleme updateProbleme(DtoProbleme dtoProbleme);
    Optional<Probleme> getProblemeById(Long problemeId);
    public void importProblemData(MultipartFile file) throws IOException;


    }

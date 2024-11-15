package ma.ismail.spring_sec_jwt.services;

import ma.ismail.spring_sec_jwt.dto.DtoSolution;
import ma.ismail.spring_sec_jwt.entites.Solution;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SolutionService {
    Solution addSolution(String libelle,Long problemeId);
    List<Solution> getAllSolutions();

    Solution updateSolution(DtoSolution dtoSolution);
    boolean deleteSolution(Long solutionId);
    public void importSolutionData(MultipartFile file) throws IOException;
}

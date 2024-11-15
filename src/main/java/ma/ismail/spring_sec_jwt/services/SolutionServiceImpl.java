package ma.ismail.spring_sec_jwt.services;

import jakarta.transaction.Transactional;
import ma.ismail.spring_sec_jwt.Exceptions.ResourceNotFoundException;
import ma.ismail.spring_sec_jwt.dto.DtoSolution;
import ma.ismail.spring_sec_jwt.entites.Comptoire;
import ma.ismail.spring_sec_jwt.entites.Probleme;
import ma.ismail.spring_sec_jwt.entites.Solution;
import ma.ismail.spring_sec_jwt.entites.Zone;
import ma.ismail.spring_sec_jwt.repository.ProblemeRepository;
import ma.ismail.spring_sec_jwt.repository.SolutionRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SolutionServiceImpl implements SolutionService {

    @Autowired
     SolutionRepository solutionRepository;

    @Autowired
     ProblemeRepository problemeRepository;

    @Override
    public Solution addSolution(String libelle,Long problemeId) {
        if (solutionRepository.findSolutionByLibelle(libelle) != null) {
            return null;
        }

//        Probleme probleme = problemeRepository.findProblemeById(problemeId);
//        if (probleme == null) {
//            System.out.println("Not Found");
//            return null;
//
//        }

        Solution solution = new Solution(null,libelle,new ArrayList<>());
        return solutionRepository.save(solution);
    }


    @Override
    public List<Solution> getAllSolutions() {
        return solutionRepository.findAll();
    }


    @Override
    public Solution updateSolution(DtoSolution dtoSolution) {
      Solution solution = solutionRepository.findSolutionById(dtoSolution.getId());
      solution.setLibelle(dtoSolution.getLibelle());
//      Probleme probleme = problemeRepository.findProblemeById(dtoSolution.getProblemId());
//      solution.setProbleme(probleme);
      return solutionRepository.save(solution);
    }

    @Override
    public boolean deleteSolution(Long solutionId) {
        if(solutionRepository.existsById(solutionId)){
            solutionRepository.deleteById(solutionId);
            return true;
        }
        else {
            System.out.println("Solution with ID '" + solutionId + "' not found.");
            return false;
        }

    }

    @Override
    public void importSolutionData(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(7);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header row
            }
            Solution solution =new Solution();
            try {
                row.getCell(1).getStringCellValue().trim();
            }catch (Exception e){
                workbook.close();
                return;
            }
            // Ignoring ID column, assuming it is auto-generated
            System.out.println("value : "+row.getCell(1).getStringCellValue().trim());
            solution.setLibelle(row.getCell(1).getStringCellValue().trim());
            solutionRepository.save(solution);
        }
        workbook.close();
    }
}

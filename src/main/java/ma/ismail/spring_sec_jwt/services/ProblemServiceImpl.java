package ma.ismail.spring_sec_jwt.services;


import jakarta.transaction.Transactional;
import ma.ismail.spring_sec_jwt.Exceptions.ResourceNotFoundException;
import ma.ismail.spring_sec_jwt.dto.DtoProbleme;
import ma.ismail.spring_sec_jwt.entites.*;
import ma.ismail.spring_sec_jwt.repository.EquipmentRepository;
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
public class ProblemServiceImpl implements ProblemService {
    @Autowired private ProblemeRepository problemeRepository;
    @Autowired private EquipmentRepository equipmentRepository;
    @Autowired private SolutionRepository solutionRepository;
    @Override
    public Probleme addNewProbleme(String libelle){
        Probleme probleme=new Probleme(null,libelle,new ArrayList<>());
        return problemeRepository.save(probleme);
    }

    @Override
    public List<Probleme> getAllProblemes() {
        return problemeRepository.findAll();
    }

    @Override
    public boolean deleteProbleme(Long problemId) {
        Probleme probleme=problemeRepository.findProblemeById(problemId);
        if(probleme!=null){
            System.out.println("Successfully deleted");
            problemeRepository.deleteById(problemId);
            return true;
        }

        else {
            System.out.println("Is not deleted");
            return false;
        }
    }

    @Override
    public Probleme updateProbleme(DtoProbleme dtoProbleme) {
        Probleme probleme = problemeRepository.findProblemeById(dtoProbleme.getId());
        probleme.setLibelle(dtoProbleme.getLibelle());
        return problemeRepository.save(probleme);
    }
    @Override
    public Optional<Probleme> getProblemeById(Long problemeId) {
        return Optional.of(problemeRepository.findById(problemeId).get());
    }

    @Override
    public void importProblemData(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(6);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header row
            }
            Probleme probleme =new Probleme();
            try {
                row.getCell(1).getStringCellValue().trim();
            }catch (Exception e){
                workbook.close();
                return;
            }
            // Ignoring ID column, assuming it is auto-generated
            System.out.println("value : "+row.getCell(1).getStringCellValue().trim());
            probleme.setLibelle(row.getCell(1).getStringCellValue().trim());
            problemeRepository.save(probleme);
        }
        workbook.close();
    }

}

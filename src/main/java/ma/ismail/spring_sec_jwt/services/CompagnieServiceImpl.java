package ma.ismail.spring_sec_jwt.services;

import jakarta.transaction.Transactional;
import ma.ismail.spring_sec_jwt.dto.DtoCompagnie;
import ma.ismail.spring_sec_jwt.dto.DtoEquipment;
import ma.ismail.spring_sec_jwt.entites.Aeroport;
import ma.ismail.spring_sec_jwt.entites.Compagnie;
import ma.ismail.spring_sec_jwt.entites.Comptoire;
import ma.ismail.spring_sec_jwt.entites.Equipment;
import ma.ismail.spring_sec_jwt.repository.AeroportRespository;
import ma.ismail.spring_sec_jwt.repository.CompagnieRepository;
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
public class CompagnieServiceImpl implements CompagnieService {
    @Autowired
    AeroportRespository aeroportRespository;
    @Autowired
    CompagnieRepository compagnieRepository;
    @Override
    public Compagnie saveCompagnie(String companieName) {
        Compagnie compagnie=new Compagnie(null,companieName);
        return compagnieRepository.save(compagnie);
    }

    @Override
    public Aeroport addCompagnieToAeroport(String companieName, String aeroportName) {
        Aeroport aeroport=aeroportRespository.findAeroportByAeroportName(aeroportName);
        if (aeroport!=null){
            List<Compagnie>compagnies=aeroport.getCompagnies();

            Compagnie compagnie=compagnieRepository.findCompagnieByCompagnieName(companieName);
            if(compagnies.contains(compagnie)){
                return null;
            }
            else {
                compagnies.add(compagnie);
                return aeroportRespository.save(aeroport);
            }
        }
        else {
            System.out.println("Compagnie Not Found");
            return null;

        }
    }

    @Override
    public Aeroport removeCompagnieToAeroport(String companieName, String aeroportName) {
        Aeroport aeroport=aeroportRespository.findAeroportByAeroportName(aeroportName);
        if (aeroport!=null){
            List<Compagnie>compagnies=aeroport.getCompagnies();

            Compagnie compagnie=compagnieRepository.findCompagnieByCompagnieName(companieName);
            if(compagnies.contains(compagnie)){
                compagnies.remove(compagnie);
                return aeroportRespository.save(aeroport);
            }
            else {
                return null;
            }
        }
        else {
            System.out.println("Compagnie Not Found");
            return null;

        }
    }

    @Override
    public Compagnie getCompagnieById(Long id) {

        if (compagnieRepository.existsById(id)){
            return compagnieRepository.findCompagnieById(id);
        }
        System.out.println("Compagnie Not Found");
        return null;
    }

    @Override
    public List<Compagnie> getAllCompagnies() {

        return compagnieRepository.findAll();
    }
    @Override
    public Compagnie updateCompagnie(DtoCompagnie dtoCompagnie) {
        Compagnie compagnie = compagnieRepository.findCompagnieById(dtoCompagnie.getId());

        if (compagnie == null) {
            System.out.println("compagnie not found");
        return null;
        }
        compagnie.setCompagnieName(dtoCompagnie.getCompagnieName());
        return compagnieRepository.save(compagnie);
    }


    @Override
    public boolean deleteCompagnie(Long id) {
        if(compagnieRepository.existsById(id)){
            compagnieRepository.deleteById(id);
            return true;
        }
        else {
            System.out.println("Compagnie not found");
            return false;
        }
    }

    @Override
    public String getCompagnieNameById(Long key) {
        return compagnieRepository.getCompagnieNameById(key);
    }

    @Override
    public void importCompagnieData(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(2);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header row
            }
            try {
                row.getCell(1).getStringCellValue().trim();
            }catch (Exception e){
                workbook.close();
                return;
            }
            Compagnie compagnie = new Compagnie();
            // Ignoring ID column, assuming it is auto-generated
            compagnie.setCompagnieName(row.getCell(1).getStringCellValue().trim());

            compagnieRepository.save(compagnie);
        }
        workbook.close();
    }


}

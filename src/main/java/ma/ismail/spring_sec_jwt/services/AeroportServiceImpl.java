package ma.ismail.spring_sec_jwt.services;

import jakarta.transaction.Transactional;
import ma.ismail.spring_sec_jwt.dto.AeroportDTO;
import ma.ismail.spring_sec_jwt.dto.DtoAeroport;
import ma.ismail.spring_sec_jwt.dto.DtoCompagnie;
import ma.ismail.spring_sec_jwt.entites.Aeroport;
import ma.ismail.spring_sec_jwt.entites.Compagnie;
import ma.ismail.spring_sec_jwt.repository.AeroportRespository;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class AeroportServiceImpl implements AeroportService {
    @Autowired
    AeroportRespository aeroportRespository;
    @Override
    public Aeroport saveAeroport(String aeroportName) {

       Aeroport aeroport=new Aeroport(null,aeroportName,new ArrayList<>(),new ArrayList<>());
        //Aeroport aeroport=new Aeroport(null,aeroportName,new ArrayList<>(),new ArrayList<>());
        return aeroportRespository.save(aeroport);
    }

    @Override
    public Aeroport getAeroportByName(String name) {

        Aeroport aeroport =aeroportRespository.findByAeroportName(name);
        if(aeroport!=null){
            return aeroport;
        }
        else {
            return null;
        }


    }

    @Override
    public List<Aeroport> getAllAeroports() {

        return aeroportRespository.findAll();
    }

    @Override
    public Aeroport updateAeroport(DtoAeroport dtoAeroport) {
            Aeroport aeroport = aeroportRespository.findAeroportById(dtoAeroport.getId());

            aeroport.setAeroportName(dtoAeroport.getAeroportName());
            return aeroportRespository.save(aeroport);



    }

    @Override
    public boolean deleteAeroport(Long id) {
        if(aeroportRespository.existsById(id)){
            aeroportRespository.deleteById(id);
            return true;
        }
        else{
            System.out.println("NOT found");
            return false;
        }


    }
    @Override
    public List<AeroportDTO> getAllAeroportsWithCompagnies() {
        List<Aeroport> aeroports = aeroportRespository.findAll();
        return aeroports.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Aeroport getAeroport(String username) {
        return aeroportRespository.findAeroportByUsers_Username(username);
    }

    @Override
    public void importAeroportData(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

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
            Aeroport aeroport = new Aeroport();
            aeroport.setAeroportName(row.getCell(1).getStringCellValue().trim());
            System.out.println("aeroport : "+aeroport);
            aeroportRespository.save(aeroport);
            System.out.println("aeroport Save : "+aeroport);
        }
        workbook.close();
    }
    private AeroportDTO convertToDTO(Aeroport aeroport) {
        AeroportDTO dto = new AeroportDTO();
        dto.setId(aeroport.getId());
        dto.setAeroportName(aeroport.getAeroportName());
        dto.setCompagnieNames(aeroport.getCompagnies().stream()
                .map(Compagnie::getCompagnieName)
                .collect(Collectors.toList()));
        return dto;
    }
}

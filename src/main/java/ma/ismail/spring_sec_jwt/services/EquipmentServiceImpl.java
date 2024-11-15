package ma.ismail.spring_sec_jwt.services;

import jakarta.transaction.Transactional;
import ma.ismail.spring_sec_jwt.dto.DtoEquipment;
import ma.ismail.spring_sec_jwt.dto.EquipmentDTO;
import ma.ismail.spring_sec_jwt.entites.*;
import ma.ismail.spring_sec_jwt.repository.ComptoireRepository;
import ma.ismail.spring_sec_jwt.repository.EquipmentRepository;
import ma.ismail.spring_sec_jwt.repository.ProjetRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.List;
@Service
@Transactional
public class EquipmentServiceImpl implements EquipmentService {

    private EquipmentRepository equipmentRepository;

    private ComptoireRepository comptoireRepository;
    private ProjetService projetService;
    private ProjetRepository projetRepository;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository, ComptoireRepository comptoireRepository, ProjetService projetService, ProjetRepository projetRepository) {
        this.equipmentRepository = equipmentRepository;
        this.comptoireRepository = comptoireRepository;
        this.projetService = projetService;
        this.projetRepository = projetRepository;
    }

    @Override
    public Equipment saveEquipment(String equipementName, Long comptoireId) {
        if (equipmentRepository.findEquipmentByEquipementName(equipementName) != null) {
            System.out.println("Equipment with name '" + equipementName + "' already exists.");
            return null;
        }

        Comptoire comptoire = comptoireRepository.findComptoireById(comptoireId);
        if (comptoire == null) {
            System.out.println("Comptoire with ID '" + comptoireId + "' not found.");
            return null;
        }

        Equipment equipment = new Equipment();
        equipment.setEquipementName(equipementName);
        return equipmentRepository.save(equipment);
    }

    @Override
    public Equipment getEquipmentById(Long id) {
        if(equipmentRepository.existsById(id)){
            return equipmentRepository.findEquipmentById(id);
        }
        else{
            System.out.println("Equipment with ID '" + id + "' not found.");
            return null;
        }
    }
//
//    @Override
//    public List<Equipment> getAllEquipments() {
//        return equipmentRepository.findAll();
//    }

    @Override
    public Equipment updateEquipment(DtoEquipment dtoEquipment) {
        Equipment equipment=equipmentRepository.findEquipmentById(dtoEquipment.getId());
        equipment.setEquipementName(dtoEquipment.getEquipmentName());
        Comptoire comptoire=comptoireRepository.findComptoireById(dtoEquipment.getIdComptoire());
        
        return equipmentRepository.save(equipment);
    }

    @Override
    public boolean deleteEquipment(Long id) {
        if(equipmentRepository.existsById(id)){
            equipmentRepository.deleteById(id);
            return true;
        }
        else{
            System.out.println("Equipment with ID '" + id + "' not found.");
            return false;
        }

    }
    @Override
    public List<EquipmentDTO> getAllEquipments() {
        List<Equipment> equipments = equipmentRepository.findAll();
        return equipments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    @Override
    public EquipmentDTO addOrUpdateEquipment(EquipmentDTO equipmentDTO) {
        Equipment equipment = new Equipment();
        equipment.setId(equipmentDTO.getId());
        equipment.setEquipementName(equipmentDTO.getEquipmentName());
        // Find the Comptoire entity from the repository and set it

        Equipment savedEquipment = equipmentRepository.save(equipment);
        return convertToDTO(savedEquipment);
    }

    @Override
    public void importEquipmentData(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(4);


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

            String equipmentName = row.getCell(1).getStringCellValue().trim();
            System.out.println("Equipment Name : "+equipmentName);
            String projetName=row.getCell(2).getStringCellValue().trim();
            System.out.println("Projet Name : "+projetName);
            Equipment equipment=new Equipment();
            equipment.setEquipementName(equipmentName);
            Equipment equipment1= equipmentRepository.save(equipment);
            Projet projet=projetRepository.findProjetByProjetName(projetName);
            List<Equipment> equipmentList=projet.getEquipments();
            System.out.println(equipment1.getId()+equipment1.getEquipementName());
            equipmentList.add(equipment1);
            projet.setEquipments(equipmentList);
            projetRepository.save(projet);
        }

        workbook.close();
    }


    private EquipmentDTO convertToDTO(Equipment equipment) {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(equipment.getId());
        dto.setEquipmentName(equipment.getEquipementName());
        return dto;
    }
}

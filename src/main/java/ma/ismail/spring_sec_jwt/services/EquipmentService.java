package ma.ismail.spring_sec_jwt.services;

import ma.ismail.spring_sec_jwt.dto.DtoEquipment;
import ma.ismail.spring_sec_jwt.dto.EquipmentDTO;
import ma.ismail.spring_sec_jwt.entites.Equipment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EquipmentService {
    Equipment saveEquipment(String equipementName, Long comptoireId);
    Equipment getEquipmentById(Long id);
    List<EquipmentDTO> getAllEquipments();
    Equipment updateEquipment(DtoEquipment equipment);
    boolean deleteEquipment(Long id);
    EquipmentDTO addOrUpdateEquipment(EquipmentDTO equipmentDTO);
    public void importEquipmentData(MultipartFile file) throws IOException;
}

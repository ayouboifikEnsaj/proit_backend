package ma.ismail.spring_sec_jwt.services;

import jakarta.transaction.Transactional;
import ma.ismail.spring_sec_jwt.dto.EquipmentDTO;
import ma.ismail.spring_sec_jwt.entites.Equipment;
import ma.ismail.spring_sec_jwt.entites.Projet;
import ma.ismail.spring_sec_jwt.repository.EquipmentRepository;
import ma.ismail.spring_sec_jwt.repository.ProjetRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjetServiceImpl implements ProjetService {
    private ProjetRepository projetRepository;
    private EquipmentRepository equipmentRepository;
    public ProjetServiceImpl(ProjetRepository projetRepository, EquipmentRepository equipmentRepository) {
        this.projetRepository = projetRepository;
        this.equipmentRepository = equipmentRepository;
    }


    @Override
    public Projet addOrUpdateProjet(Projet projet) {
        Projet projet1 = new Projet();
        if(projet.getId()!=null){
            return projetRepository.save(projet);
        }
        else {
            projet1.setId(projet.getId());
            projet1.setProjetName(projet.getProjetName());
            return projetRepository.save(projet1);
        }
    }

    @Override
    public boolean deleteProjet(Long id) {
        if(projetRepository.existsById(id)){
            projetRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Projet addEquipmentToProjet(String projetName, Long eqipmentId) {
        Equipment equipment=equipmentRepository.findEquipmentById(eqipmentId);
        Projet projet=projetRepository.findProjetByProjetName(projetName);
        if(equipment!=null && projet!=null){
            List<Equipment> equipmentList=projet.getEquipments();
            equipmentList.add(equipment);
            return projetRepository.save(projet);
        }
        else {
            return null;
        }
    }

    @Override
    public Projet removeEquipmentFromProjet(String projetName, Long eqipmentId) {
        Equipment equipment=equipmentRepository.findEquipmentById(eqipmentId);
        Projet projet=projetRepository.findProjetByProjetName(projetName);
        if(equipment!=null && projet!=null){
            System.out.println("Start");
            List<Equipment> equipmentList=projet.getEquipments();
            System.out.println("EQUIPMENT"+equipmentList.size());
            equipmentList.remove(equipment);
            return projetRepository.save(projet);
        }
        else {
            return null;
        }
    }

    @Override
    public List<Projet> getALl() {
        return projetRepository.findAll();
    }

    @Override
    public List<EquipmentDTO> getAllEquipmentsByProjet(Long id) {
        Projet projet=projetRepository.findProjetById(id);
        List<Equipment> equipmentList= projet.getEquipments();
        List<EquipmentDTO> equipmentDTOS=new ArrayList<>();
        for (Equipment e:equipmentList){
            EquipmentDTO equipmentDTO=new EquipmentDTO();
            equipmentDTO.setId(e.getId());
            equipmentDTO.setEquipmentName(e.getEquipementName());
            equipmentDTOS.add(equipmentDTO);
        }
        return equipmentDTOS;
    }


}

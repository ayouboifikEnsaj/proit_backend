package ma.ismail.spring_sec_jwt.repository;

import ma.ismail.spring_sec_jwt.entites.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment,Long> {
    Equipment findEquipmentById(Long id);
    Equipment findEquipmentByEquipementName(String name);
}

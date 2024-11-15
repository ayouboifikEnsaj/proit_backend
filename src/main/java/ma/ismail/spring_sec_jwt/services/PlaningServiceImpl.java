package ma.ismail.spring_sec_jwt.services;

import jakarta.transaction.Transactional;
import ma.ismail.spring_sec_jwt.Exceptions.ResourceNotFoundException;
import ma.ismail.spring_sec_jwt.entites.Planing;
import ma.ismail.spring_sec_jwt.repository.PlaningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlaningServiceImpl implements PlaningService{
    @Autowired
     private PlaningRepository planingRepository;

    @Override
    public Planing addNewPlaning(Date date, Date HeureDebut, Date HeureFin) {
       Planing planing = new Planing(null, date , HeureDebut, HeureFin);
        return planingRepository.save(planing);

    }

    @Override
    public List<Planing> getAllPlaning() {
        return planingRepository.findAll();
    }

    @Override
    public void deletePlaning(Long planingId) {
        Optional<Planing> thePlaning = planingRepository.findById(planingId);

        if(thePlaning.isPresent()){
            planingRepository.deleteById(planingId);
        }
    }

    @Override
    public Planing updatePlaning(Long planingId, Date date, Date HeureDebut, Date HeureFin) {
        Optional<Planing> existingPlaningOptional = planingRepository.findById(planingId);
        if (existingPlaningOptional.isPresent()) {
            Planing existingPlaning = existingPlaningOptional.get();
            existingPlaning.setDate(date);
            existingPlaning.setHeureDebut(HeureDebut);
            existingPlaning.setHeureFin(HeureFin);
            return planingRepository.save(existingPlaning);
        }
        else{
            throw new ResourceNotFoundException("Sorry, we can't update");
        }
    }
}

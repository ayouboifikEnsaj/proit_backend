package ma.ismail.spring_sec_jwt.services;

import ma.ismail.spring_sec_jwt.entites.Planing;

import java.util.Date;
import java.util.List;

public interface PlaningService{
    Planing addNewPlaning(Date date, Date HeureDebut, Date HeureFin);
    List<Planing> getAllPlaning();
    void deletePlaning(Long planingId);
    Planing updatePlaning(Long planingId, Date date, Date HeureDebut, Date HeureFin);

}

package fr.m2i.hotelback.service;

import fr.m2i.hotelback.entities.ReservationEntity;
import fr.m2i.hotelback.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.io.InvalidObjectException;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReservationService {
    ReservationRepository rr;

    public ReservationService(ReservationRepository rr) { this.rr = rr;}

    public Iterable<ReservationEntity> findAll() { return rr.findAll();}

    public ReservationEntity findClient(int id) { return rr.findById(id).get();}

    public static boolean validate(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private void checkResa(ReservationEntity r ) throws InvalidObjectException {

        if( r.getDateDepart().after(r.getDateFin()) == true){
            throw new InvalidObjectException("Date de réservation invalide");
        }

    }

    public void addResa( ReservationEntity r ) throws InvalidObjectException {
        checkResa(r);
        rr.save(r);
    }

    public void delete(int id) {
        rr.deleteById(id);
    }

    public void editResa( int id , ReservationEntity r) throws InvalidObjectException , NoSuchElementException {
        checkResa(r);
        try{
            ReservationEntity rExistante = rr.findById(id).get();

            rExistante.setClient( r.getClient() );
            rExistante.setHotel( r.getHotel() );
            rExistante.setNumChambre( r.getNumChambre() );
            rExistante.setDateDepart( r.getDateDepart() );
            rExistante.setDateFin( r.getDateFin() );
            rr.save( rExistante );

        }catch ( NoSuchElementException e ){
            throw e;
        }
    }
}

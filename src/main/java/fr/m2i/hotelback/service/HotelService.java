package fr.m2i.hotelback.service;

import fr.m2i.hotelback.entities.HotelEntity;
import fr.m2i.hotelback.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.io.InvalidObjectException;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HotelService {
    private HotelRepository hr;

    public HotelService(HotelRepository hr) { this.hr = hr;}

    public Iterable<HotelEntity> findAll() { return hr.findAll();}

    public HotelEntity findHotel(int id) { return hr.findById(id).get();}

    public static boolean validate(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private void checkHotel( HotelEntity h ) throws InvalidObjectException {

        if( h.getNom().length() <= 2  ){
            throw new InvalidObjectException("Nom de l'hotel invalide");
        }

        if( h.getEmail().length() <= 5 || !validate( h.getEmail() ) ){
            throw new InvalidObjectException("Email de l'hotel invalide");
        }

    }

    public void addHotel( HotelEntity h ) throws InvalidObjectException {
        checkHotel(h);
        hr.save(h);
    }

    public void delete(int id) {
        hr.deleteById(id);
    }

    public void editHotel( int id , HotelEntity h) throws InvalidObjectException , NoSuchElementException {
        checkHotel(h);
        try{
            HotelEntity hExistant =  hr.findById(id).get();

            hExistant.setNom( h.getNom() );
            hExistant.setEmail( h.getEmail() );
            hExistant.setAdresse( h.getAdresse() );
            hExistant.setVille( h.getVille() );
            hExistant.setTelephone( h.getTelephone() );
            hExistant.setEtoiles( h.getEtoiles() );
            hExistant.setImages( h.getImages() );
            hr.save( hExistant );

        }catch ( NoSuchElementException e ){
            throw e;
        }
    }
}

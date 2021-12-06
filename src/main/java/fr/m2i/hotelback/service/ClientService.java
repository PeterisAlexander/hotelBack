package fr.m2i.hotelback.service;

import fr.m2i.hotelback.entities.ClientEntity;
import fr.m2i.hotelback.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.io.InvalidObjectException;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ClientService {

    private ClientRepository cr;

    public ClientService(ClientRepository cr) { this.cr = cr;}

    public Iterable<ClientEntity> findAll() { return cr.findAll();}

    public ClientEntity findClient(int id) { return cr.findById(id).get();}

    public static boolean validate(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private void checkClient( ClientEntity c ) throws InvalidObjectException {

        if( c.getNomComplet().length() <= 2  ){
            throw new InvalidObjectException("Nom de client invalide");
        }

        if( c.getEmail().length() <= 5 || !validate( c.getEmail() ) ){
            throw new InvalidObjectException("Email du client invalide");
        }

    }

    public void addClient( ClientEntity c ) throws InvalidObjectException {
        checkClient(c);
        cr.save(c);
    }

    public void delete(int id) {
        cr.deleteById(id);
    }

    public void editClient( int id , ClientEntity c) throws InvalidObjectException , NoSuchElementException {
        checkClient(c);
        try{
            ClientEntity cExistante = (ClientEntity) cr.findById(id).get();

            cExistante.setNomComplet( c.getNomComplet() );
            cExistante.setEmail( c.getEmail() );
            cExistante.setAdresse( c.getAdresse() );
            cExistante.setTelephone( c.getTelephone() );
            cr.save( cExistante );

        }catch ( NoSuchElementException e ){
            throw e;
        }
    }
}

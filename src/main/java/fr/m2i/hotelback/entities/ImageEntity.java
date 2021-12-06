package fr.m2i.hotelback.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "image", schema = "hotel", catalog = "")
public class ImageEntity {
    private int id;
    private String nomFichier;
    private String path;
    private HotelEntity hotel;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nom_fichier")
    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    @Basic
    @Column(name = "path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageEntity that = (ImageEntity) o;
        return id == that.id && Objects.equals(nomFichier, that.nomFichier) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFichier, path);
    }

    @OneToOne
    @JoinColumn(name = "hotel", referencedColumnName = "id", nullable = false)
    public HotelEntity getHotel() {
        return hotel;
    }

    public void setHotel(HotelEntity hotel) {
        this.hotel = hotel;
    }
}

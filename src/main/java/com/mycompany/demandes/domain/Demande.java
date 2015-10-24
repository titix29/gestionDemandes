package com.mycompany.demandes.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mycompany.demandes.domain.enumeration.StatutDemande;

/**
 * A Demande.
 */
@Entity
@Table(name = "demande")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "demande")
public class Demande implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "titre")
    private String titre;

    @Column(name = "description")
    private String description;

    @Column(name = "date_de_besoin")
    private ZonedDateTime dateDeBesoin;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutDemande statut;

    @ManyToOne
    private User createur;

    @ManyToOne
    private User affectation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getDateDeBesoin() {
        return dateDeBesoin;
    }

    public void setDateDeBesoin(ZonedDateTime dateDeBesoin) {
        this.dateDeBesoin = dateDeBesoin;
    }

    public StatutDemande getStatut() {
        return statut;
    }

    public void setStatut(StatutDemande statut) {
        this.statut = statut;
    }

    public User getCreateur() {
        return createur;
    }

    public void setCreateur(User user) {
        this.createur = user;
    }

    public User getAffectation() {
        return affectation;
    }

    public void setAffectation(User user) {
        this.affectation = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Demande demande = (Demande) o;

        if ( ! Objects.equals(id, demande.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Demande{" +
            "id=" + id +
            ", titre='" + titre + "'" +
            ", description='" + description + "'" +
            ", dateDeBesoin='" + dateDeBesoin + "'" +
            ", statut='" + statut + "'" +
            '}';
    }
}

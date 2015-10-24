package com.mycompany.demandes.repository;

import com.mycompany.demandes.domain.Demande;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Demande entity.
 */
public interface DemandeRepository extends JpaRepository<Demande,Long> {

    @Query("select demande from Demande demande where demande.createur.login = ?#{principal.username}")
    List<Demande> findByCreateurIsCurrentUser();

    @Query("select demande from Demande demande where demande.affectation.login = ?#{principal.username}")
    List<Demande> findByAffectationIsCurrentUser();

}

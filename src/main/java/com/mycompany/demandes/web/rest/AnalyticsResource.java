package com.mycompany.demandes.web.rest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.demandes.domain.Demande;
import com.mycompany.demandes.domain.enumeration.StatutDemande;
import com.mycompany.demandes.repository.DemandeRepository;

/**
 * REST controller for managing stats.
 */
@RestController
@RequestMapping("/api")
public class AnalyticsResource {

	private final Logger log = LoggerFactory.getLogger(AnalyticsResource.class);

    @Inject
    private DemandeRepository demandeRepository;
    
    /**
     * GET  /demandes/byState -> get the demandes count grouped by state.
     */
    @RequestMapping(value = "/analytics/demandesByState",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Map<StatutDemande, Long> getDemandesByState() {
    	List<Demande> allDemandes = demandeRepository.findAll();
    	// trier les demandes par statut puis compter
    	return allDemandes.stream().collect(Collectors.groupingBy(Demande::getStatut, Collectors.counting()));
    }
}

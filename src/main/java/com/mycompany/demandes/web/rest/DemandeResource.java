package com.mycompany.demandes.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.demandes.domain.Demande;
import com.mycompany.demandes.repository.DemandeRepository;
import com.mycompany.demandes.repository.search.DemandeSearchRepository;
import com.mycompany.demandes.web.rest.util.HeaderUtil;
import com.mycompany.demandes.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Demande.
 */
@RestController
@RequestMapping("/api")
public class DemandeResource {

    private final Logger log = LoggerFactory.getLogger(DemandeResource.class);

    @Inject
    private DemandeRepository demandeRepository;

    @Inject
    private DemandeSearchRepository demandeSearchRepository;

    /**
     * POST  /demandes -> Create a new demande.
     */
    @RequestMapping(value = "/demandes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Demande> createDemande(@RequestBody Demande demande) throws URISyntaxException {
        log.debug("REST request to save Demande : {}", demande);
        if (demande.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new demande cannot already have an ID").body(null);
        }
        Demande result = demandeRepository.save(demande);
        demandeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/demandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("demande", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /demandes -> Updates an existing demande.
     */
    @RequestMapping(value = "/demandes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Demande> updateDemande(@RequestBody Demande demande) throws URISyntaxException {
        log.debug("REST request to update Demande : {}", demande);
        if (demande.getId() == null) {
            return createDemande(demande);
        }
        Demande result = demandeRepository.save(demande);
        demandeSearchRepository.save(demande);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("demande", demande.getId().toString()))
            .body(result);
    }

    /**
     * GET  /demandes -> get all the demandes.
     */
    @RequestMapping(value = "/demandes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Demande>> getAllDemandes(Pageable pageable)
        throws URISyntaxException {
        Page<Demande> page = demandeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/demandes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /demandes/:id -> get the "id" demande.
     */
    @RequestMapping(value = "/demandes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Demande> getDemande(@PathVariable Long id) {
        log.debug("REST request to get Demande : {}", id);
        return Optional.ofNullable(demandeRepository.findOne(id))
            .map(demande -> new ResponseEntity<>(
                demande,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /demandes/:id -> delete the "id" demande.
     */
    @RequestMapping(value = "/demandes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDemande(@PathVariable Long id) {
        log.debug("REST request to delete Demande : {}", id);
        demandeRepository.delete(id);
        demandeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("demande", id.toString())).build();
    }

    /**
     * SEARCH  /_search/demandes/:query -> search for the demande corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/demandes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Demande> searchDemandes(@PathVariable String query) {
        return StreamSupport
            .stream(demandeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

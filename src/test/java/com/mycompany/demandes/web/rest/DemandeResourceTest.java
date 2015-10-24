package com.mycompany.demandes.web.rest;

import com.mycompany.demandes.Application;
import com.mycompany.demandes.domain.Demande;
import com.mycompany.demandes.repository.DemandeRepository;
import com.mycompany.demandes.repository.search.DemandeSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.demandes.domain.enumeration.StatutDemande;

/**
 * Test class for the DemandeResource REST controller.
 *
 * @see DemandeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DemandeResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TITRE = "AAAAA";
    private static final String UPDATED_TITRE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final ZonedDateTime DEFAULT_DATE_DE_BESOIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_DE_BESOIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_DE_BESOIN_STR = dateTimeFormatter.format(DEFAULT_DATE_DE_BESOIN);


private static final StatutDemande DEFAULT_STATUT = StatutDemande.EN_CREATION;
    private static final StatutDemande UPDATED_STATUT = StatutDemande.SOUMISE;

    @Inject
    private DemandeRepository demandeRepository;

    @Inject
    private DemandeSearchRepository demandeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDemandeMockMvc;

    private Demande demande;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DemandeResource demandeResource = new DemandeResource();
        ReflectionTestUtils.setField(demandeResource, "demandeRepository", demandeRepository);
        ReflectionTestUtils.setField(demandeResource, "demandeSearchRepository", demandeSearchRepository);
        this.restDemandeMockMvc = MockMvcBuilders.standaloneSetup(demandeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        demande = new Demande();
        demande.setTitre(DEFAULT_TITRE);
        demande.setDescription(DEFAULT_DESCRIPTION);
        demande.setDateDeBesoin(DEFAULT_DATE_DE_BESOIN);
        demande.setStatut(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    public void createDemande() throws Exception {
        int databaseSizeBeforeCreate = demandeRepository.findAll().size();

        // Create the Demande

        restDemandeMockMvc.perform(post("/api/demandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(demande)))
                .andExpect(status().isCreated());

        // Validate the Demande in the database
        List<Demande> demandes = demandeRepository.findAll();
        assertThat(demandes).hasSize(databaseSizeBeforeCreate + 1);
        Demande testDemande = demandes.get(demandes.size() - 1);
        assertThat(testDemande.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testDemande.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDemande.getDateDeBesoin()).isEqualTo(DEFAULT_DATE_DE_BESOIN);
        assertThat(testDemande.getStatut()).isEqualTo(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    public void getAllDemandes() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

        // Get all the demandes
        restDemandeMockMvc.perform(get("/api/demandes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(demande.getId().intValue())))
                .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].dateDeBesoin").value(hasItem(DEFAULT_DATE_DE_BESOIN_STR)))
                .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }

    @Test
    @Transactional
    public void getDemande() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

        // Get the demande
        restDemandeMockMvc.perform(get("/api/demandes/{id}", demande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(demande.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dateDeBesoin").value(DEFAULT_DATE_DE_BESOIN_STR))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDemande() throws Exception {
        // Get the demande
        restDemandeMockMvc.perform(get("/api/demandes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDemande() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

		int databaseSizeBeforeUpdate = demandeRepository.findAll().size();

        // Update the demande
        demande.setTitre(UPDATED_TITRE);
        demande.setDescription(UPDATED_DESCRIPTION);
        demande.setDateDeBesoin(UPDATED_DATE_DE_BESOIN);
        demande.setStatut(UPDATED_STATUT);

        restDemandeMockMvc.perform(put("/api/demandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(demande)))
                .andExpect(status().isOk());

        // Validate the Demande in the database
        List<Demande> demandes = demandeRepository.findAll();
        assertThat(demandes).hasSize(databaseSizeBeforeUpdate);
        Demande testDemande = demandes.get(demandes.size() - 1);
        assertThat(testDemande.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testDemande.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDemande.getDateDeBesoin()).isEqualTo(UPDATED_DATE_DE_BESOIN);
        assertThat(testDemande.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    public void deleteDemande() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

		int databaseSizeBeforeDelete = demandeRepository.findAll().size();

        // Get the demande
        restDemandeMockMvc.perform(delete("/api/demandes/{id}", demande.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Demande> demandes = demandeRepository.findAll();
        assertThat(demandes).hasSize(databaseSizeBeforeDelete - 1);
    }
}

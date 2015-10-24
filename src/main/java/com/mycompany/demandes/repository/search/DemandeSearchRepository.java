package com.mycompany.demandes.repository.search;

import com.mycompany.demandes.domain.Demande;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Demande entity.
 */
public interface DemandeSearchRepository extends ElasticsearchRepository<Demande, Long> {
}

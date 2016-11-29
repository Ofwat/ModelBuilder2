package uk.gov.ofwat.fountain.modelbuilder.repository.search;

import uk.gov.ofwat.fountain.modelbuilder.domain.ModelDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ModelDetails entity.
 */
public interface ModelDetailsSearchRepository extends ElasticsearchRepository<ModelDetails, Long> {
}

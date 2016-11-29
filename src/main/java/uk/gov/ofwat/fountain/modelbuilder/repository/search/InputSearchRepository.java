package uk.gov.ofwat.fountain.modelbuilder.repository.search;

import uk.gov.ofwat.fountain.modelbuilder.domain.Input;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Input entity.
 */
public interface InputSearchRepository extends ElasticsearchRepository<Input, Long> {
}

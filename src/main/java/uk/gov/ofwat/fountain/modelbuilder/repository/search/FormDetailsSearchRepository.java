package uk.gov.ofwat.fountain.modelbuilder.repository.search;

import uk.gov.ofwat.fountain.modelbuilder.domain.FormDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FormDetails entity.
 */
public interface FormDetailsSearchRepository extends ElasticsearchRepository<FormDetails, Long> {
}

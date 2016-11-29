package uk.gov.ofwat.fountain.modelbuilder.repository.search;

import uk.gov.ofwat.fountain.modelbuilder.domain.Form;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Form entity.
 */
public interface FormSearchRepository extends ElasticsearchRepository<Form, Long> {
}

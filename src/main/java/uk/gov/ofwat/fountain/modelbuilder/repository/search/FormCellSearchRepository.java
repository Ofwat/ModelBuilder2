package uk.gov.ofwat.fountain.modelbuilder.repository.search;

import uk.gov.ofwat.fountain.modelbuilder.domain.FormCell;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FormCell entity.
 */
public interface FormCellSearchRepository extends ElasticsearchRepository<FormCell, Long> {
}

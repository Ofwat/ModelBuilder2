package uk.gov.ofwat.fountain.modelbuilder.repository.search;

import uk.gov.ofwat.fountain.modelbuilder.domain.ValidationRuleItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ValidationRuleItem entity.
 */
public interface ValidationRuleItemSearchRepository extends ElasticsearchRepository<ValidationRuleItem, Long> {
}

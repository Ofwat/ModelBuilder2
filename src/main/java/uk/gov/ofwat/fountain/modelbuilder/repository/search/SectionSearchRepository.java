package uk.gov.ofwat.fountain.modelbuilder.repository.search;

import uk.gov.ofwat.fountain.modelbuilder.domain.Section;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Section entity.
 */
public interface SectionSearchRepository extends ElasticsearchRepository<Section, Long> {
}

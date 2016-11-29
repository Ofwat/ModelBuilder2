package uk.gov.ofwat.fountain.modelbuilder.repository.search;

import uk.gov.ofwat.fountain.modelbuilder.domain.Line;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Line entity.
 */
public interface LineSearchRepository extends ElasticsearchRepository<Line, Long> {
}

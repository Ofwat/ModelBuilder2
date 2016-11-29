package uk.gov.ofwat.fountain.modelbuilder.repository.search;

import uk.gov.ofwat.fountain.modelbuilder.domain.TextBlock;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TextBlock entity.
 */
public interface TextBlockSearchRepository extends ElasticsearchRepository<TextBlock, Long> {
}

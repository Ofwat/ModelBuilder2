package uk.gov.ofwat.fountain.modelbuilder.repository.search;

import uk.gov.ofwat.fountain.modelbuilder.domain.ModelBuilderDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ModelBuilderDocument entity.
 */
public interface ModelBuilderDocumentSearchRepository extends ElasticsearchRepository<ModelBuilderDocument, Long> {
}

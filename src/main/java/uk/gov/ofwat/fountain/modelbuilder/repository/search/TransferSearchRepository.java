package uk.gov.ofwat.fountain.modelbuilder.repository.search;

import uk.gov.ofwat.fountain.modelbuilder.domain.Transfer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Transfer entity.
 */
public interface TransferSearchRepository extends ElasticsearchRepository<Transfer, Long> {
}

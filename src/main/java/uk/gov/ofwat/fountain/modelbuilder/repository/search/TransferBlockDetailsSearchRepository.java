package uk.gov.ofwat.fountain.modelbuilder.repository.search;

import uk.gov.ofwat.fountain.modelbuilder.domain.TransferBlockDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TransferBlockDetails entity.
 */
public interface TransferBlockDetailsSearchRepository extends ElasticsearchRepository<TransferBlockDetails, Long> {
}

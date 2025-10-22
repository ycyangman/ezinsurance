package ezinsurance.jpa;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="proposals", path="proposals")
public interface ProposalRepository extends PagingAndSortingRepository<Proposal, Long>{

    List<Proposal> findByPrpsNo(String prpsNo);

}

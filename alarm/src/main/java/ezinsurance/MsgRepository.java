package ezinsurance;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="msgs", path="msgs")
public interface MsgRepository extends PagingAndSortingRepository<Msg, Long>{

    List<Msg> findByCustNo(String custNo);
    List<Msg> findByCustNm(String custNm);
}

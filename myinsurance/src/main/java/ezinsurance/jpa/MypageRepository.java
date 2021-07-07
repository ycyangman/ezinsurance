package ezinsurance.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MypageRepository extends CrudRepository<Mypage, Long> {

    List<Mypage>  findByCustNo(String custNo);

    List<Mypage>  findByCustNm(String custNm);

    List<Mypage>  findByCustNoAndPpsdsnNo(String custNo, String ppsdsnNo);

    List<Mypage>  findByCustNoAndPrpsNo(String custNo, String prpsNo);

}
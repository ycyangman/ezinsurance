package ezinsurance.bm;

import ezinsurance.ProposalApplication;
import ezinsurance.dm.TBMSANB001DM;
import ezinsurance.dm.TBMSANB002DM;
import ezinsurance.dm.TBMSANB003DM;
import ezinsurance.io.NBA00100In;
import ezinsurance.support.external.Payment;
import ezinsurance.support.external.PublishEvent;
import ezinsurance.support.util.DateUtils;
import ezinsurance.vo.PrpsInfoVO;
import ezinsurance.vo.PrpsInsVO;
import ezinsurance.vo.PrpsRelsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ProposalPrcsBM {

    @Autowired
    private TBMSANB001DM tbmsanb001DM;

    @Autowired
    private TBMSANB002DM tbmsanb002DM;

    @Autowired
    private TBMSANB003DM tbmsanb003DM;

    @Autowired
    private PublishEvent publishEvent;

    @Transactional
    public int savePrps(NBA00100In in) {

        PrpsInfoVO prpsInfo = in.getPrpsInfo();
        String prpsNo = DateUtils.getCurDtm();
        prpsInfo.setPrpsNo(prpsNo);

        //결재 MSA Sync호출-------------------------------------
        Payment payment = new Payment();

        BeanUtils.copyProperties(in, payment);
        BeanUtils.copyProperties(prpsInfo, payment);

        BigDecimal sprm = prpsInfo.getSprm();
        payment.setEventType("requestPayment");
        payment.setPayAmt(sprm); //결재금액


        //동기처리
        String paymentType = in.getPaymentType();
        if("1".equals(paymentType)) {
            ProposalApplication.applicationContext.getBean(ezinsurance.support.external.PaymentService.class)
                    .makePay(payment);
        }

        //------------------------------------------------------

        prpsInfo.setPrpsStcd("10");
        prpsInfo.setPrpsStnm("청약");
        prpsInfo.setContStcd("30");
        prpsInfo.setContStnm("초회납입");

        prpsInfo.setDpsDt(DateUtils.getCurrentDate(DateUtils.EMPTY_DATE_TYPE));
        prpsInfo.setPrpsDt(DateUtils.getCurrentDate(DateUtils.EMPTY_DATE_TYPE));

        int saveCnt = tbmsanb001DM.insertTBMSANB001a(prpsInfo);

        //---------------------------------------------------
        List<PrpsRelsVO> prpsRelsList = in.getPrpsRelsList();

        int seq=1;
        for(PrpsRelsVO prpsRels : prpsRelsList) {
            prpsRels.setPrpsNo(prpsNo);
            prpsRels.setPrpsRelpSeq(seq);
            saveCnt = tbmsanb002DM.insertTBMSANB002a(prpsRels);
            seq++;
        }

        //---------------------------------------------------

        List<PrpsInsVO> prpsInsList = in.getPrpsInsList();
        seq=1;
        for(PrpsInsVO prpsIns : prpsInsList) {
            prpsIns.setPrpsInsNo(prpsNo+seq);
            prpsIns.setPrpsNo(prpsNo);
            saveCnt = tbmsanb003DM.insertTBMSANB003a(prpsIns);
            seq++;
        }

        // kafka publish
        prpsInfo = tbmsanb001DM.selectOneTBMSANB001a(prpsNo);
        if (prpsInfo != null) {
            prpsInfo.setEventType("ProposalSaved");
        }

        
        //TODO
        //청약저장 이벤트 발행
        publishEvent.publish(prpsInfo);
        
		if("2".equals(paymentType)) {
			// 초회요청
			publishEvent.publish(payment);

		}
        return 1;

    }

}

package ezinsurance.bm;

import ezinsurance.dm.TBMSAPL001DM;
import ezinsurance.dm.TBMSAPL002DM;
import ezinsurance.dm.TBMSAPL003DM;
import ezinsurance.io.PLA00200In;
import ezinsurance.support.external.PublishEvent;
import ezinsurance.support.util.DateUtils;
import ezinsurance.vo.PlanInfoVO;
import ezinsurance.vo.PlanInsVO;
import ezinsurance.vo.PlanRelsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class PlanPrcsBM {

    @Autowired
    private TBMSAPL001DM tbmsapl001DM;

    @Autowired
    private TBMSAPL002DM tbmsapl002DM;

    @Autowired
    private TBMSAPL003DM tbmsapl003DM;

    @Autowired
    private PublishEvent publishEvent;

    @Transactional
    public String savePlan(PLA00200In in) {

        PlanInfoVO planInfo = in.getPlanInfo();

        String ppsdsnNo = DateUtils.getCurDtm();
        planInfo.setPpsdsnNo(ppsdsnNo);

        planInfo.setPpsdsnDt(DateUtils.getCurrentDate(DateUtils.EMPTY_DATE_TYPE));
        planInfo.setProgSt("가입설계");

        int saveCnt = tbmsapl001DM.insertTBMSAPL001a(planInfo);

        //---------------------------------------------------
        List<PlanRelsVO> planRelsList = in.getPlanRelsList();

        int seq=1;
        for(PlanRelsVO planRels : planRelsList) {
            planRels.setPpsdsnNo(ppsdsnNo);
            planRels.setRelpSeq(seq);
            saveCnt = tbmsapl002DM.insertTBMSAPL002a(planRels);
            seq++;
        }

        //---------------------------------------------------

        List<PlanInsVO> planInsList = in.getPlanInsList();
        seq=1;
        for(PlanInsVO planIns : planInsList) {
            planIns.setDsnInsNo(ppsdsnNo+seq);
            planIns.setPpsdsnNo(ppsdsnNo);
            saveCnt = tbmsapl003DM.insertTBMSAPL003a(planIns);
            seq++;
        }

        // kafka publish
        planInfo = tbmsapl001DM.selectOneTBMSAPL001a(ppsdsnNo);
        if (planInfo != null) {
            planInfo.setEventType("PlanSaved");
        }

        publishEvent.publish(planInfo);

        return ppsdsnNo;

    }
}

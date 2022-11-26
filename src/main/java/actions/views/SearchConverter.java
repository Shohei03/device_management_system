package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.PatientDevice;

/**
 * DTOモデル⇔Search(PatientDevice)Viewモデルの変換を行うクラス
 *
 */
public class SearchConverter {

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param PatientDeviceのインスタンス
     * @return PatientDeviceViewのインスタンス
     */
    public static List<SearchPatientDeviceView> toViewList(List<PatientDevice> pdList) {
        List<SearchPatientDeviceView> spdvList = new ArrayList<>();

        if (pdList == null) {
            return null;
        }
        for (PatientDevice pd : pdList) {
            SearchPatientDeviceView spdv = new SearchPatientDeviceView(
                    pd.getId(),
                    pd.getPatient().getPatientId(),
                    pd.getPatient().getPatientName(),
                    pd.getPatient().getPatientNameKana(),
                    pd.getPackageInsert().getApprovalNumber(),
                    pd.getPackageInsert().getJmdn().getGeneralName(),
                    pd.getPackageInsert().getDeviceName(),
                    pd.getImplantedAt(),
                    pd.getPackageInsert().getAcceptabilityOfManmaExam(),
                    pd.getPackageInsert().getAcceptabilityOfXrayExam(),
                    pd.getPackageInsert().getAcceptabilityOfCtExam(),
                    pd.getPackageInsert().getAcceptabilityOfTvExam(),
                    pd.getPackageInsert().getAcceptabilityOfMrExam());

            spdvList.add(spdv);
        }
        return spdvList;
    }

}

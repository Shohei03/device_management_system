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
    public static List<SearchPatientDeviceView> toViewList(List<PatientDevice> pd_list) {
        List<SearchPatientDeviceView> spdv_list = new ArrayList<>();

        if (pd_list == null) {
            return null;
        }
        for (PatientDevice pd : pd_list) {
            SearchPatientDeviceView spdv = new SearchPatientDeviceView(
                    pd.getId(),
                    pd.getPatient().getPatient_id(),
                    pd.getPatient().getPatient_name(),
                    pd.getPatient().getPatient_name_kana(),
                    pd.getPackageInsert().getApproval_number(),
                    pd.getPackageInsert().getJmdn().getGeneral_name(),
                    pd.getPackageInsert().getDevice_name(),
                    pd.getImplantedAt(),
                    pd.getPackageInsert().getAcceptability_of_Manma_exam(),
                    pd.getPackageInsert().getAcceptability_of_X_ray_exam(),
                    pd.getPackageInsert().getAcceptability_of_CT_exam(),
                    pd.getPackageInsert().getAcceptability_of_TV_exam(),
                    pd.getPackageInsert().getAcceptability_of_MR_exam());

            spdv_list.add(spdv);
        }
        return spdv_list;
    }

}

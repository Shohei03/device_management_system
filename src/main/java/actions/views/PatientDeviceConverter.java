package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.PatientDevice;

/**
 * 体内デバイスデータのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class PatientDeviceConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param rv ReportViewのインスタンス
     * @return Reportのインスタンス
     */
    public static PatientDevice toModel(PatientDeviceView pdv) {

        return new PatientDevice(
                pdv.getId(),
                PackageInsertConverter.toModel_from_AppNum(pdv.getApproval_number()), // 添付文書番号をもとにPackageInsertテーブルにアクセスしてインスタンスを取得
                PatientConverter.toModel_FROM_PAT(pdv.getPatient_id()), //指定したPatientDeviceViewインスタンスとリレーション関係にあるPatientインスタンスを取得
                pdv.getImplantedAt(),
                pdv.getCreatedAt(),
                pdv.getUpdatedAt(),
                pdv.getDeleteFlag());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param PatientDeviceのインスタンス
     * @return PatientDeviceViewのインスタンス
     */
    public static PatientDeviceView toView(PatientDevice pd) {

        if (pd == null) {
            return null;
        }
        return new PatientDeviceView(
                pd.getId(),
                pd.getPatient().getPatient_id(),
                pd.getPatient().getPatient_name(),
                pd.getPackageInsert().getApproval_number(),
                pd.getPackageInsert().getDevice_name(),
                pd.getImplantedAt(),
                pd.getCreatedAt(),
                pd.getUpdatedAt(),
                pd.getDeleteFlag());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<PatientDeviceView> toViewList(List<PatientDevice> list) {

        List<PatientDeviceView> pdv_list = new ArrayList<>();

        for (PatientDevice pd : list) {
            pdv_list.add(toView(pd));
        }

        return pdv_list;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param p DTOモデル(コピー先)
     * @param pdv Viewモデル(コピー元)
     */
    public static void copyViewToModel(PatientDevice pd, PatientDeviceView pdv) {

        pd.setId(pdv.getId());
        pd.setPackageInsert(PackageInsertConverter.toModel_from_AppNum(pdv.getApproval_number()));
        pd.setPatient(PatientConverter.toModel_FROM_PAT(pdv.getPatient_id()));
        pd.setImplantedAt(pdv.getImplantedAt());
        pd.setCreatedAt(pdv.getCreatedAt());
        pd.setUpdatedAt(pdv.getUpdatedAt());
        pd.setDeleteFlag(pdv.getDeleteFlag());
    }
}

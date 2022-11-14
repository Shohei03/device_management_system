package actions.views;

import models.Patient;
import services.PatientDeviceService;

/**
*
* 体内デバイスデータのPatient DTOモデル⇔PatientDeviceViewモデルの変換を行うクラス
*
*/
public class PatientConverter {
    /**
     * ViewモデルのインスタンスからPatientのDTOモデルのインスタンスを作成する
     * @param pdv PatientDeviceViewのインスタンス
     * @return pt Patientのインスタンス
     */
    public static Patient toModel(PatientDeviceView pdv) {

        Patient pt = new Patient(null, pdv.getPatient_id(), pdv.getPatient_name());
        return pt;
    }

    /**
     * ViewモデルのインスタンスからデータベースにあるPatientのインスタンスを取得
     * @param pdv PatientDeviceViewのインスタンス
     * @return pt Patientのインスタンス
     */
    public static Patient toModel_FROM_PAT(PatientDeviceView pdv) {
        PatientDeviceService service = new PatientDeviceService();

        return service.findPatient(pdv.getPatient_id());
    }



}

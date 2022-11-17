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

        Patient pat = new Patient(null, pdv.getPatient_id(), pdv.getPatient_name(), null);
        return pat;
    }

    /**
     * ViewモデルのインスタンスからデータベースにあるPatientのインスタンスを取得
     * @param pdv PatientDeviceViewのインスタンス
     * @return pt Patientのインスタンス
     */
    public static Patient toModel_FROM_PAT(int patient_id) {
        PatientDeviceService service = new PatientDeviceService();

        return service.findPatient(patient_id);
    }



    /**
     * PatientExaminationViewのインスタンスからPatientのDTOモデルのインスタンスを作成する
     * @pev PatientExaminationViewのインスタンス
     * @return pat Patientのインスタンス
     */
    public static  Patient toModel_from_PatExamV(PatientExaminationView pev) {
        Patient pat = new Patient(null, pev.getId(), pev.getPatient_name(), pev.getPatient_name_kana());
        return pat;
    }




}

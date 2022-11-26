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

        Patient pat = new Patient(null, pdv.getPatientId(), pdv.getPatientName(), null);
        return pat;
    }

    /**
     * ViewモデルのインスタンスからデータベースにあるPatientのインスタンスを取得
     * @param pdv PatientDeviceViewのインスタンス
     * @return pt Patientのインスタンス
     */
    public static Patient toModelFromPat(int patientId) {
        PatientDeviceService service = new PatientDeviceService();

        return service.findPatient(patientId);
    }

    /**
     * PatientExaminationViewのインスタンスからPatientのDTOモデルのインスタンスを作成する
     * @pev PatientExaminationViewのインスタンス
     * @return pat Patientのインスタンス
     */
    public static Patient toModelFromPatExamV(PatientExaminationView pev) {

        Patient pat = new Patient(null, pev.getPatientId(), pev.getPatientName(), pev.getPatientNameKana());
        return pat;
    }

}

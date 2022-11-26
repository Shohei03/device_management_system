package actions.views;

import models.Examination;
import services.PatientExaminationService;

public class ExaminationConverter {

    /**
     * ViewモデルのインスタンスからExaminationのDTOモデルのインスタンスを作成する
     * @param pev PatientExaminationViewのインスタンス
     * @return exam Examinationのインスタンス
     */
    public static Examination toModel(PatientExaminationView pev) {
        Examination exam = new Examination(null, pev.getExaminationItem());
        return exam;
    }

    /**
     * ViewモデルのインスタンスからデータベースにあるExaminationのインスタンスを取得
     * @param pev PatientExaminationViewのインスタンス
     * @return
     */

    public static Examination CopyModel(PatientExaminationView pev) {

        PatientExaminationService service = new PatientExaminationService();

        return service.findExamination(pev.getExaminationItem());

    }

}

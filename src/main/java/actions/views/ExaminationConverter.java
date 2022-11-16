package actions.views;

import models.Examination;

public class ExaminationConverter {

    /**
     * ViewモデルのインスタンスからExaminationのDTOモデルのインスタンスを作成する
     * @param pev PatientExaminationViewのインスタンス
     * @return exam Examinationのインスタンス
     */
    public static Examination toModel(PatientExaminationView pev) {
        Examination exam = new Examination(null, pev.getExamination_item());
        return exam;
    }

    /**
     * ViewモデルのインスタンスからデータベースにあるExaminationのインスタンスを取得
     * @param pev PatientExaminationViewのインスタンス
     * @return
     */

    public static Examination toModel_FROM_PatExamV(PatientExaminationView pev) {

        PatientExaminationService service = new PatientExaminationService();

        return service.findExam(pev.getExamination_item());

    }

}

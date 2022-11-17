package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.PatientExamination;

/**
 * 患者の検査情報データのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class PatientExaminationConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param pev RatientExaminationViewのインスタンス
     * @return PatienExaminationのインスタンス
     */
    public static PatientExamination toModel(PatientExaminationView pev) {
        return new PatientExamination(
                pev.getId(),
                PatientConverter.toModel_FROM_PAT(pev.getPatient_id()),
                ExaminationConverter.CopyModel(pev),
                pev.getExamination_date(),
                pev.getReservation_time(),
                pev.getCreatedAt());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param pe  PatientExaminationのインスタンス
     * @return PatienExaminationViewのインスタンス
     */
    public static PatientExaminationView toView(PatientExamination pe) {
        if(pe == null) {
            return null;
        }

        return new PatientExaminationView(
                pe.getId(),
                pe.getPatient().getPatient_id(),
                pe.getPatient().getPatient_name(),
                pe.getPatient().getPatient_name_kana(),
                pe.getExamination().getExamination_item(),
                pe.getExamination_date(),
                pe.getReservation_time(),
                pe.getCreatedAt());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<PatientExaminationView> toViewList(List<PatientExamination> list) {
        List<PatientExaminationView> pevs = new ArrayList<>();

        for(PatientExamination pe : list) {
            pevs.add(toView(pe));
        }

        return pevs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param r DTOモデル(コピー先)
     * @param rv Viewモデル(コピー元)
     */
    public static void copyViewToModel(PatientExamination pe, PatientExaminationView pev) {
        pe.setId(pev.getId());
        pe.setPatient(PatientConverter.toModel_FROM_PAT(pev.getPatient_id()));
        pe.setExamination(ExaminationConverter.CopyModel(pev));
        pe.setExamination_date(pev.getExamination_date());
        pe.setReservation_time(pev.getReservation_time());
        pe.setCreatedAt(pev.getCreatedAt());


    }



}

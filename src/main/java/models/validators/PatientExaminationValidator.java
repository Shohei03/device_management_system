package models.validators;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import actions.views.PatientExaminationView;
import constants.MessageConst;
import models.Patient;
import models.PatientExamination;
import services.PatientExaminationService;

/**
 * 検査情報インスタンスに設定されている値のバリデーションを行うクラス
 */
public class PatientExaminationValidator {

    /**
     * 検査情報インスタンスの各項目についてバリデーションを行う
     * @param pev 検査情報インスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(PatientExaminationService patExamService, PatientExaminationView pev,
            Boolean duplicateCheck) {
        List<String> errors = new ArrayList<String>();

        //患者IDのチェック
        String patient_id_error = validatePatient_id(patExamService, pev.getPatient_id());
        if (!patient_id_error.equals("")) {
            errors.add(patient_id_error);
        }

        //患者名のチェック
        String patient_name_error = validatePatient_name(pev.getPatient_name());
        if (!patient_name_error.equals("")) {
            errors.add(patient_name_error);
        }

        //患者名（ひらがな）のチェック
        String patient_name_kana_error = validatePatient_name_kana(pev.getPatient_name_kana());
        if (!patient_name_kana_error.equals("")) {
            errors.add(patient_name_kana_error);
        }

        //検査日のチェック
        String examination_date_error = validateExamination_date(pev.getExamination_date());
        if (!examination_date_error.equals("")) {
            errors.add(examination_date_error);
        }

        //検査情報テーブルの中に、入力データと重複するものがあるかチェック(重複チェックフラグ=trueの場合の処理)
        if (duplicateCheck) {
            String all_dupli_error = validateAll_dupli_error(patExamService, pev);
            if (!all_dupli_error.equals("")) {
                errors.add(all_dupli_error);

            }
        }

        return errors;
    }

    /**
     * 患者IDの入力チェックを行い、エラーメッセージを返却
     * @param patExamService PatientExaminationServiceのインスタンス
     * @pram patirnt_id 患者ID
     * return エラーメッセージ
     */
    private static String validatePatient_id(PatientExaminationService patExamService, int patient_id) {

        String str_patient_id = String.valueOf(patient_id);

        //入力値がなければエラーメッセージを返却
        if (str_patient_id == null || str_patient_id.equals("")) {
            return MessageConst.E_NOPAT_ID.getMessage();
        }

        //patient_idが8桁以下の正の整数値でなければ、エラーメッセージを返却
        Pattern pattern = Pattern.compile("^\\d{1,8}$");
        if (!pattern.matcher(str_patient_id).matches()) {
            return MessageConst.E_PAT_ID_NOT_EIGHT_INT.getMessage();
        }

        //エラーがない場合は空文字を返却
        return "";
    }

    /**
     * 患者名に入力値があるかチェックし、入力値がなければエラーメッセージを返却
     * @param patient_name 患者名
     * @return エラーメッセージ
     */
    private static String validatePatient_name(String patient_name) {

        if (patient_name == null || patient_name.equals("")) {
            return MessageConst.E_NOPAT_NAME.getMessage();
        }

        //エラーがなければ空文字を返却
        return "";
    }

    /**
     * 患者名（ひらがな）に入力値があるかチェックし、入力値がなければエラーメッセージを返却
     * @param patient_name_kana 患者名（ひらがな）
     * @return エラーメッセージ
     */
    private static String validatePatient_name_kana(String patient_name_kana) {

        if (patient_name_kana == null || patient_name_kana.equals("")) {
            return MessageConst.E_NOPAT_NAME.getMessage();
        }

        //エラーがなければ空文字を返却
        return "";
    }

    /**
     * 検査日の入力チェックを行い、エラーを返却
     * examination_date 検査日
     * return エラー内容
     */
    private static String validateExamination_date(LocalDate examination_date) {

        //入力値がなければエラーメッセージを返却
        if (examination_date == null) {
            return MessageConst.E_NOEXAM_DATE.getMessage();
        }

        //検査日が異常な日付の場合にエラーメッセージを返却
        LocalDate today = LocalDate.now(); //今日の日付
        LocalDate today_minus_50y = today.minusYears(50); //今日より50年前（絶対ない年を指定）
        LocalDate today_plus_5y = today.plusYears(5); //今日より5年後

        if (examination_date.isBefore(today_minus_50y) || examination_date.isAfter(today_plus_5y)) {
            return MessageConst.E_EXAM_DATE.getMessage();
        }

        //エラーがない場合、空文字を返却
        return "";
    }

    /**
     * 検査情報テーブルに、入力データと同じレコードがある場合、確認のためにエラーメッセージを返却
     * @param patExamService PatientExaminationServiceのインスタンス
     * @param pev PatientExaminationViewのインスタンス（入力データ）
     * @return エラーメッセージ
     */
    private static String validateAll_dupli_error(PatientExaminationService patExamService,
            PatientExaminationView pev) {
        Patient p = patExamService.findPatient(pev.getPatient_id());

        if (p != null) {
            List<PatientExamination> patientExaminations = patExamService.findByPat_id(p);

            if (patientExaminations != null) {
                for (PatientExamination pe : patientExaminations) {
                    //入力データ（患者ID・検査日・検査項目）が検査情報テーブルに既に存在している場合は、登録してもいいか確認のエラーメッセージを返す
                    if (p.getPatient_id().equals(pev.getPatient_id())
                            && pe.getExamination_date().isEqual(pev.getExamination_date())
                            && pe.getExamination().getExamination_item().equals(pev.getExamination_item())) {
                        return MessageConst.E_DUPLI_DATA.getMessage();
                    }
                }
            }
        }
        return "";
    }

}

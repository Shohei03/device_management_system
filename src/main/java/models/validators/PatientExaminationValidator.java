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
        String patientIdError = validatePatientId(patExamService, pev.getPatientId());
        if (!patientIdError.equals("")) {
            errors.add(patientIdError);
        }

        //患者名のチェック
        String patientNameError = validatePatientName(pev.getPatientName());
        if (!patientNameError.equals("")) {
            errors.add(patientNameError);
        }

        //患者名（ひらがな）のチェック
        String patientNameKanaError = validatePatientNameKana(pev.getPatientNameKana());
        if (!patientNameKanaError.equals("")) {
            errors.add(patientNameKanaError);
        }

        //患者名一致（患者テーブルにある場合）のチェック
        String patientNameMatchError = validatePatientNameMatch(patExamService, pev);
        if (!patientNameMatchError.equals("")) {
            errors.add(patientNameMatchError);
        }

        //検査日のチェック
        String examinationDateError = validateExaminationDate(pev.getExaminationDate());
        if (!examinationDateError.equals("")) {
            errors.add(examinationDateError);
        }

        //検査情報テーブルの中に、入力データと重複するものがあるかチェック(重複チェックフラグ=trueの場合の処理)
        if (duplicateCheck) {
            String allDupliError = validateAllDupliError(patExamService, pev);
            if (!allDupliError.equals("")) {
                errors.add(allDupliError);

            }
        }

        return errors;
    }

    /**
     * 患者IDの入力チェックを行い、エラーメッセージを返却
     * @param patExamService PatientExaminationServiceのインスタンス
     * @pram patirntId 患者ID
     * return エラーメッセージ
     */
    private static String validatePatientId(PatientExaminationService patExamService, int patientId) {

        String strPatientId = String.valueOf(patientId);

        //入力値がなければエラーメッセージを返却
        if (strPatientId == null || strPatientId.equals("")) {
            return MessageConst.E_NOPAT_ID.getMessage();
        }

        //patient_idが8桁以下の正の整数値でなければ、エラーメッセージを返却
        Pattern pattern = Pattern.compile("^\\d{1,8}$");
        if (!pattern.matcher(strPatientId).matches()) {
            return MessageConst.E_PAT_ID_NOT_EIGHT_INT.getMessage();
        }

        //エラーがない場合は空文字を返却
        return "";
    }

    /**
     * 患者名に入力値があるかチェックし、入力値がなければエラーメッセージを返却
     * @param patientName 患者名
     * @return エラーメッセージ
     */
    private static String validatePatientName(String patientName) {

        if (patientName == null || patientName.equals("")) {
            return MessageConst.E_NOPAT_NAME.getMessage();
        }

        //エラーがなければ空文字を返却
        return "";
    }

    /**
     * 患者名（ひらがな）に入力値があるかチェックし、入力値がなければエラーメッセージを返却
     * @param patientNameKana 患者名（ひらがな）
     * @return エラーメッセージ
     */
    private static String validatePatientNameKana(String patientNameKana) {

        if (patientNameKana == null || patientNameKana.equals("")) {
            return MessageConst.E_NOPAT_NAME.getMessage();
        }

        //エラーがなければ空文字を返却
        return "";
    }

    /**
     * 患者テーブルに指定した患者IDがある場合に、入力した患者名が患者テーブルの患者名と一致しなければエラーメッセージを返却
     * @param patDevService 体内デバイスのserviceクラス
     * @param pdv 体内デバイスインスタンス
     * @return エラーメッセージ
     */
    private static String validatePatientNameMatch(PatientExaminationService patExamService,
            PatientExaminationView pev) {

        Patient p = patExamService.findPatient(pev.getPatientId());

        if (p != null) {

            //指定した患者IDのレコードが患者テーブルにあり、その名前が入力された名前と異なる場合エラーメッセージを返す
            if (!deleteSpace(p.getPatientName()).equals(deleteSpace(pev.getPatientName()))) {

                return MessageConst.E_WRONG_PAT_NAME.getMessage();
            } else if (!deleteSpace(p.getPatientNameKana()).equals(deleteSpace(pev.getPatientNameKana()))) {

                return MessageConst.E_WRONG_PAT_NAME.getMessage();
            } else {
                return "";
            }
        }
        return "";
    }

    /**
     * 検査日の入力チェックを行い、エラーを返却
     * examinationDate 検査日
     * return エラー内容
     */
    private static String validateExaminationDate(LocalDate examinationDate) {

        //入力値がなければエラーメッセージを返却
        if (examinationDate == null) {
            return MessageConst.E_NOEXAM_DATE.getMessage();
        }

        //検査日が異常な日付の場合にエラーメッセージを返却
        LocalDate today = LocalDate.now(); //今日の日付
        LocalDate todayMinus50y = today.minusYears(50); //今日より50年前（絶対ない年を指定）
        LocalDate todayPlus5y = today.plusYears(5); //今日より5年後

        if (examinationDate.isBefore(todayMinus50y) || examinationDate.isAfter(todayPlus5y)) {
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
    private static String validateAllDupliError(PatientExaminationService patExamService,
            PatientExaminationView pev) {
        Patient p = patExamService.findPatient(pev.getPatientId());

        if (p != null) {
            List<PatientExamination> patientExaminations = patExamService.findByPatId(p);

            if (patientExaminations != null) {
                for (PatientExamination pe : patientExaminations) {
                    //入力データ（患者ID・検査日・予約時間・検査項目）が検査情報テーブルに既に存在している場合は、登録してもいいか確認のエラーメッセージを返す
                    if (p.getPatientId().equals(pev.getPatientId())
                            && pe.getExaminationDate().isEqual(pev.getExaminationDate())
                            && pe.getReservationTime().equals(pev.getReservationTime())
                            && pe.getExamination().getExaminationItem().equals(pev.getExaminationItem())) {
                        return MessageConst.E_DUPLI_DATA.getMessage();
                    }
                }
            }
        }
        return "";
    }

    /**
     * 入力値（引数）の空白削除。入力値チェックに使用。
     * @param name 検証する文字列
     * @return Boolean型
     */
    private static String deleteSpace(String name) {
        String dHalfsName = name.replaceAll("\u0020|\u00A0| ", "");
        String dsName = dHalfsName.replaceAll("　| ", "");

        return dsName;
    }

}

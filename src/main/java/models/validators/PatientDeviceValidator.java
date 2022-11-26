package models.validators;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import actions.views.PatientDeviceView;
import constants.MessageConst;
import models.Patient;
import models.PatientDevice;
import services.PackageInsertService;
import services.PatientDeviceService;

/**
 * 体内デバイスインスタンスに設定されている値のバリデーションを行うクラス
 */
public class PatientDeviceValidator {

    /**
     * 体内デバイスインスタンスの各項目についてバリデーションを行う
     * @param packService 添付文書テーブルを扱うserviceクラス
     * @param ptDevService 体内デバイステーブルを扱うserviceクラス
     * @param pdv 患者の体内デバイスインスタンス（View）
     * @param patientIdDuplicateCheckFlag 患者ID重複チェックFlag
     * @param duplicateCheck //入力データの重複チェックFlag
     * @return
     */
    public static List<String> validate(PackageInsertService packService, PatientDeviceService ptDevService,
            PatientDeviceView pdv, Boolean patientIdDuplicateCheckFlag, Boolean duplicateCheck) {

        List<String> errors = new ArrayList<String>();

        //患者IDのチェック
        String patientIdError = validatePatientId(ptDevService, pdv.getPatientId(), patientIdDuplicateCheckFlag);
        if (!patientIdError.equals("")) {
            errors.add(patientIdError);
        }

        //患者名のチェック
        String patientNameError = validatePatientName(pdv.getPatientName());
        if (!patientNameError.equals("")) {
            errors.add(patientNameError);
        }

        //患者名（ひらがな）のチェック
        String patientNameKanaError = validatePatientNameKana(pdv.getPatientNameKana());
        if (!patientNameKanaError.equals("")) {
            errors.add(patientNameKanaError);
        }

        //患者名一致（患者テーブルにある場合）のチェック
        String patientNameMatchError = validatePatient_name_match(ptDevService, pdv);
        if (!patientNameMatchError.equals("")) {
            errors.add(patientNameMatchError);
        }

        //添付文書承認番号のチェック
        String approvalNumberError = validateApprovalNumber(packService, pdv.getApprovalNumber());
        if (!approvalNumberError.equals("")) {
            errors.add(approvalNumberError);
        }

        //デバイスの販売名のチェック
        String deviceNameError = validateDeviceName(packService, pdv);
        if (!deviceNameError.equals("")) {
            errors.add(deviceNameError);
        }

        //デバイスの埋込日のチェック
        String implantedAtError = validateImplantedAt(packService, pdv.getImplantedAt());
        if (!implantedAtError.equals("")) {
            errors.add(implantedAtError);
        }

        //体内デバイステーブルの中に、入力データと重複するものがあるかチェック(重複チェックフラグ=trueの場合の処理)
        if (duplicateCheck) {
            String allDupliError = validateAllDupliError(ptDevService, pdv);
            if (!allDupliError.equals("")) {
                errors.add(allDupliError);
            }
        }

        return errors;
    }

    /**
     * 患者IDの入力チェックを行い、エラーメッセージを返却
     * @param service PatientDeviceServiceのインスタンス
     * @param patientId 患者ID
     * @param patientIdDuplicateCheckFlag 患者IDの重複チェックを実施するかどうか(実施する:true 実施しない:false)
     * @return エラーメッセージ
     */
    private static String validatePatientId(PatientDeviceService ptDevService, int patientId,
            Boolean patientIdDuplicateCheckFlag) {
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

        //重複チェック(patientIdDuplicateCheckFlag = Trueの場合）
        if (patientIdDuplicateCheckFlag) {
            long patientIdCount = isDuplicatePatient(ptDevService, patientId);

            //同一患者IDが既に登録されている場合はエラーメッセージを返却
            if (patientIdCount > 0) {
                return MessageConst.E_PAT_ID_EXIST.getMessage();
            }
        }

        //エラーがない場合は空文字を返却
        return "";

    }

    /**
     * @param service PatientDeviceServiceのインスタンス
     * @param patientId 患者ID
     * @return 患者テーブルに登録されている同一患者IDのデータの件数
     */
    private static long isDuplicatePatient(PatientDeviceService ptDevService, int patientId) {

        long patientCount = ptDevService.countByPatientId(patientId);
        return patientCount;
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
     * 患者テーブルに指定した患者IDがある場合に、入力した患者名が患者テーブルの患者名と一致しなければエラーメッセージを返却
     * @param patDevService 体内デバイスのserviceクラス
     * @param pdv 体内デバイスインスタンス
     * @return エラーメッセージ
     */
    private static String validatePatient_name_match(PatientDeviceService patDevService, PatientDeviceView pdv) {

        Patient p = patDevService.findPatient(pdv.getPatientId());

        if (p != null) {

            //指定した患者IDのレコードが患者テーブルにあり、その名前が入力された名前と異なる場合エラーメッセージを返す
            if (!deleteSpace(p.getPatientName()).equals(deleteSpace(pdv.getPatientName()))) {
                return MessageConst.E_WRONG_PAT_NAME.getMessage();
            } else if (!deleteSpace(p.getPatientNameKana()).equals(deleteSpace(pdv.getPatientNameKana()))) {
                return MessageConst.E_WRONG_PAT_NAME.getMessage();
            } else {
                return "";
            }
        }
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
     * 添付文書承認番号の入力値チェックを行い、エラーメッセージを返却
     * 添付文書テーブル（PackageInsert）に入力した添付文書承認番号のレコードがあるか確認
     * @param packService PatientDeviceServiceのインスタンス
     * @param approvalNum 添付文書承認番号
     * return エラーメッセージ
     */
    private static String validateApprovalNumber(PackageInsertService packService, String approvalNum) {

        //入力値がなければエラーメッセージを返却
        if (approvalNum == null || approvalNum.equals("")) {
            return MessageConst.E_NOAPPROVAL_NUM.getMessage();
        }

        //入力値に英数字以外の文字があればエラーメッセージ
        if (!checkAlphanumeric(approvalNum)) {
            return MessageConst.E_APP_NUM_ERR.getMessage();
        }

        //入力した添付文書承認番号が添付文書テーブルにない場合、先に添付文書の登録するようメッセージを返す。
        if (isDuplicateApprovalNum(packService, approvalNum) == 0) {
            return MessageConst.E_NOAPP_NUM_IN_PACK_DATABASE.getMessage();

        }

        //エラーがない場合、空文字を返却
        return "";
    }

    /**
     * 添付文書テーブルの中で、指定した添付文書承認番号をもつレコードの件数
     * @param service PatientDeviceServiceのインスタンス
     * @param approvalNum 添付文書承認番号
     * @return 添付文書テーブルに登録されている同一承認番号のデータの件数
     */
    private static long isDuplicateApprovalNum(PackageInsertService service, String approvalNum) {
        long approvalNumCount = service.countByAapprovalNumber(approvalNum);

        return approvalNumCount;
    }

    /**
     * デバイスの販売名をチェックし、エラーメッセージを返却
     */
    private static String validateDeviceName(PackageInsertService packService, PatientDeviceView pdv) {
        String deviceName = pdv.getDeviceName();

        //入力値がなければエラーメッセージを返却
        if (deviceName == null || deviceName.equals("")) {
            return MessageConst.E_NODEVICE_NAME.getMessage();
        }

        //入力した添付文書承認番号の販売名を添付文書テーブルから出力し、その値がPackageDeviceViewに入力された販売名と一致してない場合エラーメッセージを表示
        //添付文書テーブルに登録されている添付文書承認番号の入力がある場合に検証する
        if (packService.countByAapprovalNumber(pdv.getApprovalNumber()) > 0) {
            String packDeviceName = packService.findPackageInsertByAppNum(pdv.getApprovalNumber()).getDeviceName();
            if (!deviceName.equals(packDeviceName)) {
                return MessageConst.E_ERR_DEVICE_NAME.getMessage();
            }
        }

        //エラーがない場合、空文字を返却
        return "";
    }

    /**
     * デバイスの埋込日の入力がない場合にエラーメッセージを返却
     * implantedAt LocalDate デバイスの埋込日
     * pack_service PackageInsertService 添付文書テーブルとのやりとりをするServiceクラス
     * return エラー内容
     */
    private static String validateImplantedAt(PackageInsertService packService, LocalDate implantedAt) {

        //入力値がなければエラーメッセージを返却
        if (implantedAt == null) {
            return MessageConst.E_NOIMP_DATE.getMessage();
        }

        //埋込日が異常な日付の場合にエラーメッセージを返却
        LocalDate today = LocalDate.now(); //今日の日付
        LocalDate todayMinus150y = today.minusYears(150); //今日より150年前（絶対ない年を指定）

        if (implantedAt.isBefore(todayMinus150y) || implantedAt.isAfter(today)) {
            return MessageConst.E_ERR_IMP_DATE.getMessage();
        }

        //エラーがない場合、空文字を返却
        return "";
    }

    /**
     * 体内デバイステーブルに、入力データと同じレコードがある場合、確認のためのエラーメッセージを返却
     * @param service PatientDeviceServiceのインスタンス
     * @param pdv PatientDeviceViewのインスタンス（入力データ）
     * @return エラーメッセージ
     */
    private static String validateAllDupliError(PatientDeviceService ptDevService, PatientDeviceView pdv) {
        Patient p = ptDevService.findPatient(pdv.getPatientId());

        if (p != null) {
            //体内デバイステーブルから、引数で指定した患者インスタンスをもつレコードを取得
            List<PatientDevice> pds = ptDevService.findAllPatDevbyPatient(p);

            for (PatientDevice pd : pds) {
                if (pd != null) {
                    //入力データ（患者ID・デバイスの添付文書承認番号・埋込日）が体内デバイステーブルに既に存在している場合は、登録してもいいか確認のエラーメッセージを返す
                    if (p.getPatientId().equals(pdv.getPatientId())
                            && pd.getPackageInsert().getApprovalNumber().equals(pdv.getApprovalNumber())
                            && pd.getImplantedAt().isEqual(pdv.getImplantedAt())) {
                        return MessageConst.E_DUPLI_DATA.getMessage();
                    }
                }
            }
        }
        return "";

    }

    /**
     * 入力値（引数）が英数字であればTrueを返す。入力値チェックに使用。
     * @param name 検証する文字列
     * @return Boolean型
     */
    public static Boolean checkAlphanumeric(String name) {
        // 正規表現パターンを用意する
        String regexNum = "^[A-Za-z0-9]+$";
        Pattern p1 = Pattern.compile(regexNum); //正規表現パターンの読み込み
        Matcher m1 = p1.matcher(name); //パターンと検査対象文字列の照合
        boolean result = m1.matches(); //照合結果をtrueまたはfalseで取得する

        return result;
    }

    /**
     * 入力値（引数）の空白削除。入力値チェックに使用。
     * @param name 検証する文字列
     * @return Boolean型
     */
    private static String deleteSpace(String name) {

        String dHalfsName = name.replaceAll("\u0020|\u00A0|  ", "");
        String dsName = dHalfsName.replaceAll("　| ", "");

        return dsName;
    }

}

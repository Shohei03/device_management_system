package models.validators;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import actions.views.PatientDeviceView;
import constants.MessageConst;
import services.PackageInsertService;
import services.PatientDeviceService;

/**
 * 体内デバイスインスタンスに設定されている値のバリデーションを行うクラス
 */
public class PatientDeviceValidator {

    /**
     * 体内デバイスインスタンスの各項目についてバリデーションを行う
     * @param pdv 体内デバイスインスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(PackageInsertService pack_service, PatientDeviceService ptDev_service,
            PatientDeviceView pdv, Boolean patient_idDuplicateCheckFlag) {

        List<String> errors = new ArrayList<String>();

        //患者IDのチェック
        String patient_id_error = validatePatient_id(ptDev_service, pdv.getPatient_id(), patient_idDuplicateCheckFlag);
        if (!patient_id_error.equals("")) {
            errors.add(patient_id_error);
        }

        //患者名のチェック
        String patient_name_error = validatePatient_name(pdv.getPatient_name());
        if (!patient_name_error.equals("")) {
            errors.add(patient_name_error);
        }

        //添付文書承認番号のチェック
        String approval_number_error = validateApproval_number(pack_service, pdv.getApproval_number());
        if (!approval_number_error.equals("")) {
            errors.add(approval_number_error);
        }

        //デバイスの販売名のチェック
        String device_name_error = validateDevice_name(pack_service, pdv);
        if (!device_name_error.equals("")) {
            errors.add(device_name_error);
        }

        //デバイスの埋込日のチェック
        String implantedAt_error = validateImplantedAt(pack_service, pdv.getImplantedAt());
        if (!implantedAt_error.equals("")) {
            errors.add(implantedAt_error);
        }

        return errors;
    }

    /**
     * 患者IDの入力チェックを行い、エラーメッセージを返却
     * @param service PatientDeviceServiceのインスタンス
     * @param patient_id 患者ID
     * @param patient_idDuplicateCheckFlag 患者IDの重複チェックを実施するかどうか(実施する:true 実施しない:false)
     * @return エラーメッセージ
     */
    private static String validatePatient_id(PatientDeviceService ptDev_service, int patient_id,
            Boolean patient_idDuplicateCheckFlag) {
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

        //重複チェック(patient_idDuplicateCheckFlag = Trueの場合）
        if (patient_idDuplicateCheckFlag) {
            long patient_idCount = isDuplicatePatient(ptDev_service, patient_id); /////////////

            //同一患者IDが既に登録されている場合はエラーメッセージを返却
            if (patient_idCount > 0) {
                return MessageConst.E_PAT_ID_EXIST.getMessage();
            }
        }

        //エラーがない場合は空文字を返却
        return "";

    }

    /**
     * @param service PatientDeviceServiceのインスタンス
     * @param patient_id 患者ID
     * @return 患者テーブルに登録されている同一患者IDのデータの件数
     */
    private static long isDuplicatePatient(PatientDeviceService ptDev_service, int patient_id) {

        long patientCount = ptDev_service.countByPatient_id(patient_id);
        return patientCount;
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
     * 添付文書承認番号の入力値チェックを行い、エラーメッセージを返却
     * 添付文書テーブル（PackageInsert）に入力した添付文書承認番号のレコードがあるか確認
     * @param pack_service PatientDeviceServiceのインスタンス
     * @param approval_num 添付文書承認番号
     * return エラーメッセージ
     */
    private static String validateApproval_number(PackageInsertService pack_service, String approval_num) {

        //入力値がなければエラーメッセージを返却
        if (approval_num == null || approval_num.equals("")) {
            return MessageConst.E_NOAPPROVAL_NUM.getMessage();
        }

        if (isDuplicateApprovalNum(pack_service, approval_num) == 0) {
            return MessageConst.E_NOAPP_NUM_IN_PACK_DATABASE.getMessage();

        }

        //エラーがない場合、空文字を返却
        return "";
    }

    /**
     * 添付文書テーブルの中で、指定した添付文書承認番号をもつレコードの件数
     * @param service PatientDeviceServiceのインスタンス
     * @param approval_num 添付文書承認番号
     * @return 添付文書テーブルに登録されている同一承認番号のデータの件数
     */
    private static long isDuplicateApprovalNum(PackageInsertService service, String approval_num) {
        long approval_numCount = service.countByAapproval_number(approval_num);

        return approval_numCount;
    }

    /**
     * デバイスの販売名をチェックし、エラーメッセージを返却
     */
    private static String validateDevice_name(PackageInsertService pack_service, PatientDeviceView pdv) {
        String device_name = pdv.getDevice_name();

        //入力値がなければエラーメッセージを返却
        if (device_name == null || device_name.equals("")) {
            return MessageConst.E_NODEVICE_NAME.getMessage();
        }

        //入力した添付文書承認番号の販売名を添付文書テーブルから出力し、その値がPackageDeviceViewに入力された販売名と一致してない場合エラーメッセージを表示
        //添付文書テーブルに登録されている添付文書承認番号の入力がある場合に検証する
        if (pack_service.countByAapproval_number(pdv.getApproval_number()) > 0) {
            String pack_device_name = pack_service.findPackageInsertByAppNum(pdv.getApproval_number()).getDevice_name();
            if (!device_name.equals(pack_device_name)) {
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
    private static String validateImplantedAt(PackageInsertService pack_service, LocalDate implantedAt) {

        //入力値がなければエラーメッセージを返却
        if (implantedAt == null) {
            return MessageConst.E_NOIMP_DATE.getMessage();
        }

        //埋込日が異常な日付の場合にエラーメッセージを返却
        LocalDate today = LocalDate.now(); //今日の日付
        LocalDate today_minus_150y = today.minusYears(150); //今日より150年前

        System.out.println(implantedAt.isBefore(today_minus_150y));

        if (implantedAt.isBefore(today_minus_150y) || implantedAt.isAfter(today)) {
            return MessageConst.E_ERR_IMP_DATE.getMessage();
        }

        //エラーがない場合、空文字を返却
        return "";
    }

}

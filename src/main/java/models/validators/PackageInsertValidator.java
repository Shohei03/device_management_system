package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.PackageInsertView;
import constants.MessageConst;
import services.PackageInsertService;

/**
 *
 * 添付文書インスタンスに設定されている値のバリデーションを行うクラス
 */
public class PackageInsertValidator {
    /**
     * 添付文書インスタンスの各項目についてバリデーションを行う
     * @param pv 添付文書インスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(PackageInsertService service, PackageInsertView pv,
            Boolean approvalNumDuplicateCheckFlag) {
        List<String> errors = new ArrayList<String>();

        //承認番号のチェック
        String approval_number_error = validateApproval_number(service, pv.getApproval_number(),
                approvalNumDuplicateCheckFlag);
        if (!approval_number_error.equals("")) {
            errors.add(approval_number_error);
        }

        //JMDNコードのチェック
        String JMDN_code_error = validateJMDN_code(pv.getJMDN_code());
        if (!JMDN_code_error.equals("")) {
            errors.add(JMDN_code_error);
        }

        //一般的名称のチェック
        String general_name_error = validateGeneral_name(pv.getGeneral_name());
        if (!general_name_error.equals("")) {
            errors.add(general_name_error);
        }

        //デバイス名（販売名）のチェック
        String device_name_error = validateDevice_name(pv.getDevice_name());
        if (!device_name_error.equals("")) {
            errors.add(device_name_error);
        }

        return errors;

    }

    /**
     * 承認番号の入力チェックを行い、入力値がなければエラーメッセージを返却
     * @param srevice PackageInsertServiceのインスタンス
     * @param approval_number 承認番号
     * @param approvalNumDuplicateCheckFlag 承認番号の重複チェックを実施するかどうか（実施する：True、実施しない：false）
     * @return エラーメッセージ
     */
    private static String validateApproval_number(PackageInsertService service, String approval_number,
            Boolean approvalNumDuplicateCheckFlag) {
        //入力値がなければエラーメッセージを返却
        if (approval_number == null || approval_number.equals("")) {
            return MessageConst.E_NOAPPROVAL_NUM.getMessage();
        }

        if (approvalNumDuplicateCheckFlag) {
            //承認番号の重複チェックを実施

            long approvalNumCount = isDuplicateApprovalNum(service, approval_number);

            //同じデバイス（添付文書番号）が既に登録されている場合はエラーメッセージを返却
            if (approvalNumCount > 0) {
                return MessageConst.E_DEVICE_EXIST.getMessage();
            }

        }

        //エラーがない場合は空文字を返却
        return "";
    }

    /**
     * @param service PackageInsertServiceのインスタンス
     * @param  approval_number 承認番号
     * @return 添付文書テーブルに登録されている同一添付文書承認番号のデータの件数
     */

    private static long isDuplicateApprovalNum(PackageInsertService service, String approval_number) {

        long approvalNumCount = service.countByAapproval_number(approval_number);
        return approvalNumCount;
    }

    /**
     * 内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param JMDN_code JMDNコード
     * @return エラーメッセージ
     */
    private static String validateJMDN_code(String JMDN_code) {
        if (JMDN_code == null || JMDN_code.equals("")) {
            return MessageConst.E_NOJMDN_CODE.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param general_name 一般的名称
     * @return エラーメッセージ
     */
    private static String validateGeneral_name(String general_name) {
        if (general_name == null || general_name.equals("")) {
            return MessageConst.E_NOGENERAL_NAME.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param device_name デバイスの販売名
     * @return エラーメッセージ
     */
    private static String validateDevice_name(String device_name) {
        if (device_name == null || device_name.equals("")) {
            return MessageConst.E_NODEVICE_NAME.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

}

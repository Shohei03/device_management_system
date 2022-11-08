package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.PackageInsertView;
import constants.MessageConst;

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
    public static List<String> validate(PackageInsertView pv) {
        List<String> errors = new ArrayList<String>();

        //承認番号のチェック
        String approval_number_error = validateApproval_number(pv.getApproval_number());
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
     * 承認番号に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param approval_number 承認番号
     * @return エラーメッセージ
     */
    private static String validateApproval_number(String approval_number) {
        if (approval_number == null || approval_number.equals("")) {
            return MessageConst.E_NOAPPROVAL_NUM.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
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

package models.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import actions.views.PackageInsertView;
import constants.AttributeConst;
import constants.MessageConst;
import services.PackageInsertService;

/**
 * 添付文書インスタンス(PackageInsertView)に設定されている値のバリデーションを行うクラス
 */
public class PackageInsertValidator {
    /**
     * 添付文書インスタンスの各項目についてバリデーションを行う
     * @param service PackageInsertServiceのインスタンス
     * @param pv 添付文書インスタンス
     * @param approvalNumDuplicateCheckFlag 承認番号の重複チェックを実施するかどうか（実施する：True、実施しない：false）
     * @return エラーのリスト
     */
    public static List<String> validate(PackageInsertService service, PackageInsertView pv,
            Boolean approvalNumDuplicateCheckFlag) {

        List<String> errors = new ArrayList<String>();

        //承認番号のチェック
        String approvalNumberError = validateApprovalNumber(service, pv.getApprovalNumber(),
                approvalNumDuplicateCheckFlag);
        if (!approvalNumberError.equals("")) {
            errors.add(approvalNumberError);
        }

        //JMDNコードのチェック
        String jmdnCodeError = validateJmdnCode(pv.getJmdnCode());
        if (!jmdnCodeError.equals("")) {
            errors.add(jmdnCodeError);
        }

        //一般的名称のチェック
        String generalNameError = validateGeneralName(pv.getGeneralName());
        if (!generalNameError.equals("")) {
            errors.add(generalNameError);
        }

        //デバイス名（販売名）のチェック
        String deviceNameError = validateDeviceName(pv.getDeviceName());
        if (!deviceNameError.equals("")) {
            errors.add(deviceNameError);
        }

        return errors;
    }

    /**
     * 各検査の可否に正しい値（可能・条件付き可能・不可能のいずれか）が入っているか検証
     * @param pv 添付文書Viewインスタンス
     * @return エラーメッセージ
     */
    public static String validateAllExamAcceptability(String manma, String xray, String ct, String tv, String mr) {

        //乳腺X線検査の可否入力チェック
        Boolean checkManma = validateExamAcceptability(manma);

        //単純X線検査の可否入力チェック
        Boolean checkXray = validateExamAcceptability(xray);

        //CT検査の可否入力チェック
        Boolean checkCt = validateExamAcceptability(ct);

        //X線TV検査の可否入力チェック
        Boolean checkTv = validateExamAcceptability(tv);

        //MR検査の可否入力チェック
        Boolean checkMr = validateExamAcceptability(mr);

        String error = null;

        //各検査の可否の値が正しくない（可能・条件付き可能・不可能以外）場合、エラーメッセージを返却。
        if (!checkManma || !checkXray || !checkCt || !checkTv || !checkMr) {
            error = MessageConst.E_ABNORMAL_ExamAcceptability.getMessage();

            return error;
        }
        return null;
    }

    /**
     * 添付文書承認番号の入力チェックを行い、入力値がなければエラーメッセージを返却。
     * 英数字でなければエラーメッセージを返却。
     * 重複チェックをする場合は重複があれば、エラーメッセージを返却。
     * @param service PackageInsertServiceのインスタンス
     * @param approvalNumber 承認番号
     * @param approvalNumDuplicateCheckFlag 承認番号の重複チェックを実施するかどうか（実施する：True、実施しない：false）
     * @return エラーメッセージ
     */
    private static String validateApprovalNumber(PackageInsertService service, String approvalNumber,
            Boolean approvalNumDuplicateCheckFlag) {

        //入力値がなければエラーメッセージを返却
        if (approvalNumber == null || approvalNumber.equals("")) {
            return MessageConst.E_NOAPPROVAL_NUM.getMessage();
        }

        //入力値に英数字以外の文字があればエラーメッセージ
        if (!checkAlphanumeric(approvalNumber)) {
            return MessageConst.E_APP_NUM_ERR.getMessage();
        }

        if (approvalNumDuplicateCheckFlag) {
            //承認番号の重複チェックを実施（approvalNumDuplicateCheckFlag = TRUEの場合）
            long approvalNumCount = isDuplicateApprovalNum(service, approvalNumber);

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
     * @param  approvalNumber 添付文書承認番号
     * @return 添付文書テーブルに登録されている同一添付文書承認番号のデータの件数
     */
    private static long isDuplicateApprovalNum(PackageInsertService service, String approvalNumber) {

        long approvalNumCount = service.countByAapprovalNumber(approvalNumber);
        return approvalNumCount;
    }

    /**
     * JMDNコードのフォームに入力値がなければエラーメッセージを返却
     * JMDNコードのフォームに数字以外が入力されている場合、エラーメッセージを返却
     * @param JMDN_code JMDNコード
     * @return エラーメッセージ
     */
    private static String validateJmdnCode(String jmdnCode) {

        if (jmdnCode == null || jmdnCode.equals("")) {
            return MessageConst.E_NOJMDN_CODE.getMessage();
        }

        //入力値に数字以外の文字があればエラーメッセージ
        if (!checkNumeric(jmdnCode)) {
            return MessageConst.E_JMDN_code_ERR.getMessage();
        }
        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 一般的名称のフォームに入力値がなければエラーメッセージを返却
     * @param generalName 一般的名称
     * @return エラーメッセージ
     */
    private static String validateGeneralName(String generalName) {

        if (generalName == null || generalName.equals("")) {
            return MessageConst.E_NOGENERAL_NAME.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 販売名のフォームに入力値がなければエラーメッセージを返却
     * @param deviceName デバイスの販売名
     * @return エラーメッセージ
     */
    private static String validateDeviceName(String deviceName) {

        if (deviceName == null || deviceName.equals("")) {
            return MessageConst.E_NODEVICE_NAME.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 入力値（引数）が英数字であればTrueを返す。入力値チェックに使用。
     * @param name 検証する文字列
     * @return Boolean型
     */
    private static Boolean checkAlphanumeric(String name) {
        // 正規表現パターンを用意する
        String regexNum = "^[A-Za-z0-9]+$";
        Pattern p1 = Pattern.compile(regexNum); //正規表現パターンの読み込み
        Matcher m1 = p1.matcher(name); //パターンと検査対象文字列の照合
        boolean result = m1.matches(); //照合結果をtrueまたはfalseで取得する

        return result;
    }

    /**
     * 入力値（引数）が数字であればTrueを返す。入力値チェックに使用。
     * @param num 検証する値
     * @return Boolean型
     */
    private static Boolean checkNumeric(String num) {
        // 正規表現パターンを用意する
        String regexNum = "^[0-9]+$";
        Pattern p1 = Pattern.compile(regexNum); //正規表現パターンの読み込み
        Matcher m1 = p1.matcher(num); //パターンと検査対象文字列の照合
        boolean result = m1.matches(); //照合結果をtrueまたはfalseで取得する

        return result;
    }

    /**
     * 検査の可否が正しく入力されているか検証
     * @param acceptabilityOfExam 各検査の可否
     * @return Boolean型（正しく入力されている場合はtrue）
     */
    public static Boolean validateExamAcceptability(String acceptabilityOfExam) {
        // 検査の可否が可能・条件付き可能・不可能のいずれかでない場合はFalseを返却
        if (!(acceptabilityOfExam.equals(AttributeConst.PACK_EXM_SAFE.getValue())
                || acceptabilityOfExam.equals(AttributeConst.PACK_EXM_CONDITIONAL_SAFE.getValue())
                || acceptabilityOfExam.equals(AttributeConst.PACK_EXM_UNSAFE.getValue()))) {
            return false;
        }
        return true;
    }

}

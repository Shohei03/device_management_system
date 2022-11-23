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
     * 各検査の可否に正しい値（可能・条件付き可能・不可能のいずれか）が入っているか検証
     * @param pv 添付文書Viewインスタンス
     * @return エラーメッセージ
     */
    public static String validateAllExamAcceptability(String manma, String X_ray, String CT, String TV, String MR) {

        //乳腺X線検査の可否入力チェック
        Boolean check_manma = validateExamAcceptability(manma);

        //単純X線検査の可否入力チェック
        Boolean check_X_ray = validateExamAcceptability(X_ray);

        //CT検査の可否入力チェック
        Boolean check_CT = validateExamAcceptability(CT);

        //X線TV検査の可否入力チェック
        Boolean check_TV = validateExamAcceptability(TV);

        //MR検査の可否入力チェック
        Boolean check_MR = validateExamAcceptability(MR);

        String error = null;

        //各検査の可否の値が正しくない（可能・条件付き可能・不可能以外）場合、エラーメッセージを返却。
        if (!check_manma || !check_X_ray || !check_CT || !check_TV || !check_MR) {
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

        //入力値に英数字以外の文字があればエラーメッセージ
        if (!checkAlphanumeric(approval_number)) {
            return MessageConst.E_APP_NUM_ERR.getMessage();
        }

        if (approvalNumDuplicateCheckFlag) {
            //承認番号の重複チェックを実施（approvalNumDuplicateCheckFlag = TRUEの場合）
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
     * @param  approval_number 添付文書承認番号
     * @return 添付文書テーブルに登録されている同一添付文書承認番号のデータの件数
     */
    private static long isDuplicateApprovalNum(PackageInsertService service, String approval_number) {

        long approvalNumCount = service.countByAapproval_number(approval_number);
        return approvalNumCount;
    }

    /**
     * JMDNコードのフォームに入力値がなければエラーメッセージを返却
     * JMDNコードのフォームに数字以外が入力されている場合、エラーメッセージを返却
     * @param JMDN_code JMDNコード
     * @return エラーメッセージ
     */
    private static String validateJMDN_code(String JMDN_code) {

        if (JMDN_code == null || JMDN_code.equals("")) {
            return MessageConst.E_NOJMDN_CODE.getMessage();
        }

        //入力値に数字以外の文字があればエラーメッセージ
        if (!checkNumeric(JMDN_code)) {
            return MessageConst.E_JMDN_code_ERR.getMessage();
        }
        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 一般的名称のフォームに入力値がなければエラーメッセージを返却
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
     * 販売名のフォームに入力値がなければエラーメッセージを返却
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

    /**
     * 入力値（引数）が英数字であればTrueを返す。入力値チェックに使用。
     * @param name 検証する文字列
     * @return Boolean型
     */
    private static Boolean checkAlphanumeric(String name) {
        // 正規表現パターンを用意する
        String regex_num = "^[A-Za-z0-9]+$";
        Pattern p1 = Pattern.compile(regex_num); //正規表現パターンの読み込み
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
        String regex_num = "^[0-9]+$";
        Pattern p1 = Pattern.compile(regex_num); //正規表現パターンの読み込み
        Matcher m1 = p1.matcher(num); //パターンと検査対象文字列の照合
        boolean result = m1.matches(); //照合結果をtrueまたはfalseで取得する

        return result;
    }

    /**
     * 検査の可否が正しく入力されているか検証
     * @param acceptability_of_exam 各検査の可否
     * @return Boolean型（正しく入力されている場合はtrue）
     */
    public static Boolean validateExamAcceptability(String acceptability_of_exam) {
        // 検査の可否が可能・条件付き可能・不可能のいずれかでない場合はFalseを返却
        if (!(acceptability_of_exam.equals(AttributeConst.PACK_EXM_SAFE.getValue())
                || acceptability_of_exam.equals(AttributeConst.PACK_EXM_CONDITIONAL_SAFE.getValue())
                || acceptability_of_exam.equals(AttributeConst.PACK_EXM_UNSAFE.getValue()))) {
            return false;
        }
        return true;
    }

}

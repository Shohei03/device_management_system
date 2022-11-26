package validators;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Part;

import constants.MessageConst;

/**
 * CSVファイルののバリデーションを行うクラス
 *
 */
public class CsvValidator {

    /**
     * アップロードファイルのサイズ（100MB以下）・ファイル形式（CSV）を検証
     * @param filePart アップロードファイル
     * @return エラーリスト
     */
    public static List<String> validate(Part filePart) {

        List<String> errors = new ArrayList<String>();

        //ファイル形式がCSVファイルでない場合、エラーメッセージを返す
        if (!filePart.getContentType().equals("text/csv")) {
            errors.add(MessageConst.E_UPLOAD_NOT_CSV.getMessage());
        }

        //ファイルサイズが100MBを超える場合、エラーメッセージを返す
        if (filePart.getSize() > 100 * 1024 * 1024) {
            errors.add(MessageConst.E_UPLOAD_DATA_OVER.getMessage());
        }

        return errors;
    }

    /**
     * 添付文書CSVファイルの項目数を検証（引数で指定した個数以外ならエラーメッセージ）
     * @param data CSV取り込み配列データ
     * @return エラーメッセージ
     */

    public static String validateItemNum(String[] data, int checkNum) {

        //CSV取り込み項目の個数
        int itemNum = data.length;

        if (itemNum != checkNum || validateBlank(data)) {
            return MessageConst.E_UPLOAD_DATA_NUM.getMessage();
        }
        return null;
    }

    /**
     * CSVで取り込んだ日付のデータが（yyyy/MM/dd）の形式であればTrueを返す。CSV取り込みデータのチェックに使用。
     * @param name 検証する文字列
     * @return エラーメッセージ
     */
    public static String checkDate(String date) {

        // 正規表現パターンを用意する
        String regexNum = "\\d{4}/\\d{1,2}/\\d{1,2}";
        Pattern p1 = Pattern.compile(regexNum); //正規表現パターンの読み込み
        Matcher m1 = p1.matcher(date); //パターンと検査対象文字列の照合
        boolean result = m1.matches(); //照合結果をtrueまたはfalseで取得する

        //照合結果がFalseの場合、エラーメッセージを返却
        if (result) {
            return null;
        } else {
            return MessageConst.E_UPLOAD_DATE.getMessage();
        }
    }

    /**
     * CSVで取り込んだ時間のデータが（HH:mm::ss）の形式であればTrueを返す。CSV取り込みデータのチェックに使用。
     * @param name 検証する文字列
     * @return エラーメッセージ
     */
    private static String checkTime(String time) {

        // 正規表現パターンを用意する
        String regexNum = "\\d{1,2}:\\d{1,2}";
        Pattern p1 = Pattern.compile(regexNum); //正規表現パターンの読み込み
        Matcher m1 = p1.matcher(time); //パターンと検査対象文字列の照合
        boolean result = m1.matches(); //照合結果をtrueまたはfalseで取得する

        //照合結果がFalseの場合、エラーメッセージを返却
        if (result) {
            return null;
        } else {
            return MessageConst.E_UPLOAD_TIME.getMessage();
        }
    }

    /**
     * CSV取り込みデータの時間の入力チェック（予約時間が9時から17時であるか確認）を行い、エラーを返却
     * reservationTime 予約時間
     * return エラー内容
     */
    public static String validateReservationTime(String time) {

        //入力値がなければエラーメッセージを返却
        if (time == null) {
            return MessageConst.E_NORESERVATION_TIME.getMessage();
        }

        //時間入力形式の検証
        String timeFormaterror = checkTime(time);

        if (timeFormaterror != null) {
            return timeFormaterror;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("[]H:[]m");
        LocalTime reservationTime = LocalTime.parse(time, dtf);

        //予約時間が9時から17時でない場合にエラーメッセージを返却
        LocalTime time9 = LocalTime.of(9, 00, 00); //9時
        LocalTime time17 = LocalTime.of(17, 00, 00); //17時

        //予約時間が9時から17時でない場合にエラーメッセージを返却
        if (reservationTime.isBefore(time9) || reservationTime.isAfter(time17)) {
            return MessageConst.E_RESERVATION_TIME.getMessage();
        }
        //エラーがない場合、nullを返却
        return null;
    }

    /**
     * CSVデータの空白・入力漏れチェック
     * @param datas CSVファイルの1行あたりのデータ
     * @return Boolean型（入力漏れあればtrueを返す）
     */
    private static Boolean validateBlank(String[] data) {
        for (String d : data) {
            if (d == null || d.equals("")) {
                return true;
            }
        }
        return false;
    }
}

package validators;

import java.util.ArrayList;
import java.util.List;

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
     * 添付文書CSVファイルの項目数を検証（9個以外ならエラーメッセージ）
     * @param data CSV取り込み配列データ
     * @return エラーメッセージ
     */

    public static String validate_PACK(String[] data) {

        //CSV取り込み項目の個数
        int item_num = data.length;

        if (item_num != 9) {
            return MessageConst.E_UPLOAD_DATA_NUM.getMessage();
        }
        return null;
    }

    /**
     * 複数レコードのCSV取り込み時の項目数検証（9個以外ならエラーメッセージ）
     * @param data CSV取り込み配列データ
     * @param line_num 取り込み行数
     * @return エラーメッセージ
     */

    public static String validateMultiplePACK(String[] data, int line_num) {

        //CSV取り込み項目の個数
        int item_num = data.length;

        if (item_num != 9) {
            return MessageConst.E_UPLOAD_DATA_NUM.getMessage();
        }
        return null;

    }




}

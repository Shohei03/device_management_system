package constants;

/**
 * 各出力メッセージを定義するEnumクラス
 *
 */
public enum MessageConst {

    //認証
    I_LOGINED("ログインしました"),
    E_LOGINED("ログインに失敗しました。"),
    I_LOGOUT("ログアウトしました。"),

    //DB更新
    I_REGISTERED("登録が完了しました。"),
    I_UPDATED("更新が完了しました。"),
    I_DELETED("削除が完了しました。"),

    //確認
    I_CHECK("内容を確認しよろしければ登録してください。間違いがあれば修正して登録してください。"),

    //バリデーション
    E_NONAME("氏名を入力してください。"),
    E_NOPASSWORD("パスワードを入力してください。"),
    E_NOEMP_CODE("社員番号を入力してください。"),
    E_EMP_CODE_EXIST("入力された社員番号の情報は既に存在しています。"),
    E_NOAPPROVAL_NUM("添付文書の承認番号を入力してください。"),
    E_NOJMDN_CODE("添付文書のJMDNコードを入力してください。"),
    E_NOGENERAL_NAME("添付文書の一般的名称を入力してください。"),
    E_NODEVICE_NAME("添付文書のデバイス名（販売名）を入力してください。"),
    E_DEVICE_EXIST("入力されたデバイスは登録済みです。"),
    E_NODATA("データが選択されていません。"),
    E_NOPAT_ID("患者IDを入力してください。"),
    E_NOPAT_NAME("患者名を入力してください。"),
    E_NOIMP_DATE("デバイスの埋込日を入力してください。"),
    E_NOEXAM_DATE("検査日を入力してください。"),
    E_NORESERVATION_TIME("予約時間を入力してください。"),
    E_ABNORMAL_ExamAcceptability("検査の可否の値は可能・条件付き可能・不可能のいずれかにしてください。"),
    E_PAT_ID_NOT_EIGHT_INT("患者IDを8桁以下の正の整数値で入力してください。"),
    E_PAT_ID_EXIST("入力された患者IDは既に存在しています。"),
    E_APP_NUM_ERR("添付文書承認番号に英数字以外の値が入っています。確認してください。"),
    E_NOAPP_NUM_IN_PACK_DATABASE("入力した添付文書承認番号のデバイスはまだ登録されていません。さきに添付文書情報を登録してください。"),
    E_JMDN_code_ERR("JMDNコードに数字以外の値が入力されています。修正してください。"),
    E_ERR_DEVICE_NAME("指定した添付文書承認番号で登録されているデバイス名と入力されているデバイス名が異なります。"),
    E_ERR_IMP_DATE("正しい埋込日を入力してください"),
    E_EXAM_DATE("正しい検査日を入力してください。"),
    E_RESERVATION_TIME("予約時間が9時から17時になるように修正してください"),
    E_DUPLI_DATA("入力したデータは既に登録されていますが、登録しますか。"),
    E_UPLOAD_NOT_CSV("CSVファイルをアップロードしてください"),
    E_CSV_BLANK("CSVファイルのデータに入力漏れがあります。確認してください"),
    E_UPLOAD_DATA_OVER("CSVファイルのサイズを100MB以下にしてください。"),
    E_UPLOAD_DATA_NUM("CSVファイルデータの項目数が異常または、データの入力漏れがあります。"),
    E_UPLOAD_DATE("CSVファイルの日付データはyyyy/MM/ddの形式で入力してください。"),
    E_UPLOAD_TIME("CSVファイルの時間データはHH:mmの形式で入力してください。"),
    E_WRONG_PAT_NAME("患者名（ひらながも）を確認してください。入力した患者IDで登録されている名前と異なります。"),


    //注意
    C_PACEMAKER("ペースメーカに対して、X線を5秒間連続で照射しないでください！！"),
    C_PACEMAKER_TV("パルス透視を使用する場合、ペースメーカ本体にX線束を照射しないでください。");




    /**
     * 文字列
     */
    private final String text;

    /**
     * コンストラクタ
     */
    private MessageConst(final String text) {
        this.text = text;
    }

    /**
     * 値(文字列)取得
     */
    public String getMessage() {
        return this.text;
    }

}

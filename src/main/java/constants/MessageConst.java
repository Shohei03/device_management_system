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
    E_NOIMP_DATE("デバイスの埋込日を入力してください"),
    E_PAT_ID_NOT_EIGHT_INT("患者IDを8桁以下の正の整数値で入力してください。"),
    E_PAT_ID_EXIST("入力された患者IDは既に存在しています。"),
    E_NOAPP_NUM_IN_PACK_DATABASE("入力した添付文書承認番号のデバイスはまだ登録されていません。さきに添付文書情報を登録してください。"),
    E_ERR_DEVICE_NAME("指定した添付文書承認番号で登録されているデバイス名と入力されているデバイス名が異なります。"),
    E_ERR_IMP_DATE("正しい埋込日を入力してください");



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

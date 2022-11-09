package constants;

/**
 *
 * リクエストパラメータの変数名、変数値、jspファイルの名前等画面推移に関わる値を定義するEnumクラス
 *
 */

public enum ForwardConst {

    //action
    ACT("action"),
    ACT_EMP("Employee"),
    ACT_AUTH("Auth"),
    ACT_SEARCH("Searcher"),
    ACT_REGI_TOP("RegisterTop"),
    ACT_PACK("PackageInsert"),

    //command
    CMD("command"),
    CMD_NONE(""),
    CMD_INDEX("index"),
    CMD_SHOW("show"),
    CMD_SHOW_LOGIN("showLogin"),
    CMD_LOGIN("login"),
    CMD_LOGOUT("logout"),
    CMD_NEW("entryNew"),
    CMD_CREATE("create"),
    CMD_EDIT("edit"),
    CMD_UPDATE("update"),
    CMD_DESTROY("destroy"),

    //jsp
    FW_ERR_UNKNOWN("error/unknown"),
    FW_LOGIN("login/login"),
    FW_EMP_INDEX("employees/index"),
    FW_EMP_SHOW("employees/show"),
    FW_EMP_NEW("employees/new"),
    FW_EMP_EDIT("employees/edit"),
    FW_SEARCH_INDEX("searches/index"),
    FW_REGI_TOP_INDEX("regi_topPage/index"),
    FW_PACK_INDEX("packageInserts/index"),
    FW_PACK_NEW("packageInserts/new");


    /**
     * 文字列
     */
    private final String text;

    /**
     * コンストラクタ
     */
    private ForwardConst(final String text) {
        this.text = text;
    }

    /**
     * 値(文字列)取得
     */
    public String getValue() {
        return this.text;
    }

}



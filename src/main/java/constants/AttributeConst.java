package constants;


/**
 *
 * 画面の項目値等を定義するEnumクラス
 *
 */

public enum AttributeConst {

    //フラッシュメッセージ
    FLUSH("flush"),

    //一覧画面共通
    MAX_ROW("maxRow"),
    PAGE("page"),

    //入力フォーム共通
    TOKEN("_token"),
    ERR("errors"),

    //ログイン中の従業員
    LOGIN_EMP("login_employee"),

    //ログイン画面
    LOGIN_ERR("loginError"),

  //従業員管理
    EMPLOYEE("employee"),
    EMPLOYEES("employees"),
    EMP_COUNT("employees_count"),
    EMP_ID("id"),
    EMP_CODE("code"),
    EMP_PASS("password"),
    EMP_NAME("name"),
    EMP_DEPARTMENT("department"),
    EMP_ADMIN_FLG("admin_flag"),

    //部署
    DEP_GENERAL("一般検査室"),
    DEP_CT("CT検査室"),
    DEP_MRI("MR検査室"),
    DEP_MAMMA("乳房検査室"),
    DEP_TV("TV検査室"),

    //添付文書
    PACKAGE_INSERT("packegeInsert"),
    PACKEGE_INSERTS("packageInserts"),
    PACK_ID("id"),
    PACK_APP_NUM("approval_number"),
    PACK_JMDN("JMDN_code"),
    PACK_GENERAL_NAME("general_name"),
    PACK_DEV_NAME("device_name"),
    PACK_MR_STR("MR_magnetic_field_strength"),
    PACK_Manma("acceptability_of_Manma_exam"),
    PACK_X_RAY("acceptability_of_X_ray_exam"),
    PACK_CT("acceptability_of_CT_exam"),
    PACK_TV("acceptability_of_TV_exam"),
    PACK_MRI("acceptability_of_MR_exam"),
    PACK_DATE("packageInsert_created_date"),
    PACK_COUNT("packageInsertsCount"),



    //管理者フラグ
    ROLE_ADMIN(2),
    ROLE_PARTIAL_ADMIN(1),
    ROLE_GENERAL(0),

    //削除フラグ
    DEL_FLAG_TRUE(1),
    DEL_FLAG_FALSE(0);

    private final String text;
    private final Integer i;

    private AttributeConst(final String text) {
        this.text = text;
        this.i = null;
    }

    private AttributeConst(final Integer i) {
        this.text = null;
        this.i = i;
    }

    public String getValue() {
        return this.text;
    }

    public Integer getIntegerValue() {
        return this.i;
    }

}

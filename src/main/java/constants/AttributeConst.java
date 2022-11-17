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
    PACKAGE_INSERT("packageInsert"),
    PACKAGE_INSERT_LIST("packageInsertList"),
    PACKEGE_INSERTS("packageInserts"),
    PACK_ID("id"),
    PACK_APP_NUM("approval_number"),  //医療機器承認番号
    PACK_JMDN("JMDN_code"),  //JMDNコード
    PACK_GENERAL_NAME("general_name"),
    PACK_DEV_NAME("device_name"),

    PACK_Manma("acceptability_of_Manma_exam"),
    PACK_X_RAY("acceptability_of_X_ray_exam"),
    PACK_CT("acceptability_of_CT_exam"),
    PACK_TV("acceptability_of_TV_exam"),
    PACK_MRI("acceptability_of_MR_exam"),
    PACK_DATE("packageInsert_created_date"),
    PACK_COUNT("packageInsertsCount"),
    PACK_EXM_SAFE("可能"),  //検査可能
    PACK_EXM_CONDITIONAL_SAFE("条件付き可能"),  //検査条件付き可
    PACK_EXM_UNSAFE("不可能"),  //検査不可
    PACK_CSV("csv"),
    PACK_INDEX("index_num"),
    PACK_ERR("pack_err"),  //エラーが生じたデバイス名

    //患者の体内デバイス
    PATIENT_DEVICE("patientDevice"),
    PATIENT_DEVICES("patientDevices"),
    PATIENT_DEVICE_LIST("patientDeviceList"),
    PATDEV_COUNT("patientDevicesCount"),
    PATDEV_PAT_ID("patient_id"),
    PATDEV_PAT_NAME("patint_name"),
    PATDEV_APP_NUM("approval_number"),
    PATDEV_DEV_NAME("device_name"),
    PATDEV_IMP_DATE("implantedAt"),
    PATDEV_ID("id"),
    PATDEV_CSV("csv"),
    PATDEV_INDEX("index_num"),
    PATDEV_ERR_PAT_NAME("patDev_err_pat_name"),  //CSV取り込み時にエラーが生じた患者名
    PATDEV_DUPLICATE_CHECK("patDev_duplicateCheck"),  //入力データの重複チェック

    //検査情報
    PATIENT_EXAMINATION("patientExamination"),
    PATIENT_EXAMINATIONS("patientExaminations"),
    PATEXAM_ID("id"),
    PATEXAM_COUNT_BY_DAY("patintExaminationsCount_byDay"),
    PATEXAM_PAT_ID("patient_id"),
    PATEXAM_PAT_NAME("patient_name"),
    PATEXAM_PAT_NAME_KANA("patient_name_kana"),
    PATEXAM_DAY("day"),
    PATEXAM_EXAM_ITEM("examination_item"),
    PATEXAM_EXAM_DATE("examination_date"),
    PATEXAM_RESERVATION_TIME("reservation_time"),
    PATEXAM_DUPLICATE_CHECK("duplicateCheck"),  //入力データの重複チェック



    //検査項目
    EXAMINATION("examination"),
    EXAMINATIONS("examinations"),
    EXAM_X_RAY("単純X線検査"),
    EXAM_CT("CT検査"),
    EXAM_TV("X線TV検査"),
    EXAM_Manma("乳腺X線検査"),
    EXAM_MRI("MR検査"),



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

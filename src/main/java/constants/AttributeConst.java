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
    LOGIN_EMP("loginEmployee"),

    //ログイン画面
    LOGIN_ERR("loginError"),

  //従業員管理
    EMPLOYEE("employee"),
    EMPLOYEES("employees"),
    EMP_COUNT("employeesCount"),
    EMP_ID("id"),
    EMP_CODE("code"),
    EMP_PASS("password"),
    EMP_NAME("name"),
    EMP_DEPARTMENT("department"),
    EMP_ADMIN_FLG("adminFlag"),

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
    PACK_APP_NUM("approvalNumber"),  //医療機器承認番号
    PACK_JMDN("jmdnCode"),  //JMDNコード
    PACK_GENERAL_NAME("generalName"),
    PACK_DEV_NAME("deviceName"),
    PACK_Manma("acceptabilityOfManma_exam"),
    PACK_X_RAY("acceptabilityOfXrayExam"),
    PACK_CT("acceptabilityOfCtExam"),
    PACK_TV("acceptabilityOfTvExam"),
    PACK_MRI("acceptabilityOfMrExam"),
    PACK_DATE("packageInsertCreatedDate"),
    PACK_COUNT("packageInsertsCount"),
    PACK_EXM_SAFE("可能"),  //検査可能
    PACK_EXM_CONDITIONAL_SAFE("条件付き可能"),  //検査条件付き可
    PACK_EXM_UNSAFE("不可能"),  //検査不可
    PACK_CSV("csv"),
    PACK_INDEX("indexNum"),
    PACK_ERR("packErr"),  //エラーが生じたデバイス名
    PACK_CSV_ERR_LINE("csvErrorLine"),  //CSV取り込み時に項目数エラーがでたindex番号
    PACK_CSV_ACCEPTABILITY_ERR_LINE("csvAcceptabilityErrorLine"),  //CSV取り込み時に検査可否データエラーがでたindex番号

    //患者の体内デバイス
    PATIENT_DEVICE("patientDevice"),
    PATIENT_DEVICES("patientDevices"),
    PATIENT_DEVICE_LIST("patientDeviceList"),
    PATDEV_COUNT("patientDevicesCount"),
    PATDEV_PAT_ID("patientId"),
    PATDEV_PAT_NAME("patientName"),
    PATDEV_PAT_NAME_KANA("patientNameKana"),
    PATDEV_APP_NUM("approvalNumber"),
    PATDEV_GENERAL_NAME("generalName"),
    PATDEV_DEV_NAME("deviceName"),
    PATDEV_IMP_DATE("implantedAt"),
    PATDEV_ID("id"),
    PATDEV_CSV("csv"),
    PATDEV_INDEX("indexNum"),
    PATDEV_ERR_PAT_NAME("patDevErrPatName"),  //CSV取り込み時にエラーが生じた患者名
    PATDEV_DUPLICATE_CHECK("duplicateCheck"),  //入力データの重複チェック
    PATDEV_CSV_ERR_LINE("csvErrorLine"),  //CSV取り込み時に項目数エラーがでたindex番号
    PATDEV_CSV_DATE_ERR_LINE("csvDateErrorLine"),  //CSV取り込み時に埋込日エラーがでたindex番号

    //検査情報
    PATIENT_EXAMINATION("patientExamination"),
    PATIENT_EXAMINATIONS("patientExaminations"),
    PATEXAM_ID("id"),
    PATEXAM_COUNT_BY_DAY("patientExaminationsCountByDay"),
    PATEXAM_PAT_ID("patientId"),
    PATEXAM_PAT_NAME("patientName"),
    PATEXAM_PAT_NAME_KANA("patientNameKana"),
    PATEXAM_DAY("day"),
    PATEXAM_EXAM_ITEM("examinationItem"),
    PATEXAM_EXAM_DATE("examinationDate"),
    PATEXAM_RESERVATION_TIME("reservationTime"),
    PATEXAM_CSV("csv"),
    PATEXAM_DUPLICATE_CHECK("duplicateCheck"),  //入力データの重複チェック
    PATEXAM_INDEX("indexNum"),
    PATEXAM_ERR_PAT_NAME("errPatientName"),  //CSV取り込み時エラーが生じた患者名
    PATEXAM_CSV_ERR_LINE("csvErrorLine"),  //CSV取り込み時に項目数エラーがでたindex番号
    PATEXAM_CSV_DATE_ERR_LINE("csvDateErrorLine"),  //CSV取り込み時に埋込日エラーがでたindex番号



    //検査項目
    EXAMINATION("examination"),
    EXAMINATIONS("examinations"),
    EXAM_X_RAY("単純X線検査"),
    EXAM_CT("CT検査"),
    EXAM_TV("X線TV検査"),
    EXAM_Manma("乳腺X線検査"),
    EXAM_MRI("MR検査"),
    EXAM_ALL("すべて"),

    //検索機能項目
    SEARCHER("searcher"),
    SEARCHES("searches"),
    SEARCH_ID("id"),
    SEARCH_DEVICE("searchDevices"),
    SEARCHES_BY_EXAM_ITEM("searchesByExamItem"),  //検査項目ごとの検索（患者重複なし）
    SEARCH_DATE("searchDate"),  //検索日
    SEARCH_PAT_ID("patientId"),  //患者ID
    SEARCH_PAT_NAME("patientName"),  //患者名
    SEARCH_PAT_NAME_KANA("patientNameKana"),  //患者名(ひらがな)
    SEARCH_APP_NUM("approvalNumber"),  //医療機器承認番号
    SEARCH_GENERAL_NAME("generalName"),
    SEARCH_DEV_NAME("deviceName"),
    SEARCH_PAT("patient"),  //患者
    SEARCH_IMP_DATE("implantedAt"),
    SEARCH_PAT_DEV_PACKS("patDevPacks"),  //患者の体内デバイス情報（添付文書データつき）
    SEARCHES_COUNT("searchesCount"),  //検索件数
    SEARCH_DEATIL("detail"),
    SEARCH_DEATILS("details"),
    SEARCH_PATDEVS("patientDevices"),
    SEARCH_EXAM_ITEM("examinationItem"),
    SEARCH_EXAM_CONDITION("examinationCondition"),
    SEARCH_NOTSAFE_COUNT("notSafeCount"),
    SEARCH_DEV_PACEMAKER("植込み型心臓ペースメーカ"),




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

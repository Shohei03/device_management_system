package constants;

/**
 *
 * DB関連の項目値を定義するインターフェース
 *
 */

public interface JpaConst {

    //persistence-unit名
    String PERSISTENCE_UNIT_NAME = "device_management_system";

    //データ取得件数の最大値
    int ROW_PER_PAGE = 20; //1ページに表示するレコードの数

    //従業員テーブル
    String TABLE_EMP = "employees"; //テーブル名
    //従業員テーブルカラム
    String EMP_COL_ID = "id"; //id
    String EMP_COL_CODE = "code"; //社員番号
    String EMP_COL_NAME = "name"; //氏名
    String EMP_COL_PASS = "password"; //パスワード
    String EMP_COL_DEPARTMENT = "department"; //部署
    String EMP_COL_ADMIN_FLAG = "admin_flag"; //管理者権限
    String EMP_COL_CREATED_AT = "created_at"; //登録日時
    String EMP_COL_UPDATED_AT = "updated_at"; //更新日時
    String EMP_COL_DELETE_FLAG = "delete_flag"; //削除フラグ

    int ROLE_ADMIN = 2; //管理者権限ON(管理者)
    int ROLE_PARTIAL_ADMIN = 1; //管理者権限ON(デバイスデータの登録削除はできるが、従業員の登録削除まではできない）
    int ROLE_GENERAL = 0; //管理者権限OFF(一般)
    int EMP_DEL_TRUE = 1; //削除フラグON(削除済み)
    int EMP_DEL_FALSE = 0; //削除フラグOFF(現役)

    //添付文書テーブル
    String TABLE_PACK = "package_inserts"; //テーブル名
    //従業員テーブルカラム
    String PACK_COL_ID = "id"; //id
    String PACK_COL_APP_NUM = "approval_number"; //承認番号
    String PACK_COL_JMDN = "JMDN_id"; //添付文書のJMDN_id番号
    String PACK_COL_DEV_NAME = "device_name"; //デバイスの販売名
    String PACK_COL_Manma = "acceptability_of_Manma_exam"; //マンモグラフィ検査の可否
    String PACK_COL_X_RAY = "acceptability_of_X_ray_exam"; //一般（X線）検査の可否
    String PACK_COL_CT = "acceptability_of_CT_exam"; //CT検査の可否
    String PACK_COL_TV = "acceptability_of_TV_exam"; //TV検査の可否
    String PACK_COL_MRI = "acceptability_of_MR_exam"; //MR検査の可否
    String PACK_COL_CREATED_AT = "created_at"; //登録日時"
    String PACK_COL_UPDATED_AT = "updated_at";  //更新日

    //JMDNテーブル
    String TABLE_JMDN = "jmdns"; //テーブル名
    //JMDNテーブルカラム
    String JMDN_COL_ID = "id"; //id
    String JMDN_COL_CODE = "jmdn_code"; //JMDNコード
    String JMDN_COL_GENE_NAME = "general_name"; //一般的名称

    //患者の体内デバイステーブル
    String TABLE_PAT_DEV = "patient_devices"; //テーブル名
    //体内デバイステーブルカラム
    String PAT_DEV_COL_ID = "id"; //id
    String PAT_DEV_COL_APP_NUM = "approval_number"; //承認番号
    String PAT_DEV_COL_PAT_ID = "patient_id"; //患者ID
    String PAT_DEV_DEV_NAME = "device_name"; //デバイスの販売名
    String PAT_DEV_COL_IMPLANTED_AT = "implanted_at"; //デバイスを体に埋めこんだ日
    String PAT_DEV_COL_CREATED_AT = "created_at"; //登録日
    String PAT_DEV_COL_UPDATED_AT = "updated_at"; //更新日
    String PAT_DEV_COL_DELETE_FLAG = "delete_flag"; //削除フラグ

    int PAT_DEL_TRUE = 1; //削除フラグON(削除済み)
    int PAT_DEL_FALSE = 0; //削除フラグOFF(未削除)

    //患者テーブル
    String TABLE_PAT = "patients"; //テーブル名
    //患者テーブルカラム
    String PAT_COL_ID = "id"; //id
    String PAT_COL_PAT_ID = "patient_id"; //患者ID
    String PAT_COL_PAT_NAME ="patient_name";  //患者名
    String PAT_COL_PAT_NAME_KANA = "patient_name_kana";  //患者名（ひらがな）
    String PAT_COL_DELETE_FLAG = "delete_flag"; //削除フラグ

    //検査テーブル
    String TABLE_EXAM = "examinations";  //テーブル名
    //検査項目テーブルカラム
    String EXAM_COL_ID = "id";  //id
    String EXAM_COL_ITEM = "examination_item";  //検査項目

    //患者の検査情報テーブル
    String TABLE_PAT_EXAM = "patient_examinations";  //デーブル名
    //患者の検査情報テーブルカラム
    String PAT_EXAM_COL_ID = "id";  //id
    String PAT_EXAM_COL_EXAM = "examination_id";  //患者さんがうける検査項目id
    String PAT_EXAM_COL_EXAM_DATE = "examination_date";  //検査日
    String PAT_EXAM_COL_RESERVATION_TIME = "reservation_time";  //予約時間
    String PAT_EXAM_COL_PAT_ID = "patient_id";  //患者ID
    String PAT_EXAM_CREATED_AT = "created_at"; //登録日
    String PAT_EXAM_COL_DELETE_FLAG = "delete_flag"; //削除フラグ

    int PAT_EXAM_TRUE = 1; //削除フラグON(削除済み)
    int PAT_EXAM_FALSE = 0; //削除フラグOFF(未削除)




    //Entity名
    String ENTITY_EMP = "employee"; //従業員
    String ENTITY_PACK = "package_insert"; //添付文書
    String ENTITY_JMDN = "jmdn"; //JMDN
    String ENTITY_PAT_DEV = "patient_device";  //患者の体内デバイス
    String ENTITY_PAT = "patient";  //患者
    String ENTITY_PAT_EXAM = "patient_examination";  //患者の検査情報
    String ENTITY_EXAM = "examination";  //検査項目

    //JPQL内パラメータ
    String JPQL_PARM_CODE = "code"; //社員番号
    String JPQL_PARM_PASSWORD = "password"; //パスワード
    String JPQL_PARM_EMPLOYEE = "employee"; //従業員
    String JPQL_PARM_APPROVAL_NUM = "approval_number"; //社員番号
    String JPQL_PARM_JMDN_CODE = "JMDN_code"; //JMDNコード
    String JPQL_PARM_PAT_ID = "patient_id";  //患者ID
    String JPQL_PARM_PATIENT = "patient";  //患者インスタンス
    String JPQL_PARM_EXAM_DATE = "examination_date";  //検査日
    String JPQL_PARM_EXAM_ITEM = "examination_item" ;  //検査項目
    String JPQL_PARM_EXAMINATION = "examination" ;  //検査項目インスタンス

    //NamedQueryの nameとquery
    //全ての従業員をidの降順に取得する
    String Q_EMP_GET_ALL = ENTITY_EMP + ".getAll"; //name
    String Q_EMP_GET_ALL_DEF = "SELECT e FROM Employee AS e ORDER BY e.id DESC"; //query
    //全ての従業員の件数を取得する
    String Q_EMP_COUNT = ENTITY_EMP + ".count";
    String Q_EMP_COUNT_DEF = "SELECT COUNT(e) FROM Employee AS e";
    //社員番号とハッシュ化済パスワードを条件に未削除の従業員を取得する
    String Q_EMP_GET_BY_CODE_AND_PASS = ENTITY_EMP + ".getByCodeAndPass";
    String Q_EMP_GET_BY_CODE_AND_PASS_DEF = "SELECT e FROM Employee AS e WHERE e.deleteFlag = 0 AND e.code = :"
            + JPQL_PARM_CODE + " AND e.password = :" + JPQL_PARM_PASSWORD;
    //指定した社員番号を保持する従業員の件数を取得する
    String Q_EMP_COUNT_REGISTERED_BY_CODE = ENTITY_EMP + ".countRegisteredByCode";
    String Q_EMP_COUNT_REGISTERED_BY_CODE_DEF = "SELECT COUNT(e) FROM Employee AS e WHERE e.code = :" + JPQL_PARM_CODE;

    //全ての添付文書をidの降順で取得する
    String Q_PACK_GET_ALL = ENTITY_PACK + ".getAll";
    String Q_PACK_GET_ALL_DEF = "SELECT e FROM PackageInsert AS e WHERE e.deleteFlag = 0 ORDER BY e.id DESC";
    //全ての添付文書の件数を取得する
    String Q_PACK_COUNT = ENTITY_PACK + ".count";
    String Q_PACK_COUNT_DEF = "SELECT COUNT(e) FROM PackageInsert AS e WHERE e.deleteFlag = 0";
    //指定した添付文書承認番号の件数を取得する
    String Q_PACK_COUNT_REGISTEREDBY_APPROVAL_NUM = ENTITY_PACK + ".countRegisteredByApprovalNum";
    String Q_PACK_COUNT_REGISTEREDBY_APPROVAL_NUM_DEF = "SELECT COUNT(e) FROM PackageInsert AS e WHERE e.approval_number = :"
            + JPQL_PARM_APPROVAL_NUM + " AND e.deleteFlag = 0";
    //指定した添付文書承認番号をもつレコードを取得する
    String Q_PACK_GET_MINE_REGISTEREDBY_APPROVAL_NUM = ENTITY_PACK + ".getMineRegisteredByApprovalNum";
    String Q_PACK_GET_MINE_REGISTEREDBY_APPROVAL_NUM_DEF = "SELECT e FROM PackageInsert AS e WHERE e.approval_number = :"
            + JPQL_PARM_APPROVAL_NUM + " AND e.deleteFlag = 0";


    //指定したJMDN_codeの件数を取得する
    String Q_JMDN_COUNT_REGISTEREDBY_JMDN_CODE = ENTITY_JMDN + ".countRegisteredByJMDN_code";
    String Q_JMDN_COUNT_REGISTEREDBY_JMDN_CODE_DEF = "SELECT COUNT(j) FROM Jmdn AS j WHERE j.JMDN_code = :"
            + JPQL_PARM_JMDN_CODE;
    //指定したJMDN_codeのJMDNレコードを取得する
    String Q_JMDN_GET_MINE_REGISTEREDBY_JMDN_CODE = ENTITY_JMDN + ".getMineRegisteredByJMDN_code";
    String Q_JMDN_GET_MINE_REGISTEREDBY_JMDN_CODE_DEF = "SELECT j FROM Jmdn AS j WHERE j.JMDN_code = :"
            + JPQL_PARM_JMDN_CODE;

    //全ての体内デバイスをidの降順で取得する
    String Q_PAT_DEV_GET_ALL = ENTITY_PAT_DEV + ".getAll";
    String Q_PAT_DEV_GET_ALL_DEF = "SELECT d FROM PatientDevice AS d ORDER BY d.id DESC";
    //全ての体内デバイスの件数を取得する
    String Q_PAT_DEV_COUNT = ENTITY_PAT_DEV + ".count";
    String Q_PAT_DEV_COUNT_DEF = "SELECT COUNT(d) FROM PatientDevice AS d";
    //指定した患者の体内デバイステーブルにあるレコード数を取得する
    String Q_PAT_DEV_COUNT_REGISTEREDBY_PAT = ENTITY_PAT_DEV + ".countRegisteredByPatient";
    String Q_PAT_DEV_COUNT_REGISTEREDBY_PAT_DEF = "SELECT COUNT(p) FROM PatientDevice AS p WHERE p.patient = :" +  JPQL_PARM_PATIENT;
    //指定した患者IDのレコードを取得する
    String Q_PAT_DEV_GET_MINE_REGISTEREDBY_PAT = ENTITY_PAT_DEV + ".getMineRegisteredByPatient";
    String Q_PAT_DEV_GET_MINE_REGISTEREDBY_PAT_DEF = "SELECT p FROM PatientDevice AS p WHERE p.patient = :" + JPQL_PARM_PATIENT;

    //全ての患者を患者idの昇順で取得する
    String Q_PAT_GET_ALL = ENTITY_PAT + ".getAll";
    String Q_PAT_GET_ALL_DEF = "SELECT p FROM Patient AS p ORDER BY p.patient_id";
    //全ての患者の件数を取得する
    String Q_PAT_COUNT = ENTITY_PAT + ".count";
    String Q_PAT_COUNT_DEF = "SELECT COUNT(p) FROM Patient AS p";
    //指定した患者IDのレコード数を取得する
    String Q_PAT_COUNT_REGISTEREDBY_PAT_ID = ENTITY_PAT + ".countRegisteredByPatient_id";
    String Q_PAT_COUNT_REGISTEREDBY_PAT_ID_DEF = "SELECT COUNT(p) FROM Patient AS p WHERE p.patient_id = :" + JPQL_PARM_PAT_ID;
    //指定した患者IDのレコードを取得する
    String Q_PAT_GET_MINE_REGISTEREDBY_PAT_ID = ENTITY_PAT + ".getMineRegisteredByPatient_id";
    String Q_PAT_GET_MINE_REGISTEREDBY_PAT_ID_DEF = "SELECT p FROM Patient AS p WHERE p.patient_id = :" + JPQL_PARM_PAT_ID;


    //検査情報を取得する
    String Q_PAT_EXAM_GET_MINE = ENTITY_PAT_EXAM + ".getMine";
    String Q_PAT_EXAM_GET_MINE_DEF = "SELECT pe FROM PatientExamination AS pe";
    //全検査のレコード数を取得する
    String Q_PAT_EXAM_COUNT_ALL = ENTITY_PAT_EXAM + ".countAll";
    String Q_PAT_EXAM_COUNT_ALL_DEF = "SELECT COUNT(pe) FROM PatientExamination AS pe";
    //指定した検査日の検査情報を取得する
    String Q_PAT_EXAM_GET_MINE_REGISTEREDBY_EXAM_DATE = ENTITY_PAT_EXAM + ".getMineRegisteredByExam_date";
    String Q_PAT_EXAM_GET_MINE_REGISTEREDBY_EXAM_DATE_DEF = "SELECT pe FROM PatientExamination AS pe WHERE pe.examination_date = :" + JPQL_PARM_EXAM_DATE;
    //指定した検査日の全検査のレコード数を取得する
    String Q_PAT_EXAM_COUNT_REGISTEREDBY_EXAM_DATE = ENTITY_PAT_EXAM + ".countRegisteredByExamination_date";
    String Q_PAT_EXAM_COUNT_REGISTEREDBY_EXAM_DATE_DEF = "SELECT COUNT(pe) FROM PatientExamination AS pe WHERE pe.examination_date = :" + JPQL_PARM_EXAM_DATE;
    //検査日と検査項目を指定し、その検査情報を取得する
    String Q_PAT_EXAM_GET_MINE_REGISTEREDBY_EXAM_DATE_AND_ITEM = ENTITY_PAT_EXAM + ".getMineRegisteredByExam_date_and_item";
    String Q_PAT_EXAM_GET_MINE_REGISTEREDBY_EXAM_DATE_AND_ITEM_DEF = "SELECT pe FROM PatientExamination AS pe WHERE pe.examination_date = :" + JPQL_PARM_EXAM_DATE + " and pe.examination = :" + JPQL_PARM_EXAMINATION;
    //検査日と検査項目を指定し、その患者情報を取得する（患者の重複なし）
    String Q_PAT_EXAM_GET_PAT_DISTINCT_PAT_REGISTEREDBY_EXAM_DATE_AND_ITEM = ENTITY_PAT_EXAM + ".getMineDistinctPatientRegisteredByExam_date_and_item";
    String Q_PAT_EXAM_GET_PAT_DISTINCT_PAT_REGISTEREDBY_EXAM_DATE_AND_ITEM_DEF = "SELECT DISTINCT pe.patient FROM PatientExamination AS pe WHERE pe.examination_date = :" + JPQL_PARM_EXAM_DATE + " and pe.examination = :" + JPQL_PARM_EXAMINATION;
    //検査日を指定し、その患者情報を取得する（患者の重複なし）
    String Q_PAT_EXAM_GET_PAT_DISTINCT_PAT_REGISTEREDBY_EXAM_DATE = ENTITY_PAT_EXAM + ".getMineDistinctPatientRegisteredByExam_date";
    String Q_PAT_EXAM_GET_PAT_DISTINCT_PAT_REGISTEREDBY_EXAM_DATE_DEF = "SELECT DISTINCT pe.patient FROM PatientExamination AS pe WHERE pe.examination_date = :" + JPQL_PARM_EXAM_DATE;

    //指定した患者の検査数を取得する
    String Q_PAT_EXAM_COUNT_REGISTEREDBY_PAT = ENTITY_PAT_EXAM + ".countRegisteredByPatient";
    String Q_PAT_EXAM_COUNT_REGISTEREDBY_PAT_DEF = "SELECT COUNT(pe) FROM PatientExamination AS pe WHERE pe.patient = :" + JPQL_PARM_PATIENT;
    //指定した患者の検査情報を取得する
    String Q_PAT_EXAM_GET_MINE_REGISTEREDBY_PAT = ENTITY_PAT_EXAM + ".getMineRegisteredByPatient";
    String Q_PAT_EXAM_GET_MINE_REGISTEREDBY_PAT_DEF = "SELECT pe FROM PatientExamination AS pe WHERE pe.patient = :" + JPQL_PARM_PATIENT;

    //検査項目テーブルから指定した検査項目のレコードを取得する
    String Q_EXAM_GET_MINE_REGISTEREDBY_PAT = ENTITY_EXAM + ".getMineRegisteredByExamination_item";
    String Q_EXAM_GET_MINE_REGISTEREDBY_PAT_DEF = "SELECT e FROM Examination AS e WHERE e.examination_item = :" + JPQL_PARM_EXAM_ITEM;

}

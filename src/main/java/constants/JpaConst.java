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
    String EMP_COL_DEPARTMENT = "department";  //部署
    String EMP_COL_ADMIN_FLAG = "admin_flag"; //管理者権限
    String EMP_COL_CREATED_AT = "created_at"; //登録日時
    String EMP_COL_UPDATED_AT = "updated_at"; //更新日時
    String EMP_COL_DELETE_FLAG = "delete_flag"; //削除フラグ

    int ROLE_ADMIN = 2;  //管理者権限ON(管理者)
    int ROLE_PARTIAL_ADMIN = 1;  //管理者権限ON(デバイスデータの登録削除はできるが、従業員の登録削除まではできない）
    int ROLE_GENERAL = 0; //管理者権限OFF(一般)
    int EMP_DEL_TRUE = 1; //削除フラグON(削除済み)
    int EMP_DEL_FALSE = 0; //削除フラグOFF(現役)


    //添付文書テーブル
    String TABLE_PACK = "package_inserts";  //テーブル名
    //従業員テーブルカラム
    String PACK_COL_ID = "id";  //id
    String PACK_COL_APP_NUM = "approval_number";  //承認番号
    String PACK_COL_JMDN = "JMDN_id"; //添付文書のJMDN_id番号
    String PACK_COL_DEV_NAME = "device_name";  //デバイスの販売名
    String PACK_COL_MR_STR = "MR_magnetic_field_strength";  //MRI静磁場強度の制限値
    String PACK_COL_MR_GRADI = "MR_gradient_magnetic_field";  //MR傾斜磁場強度の制限値
    String PACK_COL_MR_SAR = "MR_SAR";  //MR SAR制限値
    String PACK_COL_Manma = "acceptability_of_Manma_exam";  //マンモグラフィ検査の可否
    String PACK_COL_X_RAY = "acceptability_of_X_ray_exam";  //一般（X線）検査の可否
    String PACK_COL_CT = "acceptability_of_CT_exam";  //CT検査の可否
    String PACK_COL_TV = "acceptability_of_TV_exam";  //TV検査の可否
    String PACK_COL_MRI = "acceptability_of_MR_exam";  //MR検査の可否
    String PACK_COL_CREATED_AT = "created_at"; //登録日時"


    //添付文書テーブル
    String TABLE_JMDN = "JMDN";  //テーブル名
    //JMDNテーブルカラム
    String JMDN_COL_ID ="id";  //id
    String JMDN_COL_CODE = "JMDN_code"; //JMDNコード
    String JMDN_COL_GENE_NAME = "general name";  //一般的名称



    //Entity名
    String ENTITY_EMP = "employee"; //従業員
    String ENTITY_PACK = "package_insert";  //添付文書
    String ENTITY_JMDN = "JMDN";  //JMDN

    //JPQL内パラメータ
    String JPQL_PARM_CODE = "code"; //社員番号
    String JPQL_PARM_PASSWORD = "password"; //パスワード
    String JPQL_PARM_EMPLOYEE = "employee"; //従業員
    String JPQL_PARM_APPROVAL_NUM = "approval_number"; //社員番号
    String JPQL_PARM_JMDN_CODE = "JMDN_code"; //JMDNコード

    //NamedQueryの nameとquery
    //全ての従業員をidの降順に取得する
    String Q_EMP_GET_ALL = ENTITY_EMP + ".getAll"; //name
    String Q_EMP_GET_ALL_DEF = "SELECT e FROM Employee AS e ORDER BY e.id DESC"; //query
    //全ての従業員の件数を取得する
    String Q_EMP_COUNT = ENTITY_EMP + ".count";
    String Q_EMP_COUNT_DEF = "SELECT COUNT(e) FROM Employee AS e";
    //社員番号とハッシュ化済パスワードを条件に未削除の従業員を取得する
    String Q_EMP_GET_BY_CODE_AND_PASS = ENTITY_EMP + ".getByCodeAndPass";
    String Q_EMP_GET_BY_CODE_AND_PASS_DEF = "SELECT e FROM Employee AS e WHERE e.deleteFlag = 0 AND e.code = :" + JPQL_PARM_CODE + " AND e.password = :" + JPQL_PARM_PASSWORD;
    //指定した社員番号を保持する従業員の件数を取得する
    String Q_EMP_COUNT_REGISTERED_BY_CODE = ENTITY_EMP + ".countRegisteredByCode";
    String Q_EMP_COUNT_REGISTERED_BY_CODE_DEF = "SELECT COUNT(e) FROM Employee AS e WHERE e.code = :" + JPQL_PARM_CODE;

    //全ての添付文書をidの降順で取得する
    String Q_PACK_GET_ALL = ENTITY_PACK + ".getAll";
    String Q_PACK_GET_ALL_DEF = "SELECT e FROM PackageInsert AS e ORDER BY e.id DESC";
    //全ての添付文書の件数を取得する
    String Q_PACK_COUNT = ENTITY_PACK + ".count";
    String Q_PACK_COUNT_DEF = "SELECT COUNT(e) FROM PackageInsert AS e";
    //指定した添付文書承認番号の件数を取得する
    String Q_PACK_COUNT_REGISTEREDBY_APPROVAL_NUM = ENTITY_PACK + ".countRegisteredByApprovalNum";
    String Q_PACK_COUNT_REGISTEREDBY_APPROVAL_NUM_DEF = "SELECT COUNT(e) FROM PackageInsert AS e WHERE e.approval_number = :" + JPQL_PARM_APPROVAL_NUM;

    //指定したJMDNの件数を取得する
    String Q_JMDN_COUNT_REGISTEREDBY_JMDN_CODE = ENTITY_JMDN + ".countRegisteredByJMDN_code";
    String Q_JMDN_COUNT_REGISTEREDBY_JMDN_CODE_DEF = "SELECT COUNT(j) FROM JMDN AS j WHERE j.JMDN_code = :" + JPQL_PARM_JMDN_CODE;
    //指定したJMDN_codeのJMDNレコードを取得する
    String Q_JMDN_GET_MINE_REGISTEREDBY_JMDN_CODE = ENTITY_JMDN + ".getMineRegisteredByJMDN_code";
    String Q_JMDN_GET_MINE_REGISTEREDBY_JMDN_CODE_DEF = "SELECT j FROM JMDN AS j WHERE j.JMDN_code = :" + JPQL_PARM_JMDN_CODE;


}

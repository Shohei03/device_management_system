package services;

import java.time.LocalDate;
import java.util.List;

import actions.views.PatientConverter;
import actions.views.PatientDeviceConverter;
import actions.views.PatientDeviceView;
import actions.views.SearchConverter;
import actions.views.SearchPatientDeviceView;
import constants.JpaConst;
import models.Patient;
import models.PatientDevice;
import models.validators.PatientDeviceValidator;

/**
 *
 * 体内デバイス（PatientDevice）と患者情報（Patient）テーブルの操作に関わる処理を行うクラス
 *
 */

public class PatientDeviceService extends ServiceBase {
    /**
     * 指定されたページ数の一覧画面に表示する体内デバイスデータ（添付文書データつき）を取得し、SearchPatientDeviceViewのリストで返却する
     * @param page ページ数
     */
    public List<SearchPatientDeviceView> getDevAndPackPerPage(int page) {
        List<PatientDevice> pdList = em.createNamedQuery(JpaConst.Q_PAT_DEV_GET_ALL, PatientDevice.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();

        return SearchConverter.toViewList(pdList);

    }

    /**
     * 指定されたページ数の一覧画面に表示する体内デバイスデータを取得し、PatientDeviceViewのリストで返却する
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<PatientDeviceView> getAllPerPage(int page) {
        List<PatientDevice> patientDeviceList = em.createNamedQuery(JpaConst.Q_PAT_DEV_GET_ALL, PatientDevice.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();

        return PatientDeviceConverter.toViewList(patientDeviceList);
    }

    /**
     * 体内デバイスデータの全件数を取得し、返却する
     * @return 体内デバイスデータの件数
     */
    public long countAll() {
        long patientDeviceCount = (long) em.createNamedQuery(JpaConst.Q_PAT_DEV_COUNT, Long.class).getSingleResult();

        return patientDeviceCount;
    }

    /**
     * idを条件に取得したデータをPatientDeviceViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public PatientDeviceView findOne(int id) {

        return PatientDeviceConverter.toView(findOneInternal(id));
    }

    /**
     * idを条件に体内デバイス（patientDeviceインスタンス）データを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private PatientDevice findOneInternal(int id) {

        return em.find(PatientDevice.class, id);
    }

    /**
     * PatientDevice（体内デバイス）テーブルから、引数で指定した患者インスタンスのレコード数を取得し、返却する
     * @param pat Patient 患者インスタンス
     * @return 該当するデータの件数
     */
    public long countPatDevByPatientId(Patient pat) {

        //指定した体内デバイスの件数を取得する
        long patientDeviceCount = (long) em.createNamedQuery(JpaConst.Q_PAT_DEV_COUNT_REGISTEREDBY_PAT, Long.class)
                .setParameter(JpaConst.JPQL_PARM_PATIENT, pat)
                .getSingleResult();

        return patientDeviceCount;
    }

    /**
     * PatientDevice（体内デバイス）テーブルから、引数で指定した患者のデバイスインスタンスのレコードを取得。
     * @param pat Patient 患者インスタンス
     * return 指定した患者インスタンス（患者ID）をもつpatientDeviceインスタンス
     */
    public List<PatientDevice> findAllPatDevbyPatient(Patient pat) {

        List<PatientDevice> patDevList = null;

        patDevList = em.createNamedQuery(JpaConst.Q_PAT_DEV_GET_MINE_REGISTEREDBY_PAT, PatientDevice.class)
                .setParameter(JpaConst.JPQL_PARM_PATIENT, pat)
                .getResultList();

        return patDevList;
    }

    /**
     * Patient(患者)テーブルから、引数で指定した患者IDのレコード数を取得し、返却する
     * @param patientId 患者ID
     * @return 該当するデータの件数
     */
    public long countByPatientId(int patientId) {

        //指定した患者IDの件数を取得する
        long patientIdCount = (long) em.createNamedQuery(JpaConst.Q_PAT_COUNT_REGISTEREDBY_PAT_ID, Long.class)
                .setParameter(JpaConst.JPQL_PARM_PAT_ID, patientId)
                .getSingleResult();

        return patientIdCount;
    }

    /**
     * Patient(患者)テーブルから、引数で指定した患者IDのレコードを取得。
     * @param patientId 患者id
     * return 指定した患者IDをもつpatientインスタンス
     */
    public Patient findPatient(int patientId) {
        if (countByPatientId(patientId) > 0) {

            Patient pat = (Patient) em.createNamedQuery(JpaConst.Q_PAT_GET_MINE_REGISTEREDBY_PAT_ID, Patient.class)
                    .setParameter(JpaConst.JPQL_PARM_PAT_ID, patientId)
                    .getSingleResult();

            return pat;
        }
        return null;
    }

    /**
     * 画面に入力された体内デバイスの登録内容を元にデータを1件作成し、体内デバイステーブルと患者テーブルに登録する
     * @param pdv 体内デバイスの登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(PatientDeviceView pdv, Boolean duplicateCheck) {

        PackageInsertService packService = new PackageInsertService();

        //各項目の値を検証
        List<String> errors = PatientDeviceValidator.validate(packService, this, pdv, false, duplicateCheck);

        //バリデーションエラーがなければデータを登録する
        if (errors.size() == 0) {
            //登録日と更新日に今日の日付を登録する
            LocalDate ldt = LocalDate.now();
            pdv.setCreatedAt(ldt);
            pdv.setUpdatedAt(ldt);

            //患者デバイステーブルと患者テーブルにデータを登録する。
            //体内デバイステーブルの患者情報（患者IDと患者名）は重複するため、患者テーブルの患者IDコードに一意制約を設け、リレーションを組んでいる。
            //まず、患者テーブルに、これから登録する患者IDが存在していないか確認。存在している場合は登録しない。
            if (!(countByPatientId(pdv.getPatientId()) > 0)) {
                createInternalPatient(pdv);
            }
            createInternalPatDevice(pdv);

        }
        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * 画面から入力された患者デバイスの登録内容をもとに、患者デバイステーブルを更新する。
     * @param pdv patientDeviceView 体内デバイス情報の更新内容
     * @return エラー内容
     */
    public List<String> update(PatientDeviceView pdv, Boolean duplicateCheck) {
        PatientDeviceView savedPdv = findOne(pdv.getId());

        savedPdv.setApprovalNumber(pdv.getApprovalNumber());
        savedPdv.setDeviceName(pdv.getDeviceName());
        savedPdv.setImplantedAt(pdv.getImplantedAt());

        //更新日に現在の日付を設定する
        LocalDate today = LocalDate.now();
        savedPdv.setUpdatedAt(today);

        PackageInsertService packService = new PackageInsertService();

        //バリデーションを行う
        List<String> errors = PatientDeviceValidator.validate(packService, this, savedPdv, false, duplicateCheck);

        //エラーがなければ更新
        if (errors.size() == 0) {
            //デバイス情報のみ編集可能とする（患者情報は編集しない）
            updateInternal(savedPdv);
        }
        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     *患者データを1件登録する
     *@param  pdv PatientDeviceView 体内デバイスの登録内容
     */
    private void createInternalPatient(PatientDeviceView pdv) {

        em.getTransaction().begin();
        em.persist(PatientConverter.toModel(pdv));
        em.getTransaction().commit();
    }

    /**
     *体内デバイスを1件登録する
     */
    private void createInternalPatDevice(PatientDeviceView pdv) {

        em.getTransaction().begin();
        em.persist(PatientDeviceConverter.toModel(pdv));
        em.getTransaction().commit();
    }

    /**
     * 体内デバイス情報を更新する
     * @param pdv 体内デバイスデータ
     */
    private void updateInternal(PatientDeviceView pdv) {

        PatientDevice pd = findOneInternal(pdv.getId());
        em.getTransaction().begin();
        PatientDeviceConverter.copyViewToModel(pd, pdv);
        em.getTransaction().commit();
    }

    /**
     * idを条件に体内デバイスデータを論理削除する
     * @param id
     */
    public void destroy(Integer id) {

        //idを条件に登録済みの体内デバイス情報を取得する
        PatientDeviceView savedPdv = findOne(id);

        //更新日時に現在時刻を設定する
        LocalDate today = LocalDate.now();
        savedPdv.setUpdatedAt(today);

        //論理削除フラグをたてる
        savedPdv.setDeleteFlag(JpaConst.PAT_DEL_TRUE);

        //更新処理を行う
        updateInternal(savedPdv);

    }

}

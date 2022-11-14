package services;

import java.time.LocalDate;
import java.util.List;

import actions.views.PatientConverter;
import actions.views.PatientDeviceConverter;
import actions.views.PatientDeviceView;
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
     * 指定されたページ数の一覧画面に表示する体内デバイスデータを取得し、PatientDeviceViewのリストで返却する
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<PatientDeviceView> getAllPerPage(int page) {
        List<PatientDevice> patientDevice_list = em.createNamedQuery(JpaConst.Q_PAT_DEV_GET_ALL, PatientDevice.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();

        return PatientDeviceConverter.toViewList(patientDevice_list);
    }

    /**
     * 体内デバイスデータの全件数を取得し、返却する
     * @return 体内デバイスデータの件数
     */
    public long countAll() {
        long patient_device_count = (long) em.createNamedQuery(JpaConst.Q_PAT_DEV_COUNT, Long.class).getSingleResult();

        return patient_device_count;
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
     * Patient(患者)テーブルから、引数で指定した患者IDのレコード数を取得し、返却する
     * @param patient_id 患者ID
     * @return 該当するデータの件数
     */
    public long countByPatient_id(int patient_id) {

        //指定した患者IDの件数を取得する
        long patient_idCount = (long) em.createNamedQuery(JpaConst.Q_PAT_COUNT_REGISTEREDBY_PAT_ID, Long.class)
                .setParameter(JpaConst.JPQL_PARM_PAT_ID, patient_id)
                .getSingleResult();

        return patient_idCount;
    }

    /**
     * Patient(患者)テーブルから、引数で指定した患者IDのレコードを取得。
     * @param patient_id 患者id
     * return 指定した患者IDをもつpatientインスタンス
     */
    public Patient findPatient(int patient_id) {
        Patient pat = (Patient) em.createNamedQuery(JpaConst.Q_PAT_GET_MINE_REGISTEREDBY_PAT_ID, Patient.class)
                .setParameter(JpaConst.JPQL_PARM_PAT_ID, patient_id)
                .getSingleResult();

        return pat;
    }

    /**
     * 画面に入力された体内デバイスの登録内容を元にデータを1件作成し、体内デバイステーブルと患者テーブルに登録する
     * @param pdv 体内デバイスの登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(PatientDeviceView pdv) {

        PackageInsertService pack_service = new PackageInsertService();

        //各項目の値を検証
        List<String> errors = PatientDeviceValidator.validate(pack_service, this, pdv, false);

        //バリデーションエラーがなければデータを登録する
        if (errors.size() == 0) {
            //今日の日付を登録する
            LocalDate ldt = LocalDate.now();
            pdv.setCreatedAt(ldt);
            pdv.setUpdatedAt(ldt);

            //患者デバイステーブルと患者テーブルにデータを登録する。
            //体内デバイステーブルの患者情報（患者IDと患者名）は重複するため、患者テーブルの患者IDコードに一意制約を設け、リレーションを組んでいる。
            //まず、患者テーブルに、これから登録する患者IDが存在していないか確認。存在している場合は登録しない。
            if (!(countByPatient_id(pdv.getPatient_id()) > 0)) {
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
    public List<String> update(PatientDeviceView pdv) {
        PatientDeviceView savedPdv = findOne(pdv.getId());

        savedPdv.setApproval_number(pdv.getApproval_number());
        savedPdv.setDevice_name(pdv.getDevice_name());

        //更新日に現在の日付を設定する
        LocalDate today = LocalDate.now();
        savedPdv.setUpdatedAt(today);

        PackageInsertService pack_service = new PackageInsertService();

        //バリデーションを行う
        List<String> errors = PatientDeviceValidator.validate(pack_service, this, savedPdv, false);

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

}

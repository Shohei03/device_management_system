package services;

import java.time.LocalDate;
import java.util.List;

import actions.views.PatientConverter;
import actions.views.PatientExaminationConverter;
import actions.views.PatientExaminationView;
import constants.JpaConst;
import models.Examination;
import models.Patient;
import models.PatientExamination;
import models.validators.PatientExaminationValidator;

/**
 *
 * 検査情報テーブルに関わる処理を行うクラス
 *
 */
public class PatientExaminationService extends ServiceBase {

    /**
     * 指定されたページ数の一覧画面に表示する検査情報データを取得し、PatientExaminationViewのリストで返却する
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<PatientExaminationView> getExamPerPage(int page) {
        List<PatientExamination> patientExamination_list = em
                .createNamedQuery(JpaConst.Q_PAT_EXAM_GET_MINE, PatientExamination.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();

        return PatientExaminationConverter.toViewList(patientExamination_list);
    }

    /**
     * 全検査件数を取得し、返却する
     * @return 全検査件数
     */
    public long countExamination() {

        long patientExamination_countAll = (long) em
                .createNamedQuery(JpaConst.Q_PAT_EXAM_COUNT_ALL, Long.class)
                .getSingleResult();

        return patientExamination_countAll;
    }

    /**
     * 指定されたページ数の一覧画面に表示する指定日の検査情報データを取得し、PatientExaminationViewのリストで返却する
     * @param page ページ数
     * @param exam_date 指定日
     * @return 一覧画面に表示するデータのリスト
     */
    public List<PatientExaminationView> getExamPerPageInTheExamDate(LocalDate exam_date, int page) {
        List<PatientExamination> patientExamination_list = em
                .createNamedQuery(JpaConst.Q_PAT_EXAM_GET_MINE_REGISTEREDBY_EXAM_DATE, PatientExamination.class)
                .setParameter(JpaConst.JPQL_PARM_EXAM_DATE, exam_date)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();

        return PatientExaminationConverter.toViewList(patientExamination_list);
    }

    /**
     * 指定した検査日の検査件数を取得し、返却する
     * @return 指定日の検査件数
     */
    public long countExamInTheExamDate(LocalDate exam_date) {

        long patientExamination_count = (long) em
                .createNamedQuery(JpaConst.Q_PAT_EXAM_COUNT_REGISTEREDBY_EXAM_DATE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EXAM_DATE, exam_date)
                .getSingleResult();

        return patientExamination_count;

    }

    /**
     * 検査日と検査項目を指定して検査情報データを取得し、PatientExaminationのリストで返却する
     * @param examination_item 検査項目
     * @param exam_date 指定日
     * @return 一覧画面に表示するデータのリスト
     */
    public List<PatientExamination> getTheExamInTheExamDate(LocalDate exam_date, Examination e) {
        List<PatientExamination> patientExamination_list = em
                .createNamedQuery(JpaConst.Q_PAT_EXAM_GET_MINE_REGISTEREDBY_EXAM_DATE_AND_ITEM, PatientExamination.class)
                .setParameter(JpaConst.JPQL_PARM_EXAM_DATE, exam_date)
                .setParameter(JpaConst.JPQL_PARM_EXAMINATION, e)
                .getResultList();

        return patientExamination_list;
    }

    /**
     * 検査日を指定して患者データを取得し、患者のリストで返却する(患者IDの重複なし)
     * @param exam_date 指定日
     * @return 一覧画面に表示するデータのリスト
     */
    public List<Patient> getPatNoDupliInTheExamDate(LocalDate exam_date) {
        List<Patient> patient_list = em
                .createNamedQuery(JpaConst.Q_PAT_EXAM_GET_PAT_DISTINCT_PAT_REGISTEREDBY_EXAM_DATE, Patient.class)
                .setParameter(JpaConst.JPQL_PARM_EXAM_DATE, exam_date)
                .getResultList();

        return patient_list;
    }


    /**
     * 検査日と検査項目を指定して患者データを取得し、患者のリストで返却する(患者IDの重複なし)
     * @param examination_item 検査項目
     * @param exam_date 指定日
     * @return 一覧画面に表示するデータのリスト
     */
    public List<Patient> getPatNoDupliByExamItemInTheExamDate(LocalDate exam_date, Examination e) {
        List<Patient> patient_list = em
                .createNamedQuery(JpaConst.Q_PAT_EXAM_GET_PAT_DISTINCT_PAT_REGISTEREDBY_EXAM_DATE_AND_ITEM, Patient.class)
                .setParameter(JpaConst.JPQL_PARM_EXAM_DATE, exam_date)
                .setParameter(JpaConst.JPQL_PARM_EXAMINATION, e)
                .getResultList();

        return patient_list;
    }


    /**
     * idを条件に取得したデータをPatientExaminationのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public PatientExaminationView findOne(int id) {
        return PatientExaminationConverter.toView(findOneInternal(id));
    }

    /**
     * idを条件に検査情報（PatientExaminationインスタンス）データを1件取得する
     * @param id
     * @return 取得したデータのインスタンス
     */
    private PatientExamination findOneInternal(int id) {

        return em.find(PatientExamination.class, id);
    }

    /**
     * 患者インスタンスを条件に検査情報テーブルから取得したレコードをPatientExaminationインスタンスのリストとして返却する
     * @param p 患者インスタンス
     * @return 取得データのインスタンス
     */
    public List<PatientExamination> findByPat_id(Patient p) {
        if (countPatExamByPatient(p) > 0) {
            List<PatientExamination> patExams = em
                    .createNamedQuery(JpaConst.Q_PAT_EXAM_GET_MINE_REGISTEREDBY_PAT, PatientExamination.class)
                    .setParameter(JpaConst.JPQL_PARM_PATIENT, p)
                    .getResultList();

            return patExams;
        }
        return null;
    }

    /**
     * 検査情報テーブルにある指定した患者IDの件数を取得
     * @param p 患者インスタンス
     * @return 取得したデータのインスタンス
     */
    private long countPatExamByPatient(Patient p) {

        //指定した患者IDの件数を取得する
        long patExam_Count = (long) em.createNamedQuery(JpaConst.Q_PAT_EXAM_COUNT_REGISTEREDBY_PAT, Long.class)
                .setParameter(JpaConst.JPQL_PARM_PATIENT, p)
                .getSingleResult();

        return patExam_Count;
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
        if (countByPatient_id(patient_id) > 0) {

            Patient pat = (Patient) em.createNamedQuery(JpaConst.Q_PAT_GET_MINE_REGISTEREDBY_PAT_ID, Patient.class)
                    .setParameter(JpaConst.JPQL_PARM_PAT_ID, patient_id)
                    .getSingleResult();

            return pat;
        }
        return null;
    }

    /**
     * Examination(検査項目)テーブルから、引数で指定した検査項目のレコードを取得。
     * @param examination_item 検査項目
     * return 指定した検査項目をもつExaminationインスタンス
     */
    public Examination findExamination(String examination_item) {

        Examination e = (Examination) em.createNamedQuery(JpaConst.Q_EXAM_GET_MINE_REGISTEREDBY_PAT, Examination.class)
                .setParameter(JpaConst.JPQL_PARM_EXAM_ITEM, examination_item)
                .getSingleResult();

        return e;

    }

    /**
     * 画面に入力された患者の検査情報の登録内容を元にデータを1件作成し、検査情報テーブルと患者テーブルに登録する
     * @param pdv 患者の検査情報の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(PatientExaminationView pev, Boolean duplicateCheck) {

        //各項目の値を検証
        List<String> errors = PatientExaminationValidator.validate(this, pev, duplicateCheck);

        //バリデーションエラーがなければデータを登録する
        if (errors.size() == 0) {

            //登録日に今日の日付を登録する
            LocalDate ldt = LocalDate.now();
            pev.setCreatedAt(ldt);

            //患者の検査情報テーブルと患者テーブルにデータを登録する
            //まず、患者テーブルにこれから登録する患者IDが存在していないか確認。存在している場合は登録しない。
            if (!(countByPatient_id(pev.getPatient_id()) > 0)) {
                createInternalPatient(pev);
            }
            createInternalPatExam(pev);
        }
        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;

    }

    /**
     * 画面から入力された検査情報の登録内容をもとに、検査情報テーブルを更新する。
     * @param pdv patientExaminationView 検査情報の更新内容
     * @return エラー内容
     */
    public List<String> update(PatientExaminationView pev, Boolean duplicateCheck) {
        PatientExaminationView savedPev = findOne(pev.getId());

        savedPev.setPatient_id(pev.getPatient_id());
        savedPev.setPatient_name(pev.getPatient_name());
        savedPev.setPatient_name_kana(pev.getPatient_name_kana());
        savedPev.setExamination_item(pev.getExamination_item());
        savedPev.setExamination_date(pev.getExamination_date());
        savedPev.setReservation_time(pev.getReservation_time());
        savedPev.setCreatedAt(LocalDate.now());

        //バリデーションを行う
        List<String> errors = PatientExaminationValidator.validate(this, savedPev, duplicateCheck);

        //エラーがなければ更新
        if (errors.size() == 0) {
            updateInternal(savedPev);
        }
        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     *患者データを1件登録する
     *@param pev 検査情報の登録内容
     */
    private void createInternalPatient(PatientExaminationView pev) {

        em.getTransaction().begin();
        em.persist(PatientConverter.toModel_from_PatExamV(pev));
        em.getTransaction().commit();
    }

    /**
     * 患者の検査情報を1件登録する
     * @pev 検査情報の登録内容
     */
    private void createInternalPatExam(PatientExaminationView pev) {

        em.getTransaction().begin();
        em.persist(PatientExaminationConverter.toModel(pev));
        em.getTransaction().commit();
    }

    /**
     * 検査情報を更新する
     * @pev 検査情報の更新内容
     */
    private void updateInternal(PatientExaminationView pev) {

        PatientExamination pe = findOneInternal(pev.getId());
        em.getTransaction().begin();
        PatientExaminationConverter.copyViewToModel(pe, pev);
        em.getTransaction().commit();
    }

    /**
     * idを条件に検査情報を削除する
     * @param id
     */
    public void destroy(Integer id) {

        //idを条件に登録済みの検査情報を取得する
        PatientExamination pe = findOneInternal(id);

        em.getTransaction().begin();
        //指定した検査情報を削除
        em.remove(pe);
        em.getTransaction().commit();
    }

}

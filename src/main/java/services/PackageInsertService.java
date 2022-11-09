package services;

import java.time.LocalDate;
import java.util.List;

import actions.views.PackageInsertConverter;
import actions.views.PackageInsertView;
import constants.JpaConst;
import models.JMDN;
import models.PackageInsert;
import models.validators.PackageInsertValidator;

/**
 * 添付文書テーブルの操作に関わる処理を行うクラス
 */
public class PackageInsertService extends ServiceBase {

    /**
     * 指定されたページ数の一覧画面に表示する添付文書データを取得し、PackageInsertViewのリストで返却する
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<PackageInsertView> getAllPerPage(int page) {
        List<PackageInsert> packageInserts = em.createNamedQuery(JpaConst.Q_PACK_GET_ALL, PackageInsert.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();

        return PackageInsertConverter.toViewList(packageInserts);

    }

    /**
     * 添付文書データの全件数を取得し、返却する
     * @return 添付文書データの件数
     */
    public long countAll() {
        long package_insert_count = (long) em.createNamedQuery(JpaConst.Q_PACK_COUNT, Long.class).getSingleResult();

        return package_insert_count;
    }

    /**
     * idを条件に取得したデータをPackageInsertViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public PackageInsertView findOne(int id) {
        return PackageInsertConverter.toView(findOneInternal(id));
    }

    /**
     * 添付文書の承認番号を条件に該当するデータの件数を取得し、返却する
     * @param approval_number 承認番号
     * @return 該当するデータの件数
     */

    public long countByAapproval_number(String approval_number) {

        //指定した添付文書承認番号の件数を取得する
        long approvalNumCount = (long) em.createNamedQuery(JpaConst.Q_PACK_COUNT_REGISTEREDBY_APPROVAL_NUM, Long.class)
                .setParameter(JpaConst.JPQL_PARM_APPROVAL_NUM, approval_number)
                .getSingleResult();
        return approvalNumCount;
    }

    /**
     * 添付文書のJMDNコードに該当するデータの件数を取得し、返却する
     * @param JMDN_code JMDNコード
     * @return 該当するデータの件数
     */
    public long countByJMDN_CODE(String JMDN_code) {

        //指定したJMDNの件数を取得する
        long JMDN_CODECount = (long) em.createNamedQuery(JpaConst.Q_JMDN_COUNT_REGISTEREDBY_JMDN_CODE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_JMDN_CODE, JMDN_code)
                .getSingleResult();
        return JMDN_CODECount;
    }

    /**
     * 指定されたJMDN_CODEのJMDNインスタンスを取得
     *
     */
    public JMDN findJMDN(String JMDN_code) {
        JMDN j = (JMDN) em.createNamedQuery(JpaConst.Q_JMDN_GET_MINE_REGISTEREDBY_JMDN_CODE, JMDN.class)
                .setParameter(JpaConst.JPQL_PARM_JMDN_CODE, JMDN_code)
                .getSingleResult();

        return j;

    }

    /**
     * 画面から入力された添付文書情報の登録内容を元にデータを1件作成し、添付文書テーブルとJMDNテーブルに登録する
     * @param pv 添付文書情報の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(PackageInsertView pv) {
        List<String> errors = PackageInsertValidator.validate(this, pv, true);
        //バリデーションエラーがなければデータを登録する
        if (errors.size() == 0) {
            LocalDate ldt = LocalDate.now();
            pv.setCreatedAt(ldt);
            createInternal(pv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * 画面から入力された添付文書情報の登録内容を元に、添付文書情報データを更新する
     * @param pv 添付文書情報の更新内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> update(PackageInsertView pv) {
        PackageInsertView savedPack = findOne(pv.getId());

        boolean validateApproval_number = false;

        if (!savedPack.getApproval_number().equals(pv.getApproval_number())) {
            //添付文書番号を更新する場合

            //添付文書番号についてのバリデーションを行う
            validateApproval_number = true;
            //変更後の添付文書番号を設定する
            savedPack.setApproval_number(pv.getApproval_number());

        }

        savedPack.setJMDN_code(pv.getJMDN_code()); //変更後のJMDNコードを設定する
        savedPack.setGeneral_name(pv.getGeneral_name()); //変更後の一般的名称を設定する
        savedPack.setDevice_name(pv.getDevice_name()); //変更後のデバイス名を設定する
        savedPack.setAcceptability_of_MR_exam(pv.getAcceptability_of_MR_exam()); //変更後のMR検査の可否を設定する
        savedPack.setMR_magnetic_field_strength(pv.getMR_magnetic_field_strength()); //変更後のMR静磁場強度制限を設定する

        //登録日に現在の日付を設定する
        LocalDate today = LocalDate.now();
        savedPack.setCreatedAt(today);

        //バリデーションを行う
        List<String> errors = PackageInsertValidator.validate(this, savedPack, validateApproval_number);

        if (errors.size() == 0) {
            updateInternal(pv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * idを条件にデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private PackageInsert findOneInternal(int id) {
        return em.find(PackageInsert.class, id);
    }

    /**
     * 添付文書データを1件登録する
     * @param pv 添付文書データ
     */
    private void createInternal(PackageInsertView pv) {

        em.getTransaction().begin();
        em.persist(PackageInsertConverter.toModel(pv));
        em.getTransaction().commit();

    }

    /**
     * 添付文書データを更新する
     * @param pv 添付文書データ
     */
    private void updateInternal(PackageInsertView pv) {

        em.getTransaction().begin();
        PackageInsert p = findOneInternal(pv.getId());
        PackageInsertConverter.copyViewToModel(p, pv);
        em.getTransaction().commit();

    }

}

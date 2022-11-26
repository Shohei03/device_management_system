package services;

import java.time.LocalDate;
import java.util.List;

import actions.views.JmdnConverter;
import actions.views.PackageInsertConverter;
import actions.views.PackageInsertView;
import constants.JpaConst;
import models.Jmdn;
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

        long packageInsertCount = (long) em.createNamedQuery(JpaConst.Q_PACK_COUNT, Long.class).getSingleResult();

        return packageInsertCount;
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
     * @param approvalNumber 承認番号
     * @return 該当するデータの件数
     */
    public long countByAapprovalNumber(String approvalNumber) {

        //指定した添付文書承認番号の件数を取得する（論理削除されていない。つまり、deleteFlagが0のレコード数を取得）
        long approvalNumCount = (long) em.createNamedQuery(JpaConst.Q_PACK_COUNT_REGISTEREDBY_APPROVAL_NUM, Long.class)
                .setParameter(JpaConst.JPQL_PARM_APPROVAL_NUM, approvalNumber)
                .getSingleResult();
        return approvalNumCount;
    }

    /**
     * 添付文書の承認番号を条件に該当するデータのインスタンスを取得
     *
     */
    public PackageInsert findPackageInsertByAppNum(String approvalNumber) {
        if (countByAapprovalNumber(approvalNumber) > 0) {

            PackageInsert p = (PackageInsert) em
                    .createNamedQuery(JpaConst.Q_PACK_GET_MINE_REGISTEREDBY_APPROVAL_NUM, PackageInsert.class)
                    .setParameter(JpaConst.PAT_DEV_COL_APP_NUM, approvalNumber)
                    .getSingleResult();

            return p;
        }
        return null;

    }

    /**
     * Jmdnテーブルから、引数で指定したJMDNコードのレコード数を取得し、返却する
     * @param jmdnCode JMDNコード
     * @return 該当するデータの件数
     */
    private long countByJmdnCode(String jmdnCode) {

        //指定したJMDNの件数を取得する
        long jmdnCodeCount = (long) em.createNamedQuery(JpaConst.Q_JMDN_COUNT_REGISTEREDBY_JMDN_CODE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_JMDN_CODE, jmdnCode)
                .getSingleResult();

        return jmdnCodeCount;
    }

    /**
     * 指定されたjmdnCodeのJmdnインスタンスを取得
     *
     */
    public Jmdn findJmdn(String jmdnCode) {

        Jmdn j = (Jmdn) em.createNamedQuery(JpaConst.Q_JMDN_GET_MINE_REGISTEREDBY_JMDN_CODE, Jmdn.class)
                .setParameter(JpaConst.JPQL_PARM_JMDN_CODE, jmdnCode)
                .getSingleResult();

        return j;
    }

    /**
     * 画面から入力された添付文書情報の登録内容を元にデータを1件作成し、添付文書テーブルとJMDNテーブルに登録する
     * @param pv 添付文書情報の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(PackageInsertView pv) {

        //各項目の値を検証
        List<String> errors = PackageInsertValidator.validate(this, pv, true);

        //バリデーションエラーがなければデータを登録する
        if (errors.size() == 0) {

            //添付文書テーブルとJmdnテーブルにデータを登録する。
            //添付文書テーブルのJmdnカラムには重複するJMDNコードがあるため、Jmdnテーブルを別に作成し、リレーションを組んでいる。
            //Jmdnテーブルのjmdnコードに一意制約を設け、リレーションを組んでいる。
            //まず、Jmdnテーブルに、これから登録するjmdnコードがないか確認。重複がない場合はJmdnテーブルにも登録。
            if (!(countByJmdnCode(pv.getJmdnCode()) > 0)) {
                createInternalJmdn(pv);
            }
            createInternalPack(pv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * 画面から入力された添付文書情報の登録内容をもとに、添付文書情報データを更新する
     * @param pv 添付文書情報の更新内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> update(PackageInsertView pv) {
        PackageInsertView savedPack = findOne(pv.getId());

        boolean validateApprovalNumber = false;

        if (!savedPack.getApprovalNumber().equals(pv.getApprovalNumber())) {
            //添付文書番号を更新する場合

            //添付文書番号についてのバリデーションを行う
            validateApprovalNumber = true;
            //変更後の添付文書番号を設定する
            savedPack.setApprovalNumber(pv.getApprovalNumber());
        }

        savedPack.setJmdnCode(pv.getJmdnCode()); //変更後のJMDNコードを設定する
        savedPack.setGeneralName(pv.getGeneralName()); //変更後の一般的名称を設定する
        savedPack.setDeviceName(pv.getDeviceName()); //変更後のデバイス名を設定する
        savedPack.setAcceptabilityOfMrExam(pv.getAcceptabilityOfMrExam()); //変更後のMR検査の可否を設定する

        //更新日に現在の日付を設定する
        LocalDate today = LocalDate.now();
        savedPack.setUpdatedAt(today);

        //バリデーションを行う
        List<String> errors = PackageInsertValidator.validate(this, savedPack, validateApprovalNumber);

        //エラーがなければ更新
        if (errors.size() == 0) {
            updateInternalJmdn(savedPack); //
            updateInternal(savedPack); //
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * idを条件に添付文書（PackageInsertインスタンス）データを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private PackageInsert findOneInternal(int id) {
        return em.find(PackageInsert.class, id);
    }

    /**
     * JMDNデータを1件登録する
     * @param pv 添付文書データ
     */
    private void createInternalJmdn(PackageInsertView pv) {

        em.getTransaction().begin();
        em.persist(JmdnConverter.toModel(pv));
        em.getTransaction().commit();

    }

    /**
     * 添付文書データを1件登録する
     * @param pv 添付文書データ
     */
    private void createInternalPack(PackageInsertView pv) {

        em.getTransaction().begin();
        em.persist(PackageInsertConverter.toModel(pv));
        em.getTransaction().commit();

    }

    /**
     * JMDNデータを1件更新する
     * @param pv 添付文書データ
     */
    private void updateInternalJmdn(PackageInsertView pv) {

        if ((countByJmdnCode(pv.getJmdnCode())) == 0) {
            //JMDNコードを変更する場合（JMDNコードがJMDNデータベースにない場合のみ、登録処理）
            em.getTransaction().begin();
            em.persist(JmdnConverter.toModel(pv));
            em.getTransaction().commit();

        } else {
            //JMDNは変更せず、一般的名称のみ変更したいとき
            Jmdn j = findJmdn(pv.getJmdnCode());
            em.getTransaction().begin();
            JmdnConverter.copyViewToModelGeneralName(j, pv);
            em.getTransaction().commit();
        }
    }

    /**
     * 添付文書データを更新する
     * @param pv 添付文書データ
     */
    private void updateInternal(PackageInsertView pv) {
        PackageInsert p = findOneInternal(pv.getId());
        em.getTransaction().begin();
        PackageInsertConverter.copyViewToModel(p, pv);
        em.getTransaction().commit();
    }

    /**
     * idを条件に添付文書レコードを論理削除する
     * @param id
     */
    public void destroy(Integer id) {

        //idを条件に登録済みの体内デバイス情報を取得する
        PackageInsertView savedPiv = findOne(id);

        //更新日時に現在時刻を設定する
        LocalDate today = LocalDate.now();
        savedPiv.setUpdatedAt(today);

        //論理削除フラグをたてる
        savedPiv.setDeleteFlag(JpaConst.PAT_EXAM_TRUE);

        //更新処理を行う
        updateInternal(savedPiv);

    }

}

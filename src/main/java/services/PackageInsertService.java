package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.PackageInsertConverter;
import actions.views.PackageInsertView;
import constants.JpaConst;
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
    public List<PackageInsertView> getAllPage(int page) {
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
     * 画面から入力された添付文書情報の登録内容を元にデータを1件作成し、添付文書テーブルとJMDNテーブルに登録する
     * @param pv 添付文書情報の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(PackageInsertView pv) {
        List<String> errors = PackageInsertValidator.validate(pv);
        if (errors.size() == 0) {
            LocalDateTime ldt = LocalDateTime.now();
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

        //バリデーションを行う
        List<String> errors = PackageInsertValidator.validate(pv);

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

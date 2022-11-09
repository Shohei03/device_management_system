package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.PackageInsertView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.PackageInsertService;

/**
 * 添付文書に関する処理を行うActionクラス
 *
 */
public class PackageInsertAction extends ActionBase {

    private PackageInsertService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {
        service = new PackageInsertService();

        //メソッドを実行
        invoke();
        service.close();
    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {
        //指定されたページ数の一覧画面に表示する添付文書データを取得
        int page = getPage();
        List<PackageInsertView> packageInserts = service.getAllPerPage(page);

        //全添付文書データの件数を取得
        long packageInserts_count = service.countAll();

        putRequestScope(AttributeConst.PACKEGE_INSERTS, packageInserts); //取得した添付文書データ
        putRequestScope(AttributeConst.PACK_COUNT, packageInserts_count); //全ての添付文書データの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_PACK_INDEX);
    }

    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException {
        putRequestScope(AttributeConst.TOKEN, getTokenId());  //CSRF対策トークン

        //添付文書情報の空インスタンスに、登録日時＝今日の日時を設定する
        PackageInsertView pv = new PackageInsertView();
        pv.setCreatedAt(LocalDate.now());
        putRequestScope(AttributeConst.PACKAGE_INSERT, pv);  //日時のみ設定済みの添付文書インスタンス

        //新規登録画面を表示
        forward(ForwardConst.FW_PACK_NEW);



    }

}

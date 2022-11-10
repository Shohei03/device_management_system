package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.PackageInsertView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
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
        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策トークン

        //添付文書情報の空インスタンスに、登録日時＝今日の日時を設定する
        PackageInsertView pv = new PackageInsertView();
        pv.setCreatedAt(LocalDate.now());
        putRequestScope(AttributeConst.PACKAGE_INSERT, pv); //日時のみ設定済みの添付文書インスタンス

        //新規登録画面を表示
        forward(ForwardConst.FW_PACK_NEW);

    }

    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {
        //CSRF対策 tokenのチェック
        if (checkToken()) {
            //日付が入力されていなければ、今日の日付を設定
            LocalDate day = null;
            if (getRequestParam(AttributeConst.PACK_DATE) == null
                    || getRequestParam(AttributeConst.PACK_DATE).equals("")) {
                day = LocalDate.now();
            } else {
                day = LocalDate.parse(getRequestParam(AttributeConst.PACK_DATE));
            }
            //パラメータの値をもとに添付文書のインスタンスを作成する
            PackageInsertView pv = new PackageInsertView(
                    null,
                    getRequestParam(AttributeConst.PACK_APP_NUM),
                    getRequestParam(AttributeConst.PACK_JMDN),
                    getRequestParam(AttributeConst.PACK_GENERAL_NAME),
                    getRequestParam(AttributeConst.PACK_DEV_NAME),
                    getRequestParam(AttributeConst.PACK_Manma),
                    getRequestParam(AttributeConst.PACK_X_RAY),
                    getRequestParam(AttributeConst.PACK_CT),
                    getRequestParam(AttributeConst.PACK_TV),
                    getRequestParam(AttributeConst.PACK_MRI),
                    day);

            //添付文書情報を登録
            List<String> errors = service.create(pv);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合
                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用
                putRequestScope(AttributeConst.PACKAGE_INSERT, pv); //入力された添付文書情報
                putRequestScope(AttributeConst.ERR, errors); //エラーリスト

                //新規登録画面を再表示
                forward(ForwardConst.FW_PACK_NEW);

            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_PACK, ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * 詳細画面を表示
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {
        //idを条件に添付文書データを取得する
        PackageInsertView pv = service.findOne(toNumber(getRequestParam(AttributeConst.PACK_ID)));

        if (pv == null) {
            //該当の添付文書データが存在しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);
        } else {
            putRequestScope(AttributeConst.PACKAGE_INSERT, pv); //取得した添付文書データ

            //詳細画面を表示
            forward(ForwardConst.FW_PACK_SHOW);
        }
    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {
        //idを条件に添付文書データを取得する
        PackageInsertView pv = service.findOne(toNumber(getRequestParam(AttributeConst.PACK_ID)));

        if (pv == null) {
            forward(ForwardConst.FW_ERR_UNKNOWN);
        } else {
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用
            putRequestScope(AttributeConst.PACKAGE_INSERT, pv); //取得した添付文書データ

            //編集画面を表示
            forward(ForwardConst.FW_PACK_EDIT);
        }
    }

    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {
        //CSRF対策 tokenチェック
        if (checkToken()) {
            //idを条件に添付文書データを取得する
            PackageInsertView pv = service.findOne(toNumber(getRequestParam(AttributeConst.PACK_ID)));

            //入力された添付文書内容を設定する
            pv.setCreatedAt(toLocalDate(getRequestParam(AttributeConst.PACK_DATE)));
            pv.setApproval_number(getRequestParam(AttributeConst.PACK_APP_NUM));
            pv.setJMDN_code(getRequestParam(AttributeConst.PACK_JMDN));
            pv.setGeneral_name(getRequestParam(AttributeConst.PACK_GENERAL_NAME));
            pv.setDevice_name(getRequestParam(AttributeConst.PACK_DEV_NAME));
            pv.setAcceptability_of_Manma_exam(getRequestParam(AttributeConst.PACK_Manma));
            pv.setAcceptability_of_X_ray_exam(getRequestParam(AttributeConst.PACK_X_RAY));
            pv.setAcceptability_of_CT_exam(getRequestParam(AttributeConst.PACK_CT));
            pv.setAcceptability_of_TV_exam(getRequestParam(AttributeConst.PACK_TV));
            pv.setAcceptability_of_MR_exam(getRequestParam(AttributeConst.PACK_MRI));

            //添付文書データを更新する
            List<String> errors = service.update(pv);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.PACKAGE_INSERT, pv); //入力された添付文書情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst.FW_PACK_EDIT);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_PACK, ForwardConst.CMD_INDEX);
            }

        }
    }

}

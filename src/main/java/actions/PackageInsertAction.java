package actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;

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
@MultipartConfig
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

        //セッションにエラーメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String error = getSessionScope(AttributeConst.ERR);
        if (error != null) {
            putRequestScope(AttributeConst.ERR, error);
            removeSessionScope(AttributeConst.ERR);
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
                //入力されている場合はその日付を取得
                day = LocalDate.parse(getRequestParam(AttributeConst.PACK_DATE));
            }

            //パラメータの値をもとに添付文書（View）のインスタンスを作成する
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

    /**
     * CSVデータ（1レコード）を取り込む
     * @throws ServletException
     * @throws IOException
     */
    public void csvImport() throws ServletException, IOException {

        Part filePart = getRequestPart(AttributeConst.PACK_CSV);
        PackageInsertView pv = null;

        // csv読み込み
        try (InputStream is = filePart.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");  //shift-JISからUTF-8に変更
                BufferedReader br = new BufferedReader(isr);) {

            String line;

            line = br.readLine();
            String[] data = line.split(",");

            pv = new PackageInsertView(
                    null,
                    data[0].trim(),
                    data[1].trim(),
                    data[2].trim(),
                    data[3].trim(),
                    data[4].trim(),
                    data[5].trim(),
                    data[6].trim(),
                    data[7].trim(),
                    data[8].trim(),
                    LocalDate.now());

        } catch (IOException e) {
            e.printStackTrace();
        }

        //ファイルを読み込めなかった場合の処理
        if (pv == null) {
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用
            putRequestScope(AttributeConst.ERR, "csvファイルを読み込めませんでした");

            //新規作成画面にcsv取り込みデータを表示
            forward(ForwardConst.FW_PACK_NEW);
        }

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用
        putRequestScope(AttributeConst.PACKAGE_INSERT, pv); //取得した添付文書データ

        //新規作成画面にcsv取り込みデータを表示
        forward(ForwardConst.FW_PACK_NEW);
    }

    /**
     * csvデータ（複数レコード）をまとめて取り込む
     * @throws ServletException
     * @throws IOException
     */

    public void csvAllImport() throws ServletException, IOException {

        Part filePart = getRequestPart(AttributeConst.PACK_CSV);
        PackageInsertView pv = null;
        List<PackageInsertView> pv_list = new ArrayList<>();

        // csv読み込み
        try (InputStream is = filePart.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");  //shift-JISからUTF-8に変更
                BufferedReader br = new BufferedReader(isr);) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                pv = new PackageInsertView(
                        null,
                        data[0].trim(),
                        data[1].trim(),
                        data[2].trim(),
                        data[3].trim(),
                        data[4].trim(),
                        data[5].trim(),
                        data[6].trim(),
                        data[7].trim(),
                        data[8].trim(),
                        LocalDate.now());
                pv_list.add(pv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        putSessionScope(AttributeConst.PACKAGE_INSERT_LIST, pv_list);

        //csv取り込みデータ確認画面を表示
        forward(ForwardConst.FW_PACK_CSV_CHECK);
    }

    /**
     * 読み込んだCSVデータの中で不要なレコードを消去
     * @throws ServletException
     * @throws IOException
     */

    public void csvModify() throws ServletException, IOException {

        //セッションからcsvで取り込んだ添付文書データリストを取得
        List<PackageInsertView> pv_list = getSessionScope(AttributeConst.PACKAGE_INSERT_LIST);

        //csvの取り込みをやめたいレコードのindex番号を取得
        int index_num = toNumber(getRequestParam(AttributeConst.PACK_INDEX));

        //データがある場合、セッションからcsvで取り込んだ添付文書リストを削除し、残ったデータリストをセッションに登録。
        if (!(pv_list == null)) {
            //index番号を指定して、csvの取り込みをやめたいレコードを削除
            pv_list.remove(index_num);
            removeSessionScope(AttributeConst.PACKAGE_INSERT_LIST);
            putSessionScope(AttributeConst.PACKAGE_INSERT_LIST, pv_list);
        }

        //csv取り込みデータ確認画面を表示
        forward(ForwardConst.FW_PACK_CSV_CHECK);

    }

    /**
     * 取り込んだCSVデータをデータベースに登録
     * @throws ServletException
     * @throws IOException
     */
    public void csvAllCreate() throws ServletException, IOException {

        //セッションからcsvで取り込んだ添付文書データリストを取得
        List<PackageInsertView> pv_list = getSessionScope(AttributeConst.PACKAGE_INSERT_LIST);

        //リストに添付文書データがない場合、CSV取り込みデータ確認画面に戻る。
        if (pv_list == null) {
            putSessionScope(AttributeConst.ERR, MessageConst.E_NODATA.getMessage());
            //csv取り込みデータ確認画面に戻る
            forward(ForwardConst.FW_PACK_CSV_CHECK);
        }

        //リスト内の添付文書データの項目値をバリデーションし、エラーがあれば、データ確認画面に戻る
        for (PackageInsertView pv : pv_list) {

            //添付文書情報を登録
            List<String> errors = service.create(pv);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合
                putRequestScope(AttributeConst.PACK_ERR, pv.getDevice_name()); //エラーが生じた添付文章のデバイス名
                putRequestScope(AttributeConst.ERR, errors); //エラーリスト

                //csv取り込みデータ確認画面に戻る
                forward(ForwardConst.FW_PACK_CSV_CHECK);
            }
        }

        //登録中にエラーがなかった場合
        //セッションから添付文書リストを削除
        removeSessionScope(AttributeConst.PACKAGE_INSERT_LIST);

        //セッションに登録完了のフラッシュメッセージを設定
        putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

        //一覧画面にリダイレクト
        redirect(ForwardConst.ACT_PACK, ForwardConst.CMD_INDEX);
    }
}

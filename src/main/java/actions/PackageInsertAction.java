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

import actions.views.PackageInsertConverter;
import actions.views.PackageInsertView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import models.PackageInsert;
import models.validators.PackageInsertValidator;
import services.PackageInsertService;
import validators.CsvValidator;

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
        List<String> errors = getSessionScope(AttributeConst.ERR);
        if (errors != null) {
            putRequestScope(AttributeConst.ERR, errors);
            removeSessionScope(AttributeConst.ERR);
        }
        //CSVファイルのエラー行数を格納した変数もセッションから削除する
        List<Integer> err_line_num_list = getSessionScope(AttributeConst.PACK_CSV_ERR_LINE);
        if (err_line_num_list != null) {
            putRequestScope(AttributeConst.PACK_CSV_ERR_LINE, err_line_num_list);
            removeSessionScope(AttributeConst.PACK_CSV_ERR_LINE);
        }

        //CSVファイルのエラー行数(検査の可否)を格納した変数もセッションから削除する
        List<Integer> err_line_acceptability_list = getSessionScope(AttributeConst.PACK_CSV_ACCEPTABILITY_ERR_LINE);
        if (err_line_acceptability_list != null) {

            putRequestScope(AttributeConst.PACK_CSV_ACCEPTABILITY_ERR_LINE, err_line_acceptability_list);
            removeSessionScope(AttributeConst.PACK_CSV_ACCEPTABILITY_ERR_LINE);
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

            //登録日・更新日は今日の日付
            LocalDate createdDay = LocalDate.now();
            LocalDate updatedDay = LocalDate.now();

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
                    createdDay,
                    updatedDay,
                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue());

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

        //ファイル形式がCSVでない場合、またはファイルの容量が100MBを超える場合はエラーメッセージを返す
        List<String> errors = CsvValidator.validate(filePart);

        if (errors.size() > 0) {
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策トークン
            putRequestScope(AttributeConst.ERR, errors); //エラーメッセージ

            //新規作成画面に戻る
            forward(ForwardConst.FW_PACK_NEW);
        }

        PackageInsertView pv = null;
        String manmaAcceptability = null; //乳腺検査の可否
        String xRayAcceptability = null; //X線検査の可否
        String ctAcceptability = null; //CT検査の可否
        String tvAcceptability = null; //X線TV検査の可否
        String mrAcceptability = null; //MR検査の可否

        //CSV読み込み（CSV Validationでエラーがない場合）
        try (InputStream is = filePart.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8"); //UTF-8
                BufferedReader br = new BufferedReader(isr);) {

            String line;

            line = br.readLine();
            String[] data = line.split(",");

            //CSVで取り込むデータ数（項目数）が設定値（9個）以外の場合にエラーを返す。
            String error = CsvValidator.validate_PACK(data);
            if (error != null) {
                errors.add(error);
            } else {
                //CSVの入力項目数が正しい場合、各検査の可否が正しく入力されているか検証（正しく入力されていない場合はエラーリストに追加）
                manmaAcceptability = data[4].trim();
                xRayAcceptability = data[5].trim();
                ctAcceptability = data[6].trim();
                tvAcceptability = data[7].trim();
                mrAcceptability = data[8].trim();

                String error_examAccept = PackageInsertValidator.validateAllExamAcceptability(manmaAcceptability,
                        xRayAcceptability, ctAcceptability, tvAcceptability, mrAcceptability);

                if (error_examAccept != null) {
                    errors.add(error_examAccept);
                }
            }

            //エラーがなければ、PackageInsertView（添付文書View）をインスタンス化
            if (errors.size() == 0) {

                //登録日・更新日は今日の日付
                LocalDate createdDay = LocalDate.now();
                LocalDate updatedDay = LocalDate.now();

                pv = new PackageInsertView(
                        null,
                        data[0].trim(),
                        data[1].trim(),
                        data[2].trim(),
                        data[3].trim(),
                        manmaAcceptability,
                        xRayAcceptability,
                        ctAcceptability,
                        tvAcceptability,
                        mrAcceptability,
                        createdDay,
                        updatedDay,
                        AttributeConst.DEL_FLAG_FALSE.getIntegerValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //エラーがあれば新規作成画面に戻る
        if (errors.size() > 0) {
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策トークン
            putRequestScope(AttributeConst.ERR, errors); //エラーメッセージ

            forward(ForwardConst.FW_PACK_NEW);
        } else if (errors.size() == 0) {
            //エラーがなければ、CSVデータ確認画面に
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用
            putRequestScope(AttributeConst.PACKAGE_INSERT, pv); //取得した添付文書データ

            //csvデータ確認画面に
            forward(ForwardConst.FW_PACK_NEW);
        }
    }

    /**
     * csvデータ（複数レコード）をまとめて取り込む
     * @throws ServletException
     * @throws IOException
     */

    public void csvAllImport() throws ServletException, IOException {

        Part filePart = getRequestPart(AttributeConst.PACK_CSV);

        //ファイル形式がCSVでない場合、またはファイルの容量が100MBを超える場合はエラーメッセージを返す
        List<String> errors = CsvValidator.validate(filePart);

        if (errors.size() > 0) {
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策トークン
            putRequestScope(AttributeConst.ERR, errors); //エラーメッセージ

            //一覧画面に戻る
            forward(ForwardConst.FW_PACK_INDEX);

        }

        //各検査の可否を示す入力変数を先に設定
        String manmaAcceptability = null; //乳腺検査の可否
        String xRayAcceptability = null; //X線検査の可否
        String ctAcceptability = null; //CT検査の可否
        String tvAcceptability = null; //X線TV検査の可否
        String mrAcceptability = null; //MR検査の可否

        PackageInsertView pv = null;
        List<PackageInsertView> pv_list = new ArrayList<>();

        //CSVの取り込み行数を格納する変数を設定
        int line_num = 1;
        List<Integer> err_line_num_list = new ArrayList<>(); //CSVデータの中で項目数が異常なデータの行数を格納
        List<Integer> err_line_acceptability_list = new ArrayList<>(); //CSVデータの中で検査の可否の値が異常な値をもつ行数を格納

        // csv読み込み
        try (InputStream is = filePart.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8"); //UTF-8
                BufferedReader br = new BufferedReader(isr);) {

            //登録日・更新日は今日の日付
            LocalDate createdDay = LocalDate.now();
            LocalDate updatedDay = LocalDate.now();

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                String error_examAccept = null; //検査の可否入力エラーを格納する変数をnullにして準備

                //CSVで取り込むデータ数（項目数）が設定値（9個）以外の場合にエラーを返す。
                String error = CsvValidator.validateMultiplePACK(data, line_num);
                if (error != null && !errors.contains(error)) {
                    errors.add(error);
                } else if (error == null) {
                    //CSVの入力項目数が正しい場合、各検査の可否が正しく入力されているか検証（正しく入力されていない場合はエラーリストに追加）
                    manmaAcceptability = data[4].trim();
                    xRayAcceptability = data[5].trim();
                    ctAcceptability = data[6].trim();
                    tvAcceptability = data[7].trim();
                    mrAcceptability = data[8].trim();

                    error_examAccept = PackageInsertValidator.validateAllExamAcceptability(manmaAcceptability,
                            xRayAcceptability, ctAcceptability, tvAcceptability, mrAcceptability);

                    if (error_examAccept != null && !errors.contains(error_examAccept)) {
                        errors.add(error_examAccept);
                    }
                }

                //エラーがなければ、PackageInsertView（添付文書View）をインスタンス化
                if (errors.size() == 0) {

                    pv = new PackageInsertView(
                            null,
                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim(),
                            manmaAcceptability,
                            xRayAcceptability,
                            ctAcceptability,
                            tvAcceptability,
                            mrAcceptability,
                            createdDay,
                            updatedDay,
                            AttributeConst.DEL_FLAG_FALSE.getIntegerValue());
                    pv_list.add(pv);
                } else {
                    //エラーがある場合、その行数をリストに追加
                    if (error != null) {
                        err_line_num_list.add(line_num);
                    } else if (error_examAccept != null) {
                        err_line_acceptability_list.add(line_num);
                    }
                }
                line_num++;
            }
        } catch (

        IOException e) {
            e.printStackTrace();
        }

        //エラーがあれば一覧画面に戻る
        if (errors.size() > 0) {
            putSessionScope(AttributeConst.ERR, errors); //エラーメッセージ
            putSessionScope(AttributeConst.PACK_CSV_ERR_LINE, err_line_num_list); // CSVの項目数異常によりエラーが起きたCSVファイルの行数
            putSessionScope(AttributeConst.PACK_CSV_ACCEPTABILITY_ERR_LINE, err_line_acceptability_list); //CSVデータの中で検査の可否の値が異常値を示した行数

            redirect(ForwardConst.ACT_PACK, ForwardConst.CMD_INDEX);

        } else if (errors.size() == 0) {

            //エラーがなければ、CSVデータ確認画面に
            putSessionScope(AttributeConst.PACKAGE_INSERT_LIST, pv_list);

            //複数csv取り込みデータ確認画面を表示
            forward(ForwardConst.FW_PACK_CSV_CHECK);
        }
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

    /**
     * 論理削除を行う
     * @throws ServletException
     * @throws IOException
     */
    public void destroy() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //idを条件に添付文書データを論理削除する
            service.destroy(toNumber(getRequestParam(AttributeConst.PACK_ID)));

            //セッションに削除完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_DELETED.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst.ACT_PACK, ForwardConst.CMD_INDEX);
        }
    }

    /**
     * 添付文書承認番号をもとに検索
     * @throws ServletException
     * @throws IOException
     */
    public void searchByApprovalNum() throws ServletException, IOException {

        //指定されたページ数の一覧画面に表示する添付文書データを取得
        int page = getPage();

        //検索フォームに入力された添付文書承認番号を取得
        String approval_num = getRequestParam(AttributeConst.PACK_APP_NUM);

        //指定した添付文書承認番号の件数を取得
        long count = service.countByAapproval_number(approval_num);

        //添付文書承認番号を指定して、添付文書テーブルからそのインスタンスを取得
        PackageInsert pi = service.findPackageInsertByAppNum(approval_num);

        //View（表示用）に変換
        PackageInsertView piv = PackageInsertConverter.toView(pi);

        List<PackageInsertView> packageInserts = new ArrayList<>();
        packageInserts.add(piv);

        //リクエストスコープに登録
        putRequestScope(AttributeConst.PACKEGE_INSERTS, packageInserts);  //検索した添付文書
        putRequestScope(AttributeConst.PACK_COUNT, count);  //検索した添付文書の件数
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




}

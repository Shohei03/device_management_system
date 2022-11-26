package actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Part;

import actions.views.PatientDeviceConverter;
import actions.views.PatientDeviceView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import models.Patient;
import models.PatientDevice;
import services.PatientDeviceService;
import validators.CsvValidator;

/**
 * 体内デバイスに関する処理を行うActionクラス
 * @author sn137
 *
 */
public class PatientDeviceAction extends ActionBase {

    private PatientDeviceService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new PatientDeviceService();

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

        //指定されたページ数の一覧画面に表示する体内デバイスデータを取得
        int page = getPage();
        List<PatientDeviceView> patientDevices = service.getAllPerPage(page);

        //全体内デバイスデータの件数を取得
        long patientsDevicesCount = service.countAll();

        putRequestScope(AttributeConst.PATIENT_DEVICES, patientDevices); //取得した体内デバイスデータ
        putRequestScope(AttributeConst.PATDEV_COUNT, patientsDevicesCount); //全ての体内デバイスデータの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコード数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに差し替え、セッションからは削除する
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
        List<Integer> errLineNumList = getSessionScope(AttributeConst.PATDEV_CSV_ERR_LINE);
        if (errLineNumList != null) {
            putRequestScope(AttributeConst.PATDEV_CSV_ERR_LINE, errLineNumList);
            removeSessionScope(AttributeConst.PATDEV_CSV_ERR_LINE);
        }

        //CSVファイルのエラー行数(検査の可否)を格納した変数もセッションから削除する
        List<Integer> errLineDateList = getSessionScope(AttributeConst.PATDEV_CSV_DATE_ERR_LINE);
        if (errLineDateList != null) {

            putRequestScope(AttributeConst.PATDEV_CSV_DATE_ERR_LINE, errLineDateList);
            removeSessionScope(AttributeConst.PATDEV_CSV_DATE_ERR_LINE);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_PATDEV_INDEX);
    }

    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException {

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン

        //体内デバイス情報の空インスタンスを準備
        PatientDeviceView pdv = new PatientDeviceView();

        putRequestScope(AttributeConst.PATIENT_DEVICE, pdv); //体内デバイスインスタンス

        //新規登録画面を表示
        forward(ForwardConst.FW_PATDEV_NEW);
    }

    /**
     * 新規登録前に内容が正しいか確認するよう促す
     * @throws ServletException
     * @throws IOException
     */
    public void check() throws ServletException, IOException {

        //苗字と名前の空白が半角スペースの場合、全角に変換

        //患者名
        String patientName = getRequestParam(AttributeConst.PATDEV_PAT_NAME);
        if (patientName != null) {
            patientName = toZenkakuSpace(patientName);
        }
        //患者名（ひらがな）
        String patientNameKana = getRequestParam(AttributeConst.PATDEV_PAT_NAME_KANA);
        if (patientNameKana != null) {
            patientNameKana = toZenkakuSpace(patientNameKana);
        }

        //デバイス埋込日が入力されている場合のみ、LocalDateに。
        String inputDate = getRequestParam(AttributeConst.PATDEV_IMP_DATE);
        LocalDate implantedAt;
        if (!(inputDate == null || inputDate.equals(""))) {
            implantedAt = LocalDate.parse(getRequestParam(AttributeConst.PATDEV_IMP_DATE));
        } else {
            implantedAt = null;
        }

        //パラメータの値を元に体内デバイス情報のインスタンスを作成する
        PatientDeviceView pdv = new PatientDeviceView(
                null,
                toNumber(getRequestParam(AttributeConst.PATDEV_PAT_ID)),
                patientName,
                patientNameKana,
                getRequestParam(AttributeConst.PATDEV_APP_NUM),
                getRequestParam(AttributeConst.PATDEV_DEV_NAME),
                implantedAt,
                null,
                null,
                AttributeConst.DEL_FLAG_FALSE.getIntegerValue());

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
        putRequestScope(AttributeConst.PATIENT_DEVICE, pdv);
        //確認呼びかけのメッセージを設定
        putRequestScope(AttributeConst.FLUSH, MessageConst.I_CHECK.getMessage());

        //確認画面を表示
        forward(ForwardConst.FW_PATDEV_CHECK);

    }

    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        //入力データが体内デバイステーブルに既に存在するか確認（duplicateCheck = true）
        Boolean duplicateCheck = true;

        //入力データが体内デバイステーブルのレコードと重複してもいい場合（duplicateCheck = false）＝同日に同じデバイスを複数埋め込んだ場合
        if (getRequestParam(AttributeConst.PATDEV_DUPLICATE_CHECK) != null) {
            duplicateCheck = Boolean.valueOf(getRequestParam(AttributeConst.PATDEV_DUPLICATE_CHECK));
        }

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //デバイス埋込日が入力されている場合のみ、LocalDateに。
            String inputDate = getRequestParam(AttributeConst.PATDEV_IMP_DATE);
            LocalDate implantedAt;
            if (!(inputDate == null || inputDate.equals(""))) {
                implantedAt = LocalDate.parse(getRequestParam(AttributeConst.PATDEV_IMP_DATE));
            } else {
                implantedAt = null;
            }

            //パラメータの値を元に体内デバイス情報のインスタンスを作成する
            PatientDeviceView pdv = new PatientDeviceView(
                    null,
                    toNumber(getRequestParam(AttributeConst.PATDEV_PAT_ID)),
                    getRequestParam(AttributeConst.PATDEV_PAT_NAME),
                    getRequestParam(AttributeConst.PATDEV_PAT_NAME_KANA),
                    getRequestParam(AttributeConst.PATDEV_APP_NUM),
                    getRequestParam(AttributeConst.PATDEV_DEV_NAME),
                    implantedAt,
                    null,
                    null,
                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue());

            //体内デバイス情報を登録
            List<String> errors = service.create(pdv, duplicateCheck);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.PATIENT_DEVICE, pdv); //入力された体内デバイス情報
                putRequestScope(AttributeConst.ERR, errors); //エラーリスト

                //新規登録画面を再表示
                forward(ForwardConst.FW_PATDEV_NEW);
            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_PATDEV, ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * 詳細画面を表示
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

        //idを条件に体内デバイスデータを取得する
        PatientDeviceView pdv = service.findOne(toNumber(getRequestParam(AttributeConst.PATDEV_ID)));

        if (pdv == null) {
            //該当の体内デバイスデータが存在しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);
        } else {
            putRequestScope(AttributeConst.PATIENT_DEVICE, pdv); //取得した体内デバイスデータ

            //詳細画面を表示
            forward(ForwardConst.FW_PATDEV_SHOW);
        }
    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        //idを条件に体内デバイスデータを取得する
        PatientDeviceView pdv = service.findOne(toNumber(getRequestParam(AttributeConst.PATDEV_ID)));

        //該当の体内デバイスデータが存在しない場合はエラー画面を表示
        if (pdv == null) {
            forward(ForwardConst.FW_ERR_UNKNOWN);
        } else {

            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst.PATIENT_DEVICE, pdv); //取得した体内デバイスデータ

            //編集画面を表示
            forward(ForwardConst.FW_PATDEV_EDIT);
        }
    }

    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {

        Boolean duplicateCheck = false;

        //CSRF対策 tokenのチェック
        if (checkToken()) {
            //idを条件に体内デバイスデータを取得する
            PatientDeviceView pdv = service.findOne(toNumber(getRequestParam(AttributeConst.PATDEV_ID)));

            //入力された体内デバイスの内容を設定する
            pdv.setImplantedAt(toLocalDate(getRequestParam(AttributeConst.PATDEV_IMP_DATE)));
            pdv.setApprovalNumber(getRequestParam(AttributeConst.PATDEV_APP_NUM));
            pdv.setDeviceName(getRequestParam(AttributeConst.PATDEV_DEV_NAME));

            //体内デバイスデータを更新する
            List<String> errors = service.update(pdv, duplicateCheck);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.PATIENT_DEVICE, pdv); //入力された体内デバイス情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst.FW_PATDEV_EDIT);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_PATDEV, ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * 論理削除を行う
     * @throws ServletException
     * @throws IOException
     */
    public void destroy() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //idを条件に体内デバイスデータを論理削除する
            service.destroy(toNumber(getRequestParam(AttributeConst.PATDEV_ID)));

            //セッションに削除完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_DELETED.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst.ACT_PATDEV, ForwardConst.CMD_INDEX);
        }
    }

    /**
     * CSVデータ（1レコード）を取り込む
     * @throws ServletException
     * @throws IOException
     */
    public void csvImport() throws ServletException, IOException {

        Part filePart = getRequestPart(AttributeConst.PATDEV_CSV);
        //Viewデータの変数設定
        PatientDeviceView pdv = null;
        //デバイス埋込日の変数設定
        LocalDate impDate = null;

        //ファイル形式がCSVでない場合、またはファイルの容量が100MBを超える場合はエラーメッセージを返す
        List<String> errors = CsvValidator.validate(filePart);

        if (errors.size() > 0) {
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策トークン
            putRequestScope(AttributeConst.ERR, errors); //エラーメッセージ

            //新規作成画面に戻る
            forward(ForwardConst.FW_PATDEV_NEW);
        }

        //CSV読込
        try (InputStream is = filePart.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);) {

            String line;
            line = br.readLine();
            String[] data = line.split(",");

            //CSVで取り込むデータ数（項目数）が設定値（6個）以外の場合にエラーを返す。
            String error = CsvValidator.validateItemNum(data, 6);
            if (error != null) {
                errors.add(error);
            } else {
                //CSVの入力項目数が正しい場合
                //埋込日データが、yyyy/(M)M/(d)dの形式であることを確認（異なる形式の場合はエラーメッセージを返却）
                String implantedDate = data[5];

                String errorDate = CsvValidator.checkDate(implantedDate);
                if (errorDate != null) {
                    errors.add(errorDate);
                } else {
                    //CSVの埋込日データをLocalDate型に変換
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/[]M/[]d");
                    impDate = LocalDate.parse(implantedDate, dtf);
                }
            }

            //エラーがなければ、PackageInsertView（添付文書View）をインスタンス化
            if (errors.size() == 0) {

                //患者IDに数字以外に記号があれば除去（BOM排除）
                String patientId = data[0];
                String intStrPatientId = patientId.replaceAll("[^0-9]", "");

                //苗字と名前の空白が半角スペースの場合、全角に変換
                //患者名
                String patientName = data[1].trim();

                if (patientName != null) {
                    patientName = toZenkakuSpace(patientName);
                }
                //患者名（ひらがな）
                String patientNameKana = data[2].trim();
                if (patientNameKana != null) {
                    patientNameKana = toZenkakuSpace(patientNameKana);
                }

                pdv = new PatientDeviceView(
                        null,
                        toNumber(intStrPatientId),
                        patientName,
                        patientNameKana,
                        data[3].trim(),
                        data[4].trim(),
                        impDate,
                        LocalDate.now(),
                        LocalDate.now(),
                        null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //エラーがあれば新規作成画面に戻る
        if (errors.size() > 0) {
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策トークン
            putRequestScope(AttributeConst.ERR, errors); //エラーメッセージ

            forward(ForwardConst.FW_PATDEV_NEW);

        } else if (errors.size() == 0) {
            //エラーがなければ、CSVデータ確認画面に
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用
            putRequestScope(AttributeConst.PATIENT_DEVICE, pdv); //取得した添付文書データ

            //csvデータ確認画面に
            forward(ForwardConst.FW_PATDEV_NEW);
        }
    }

    /**
     * CSVデータ（複数レコード）をまとめて取り込む
     * @throws ServletException
     * @throws IOException
     */

    public void csvAllImport() throws ServletException, IOException {

        Part filePart = getRequestPart(AttributeConst.PATDEV_CSV);

        //ファイル形式がCSVでない場合、またはファイルの容量が100MBを超える場合はエラーメッセージを返す
        List<String> errors = CsvValidator.validate(filePart);

        if (errors.size() > 0) {
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策トークン
            putRequestScope(AttributeConst.ERR, errors); //エラーメッセージ

            //一覧画面に戻る
            forward(ForwardConst.FW_PATDEV_INDEX);
        }

        //ViewとそのListの変数を先に設定
        PatientDeviceView pdv = null;
        List<PatientDeviceView> pdvList = new ArrayList<>();

        LocalDate impDate = null; //埋込日を格納する変数

        //CSVの取り込み行数を格納する変数を設定
        int lineNum = 1;
        List<Integer> errLineNumList = new ArrayList<>(); //CSVデータの中で項目数が異常なデータの行数を格納
        List<Integer> errLineDateList = new ArrayList<>(); //CSVデータの中で埋込日が異常な値をもつ行数を格納

        // CSV読み込み
        try (InputStream is = filePart.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8"); //UTF-8
                BufferedReader br = new BufferedReader(isr);) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                String errorDate = null; //埋込日入力エラーを格納する変数をnullにして準備

                //CSVで取り込むデータ数（項目数）が設定値（6個）以外の場合にエラーを返す。
                String error = CsvValidator.validateItemNum(data, 6);
                if (error != null && !errors.contains(error)) {
                    errors.add(error);
                } else if (error == null) {
                    //CSVの入力項目数が正しい場合、埋込日が正しく入力されているか検証（正しく入力されていない場合はエラーリストに追加）
                    String implantedDate = data[5];
                    errorDate = CsvValidator.checkDate(implantedDate);

                    if (errorDate != null) {
                        errors.add(errorDate);
                    } else {
                        //入力値の形式が正しければCSVの埋込日データをLocalDate型に変換
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/[]M/[]d");
                        impDate = LocalDate.parse(implantedDate, dtf);
                    }
                }

                //エラーがなければ、PatientDeviceView（患者の体内デバイスView）をインスタンス化
                if (errors.size() == 0) {
                    //患者IDに数字以外に記号があれば除去（BOM排除）
                    String patientId = data[0];
                    String intStrPatientId = patientId.replaceAll("[^0-9]", "");

                    //苗字と名前の空白が半角スペースの場合、全角に変換
                    //患者名
                    String patientName = data[1].trim();
                    if (patientName != null) {
                        patientName = toZenkakuSpace(patientName);
                    }
                    //患者名（ひらがな）
                    String patientNameKana = data[2].trim();
                    if (patientNameKana != null) {
                        patientNameKana = toZenkakuSpace(patientNameKana);
                    }

                    pdv = new PatientDeviceView(
                            null,
                            toNumber(intStrPatientId),
                            patientName,
                            patientNameKana,
                            data[3].trim(),
                            data[4].trim(),
                            impDate,
                            LocalDate.now(),
                            LocalDate.now(),
                            AttributeConst.DEL_FLAG_FALSE.getIntegerValue());
                    pdvList.add(pdv);
                } else {
                    //エラーがある場合、その行数をリストに追加
                    if (error != null) {
                        errLineNumList.add(lineNum);
                    } else if (errorDate != null) {
                        errLineDateList.add(lineNum);
                    }
                }
                lineNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //エラーがあれば一覧画面に戻る
        if (errors.size() > 0) {
            putSessionScope(AttributeConst.ERR, errors); //エラーメッセージ
            putSessionScope(AttributeConst.PATDEV_CSV_ERR_LINE, errLineNumList); // CSVの項目数異常によりエラーが起きたCSVファイルの行数
            putSessionScope(AttributeConst.PATDEV_CSV_DATE_ERR_LINE, errLineDateList); //CSVデータの中で埋込日の値が異常値を示した行数

            redirect(ForwardConst.ACT_PATDEV, ForwardConst.CMD_INDEX);

        } else if (errors.size() == 0) {

            //エラーがなければ、CSVデータ確認画面に
            putSessionScope(AttributeConst.PATIENT_DEVICE_LIST, pdvList);

            //複数csv取り込みデータ確認画面を表示
            forward(ForwardConst.FW_PATDEV_CSV_CHECK);
        }
    }

    /**
     * 読み込んだCSVデータの中で不要なレコードを消去
     * @throws ServletException
     * @throws IOException
     */

    public void csvModify() throws ServletException, IOException {

        //セッションからcsvで取り込んだ体内デバイスデータリストを取得
        List<PatientDeviceView> pdvList = getSessionScope(AttributeConst.PATIENT_DEVICE_LIST);

        //csvの取り込みをやめたいレコードのindex番号を取得
        int indexNum = toNumber(getRequestParam(AttributeConst.PATDEV_INDEX));

        //データがある場合、セッションからcsvで取り込んだ体内デバイスリストを削除し、残ったデータリストをセッションに登録。
        if (!(pdvList == null)) {
            //index番号を指定して、csvの取り込みをやめたいレコードを削除
            pdvList.remove(indexNum);
            removeSessionScope(AttributeConst.PATIENT_DEVICE_LIST);
            putSessionScope(AttributeConst.PATIENT_DEVICE_LIST, pdvList);
        }

        //csv取り込みデータ確認画面を表示
        forward(ForwardConst.FW_PATDEV_CSV_CHECK);

    }

    /**
     * 取り込んだCSVデータをデータベースに登録
     * @throws ServletException
     * @throws IOException
     */
    public void csvAllCreate() throws ServletException, IOException {
        //入力データが体内デバイスデータに既に存在するか確認（duplicateCheck = true）
        Boolean duplicateCheck = true;

        //入力データが体内デバイスデータのレコードと重複してもいい場合（duplicateCheck = false）＝同日に同じデバイスを複数埋め込んだ場合
        if (getRequestParam(AttributeConst.PATDEV_DUPLICATE_CHECK) != null) {
            duplicateCheck = Boolean.valueOf(getRequestParam(AttributeConst.PATDEV_DUPLICATE_CHECK));
        }

        //セッションからcsvで取り込んだ体内デバイスデータリストを取得
        ArrayList<PatientDeviceView> pdvList = new ArrayList<>();
        pdvList = getSessionScope(AttributeConst.PATIENT_DEVICE_LIST);

        //リストに体内デバイスデータがない場合、CSV取り込みデータ確認画面に戻る。
        if (pdvList == null || pdvList.isEmpty()) {
            putRequestScope(AttributeConst.ERR, MessageConst.E_NODATA.getMessage());
            //csv取り込みデータ確認画面に戻る
            forward(ForwardConst.FW_PATDEV_CSV_CHECK);
            return;
        } else {

            ArrayList<PatientDeviceView> pdvListCopy = new ArrayList<PatientDeviceView>(pdvList);

            //リスト内の体内デバイスデータの項目値をバリデーションし、エラーがあれば、データ確認画面に戻る
            Iterator<PatientDeviceView> patDevIt = pdvListCopy.iterator();
            while (patDevIt.hasNext()) {
                PatientDeviceView pdv = (PatientDeviceView) patDevIt.next();

                //添付文書情報を登録
                List<String> errors = service.create(pdv, duplicateCheck);

                if (errors.size() > 0) {
                    //登録中にエラーがあった場合
                    putRequestScope(AttributeConst.PATDEV_ERR_PAT_NAME, pdv.getPatientName()); //エラーが生じた患者名
                    putRequestScope(AttributeConst.ERR, errors); //エラーリスト

                    //csv取り込みデータ確認画面に戻る
                    forward(ForwardConst.FW_PATDEV_CSV_CHECK);
                    return;
                } else {

                    pdvList.remove(0);
                    removeSessionScope(AttributeConst.PATIENT_DEVICE_LIST);
                    putSessionScope(AttributeConst.PATIENT_DEVICE_LIST, pdvList);

                    //重複チェックが解除されている(duplicateCheck = false)場合、次から重複チェックをする
                    duplicateCheck = true;
                }

            }

            //セッションに登録完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst.ACT_PATDEV, ForwardConst.CMD_INDEX);
        }
    }

    /**
     * 患者IDをもとに検索
     * @throws ServletException
     * @throws IOException
     */
    public void searchByPatId() throws ServletException, IOException {

        //指定されたページ数の一覧画面に表示する体内デバイスデータを取得
        int page = getPage();

        //検索フォームに入力された患者IDを取得
        int patientId = toNumber(getRequestParam(AttributeConst.PATDEV_PAT_ID));

        //患者IDを指定して、患者テーブルからその患者インスタンスを取得
        Patient p = service.findPatient(patientId);

        //指定した患者がもつ体内デバイスのリストを取得
        List<PatientDevice> patientDevices = service.findAllPatDevbyPatient(p);

        List<PatientDeviceView> patientDevicesViews = PatientDeviceConverter.toViewList(patientDevices);

        //患者がもつデバイスデータの数を取得
        long patientsDevicesCount = service.countPatDevByPatientId(p);

        putRequestScope(AttributeConst.PATIENT_DEVICES, patientDevicesViews); //取得した体内デバイスデータ
        putRequestScope(AttributeConst.PATDEV_COUNT, patientsDevicesCount); //体内デバイスデータの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコード数

        //検索結果画面を表示
        forward(ForwardConst.FW_PATDEV_INDEX);

    }

}

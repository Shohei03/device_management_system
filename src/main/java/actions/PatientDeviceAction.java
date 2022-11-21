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
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;

import actions.views.PatientDeviceView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.PatientDeviceService;

/**
 * 体内デバイスに関する処理を行うActionクラス
 * @author sn137
 *
 */
@MultipartConfig
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
        // pdv.setCreatedAt(LocalDate.now());
        // pdv.setUpdatedAt(LocalDate.now());
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

        //患者名の苗字と名前の間の空白スペースが半角の場合は、全角に変換する
        String patient_name = getRequestParam(AttributeConst.PATDEV_PAT_NAME);
        if (patient_name != null) {
            patient_name = patient_name.replaceAll(" ", "  ");
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
                patient_name,
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

        //入力データが体内デバイスデータに既に存在するか確認（duplicateCheck = true）
        Boolean duplicateCheck = true;

        //入力データが体内デバイスデータのレコードと重複してもいい場合（duplicateCheck = false）＝同日に同じデバイスを複数埋め込んだ場合
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
            pdv.setApproval_number(getRequestParam(AttributeConst.PATDEV_APP_NUM));
            pdv.setDevice_name(getRequestParam(AttributeConst.PATDEV_DEV_NAME));

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
        PatientDeviceView pdv = null;

        //CSV読込
        try (InputStream is = filePart.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);) {

            String line;

            line = br.readLine();
            String[] data = line.split(",");

            //CSVの埋込日をLocalDate型に変換
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/[]M/[]d");
            LocalDate imp_date = LocalDate.parse(data[4], dtf);

            String patient_id = data[0];
            String intStr_patient_id = patient_id.replaceAll("[^0-9]", "");

            pdv = new PatientDeviceView(
                    null,
                    toNumber(intStr_patient_id),
                    data[1].trim(),
                    data[2].trim(),
                    data[3].trim(),
                    imp_date,
                    LocalDate.now(),
                    LocalDate.now(),
                    null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ファイルを読み込めなかった場合の処理
        if (pdv == null) {
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用
            putRequestScope(AttributeConst.ERR, "csvファイルを読み込めませんでした");

            //新規作成画面にcsv取り込みデータを表示
            forward(ForwardConst.FW_PATDEV_NEW);

        }

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用
        putRequestScope(AttributeConst.PATIENT_DEVICE, pdv); //取得した添付文書データ

        //新規作成画面にcsv取り込みデータを表示
        forward(ForwardConst.FW_PATDEV_NEW);

    }

    /**
     * CSVデータ（複数レコード）をまとめて取り込む
     * @throws ServletException
     * @throws IOException
     */

    public void csvAllImport() throws ServletException, IOException {

        Part filePart = getRequestPart(AttributeConst.PATDEV_CSV);
        PatientDeviceView pdv = null;
        List<PatientDeviceView> pdv_list = new ArrayList<>();

        // CSV読み込み
        try (InputStream is = filePart.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                //患者IDに数字以外に記号があれば除去
                String patient_id = data[0];
                String intStr_patient_id = patient_id.replaceAll("[^0-9]", "");

                //CSVの埋込日をLocalDate型に変換
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/[]M/[]d");
                LocalDate imp_date = LocalDate.parse(data[4], dtf);

                pdv = new PatientDeviceView(
                        null,
                        toNumber(intStr_patient_id),
                        data[1].trim(),
                        data[2].trim(),
                        data[3].trim(),
                        imp_date,
                        LocalDate.now(),
                        LocalDate.now(),
                        AttributeConst.DEL_FLAG_FALSE.getIntegerValue());
                pdv_list.add(pdv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        putSessionScope(AttributeConst.PATIENT_DEVICE_LIST, pdv_list);

        //csv取り込みデータ確認画面を表示
        forward(ForwardConst.FW_PATDEV_CSV_CHECK);
    }

    /**
     * 読み込んだCSVデータの中で不要なレコードを消去
     * @throws ServletException
     * @throws IOException
     */

    public void csvModify() throws ServletException, IOException {

        //セッションからcsvで取り込んだ体内デバイスデータリストを取得
        List<PatientDeviceView> pdv_list = getSessionScope(AttributeConst.PATIENT_DEVICE_LIST);

        //csvの取り込みをやめたいレコードのindex番号を取得
        int index_num = toNumber(getRequestParam(AttributeConst.PATDEV_INDEX));

        //データがある場合、セッションからcsvで取り込んだ体内デバイスリストを削除し、残ったデータリストをセッションに登録。
        if (!(pdv_list == null)) {
            //index番号を指定して、csvの取り込みをやめたいレコードを削除
            pdv_list.remove(index_num);
            removeSessionScope(AttributeConst.PATIENT_DEVICE_LIST);
            putSessionScope(AttributeConst.PATIENT_DEVICE_LIST, pdv_list);
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
        ArrayList<PatientDeviceView> pdv_list = new ArrayList<>();
        pdv_list = getSessionScope(AttributeConst.PATIENT_DEVICE_LIST);

        //リストに体内デバイスデータがない場合、CSV取り込みデータ確認画面に戻る。
        if (pdv_list == null || pdv_list.isEmpty()) {
            putRequestScope(AttributeConst.ERR, MessageConst.E_NODATA.getMessage());
            //csv取り込みデータ確認画面に戻る
            forward(ForwardConst.FW_PATDEV_CSV_CHECK);
        } else {

            ArrayList<PatientDeviceView> pdv_list_copy = new ArrayList<PatientDeviceView>(pdv_list);

            //リスト内の体内デバイスデータの項目値をバリデーションし、エラーがあれば、データ確認画面に戻る
            Iterator<PatientDeviceView> patDev_it = pdv_list_copy.iterator();
            while (patDev_it.hasNext()) {
                PatientDeviceView pdv = (PatientDeviceView) patDev_it.next();

                //添付文書情報を登録
                List<String> errors = service.create(pdv, duplicateCheck);

                if (errors.size() > 0) {
                    //登録中にエラーがあった場合
                    putRequestScope(AttributeConst.PATDEV_ERR_PAT_NAME, pdv.getPatient_name()); //エラーが生じた患者名
                    putRequestScope(AttributeConst.ERR, errors); //エラーリスト

                    //csv取り込みデータ確認画面に戻る
                    forward(ForwardConst.FW_PATDEV_CSV_CHECK);
                } else {

                    pdv_list.remove(0);
                    removeSessionScope(AttributeConst.PATIENT_DEVICE_LIST);
                    putSessionScope(AttributeConst.PATIENT_DEVICE_LIST, pdv_list);

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

}

package actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Part;

import actions.views.PatientExaminationConverter;
import actions.views.PatientExaminationView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import models.Patient;
import models.PatientExamination;
import services.PatientExaminationService;

/**
 * 検査情報に関する処理を行うActionクラス
 *
 */
public class PatientExaminationAction extends ActionBase {

    private PatientExaminationService patExamservice;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {
        patExamservice = new PatientExaminationService();

        //メソッドを実行
        invoke();
        patExamservice.close();
    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {
        //指定されたページ数の一覧画面に表示する検査情報データ（日付指定）を取得
        int page = getPage();

        //指定する日付
        LocalDate date = toLocalDate(getRequestParam(AttributeConst.PATEXAM_DAY));
        if (date == null) {
            date = LocalDate.now();
        }
        //一覧画面に表示する検査情報データを取得

        List<PatientExaminationView> patientExaminations = patExamservice.getExamPerPage(page);

        //検査情報データの件数を取得
        long patientExamCount = patExamservice.countExamination();

        putRequestScope(AttributeConst.PATIENT_EXAMINATIONS, patientExaminations); //取得した検査情報データ
        putRequestScope(AttributeConst.PATEXAM_COUNT_BY_DAY, patientExamCount); // 検査件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_PATEXAM_INDEX);
    }

    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException {

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン

        //検査情報の空インスタンスを準備
        PatientExaminationView pev = new PatientExaminationView();
        putRequestScope(AttributeConst.PATIENT_EXAMINATION, pev);

        //新規登録画面を表示
        forward(ForwardConst.FW_PATEXAM_NEW);
    }

    /**
     * 新規登録前の内容確認
     * @throws ServletException
     * @throws IOException
     */
    public void check() throws ServletException, IOException {

        //患者名の苗字と名前の間の空白スペースが半角の場合は、全角に変換する
        String patient_name = getRequestParam(AttributeConst.PATEXAM_PAT_NAME);
        if (patient_name != null) {
            patient_name = patient_name.replaceAll(" ", "  ");
        }

        //患者名（ひらがな）の苗字と名前の間の空白スペースが半角の場合は、全角に変換する
        String patient_name_kana = getRequestParam(AttributeConst.PATEXAM_PAT_NAME_KANA);
        if (patient_name_kana != null) {
            patient_name_kana = patient_name_kana.replaceAll(" ", "  ");
        }

        //検査日が入力されている場合のみ、LocalDateに。
        String input_examDate = getRequestParam(AttributeConst.PATEXAM_EXAM_DATE);
        LocalDate examination_date;
        if (!(input_examDate == null || input_examDate.equals(""))) {
            examination_date = LocalDate.parse(getRequestParam(AttributeConst.PATEXAM_EXAM_DATE));
        } else {
            examination_date = null;
        }

        //予約時間が入力されている場合のみ、LocalTimeに。
        String input_reservationTime = getRequestParam(AttributeConst.PATEXAM_RESERVATION_TIME);
        LocalTime reservation_time;
        if (!(input_reservationTime == null || input_reservationTime.equals(""))) {
            reservation_time = LocalTime.parse(getRequestParam(AttributeConst.PATEXAM_RESERVATION_TIME));
        } else {
            reservation_time = null;
        }

        //パラメータの値を元に検査情報のインスタンスを作成する
        PatientExaminationView pev = new PatientExaminationView(
                null,
                toNumber(getRequestParam(AttributeConst.PATEXAM_PAT_ID)),
                patient_name,
                patient_name_kana,
                getRequestParam(AttributeConst.PATEXAM_EXAM_ITEM),
                examination_date,
                reservation_time,
                null);

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
        putRequestScope(AttributeConst.PATIENT_EXAMINATION, pev);
        //確認呼びかけのメッセージを設定
        putRequestScope(AttributeConst.FLUSH, MessageConst.I_CHECK.getMessage());

        //確認画面を表示
        forward(ForwardConst.FW_PATEXAM_CHECK);
    }

    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        //入力データが検査情報データに既に存在するか（duplicateCheck = true）
        Boolean duplicateCheck = true;

        //入力データが検査情報テーブルのレコードと重複してもいい場合（duplicate = false） =同日に同じ検査を施行してもいい場合
        if (getRequestParam(AttributeConst.PATEXAM_DUPLICATE_CHECK) != null) {

            duplicateCheck = Boolean.valueOf(getRequestParam(AttributeConst.PATEXAM_DUPLICATE_CHECK));
        }

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //検査日が入力されている場合のみ、LocalDateに。
            String input_examDate = getRequestParam(AttributeConst.PATEXAM_EXAM_DATE);
            LocalDate examination_date;
            if (!(input_examDate == null || input_examDate.equals(""))) {
                examination_date = LocalDate.parse(getRequestParam(AttributeConst.PATEXAM_EXAM_DATE));
            } else {
                examination_date = null;
            }

            //予約時間が入力されている場合のみ、LocalTimeに。
            String input_reservationTime = getRequestParam(AttributeConst.PATEXAM_RESERVATION_TIME);
            LocalTime reservation_time;
            if (!(input_reservationTime == null || input_reservationTime.equals(""))) {
                reservation_time = LocalTime.parse(getRequestParam(AttributeConst.PATEXAM_RESERVATION_TIME));
            } else {
                reservation_time = null;
            }

            //パラメータの値を元に検査情報のインスタンスを作成する
            PatientExaminationView pev = new PatientExaminationView(
                    null,
                    toNumber(getRequestParam(AttributeConst.PATEXAM_PAT_ID)),
                    getRequestParam(AttributeConst.PATEXAM_PAT_NAME),
                    getRequestParam(AttributeConst.PATEXAM_PAT_NAME_KANA),
                    getRequestParam(AttributeConst.PATEXAM_EXAM_ITEM),
                    examination_date,
                    reservation_time,
                    null);

            //検査情報を登録
            List<String> errors = patExamservice.create(pev, duplicateCheck);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.PATIENT_EXAMINATION, pev); //入力された検査情報
                putRequestScope(AttributeConst.ERR, errors); //エラーリスト

                if (!duplicateCheck) {
                    //確認画面を再表示
                    forward(ForwardConst.FW_PATEXAM_CHECK);

                } else {
                    //新規登録画面を再表示
                    forward(ForwardConst.FW_PATEXAM_NEW);
                }

            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_PATEXAM, ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * 詳細画面を表示
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

        //idを条件に検査情報データを取得する
        PatientExaminationView pev = patExamservice.findOne(toNumber(getRequestParam(AttributeConst.PATEXAM_ID)));

        if (pev == null) {
            //該当の検査情報データが存在しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);
        } else {
            putRequestScope(AttributeConst.PATIENT_EXAMINATION, pev); //取得した検査情報データ

            //詳細画面を表示
            forward(ForwardConst.FW_PATEXAM_SHOW);
        }
    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        //idを条件に検査情報データを取得する
        PatientExaminationView pev = patExamservice.findOne(toNumber(getRequestParam(AttributeConst.PATEXAM_ID)));

        //該当の検査データが存在しない場合はエラー画面を表示
        if (pev == null) {
            forward(ForwardConst.FW_ERR_UNKNOWN);
        } else {

            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst.PATIENT_EXAMINATION, pev); //取得した体内デバイスデータ

            //編集画面を表示
            forward(ForwardConst.FW_PATEXAM_EDIT);
        }
    }

    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {

        Boolean duplicateCheck = true;

        //入力データが検査情報テーブルのレコードと重複してもいい場合（duplicate = false） =同日に同じ検査を施行してもいい場合
        if (getRequestParam(AttributeConst.PATEXAM_DUPLICATE_CHECK) != null) {
            duplicateCheck = Boolean.valueOf(getRequestParam(AttributeConst.PATEXAM_DUPLICATE_CHECK));
        }

        //CSRF対策 tokenのチェック
        if (checkToken()) {
            //idを条件に検査情報データを取得する
            PatientExaminationView pev = patExamservice.findOne(toNumber(getRequestParam(AttributeConst.PATEXAM_ID)));

            //入力された検査情報の内容を設定する
            pev.setExamination_item(getRequestParam(AttributeConst.PATEXAM_EXAM_ITEM));
            pev.setExamination_date(toLocalDate(getRequestParam(AttributeConst.PATEXAM_EXAM_DATE)));
            pev.setReservation_time(LocalTime.parse(getRequestParam(AttributeConst.PATEXAM_RESERVATION_TIME)));

            pev.setPatient_id(toNumber(getRequestParam(AttributeConst.PATEXAM_PAT_ID)));
            pev.setPatient_name(getRequestParam(AttributeConst.PATEXAM_PAT_NAME));
            pev.setPatient_name_kana(getRequestParam(AttributeConst.PATEXAM_PAT_NAME_KANA));

            //検査情報を更新する
            List<String> errors = patExamservice.update(pev, duplicateCheck);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.PATIENT_EXAMINATION, pev); //入力された検査情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst.FW_PATEXAM_EDIT);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_PATEXAM, ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * 削除を行う
     * @throws ServletException
     * @throws IOException
     */
    public void destroy() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //idを条件に検査情報データを削除する
            patExamservice.destroy(toNumber(getRequestParam(AttributeConst.PATDEV_ID)));

            //セッションに削除完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_DELETED.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst.ACT_PATEXAM, ForwardConst.CMD_INDEX);
        }
    }

    /**
     * CSVデータ（1レコード）を取り込む
     * @throws ServletException
     * @throws IOException
     */
    public void csvImport() throws ServletException, IOException {

        Part filePart = getRequestPart(AttributeConst.PATEXAM_CSV);
        PatientExaminationView pev = null;

        //CSV読込
        try (InputStream is = filePart.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);) {

            String line;

            line = br.readLine();
            System.out.println("line" + line);
            String[] data = line.split(",");

            //CSVの検査日をLocalDate型に変換
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/[]M/[]d");
            LocalDate exam_date = LocalDate.parse(data[1], dtf);

            //CSVの予約時間をLocalTime型に変換
            DateTimeFormatter dtf_reserveTime = DateTimeFormatter.ofPattern("[]H:mm");
            LocalTime reserve_time = LocalTime.parse(data[2], dtf_reserveTime);

            //患者ID 数字以外に記号があれば削除
            String patient_id = data[3];
            String intStr_patient_id = patient_id.replaceAll("[^0-9]", "");

            //検査項目に不要な値が入っている場合は削除
            String examination_item = data[0];
            String str_examination_item = examination_item.replaceAll("[^単純X線CTMRTV乳腺検査]", "");

            pev = new PatientExaminationView(
                    null,
                    toNumber(intStr_patient_id),
                    data[4].trim(),
                    data[5].trim(),
                    str_examination_item,
                    exam_date,
                    reserve_time,
                    null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ファイルを読み込めなかった場合の処理
        if (pev == null) {
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用
            putRequestScope(AttributeConst.ERR, "csvファイルを読み込めませんでした");

            //新規作成画面にcsv取り込みデータを表示
            forward(ForwardConst.FW_PATEXAM_NEW);

        }

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用
        putRequestScope(AttributeConst.PATIENT_EXAMINATION, pev); //取得した検査情報データ

        //新規作成画面にcsv取り込みデータを表示
        forward(ForwardConst.FW_PATEXAM_NEW);
    }

    /**
     * CSVデータ（複数レコード）をまとめて取り込む
     * @throws ServletException
     * @throws IOException
     */

    public void csvAllImport() throws ServletException, IOException {

        Part filePart = getRequestPart(AttributeConst.PATEXAM_CSV);
        PatientExaminationView pev = null;
        List<PatientExaminationView> pev_list = new ArrayList<>();

        // CSV読み込み
        try (InputStream is = filePart.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                //患者IDに数字以外に記号があれば除去
                String patient_id = data[3];
                String intStr_patient_id = patient_id.replaceAll("[^0-9]", "");

                //CSVの検査日をLocalDate型に変換
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/[]M/[]d");
                LocalDate exam_date = LocalDate.parse(data[1], dtf);

                //CSVの予約時間をLocalTime型に変換
                DateTimeFormatter dtf_reserveTime = DateTimeFormatter.ofPattern("[]H:mm");
                LocalTime reserve_time = LocalTime.parse(data[2], dtf_reserveTime);

                //検査項目に不要な値が入っている場合は削除
                String examination_item = data[0];
                String str_examination_item = examination_item.replaceAll("[^単純X線CTMRTV乳腺検査]", "");

                pev = new PatientExaminationView(
                        null,
                        toNumber(intStr_patient_id),
                        data[4].trim(),
                        data[5].trim(),
                        str_examination_item,
                        exam_date,
                        reserve_time,
                        null);

                pev_list.add(pev);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        putSessionScope(AttributeConst.PATIENT_EXAMINATIONS, pev_list);

        //csv取り込みデータ確認画面を表示
        forward(ForwardConst.FW_PATEXAM_CSV_CHECK);
    }

    /**
     * 読み込んだCSVデータの中で不要なレコードを消去
     * @throws ServletException
     * @throws IOException
     */
    public void csvModify() throws ServletException, IOException {

        //セッションからcsvで取り込んだ検査情報リストを取得
        List<PatientExaminationView> pev_list = getSessionScope(AttributeConst.PATIENT_EXAMINATIONS);

        //csvの取り込みをやめたいレコードのindex番号を取得
        int index_num = toNumber(getRequestParam(AttributeConst.PATEXAM_INDEX));

        //データがある場合、セッションからcsvで取り込んだ検査情報リストを削除し、残ったデータリストをセッションに登録。
        if (!(pev_list == null)) {
            //index番号を指定して、csvの取り込みをやめたいレコードを削除
            pev_list.remove(index_num);
            removeSessionScope(AttributeConst.PATIENT_DEVICE_LIST);
            putSessionScope(AttributeConst.PATIENT_DEVICE_LIST, pev_list);
        }

        //csv取り込みデータ確認画面を表示
        forward(ForwardConst.FW_PATEXAM_CSV_CHECK);
    }

    /**
     * 取り込んだCSVデータをデータベースに登録
     * @throws ServletException
     * @throws IOException
     */
    public void csvAllCreate() throws ServletException, IOException {

        //入力データが検査情報テーブルに既に存在するか確認するフラグ設定（duplicateCheck = true）
        Boolean duplicateCheck = true;

        System.out.println("duplicateCheck_________" + getRequestParam(AttributeConst.PATEXAM_DUPLICATE_CHECK));

        //入力データが検査情報テーブルのレコードと重複してもいい場合（duplicateCheck = false）＝同日に同じ検査があってもいい場合
        if (getRequestParam(AttributeConst.PATEXAM_DUPLICATE_CHECK) != null) {
            duplicateCheck = Boolean.valueOf(getRequestParam(AttributeConst.PATEXAM_DUPLICATE_CHECK));
        }

        System.out.println("duplicate ===========" + duplicateCheck);

        //セッションからcsvで取り込んだ検査情報リストを取得
        ArrayList<PatientExaminationView> pev_list = new ArrayList<>();
        pev_list = getSessionScope(AttributeConst.PATIENT_EXAMINATIONS);

        //リストに検査情報データがない場合、CSV取り込みデータ確認画面に戻る。
        if (pev_list == null || pev_list.isEmpty()) {
            putRequestScope(AttributeConst.ERR, MessageConst.E_NODATA.getMessage());
            //csv取り込みデータ確認画面に戻る
            forward(ForwardConst.FW_PATEXAM_CSV_CHECK);
        } else {

            ArrayList<PatientExaminationView> pev_list_copy = new ArrayList<PatientExaminationView>(pev_list);

            //リスト内の検査情報データの項目値をバリデーションし、エラーがあれば、データ確認画面に戻る
            Iterator<PatientExaminationView> patExam_it = pev_list_copy.iterator();
            while (patExam_it.hasNext()) {
                PatientExaminationView pev = (PatientExaminationView) patExam_it.next();

                //添付文書情報を登録
                List<String> errors = patExamservice.create(pev, duplicateCheck);

                if (errors.size() > 0) {
                    //登録中にエラーがあった場合
                    putSessionScope(AttributeConst.PATEXAM_ERR_PAT_NAME, pev.getPatient_name()); //エラーが生じた患者名
                    putRequestScope(AttributeConst.ERR, errors); //エラーリスト

                    //csv取り込みデータ確認画面に戻る
                    forward(ForwardConst.FW_PATEXAM_CSV_CHECK);
                } else {

                    pev_list.remove(0);
                    removeSessionScope(AttributeConst.PATIENT_EXAMINATIONS);
                    putSessionScope(AttributeConst.PATIENT_EXAMINATIONS, pev_list);

                    //重複チェックが解除されている(duplicateCheck = false)場合、次から重複チェックをする
                    duplicateCheck = true;
                }

            }

            //セッションに登録完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst.ACT_PATEXAM, ForwardConst.CMD_INDEX);
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
        int patient_id = toNumber(getRequestParam(AttributeConst.PATEXAM_PAT_ID));

        //患者IDを指定して、患者テーブルからその患者インスタンスを取得
        Patient p = patExamservice.findPatient(patient_id);

        List<PatientExamination> patientExaminations = null;
        List<PatientExaminationView> patientExamViews = null;
        long patientExamCount = 0;

        if (p != null) {

            //指定した患者がもつ検査情報のリストを取得
            patientExaminations = patExamservice.findByPat_id(p);

            patientExamViews = PatientExaminationConverter.toViewList(patientExaminations);

            //患者の検査数を取得
            patientExamCount = patExamservice.countPatExamByPatient(p);
        }

        putRequestScope(AttributeConst.PATIENT_EXAMINATIONS, patientExamViews); //取得した検査情報データ
        putRequestScope(AttributeConst.PATEXAM_COUNT_BY_DAY, patientExamCount); // 検査件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_PATEXAM_INDEX);
    }
}

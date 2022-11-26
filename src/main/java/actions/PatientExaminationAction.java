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
import validators.CsvValidator;

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

        //セッションにエラーメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        List<String> errors = getSessionScope(AttributeConst.ERR);
        if (errors != null) {
            putRequestScope(AttributeConst.ERR, errors);
            removeSessionScope(AttributeConst.ERR);
        }
        //CSVファイルのエラー行数を格納した変数もセッションから削除する
        List<Integer> errLineNumList = getSessionScope(AttributeConst.PATEXAM_CSV_ERR_LINE);
        if (errLineNumList != null) {
            putRequestScope(AttributeConst.PATEXAM_CSV_ERR_LINE, errLineNumList);
            removeSessionScope(AttributeConst.PATEXAM_CSV_ERR_LINE);
        }

        //CSVファイルのエラー行数(検査の可否)を格納した変数もセッションから削除する
        List<Integer> errLineDateList = getSessionScope(AttributeConst.PATEXAM_CSV_DATE_ERR_LINE);
        if (errLineDateList != null) {

            putRequestScope(AttributeConst.PATEXAM_CSV_DATE_ERR_LINE, errLineDateList);
            removeSessionScope(AttributeConst.PATEXAM_CSV_DATE_ERR_LINE);
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

        //検査日が入力されている場合のみ、LocalDateに。
        String inputExamDate = getRequestParam(AttributeConst.PATEXAM_EXAM_DATE);
        LocalDate examinationDate;
        if (!(inputExamDate == null || inputExamDate.equals(""))) {
            examinationDate = LocalDate.parse(getRequestParam(AttributeConst.PATEXAM_EXAM_DATE));
        } else {
            examinationDate = null;
        }

        //予約時間が入力されている場合のみ、LocalTimeに。
        String inputReservationTime = getRequestParam(AttributeConst.PATEXAM_RESERVATION_TIME);
        LocalTime reservationTime;
        if (!(inputReservationTime == null || inputReservationTime.equals(""))) {
            reservationTime = LocalTime.parse(getRequestParam(AttributeConst.PATEXAM_RESERVATION_TIME));
        } else {
            reservationTime = null;
        }

        //パラメータの値を元に検査情報のインスタンスを作成する
        PatientExaminationView pev = new PatientExaminationView(
                null,
                toNumber(getRequestParam(AttributeConst.PATEXAM_PAT_ID)),
                patientName,
                patientNameKana,
                getRequestParam(AttributeConst.PATEXAM_EXAM_ITEM),
                examinationDate,
                reservationTime,
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

        //入力データが検査情報テーブルのレコードと重複してもいい場合（duplicate = false） =同日に同じ時間に同じ検査を施行してもいい場合
        if (getRequestParam(AttributeConst.PATEXAM_DUPLICATE_CHECK) != null) {

            duplicateCheck = Boolean.valueOf(getRequestParam(AttributeConst.PATEXAM_DUPLICATE_CHECK));
        }

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //検査日が入力されている場合のみ、LocalDateに。
            String inputExamDate = getRequestParam(AttributeConst.PATEXAM_EXAM_DATE);
            LocalDate examinationDate;
            if (!(inputExamDate == null || inputExamDate.equals(""))) {
                examinationDate = LocalDate.parse(getRequestParam(AttributeConst.PATEXAM_EXAM_DATE));
            } else {
                examinationDate = null;
            }

            //予約時間が入力されている場合のみ、LocalTimeに。
            String inputReservationTime = getRequestParam(AttributeConst.PATEXAM_RESERVATION_TIME);
            LocalTime reservationTime;
            if (!(inputReservationTime == null || inputReservationTime.equals(""))) {
                reservationTime = LocalTime.parse(getRequestParam(AttributeConst.PATEXAM_RESERVATION_TIME));
            } else {
                reservationTime = null;
            }

            //パラメータの値を元に検査情報のインスタンスを作成する
            PatientExaminationView pev = new PatientExaminationView(
                    null,
                    toNumber(getRequestParam(AttributeConst.PATEXAM_PAT_ID)),
                    getRequestParam(AttributeConst.PATEXAM_PAT_NAME),
                    getRequestParam(AttributeConst.PATEXAM_PAT_NAME_KANA),
                    getRequestParam(AttributeConst.PATEXAM_EXAM_ITEM),
                    examinationDate,
                    reservationTime,
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

        //入力データが検査情報テーブルのレコードと重複してもいい場合（duplicate = false） =同日に同じ時間に同じ検査を施行してもいい場合
        if (getRequestParam(AttributeConst.PATEXAM_DUPLICATE_CHECK) != null) {
            duplicateCheck = Boolean.valueOf(getRequestParam(AttributeConst.PATEXAM_DUPLICATE_CHECK));
        }

        //CSRF対策 tokenのチェック
        if (checkToken()) {
            //idを条件に検査情報データを取得する
            PatientExaminationView pev = patExamservice.findOne(toNumber(getRequestParam(AttributeConst.PATEXAM_ID)));

            //入力された検査情報の内容を設定する
            pev.setExaminationItem(getRequestParam(AttributeConst.PATEXAM_EXAM_ITEM));
            pev.setExaminationDate(toLocalDate(getRequestParam(AttributeConst.PATEXAM_EXAM_DATE)));
            pev.setReservationTime(LocalTime.parse(getRequestParam(AttributeConst.PATEXAM_RESERVATION_TIME)));

            pev.setPatientId(toNumber(getRequestParam(AttributeConst.PATEXAM_PAT_ID)));
            pev.setPatientName(getRequestParam(AttributeConst.PATEXAM_PAT_NAME));
            pev.setPatientNameKana(getRequestParam(AttributeConst.PATEXAM_PAT_NAME_KANA));

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

        LocalDate examDate = null;
        LocalTime reserveTime = null;

        //ファイル形式がCSVでない場合、またはファイルの容量が100MBを超える場合はエラーメッセージを返す
        List<String> errors = CsvValidator.validate(filePart);

        if (errors.size() > 0) {
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策トークン
            putRequestScope(AttributeConst.ERR, errors); //エラーメッセージ

            //新規作成画面に戻る
            forward(ForwardConst.FW_PATEXAM_NEW);
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
                //検査日データが、yyyy/(M)M/(d)dの形式であることを確認（異なる形式の場合はエラーメッセージを返却）
                String examDateStr = data[1];
                String errorDate = CsvValidator.checkDate(examDateStr);

                //予約時間が9時から17時であるか検証（正しく入力されていない場合はエラーリストに追加）
                String reservationTime = data[2];
                String errorTime = CsvValidator.validateReservationTime(reservationTime);

                if (errorDate != null) {
                    errors.add(errorDate);
                } else if (errorTime != null) {
                    errors.add(errorTime);
                } else {
                    //CSVの検査日データをLocalDate型に変換
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/[]M/[]d");
                    examDate = LocalDate.parse(examDateStr, dtf);

                    //CSVの予約時間をLocalTime型に変換
                    DateTimeFormatter dtfReserveTime = DateTimeFormatter.ofPattern("[]H:mm");
                    reserveTime = LocalTime.parse(data[2], dtfReserveTime);
                }
            }
            //エラーがなければ、PatientExaminationView（患者の検査情報View）をインスタンス化
            if (errors.size() == 0) {
                //患者ID 数字以外に記号があれば削除
                String patientId = data[3];
                String intStrPatientId = patientId.replaceAll("[^0-9]", "");

                //検査項目に不要な値が入っている場合は削除
                String examinationItem = data[0];
                String strExaminationItem = examinationItem.replaceAll("[^単純X線CTMRTV乳腺検査]", "");

                pev = new PatientExaminationView(
                        null,
                        toNumber(intStrPatientId),
                        data[4].trim(),
                        data[5].trim(),
                        strExaminationItem,
                        examDate,
                        reserveTime,
                        null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //エラーがあれば新規作成画面に戻る
        if (errors.size() > 0) {
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策トークン
            putRequestScope(AttributeConst.ERR, errors); //エラーメッセージ

            forward(ForwardConst.FW_PATEXAM_NEW);

        } else if (errors.size() == 0) {
            //エラーがなければ、CSVデータ確認画面に
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用
            putRequestScope(AttributeConst.PATIENT_EXAMINATION, pev); //取得した検査情報データ

            //csvデータ確認画面に
            forward(ForwardConst.FW_PATEXAM_NEW);
        }
    }

    /**
     * CSVデータ（複数レコード）をまとめて取り込む
     * @throws ServletException
     * @throws IOException
     */

    public void csvAllImport() throws ServletException, IOException {

        Part filePart = getRequestPart(AttributeConst.PATEXAM_CSV);
        PatientExaminationView pev = null;
        List<PatientExaminationView> pevList = new ArrayList<>();

        LocalDate examDate = null; //検査日を格納する変数
        LocalTime reserveTime = null; //予約時間を格納する変数

        //ファイル形式がCSVでない場合、またはファイルの容量が100MBを超える場合はエラーメッセージを返す
        List<String> errors = CsvValidator.validate(filePart);

        if (errors.size() > 0) {
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策トークン
            putRequestScope(AttributeConst.ERR, errors); //エラーメッセージ

            //一覧画面に戻る
            forward(ForwardConst.FW_PATEXAM_INDEX);
        }

        //CSVの取り込み行数を格納する変数を設定
        int lineNum = 1;
        List<Integer> errLineNumList = new ArrayList<>(); //CSVデータの中で項目数が異常なデータの行数を格納
        List<Integer> errLineDateList = new ArrayList<>(); //CSVデータの中で検査日が異常な値をもつ行数を格納

        // CSV読み込み
        try (InputStream is = filePart.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                String errorDate = null; //検査日入力エラーを格納する変数をnullにして準備
                String errorTime = null; //予約時間入力エラーを格納する変数をnullにして準備

                //CSVで取り込むデータ数（項目数）が設定値（6個）以外の場合にエラーを返す。
                String error = CsvValidator.validateItemNum(data, 6);
                if (error != null && !errors.contains(error)) {
                    errors.add(error);
                } else if (error == null) {
                    //CSVの入力項目数が正しい場合、検査日が正しく入力されているか検証（正しく入力されていない場合はエラーリストに追加）
                    String examDateStr = data[1];
                    errorDate = CsvValidator.checkDate(examDateStr);

                    //予約時間が9時から17時であるか・形式が正しいか検証（正しく入力されていない場合はエラーリストに追加）
                    String reservationTime = data[2];
                    errorTime = CsvValidator.validateReservationTime(reservationTime);

                    if (errorDate != null && !errors.contains(errorDate)) {
                        errors.add(errorDate);
                    } else if (errorTime != null && !errors.contains(errorTime)) {
                        errors.add(errorTime);
                    } else {
                        //入力値の形式が正しければCSVの検査日データをLocalDate型に変換
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/[]M/[]d");
                        examDate = LocalDate.parse(examDateStr, dtf);

                        //CSVの予約時間をLocalTime型に変換
                        DateTimeFormatter dtfReserveTime = DateTimeFormatter.ofPattern("[]H:mm");
                        reserveTime = LocalTime.parse(reservationTime, dtfReserveTime);
                    }
                }

                //エラーがなければ、PatientDeviceView（患者の体内デバイスView）をインスタンス化
                if (errors.size() == 0) {

                    //患者IDに数字以外に記号があれば除去
                    String patientId = data[3];
                    String intStrPatientId = patientId.replaceAll("[^0-9]", "");

                    //検査項目に不要な値が入っている場合は削除
                    String examinationItem = data[0];
                    String strExaminationItem = examinationItem.replaceAll("[^単純X線CTMRTV乳腺検査]", "");

                    //苗字と名前の空白が半角スペースの場合、全角に変換
                    //患者名
                    String patientName = data[4].trim();
                    if (patientName != null) {
                        patientName = toZenkakuSpace(patientName);
                    }
                    //患者名（ひらがな）
                    String patientNameKana = data[5].trim();
                    if (patientNameKana != null) {
                        patientNameKana = toZenkakuSpace(patientNameKana);
                    }

                    pev = new PatientExaminationView(
                            null,
                            toNumber(intStrPatientId),
                            patientName,
                            patientNameKana,
                            strExaminationItem,
                            examDate,
                            reserveTime,
                            null);

                    pevList.add(pev);
                } else {
                    //エラーがある場合、その行数をリストに追加
                    if (error != null) {
                        errLineNumList.add(lineNum);
                    } else if (errorDate != null || errorTime != null) {
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
            putSessionScope(AttributeConst.PATEXAM_CSV_ERR_LINE, errLineNumList); // CSVの項目数異常によりエラーが起きたCSVファイルの行数
            putSessionScope(AttributeConst.PATEXAM_CSV_DATE_ERR_LINE, errLineDateList); //CSVデータの中で検査日または予約時間の値が異常値を示した行数

            redirect(ForwardConst.ACT_PATEXAM, ForwardConst.CMD_INDEX);

        } else if (errors.size() == 0) {

            //エラーがなければ、CSVデータ確認画面に
            putSessionScope(AttributeConst.PATIENT_EXAMINATIONS, pevList);

            //複数csv取り込みデータ確認画面を表示
            forward(ForwardConst.FW_PATEXAM_CSV_CHECK);
        }
    }

    /**
     * 読み込んだCSVデータの中で不要なレコードを消去
     * @throws ServletException
     * @throws IOException
     */
    public void csvModify() throws ServletException, IOException {

        //セッションからcsvで取り込んだ検査情報リストを取得
        List<PatientExaminationView> pevList = getSessionScope(AttributeConst.PATIENT_EXAMINATIONS);

        //csvの取り込みをやめたいレコードのindex番号を取得
        int indexNum = toNumber(getRequestParam(AttributeConst.PATEXAM_INDEX));

        //データがある場合、セッションからcsvで取り込んだ検査情報リストを削除し、残ったデータリストをセッションに登録。
        if (!(pevList == null)) {
            //index番号を指定して、csvの取り込みをやめたいレコードを削除
            pevList.remove(indexNum);
            removeSessionScope(AttributeConst.PATIENT_DEVICE_LIST);
            putSessionScope(AttributeConst.PATIENT_DEVICE_LIST, pevList);
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

        //入力データが検査情報テーブルのレコードと重複してもいい場合（duplicateCheck = false）＝同日に同じ検査があってもいい場合
        if (getRequestParam(AttributeConst.PATEXAM_DUPLICATE_CHECK) != null) {
            duplicateCheck = Boolean.valueOf(getRequestParam(AttributeConst.PATEXAM_DUPLICATE_CHECK));
        }

        //セッションからcsvで取り込んだ検査情報リストを取得
        ArrayList<PatientExaminationView> pevList = new ArrayList<>();
        pevList = getSessionScope(AttributeConst.PATIENT_EXAMINATIONS);

        //リストに検査情報データがない場合、CSV取り込みデータ確認画面に戻る。
        if (pevList == null || pevList.isEmpty()) {
            putRequestScope(AttributeConst.ERR, MessageConst.E_NODATA.getMessage());
            //一覧画面に戻る
            forward(ForwardConst.FW_PATEXAM_INDEX);
            return;
        } else {

            ArrayList<PatientExaminationView> pevListCopy = new ArrayList<PatientExaminationView>(pevList);

            //リスト内の検査情報データの項目値をバリデーションし、エラーがあれば、データ確認画面に戻る
            Iterator<PatientExaminationView> patExamIt = pevListCopy.iterator();
            while (patExamIt.hasNext()) {
                PatientExaminationView pev = (PatientExaminationView) patExamIt.next();

                //添付文書情報を登録
                List<String> errors = patExamservice.create(pev, duplicateCheck);

                if (errors.size() > 0) {
                    //登録中にエラーがあった場合
                    putSessionScope(AttributeConst.PATEXAM_ERR_PAT_NAME, pev.getPatientName()); //エラーが生じた患者名
                    putRequestScope(AttributeConst.ERR, errors); //エラーリスト

                    //csv取り込みデータ確認画面に戻る
                    forward(ForwardConst.FW_PATEXAM_CSV_CHECK);
                    return;
                } else {

                    pevList.remove(0);
                    removeSessionScope(AttributeConst.PATIENT_EXAMINATIONS);
                    putSessionScope(AttributeConst.PATIENT_EXAMINATIONS, pevList);

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
        int patientId = toNumber(getRequestParam(AttributeConst.PATEXAM_PAT_ID));

        //患者IDを指定して、患者テーブルからその患者インスタンスを取得
        Patient p = patExamservice.findPatient(patientId);

        List<PatientExamination> patientExaminations = null;
        List<PatientExaminationView> patientExamViews = null;
        long patientExamCount = 0;

        if (p != null) {

            //指定した患者がもつ検査情報のリストを取得
            patientExaminations = patExamservice.findByPatId(p);

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

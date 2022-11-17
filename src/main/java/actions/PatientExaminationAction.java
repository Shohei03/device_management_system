package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.PatientExaminationView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
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

        System.out.println(page);

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

                }

                //新規登録画面を再表示
                forward(ForwardConst.FW_PATEXAM_NEW);
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

}

package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

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

        //体内デバイス情報の空インスタンスに、登録日・更新日=今日の日付を設定する
        PatientDeviceView pdv = new PatientDeviceView();
        pdv.setCreatedAt(LocalDate.now());
        pdv.setUpdatedAt(LocalDate.now());
        putRequestScope(AttributeConst.PATIENT_DEVICE, pdv); //日付のみ設定済みの体内デバイスインスタンス

        //新規登録画面を表示
        forward(ForwardConst.FW_PATDEV_NEW);
    }

    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //デバイス埋込日が入力されている場合のみ、LocalDateに。
            String inputDate = getRequestParam(AttributeConst.PATDEV_IMP_DATE);
            LocalDate implantedAt;
            if(!(inputDate == null || inputDate.equals(""))) {
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
            List<String> errors = service.create(pdv);

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

        if(pdv == null) {
            //該当の体内デバイスデータが存在しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);
        } else {
            putRequestScope(AttributeConst.PATIENT_DEVICE, pdv);  //取得した体内デバイスデータ

            //詳細画面を表示
            forward(ForwardConst.FW_PATDEV_SHOW);
        }
    }

}

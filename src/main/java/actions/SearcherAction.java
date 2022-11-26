package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.SearchPatientDeviceView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import models.Examination;
import models.Patient;
import models.PatientDevice;
import services.PatientDeviceService;
import services.PatientExaminationService;

/**
 *
 * デバイス検索画面に関する処理を行うActionクラス
 *
 */
public class SearcherAction extends ActionBase {

    /**
     * indexメソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        //メソッドを実行
        invoke();

    }

    /**
     * 従業員が所属している部署で検査をうける患者のデバイス一覧画面を表示する
     */
    public void searchByDepartment() throws ServletException, IOException {
        //指定されたページ数
        int page = getPage();

        //検索日が選択されていれば、取得
        //指定日（なければ今日の日付）
        LocalDate date = toLocalDate(getRequestParam(AttributeConst.SEARCH_DATE));

        //テストデータ検索用////////////////
        if (getRequestParam(AttributeConst.SEARCH_DATE) == null) {

            date = LocalDate.of(2022, 11, 01);
        }

        //検査項目が選択されていれば、取得
        String examinationItem = getRequestParam(AttributeConst.SEARCH_EXAM_ITEM);

        //検査項目が選択されていなければ、従業員の所属部署を検査項目に指定。
        if (examinationItem == null) {
            //セッションからログイン中の従業員情報を取得
            EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            if (loginEmployee.getDepartment().equals(AttributeConst.DEP_GENERAL.getValue())) {
                examinationItem = AttributeConst.EXAM_X_RAY.getValue();
            } else if (loginEmployee.getDepartment().equals(AttributeConst.DEP_CT.getValue())) {
                examinationItem = AttributeConst.EXAM_CT.getValue();
            } else if (loginEmployee.getDepartment().equals(AttributeConst.DEP_TV.getValue())) {
                examinationItem = AttributeConst.EXAM_TV.getValue();
            } else if (loginEmployee.getDepartment().equals(AttributeConst.DEP_MRI.getValue())) {
                examinationItem = AttributeConst.EXAM_MRI.getValue();
            } else if (loginEmployee.getDepartment().equals(AttributeConst.DEP_MAMMA.getValue())) {
                examinationItem = AttributeConst.EXAM_Manma.getValue();
            }
        }

        SearchPatientDeviceView spdv = null;
        List<SearchPatientDeviceView> spdvList = new ArrayList<>();
        PatientExaminationService patExamService = new PatientExaminationService();
        PatientDeviceService patDevService = new PatientDeviceService();

        //各検査の検査可以外のカウント（初期値0に設定）
        int manmaNotSafeCount = 0;
        int xrayNotSafeCount = 0;
        int ctNotSafeCount = 0;
        int tvNotSafeCount = 0;
        int mrNotSafeCount = 0;

        //検査項目に"すべて"が選択されていない場合の処理
        if (!examinationItem.equals("すべて")) {

            Examination e = patExamService.findExamination(examinationItem);

            //指定された検査項目の患者情報リスト(患者重複なし)を取得
            List<Patient> PatListByExamItemNoDupli = patExamService.getPatNoDupliByExamItemInTheExamDate(date, e);

            int id = 1;

            for (Patient p : PatListByExamItemNoDupli) {
                List<PatientDevice> pdsNoDupli = patDevService.findAllPatDevbyPatient(p);
                for (PatientDevice pd : pdsNoDupli) {
                    spdv = new SearchPatientDeviceView(
                            id,
                            pd.getPatient().getPatientId(),
                            pd.getPatient().getPatientName(),
                            pd.getPatient().getPatientNameKana(),
                            pd.getPackageInsert().getApprovalNumber(),
                            pd.getPackageInsert().getJmdn().getGeneralName(),
                            pd.getPackageInsert().getDeviceName(),
                            pd.getImplantedAt(),
                            pd.getPackageInsert().getAcceptabilityOfManmaExam(),
                            pd.getPackageInsert().getAcceptabilityOfXrayExam(),
                            pd.getPackageInsert().getAcceptabilityOfCtExam(),
                            pd.getPackageInsert().getAcceptabilityOfTvExam(),
                            pd.getPackageInsert().getAcceptabilityOfMrExam());

                    if (!pd.getPackageInsert().getAcceptabilityOfManmaExam()
                            .equals(AttributeConst.PACK_EXM_SAFE.getValue())) {
                        manmaNotSafeCount++;
                    }
                    if (!pd.getPackageInsert().getAcceptabilityOfXrayExam()
                            .equals(AttributeConst.PACK_EXM_SAFE.getValue())) {
                        xrayNotSafeCount++;
                    }
                    if (!pd.getPackageInsert().getAcceptabilityOfCtExam()
                            .equals(AttributeConst.PACK_EXM_SAFE.getValue())) {
                        ctNotSafeCount++;
                    }
                    if (!pd.getPackageInsert().getAcceptabilityOfTvExam()
                            .equals(AttributeConst.PACK_EXM_SAFE.getValue())) {
                        tvNotSafeCount++;
                    }
                    if (!pd.getPackageInsert().getAcceptabilityOfMrExam()
                            .equals(AttributeConst.PACK_EXM_SAFE.getValue())) {
                        mrNotSafeCount++;
                    }

                    spdvList.add(spdv);

                    id++;
                }
            }
        } else {
            //検査項目に"すべて"が選択された場合の処理

            //検索日の患者情報リスト(患者重複なし)を取得（すべての検査項目）
            List<Patient> PatListNoDupli = patExamService.getPatNoDupliInTheExamDate(date);
            spdvList = new ArrayList<>();

            int id = 1;

            for (Patient p : PatListNoDupli) {
                List<PatientDevice> pdsNoDupli = patDevService.findAllPatDevbyPatient(p);
                for (PatientDevice pd : pdsNoDupli) {
                    spdv = new SearchPatientDeviceView(
                            id,
                            pd.getPatient().getPatientId(),
                            pd.getPatient().getPatientName(),
                            pd.getPatient().getPatientNameKana(),
                            pd.getPackageInsert().getApprovalNumber(),
                            pd.getPackageInsert().getJmdn().getGeneralName(),
                            pd.getPackageInsert().getDeviceName(),
                            pd.getImplantedAt(),
                            pd.getPackageInsert().getAcceptabilityOfManmaExam(),
                            pd.getPackageInsert().getAcceptabilityOfXrayExam(),
                            pd.getPackageInsert().getAcceptabilityOfCtExam(),
                            pd.getPackageInsert().getAcceptabilityOfTvExam(),
                            pd.getPackageInsert().getAcceptabilityOfMrExam());

                    spdvList.add(spdv);

                    id++;
                }
            }
        }

        Map<String, Integer> notSafeCountMap = new HashMap<>();
        notSafeCountMap.put(AttributeConst.EXAM_X_RAY.getValue(), xrayNotSafeCount);
        notSafeCountMap.put(AttributeConst.EXAM_CT.getValue(), ctNotSafeCount);
        notSafeCountMap.put(AttributeConst.EXAM_TV.getValue(), tvNotSafeCount);
        notSafeCountMap.put(AttributeConst.EXAM_MRI.getValue(), mrNotSafeCount);
        notSafeCountMap.put(AttributeConst.EXAM_Manma.getValue(), manmaNotSafeCount);

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        putSessionScope(AttributeConst.SEARCH_DEVICE, spdvList); //検索用に取得した体内デバイスデータ（検査重複なし）
        putSessionScope(AttributeConst.SEARCH_EXAM_ITEM, examinationItem); //指定している検査項目
        putSessionScope(AttributeConst.SEARCH_DATE, date); //検索日
        putRequestScope(AttributeConst.SEARCH_NOTSAFE_COUNT, notSafeCountMap); //検査不可・条件付き可能をあわせた件数
        putRequestScope(AttributeConst.SEARCHES_COUNT, spdvList.size()); //件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコード数

        //検査項目ごとの検索一覧画面を表示
        forward(ForwardConst.FW_SEARCH_INDEX_BY_EXAM_ITEM);
    }

    /**
     * 詳細画面を表示
     * @throws ServletException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void show() throws ServletException, IOException {
        List<SearchPatientDeviceView> svList = new ArrayList<>();

        //検査項目を取得

        //設定した検査項目を取得
        String examinationItem = getRequestParam(AttributeConst.SEARCH_EXAM_ITEM);

        //検索した患者の体内デバイス一覧をセッションから取得
        svList = (List<SearchPatientDeviceView>) getSessionScope(AttributeConst.SEARCH_DEVICE);

        String idStr = getRequestParam(AttributeConst.SEARCH_ID);

        Integer id = toNumber(idStr);

        int patientId = svList.get(id - 1).getPatientId();

        SearchPatientDeviceView spdv = null;
        List<SearchPatientDeviceView> spdvList = new ArrayList<>();
        PatientDeviceService patDevService = new PatientDeviceService();

        //患者IDを指定して、患者テーブルからその患者インスタンスを取得
        Patient p = patDevService.findPatient(patientId);

        int spdv_id = 1;

        //対愛デバイステーブルから、指定した患者インスタンスのレコードを取得

        List<PatientDevice> pds_noDupli = patDevService.findAllPatDevbyPatient(p);
        for (PatientDevice pd : pds_noDupli) {
            spdv = new SearchPatientDeviceView(
                    spdv_id,
                    pd.getPatient().getPatientId(),
                    pd.getPatient().getPatientName(),
                    pd.getPatient().getPatientNameKana(),
                    pd.getPackageInsert().getApprovalNumber(),
                    pd.getPackageInsert().getJmdn().getGeneralName(),
                    pd.getPackageInsert().getDeviceName(),
                    pd.getImplantedAt(),
                    pd.getPackageInsert().getAcceptabilityOfManmaExam(),
                    pd.getPackageInsert().getAcceptabilityOfXrayExam(),
                    pd.getPackageInsert().getAcceptabilityOfCtExam(),
                    pd.getPackageInsert().getAcceptabilityOfTvExam(),
                    pd.getPackageInsert().getAcceptabilityOfMrExam());

            spdvList.add(spdv);
        }
        removeSessionScope(AttributeConst.SEARCH_EXAM_ITEM);

        removeSessionScope(AttributeConst.SEARCH_DEVICE);

        putRequestScope(AttributeConst.SEARCH_PAT, p);
        putRequestScope(AttributeConst.SEARCH_ID, id);
        putSessionScope(AttributeConst.SEARCH_EXAM_ITEM, examinationItem);
        putSessionScope(AttributeConst.SEARCH_DEVICE, spdvList); //患者IDをもとに取得した体内デバイスデータ（検査重複なし）

        //検索結果画面を表示
        forward(ForwardConst.FW_SEARCH_SHOW);

    }

    /**
     * 患者IDをもとに検索した結果（体内デバイス情報）を取得
     * @throws ServletException
     * @throws IOException
     */
    public void searchByPatId() throws ServletException, IOException {

        //検索する患者IDを取得
        int patientId = toNumber(getRequestParam(AttributeConst.SEARCH_PAT_ID));

        SearchPatientDeviceView spdv = null;
        List<SearchPatientDeviceView> spdvList = new ArrayList<>();
        PatientDeviceService patDevService = new PatientDeviceService();

        //患者IDを指定して、患者テーブルからその患者インスタンスを取得
        Patient p = patDevService.findPatient(patientId);

        int id = 1;

        List<PatientDevice> pdsNoDupli = patDevService.findAllPatDevbyPatient(p);
        for (PatientDevice pd : pdsNoDupli) {
            spdv = new SearchPatientDeviceView(
                    id,
                    pd.getPatient().getPatientId(),
                    pd.getPatient().getPatientName(),
                    pd.getPatient().getPatientNameKana(),
                    pd.getPackageInsert().getApprovalNumber(),
                    pd.getPackageInsert().getJmdn().getGeneralName(),
                    pd.getPackageInsert().getDeviceName(),
                    pd.getImplantedAt(),
                    pd.getPackageInsert().getAcceptabilityOfManmaExam(),
                    pd.getPackageInsert().getAcceptabilityOfXrayExam(),
                    pd.getPackageInsert().getAcceptabilityOfCtExam(),
                    pd.getPackageInsert().getAcceptabilityOfTvExam(),
                    pd.getPackageInsert().getAcceptabilityOfMrExam());

            spdvList.add(spdv);
        }

        removeSessionScope(AttributeConst.SEARCH_DEVICE);

        putRequestScope(AttributeConst.SEARCH_PAT, p);
        putSessionScope(AttributeConst.SEARCH_DEVICE, spdvList); //患者IDをもとに取得した体内デバイスデータ（検査重複なし）

        //検索結果画面を表示
        forward(ForwardConst.FW_SEARCH_INDEX_BY_PAT_ID);

    }

}

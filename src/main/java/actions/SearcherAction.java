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
import actions.views.SearchView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import models.Examination;
import models.Patient;
import models.PatientDevice;
import models.PatientExamination;
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
     * 一覧画面を表示する
     */
    public void index() throws ServletException, IOException {

        PatientDeviceService patDev_service = new PatientDeviceService();

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //指定されたページ数の一覧画面に表示する体内デバイスデータを取得
        int page = getPage();
        List<SearchPatientDeviceView> sPatientDevices = patDev_service.getDevAndPackPerPage(page);

        //全体内デバイスデータの件数を取得
        long patientsDevicesCount = patDev_service.countAll();

        putRequestScope(AttributeConst.SEARCH_PAT_DEV_PACKS, sPatientDevices); //検索用に取得した体内デバイスデータのリスト（添付文書つき）
        putRequestScope(AttributeConst.PATDEV_COUNT, patientsDevicesCount); //全ての体内デバイスデータの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコード数

        //一覧画面を表示
        forward(ForwardConst.FW_SEARCH_INDEX);
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
        String examination_item = getRequestParam(AttributeConst.SEARCH_EXAM_ITEM);

        //検査項目が選択されていなければ、従業員の所属部署を検査項目に指定。

        if (examination_item == null) {
            //セッションからログイン中の従業員情報を取得
            EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            if (loginEmployee.getDepartment().equals(AttributeConst.DEP_GENERAL.getValue())) {
                examination_item = AttributeConst.EXAM_X_RAY.getValue();
            } else if (loginEmployee.getDepartment().equals(AttributeConst.DEP_CT.getValue())) {
                examination_item = AttributeConst.EXAM_CT.getValue();
            } else if (loginEmployee.getDepartment().equals(AttributeConst.DEP_TV.getValue())) {
                examination_item = AttributeConst.EXAM_TV.getValue();
            } else if (loginEmployee.getDepartment().equals(AttributeConst.DEP_MRI.getValue())) {
                examination_item = AttributeConst.EXAM_MRI.getValue();
            } else if (loginEmployee.getDepartment().equals(AttributeConst.DEP_MAMMA.getValue())) {
                examination_item = AttributeConst.EXAM_Manma.getValue();
            }
        }

        SearchPatientDeviceView spdv = null;
        List<SearchPatientDeviceView> spdv_list = new ArrayList<>();
        PatientExaminationService patExamService = new PatientExaminationService();
        PatientDeviceService patDev_service = new PatientDeviceService();
        List<SearchView> sv_list = new ArrayList<>();
        SearchView sv = null;

        int Manma_notSafe_count = 0;
        int X_ray_notSafe_count = 0;
        int CT_notSafe_count = 0;
        int TV_notSafe_count = 0;
        int MR_notSafe_count = 0;

        //検査項目に"すべて"が選択されていない場合の処理
        if (!examination_item.equals("すべて")) {

            Examination e = patExamService.findExamination(examination_item);

            //指定された検査項目の検査情報リストを取得
            List<PatientExamination> pe_list = patExamService.getTheExamInTheExamDate(date, e);

            List<PatientDevice> pd_list = new ArrayList<>();

            int id = 1;

            for (PatientExamination pe : pe_list) {
                pd_list = patDev_service.findAllPatDevbyPatient(pe.getPatient());
                for (PatientDevice pd : pd_list) {
                    sv = new SearchView(
                            id,
                            pe.getId(), // PatientExaminationのid
                            pd.getId(), //PatientDeviceのid
                            pd.getPatient().getPatient_id(),
                            pd.getPatient().getPatient_name(),
                            pd.getPatient().getPatient_name_kana(),
                            pe.getExamination().getExamination_item(),
                            pe.getExamination_date(),
                            pe.getReservation_time(),
                            pd.getPackageInsert().getApproval_number(),
                            pd.getPackageInsert().getJmdn().getGeneral_name(),
                            pd.getPackageInsert().getDevice_name(),
                            pd.getImplantedAt(),
                            pd.getPackageInsert().getAcceptability_of_Manma_exam(),
                            pd.getPackageInsert().getAcceptability_of_X_ray_exam(),
                            pd.getPackageInsert().getAcceptability_of_CT_exam(),
                            pd.getPackageInsert().getAcceptability_of_TV_exam(),
                            pd.getPackageInsert().getAcceptability_of_MR_exam());
                    id++;

                    sv_list.add(sv);
                }

            }

            //指定された検査項目の患者情報リスト(患者重複なし)を取得
            List<Patient> PatListByExamItem_noDupli = patExamService.getPatNoDupliByExamItemInTheExamDate(date, e);

            int spdv_id = 1;

            for (Patient p : PatListByExamItem_noDupli) {
                List<PatientDevice> pds_noDupli = patDev_service.findAllPatDevbyPatient(p);
                for (PatientDevice pd : pds_noDupli) {
                    spdv = new SearchPatientDeviceView(
                            spdv_id,
                            pd.getPatient().getPatient_id(),
                            pd.getPatient().getPatient_name(),
                            pd.getPatient().getPatient_name_kana(),
                            pd.getPackageInsert().getApproval_number(),
                            pd.getPackageInsert().getJmdn().getGeneral_name(),
                            pd.getPackageInsert().getDevice_name(),
                            pd.getImplantedAt(),
                            pd.getPackageInsert().getAcceptability_of_Manma_exam(),
                            pd.getPackageInsert().getAcceptability_of_X_ray_exam(),
                            pd.getPackageInsert().getAcceptability_of_CT_exam(),
                            pd.getPackageInsert().getAcceptability_of_TV_exam(),
                            pd.getPackageInsert().getAcceptability_of_MR_exam());

                    if (!pd.getPackageInsert().getAcceptability_of_Manma_exam()
                            .equals(AttributeConst.PACK_EXM_SAFE.getValue())) {
                        Manma_notSafe_count++;
                    }
                    if (!pd.getPackageInsert().getAcceptability_of_X_ray_exam()
                            .equals(AttributeConst.PACK_EXM_SAFE.getValue())) {
                        X_ray_notSafe_count++;
                    }
                    if (!pd.getPackageInsert().getAcceptability_of_CT_exam()
                            .equals(AttributeConst.PACK_EXM_SAFE.getValue())) {
                        CT_notSafe_count++;
                    }
                    if (!pd.getPackageInsert().getAcceptability_of_TV_exam()
                            .equals(AttributeConst.PACK_EXM_SAFE.getValue())) {
                        TV_notSafe_count++;
                    }
                    if (!pd.getPackageInsert().getAcceptability_of_MR_exam()
                            .equals(AttributeConst.PACK_EXM_SAFE.getValue())) {
                        MR_notSafe_count++;
                    }

                    spdv_list.add(spdv);

                    spdv_id++;

                }
            }
        } else {
            //検査項目に"すべて"が選択された場合の処理

            //検索日の患者情報リスト(患者重複なし)を取得（すべての検査項目）
            List<Patient> PatList_noDupli = patExamService.getPatNoDupliInTheExamDate(date);
            spdv_list = new ArrayList<>();

            int spdv_id = 1;

            for (Patient p : PatList_noDupli) {
                List<PatientDevice> pds_noDupli = patDev_service.findAllPatDevbyPatient(p);
                for (PatientDevice pd : pds_noDupli) {
                    spdv = new SearchPatientDeviceView(
                            spdv_id,
                            pd.getPatient().getPatient_id(),
                            pd.getPatient().getPatient_name(),
                            pd.getPatient().getPatient_name_kana(),
                            pd.getPackageInsert().getApproval_number(),
                            pd.getPackageInsert().getJmdn().getGeneral_name(),
                            pd.getPackageInsert().getDevice_name(),
                            pd.getImplantedAt(),
                            pd.getPackageInsert().getAcceptability_of_Manma_exam(),
                            pd.getPackageInsert().getAcceptability_of_X_ray_exam(),
                            pd.getPackageInsert().getAcceptability_of_CT_exam(),
                            pd.getPackageInsert().getAcceptability_of_TV_exam(),
                            pd.getPackageInsert().getAcceptability_of_MR_exam());

                    spdv_list.add(spdv);

                    spdv_id++;
                }
            }
        }
        System.out.println("CT_notSafe_count" + CT_notSafe_count);

        Map<String, Integer> notSafeCountMap = new HashMap<>();
        notSafeCountMap.put(AttributeConst.EXAM_X_RAY.getValue(), X_ray_notSafe_count);
        notSafeCountMap.put(AttributeConst.EXAM_CT.getValue(), CT_notSafe_count);
        notSafeCountMap.put(AttributeConst.EXAM_TV.getValue(), TV_notSafe_count);
        notSafeCountMap.put(AttributeConst.EXAM_MRI.getValue(), MR_notSafe_count);
        notSafeCountMap.put(AttributeConst.EXAM_Manma.getValue(), Manma_notSafe_count);

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        putSessionScope(AttributeConst.SEARCHES_BY_EXAM_ITEM, sv_list); //検索用に取得した体内デバイスデータのリスト（添付文書つき）
        putSessionScope(AttributeConst.SEARCH_DEVICE, spdv_list); //検索用に取得した体内デバイスデータ（検査重複なし）
        putSessionScope(AttributeConst.SEARCH_EXAM_ITEM, examination_item); //指定している検査項目
        putSessionScope(AttributeConst.SEARCH_DATE, date); //検索日
        putRequestScope(AttributeConst.SEARCH_NOTSAFE_COUNT, notSafeCountMap); //検査不可・条件付き可能をあわせた件数
        putRequestScope(AttributeConst.SEARCHES_COUNT, sv_list.size()); //件数
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
    public void show() throws ServletException, IOException {
        List<SearchPatientDeviceView> sv_list = new ArrayList<>();

        //検索日を取得
        LocalDate date = getSessionScope(AttributeConst.SEARCH_DATE);

        //検査項目を取得
        String examination_item = getRequestParam(AttributeConst.SEARCH_EXAM_ITEM);

        sv_list = (List<SearchPatientDeviceView>) getSessionScope(AttributeConst.SEARCH_DEVICE);

        String id_str = getRequestParam(AttributeConst.SEARCH_ID);

        Integer id = toNumber(id_str);

        int patient_id = sv_list.get(id - 1).getPatient_id();

        SearchPatientDeviceView spdv = null;
        List<SearchPatientDeviceView> spdv_list = new ArrayList<>();
        PatientDeviceService patDev_service = new PatientDeviceService();

        //患者IDを指定して、患者テーブルからその患者インスタンスを取得
        Patient p = patDev_service.findPatient(patient_id);

        int spdv_id = 1;

        List<PatientDevice> pds_noDupli = patDev_service.findAllPatDevbyPatient(p);
        for (PatientDevice pd : pds_noDupli) {
            spdv = new SearchPatientDeviceView(
                    spdv_id,
                    pd.getPatient().getPatient_id(),
                    pd.getPatient().getPatient_name(),
                    pd.getPatient().getPatient_name_kana(),
                    pd.getPackageInsert().getApproval_number(),
                    pd.getPackageInsert().getJmdn().getGeneral_name(),
                    pd.getPackageInsert().getDevice_name(),
                    pd.getImplantedAt(),
                    pd.getPackageInsert().getAcceptability_of_Manma_exam(),
                    pd.getPackageInsert().getAcceptability_of_X_ray_exam(),
                    pd.getPackageInsert().getAcceptability_of_CT_exam(),
                    pd.getPackageInsert().getAcceptability_of_TV_exam(),
                    pd.getPackageInsert().getAcceptability_of_MR_exam());

            spdv_list.add(spdv);
        }
        removeSessionScope(AttributeConst.SEARCH_EXAM_ITEM);

        removeSessionScope(AttributeConst.SEARCH_DEVICE);

        putRequestScope(AttributeConst.SEARCH_PAT, p);
        putRequestScope(AttributeConst.SEARCH_ID, id);
        putSessionScope(AttributeConst.SEARCH_EXAM_ITEM, examination_item);
        putSessionScope(AttributeConst.SEARCH_DEVICE, spdv_list); //患者IDをもとに取得した体内デバイスデータ（検査重複なし）

        //検索結果画面を表示
        forward(ForwardConst.FW_SEARCH_SHOW);

    }

    /**
     * 詳細画面を表示
     * @throws ServletException
     * @throws IOException
     */
    public void show2() throws ServletException, IOException {
        List<SearchPatientDeviceView> sv_list = new ArrayList<>();
        // sv_list = (List<SearchView>) getSessionScope(AttributeConst.SEARCHES_BY_EXAM_ITEM);

        sv_list = (List<SearchPatientDeviceView>) getSessionScope(AttributeConst.SEARCH_DEVICE);

        System.out.println("String_id+++++" + getRequestParam(AttributeConst.SEARCH_ID));

        String id_str = getRequestParam(AttributeConst.SEARCH_ID);

        Integer id = toNumber(id_str);

        List<SearchPatientDeviceView> search_detail_list = new ArrayList<>();

        SearchPatientDeviceView searchShow = null;

        //検索日を取得
        LocalDate date = getSessionScope(AttributeConst.SEARCH_DATE);

        //検査項目を取得
        String examination_item = getSessionScope(AttributeConst.SEARCH_EXAM_ITEM);

        for (SearchPatientDeviceView sv : sv_list) {

            if (sv.getId() == id) {
                searchShow = sv;
            }
        }

        for (SearchPatientDeviceView sv : sv_list) {

            if (sv.getPatient_id() == searchShow.getPatient_id()) {
                search_detail_list.add(sv);
            }
        }
        putRequestScope(AttributeConst.SEARCH_EXAM_ITEM, examination_item); //検査項目
        putRequestScope(AttributeConst.SEARCH_DEATIL, searchShow); //詳細データ
        putRequestScope(AttributeConst.SEARCH_DEATILS, search_detail_list); //指定した患者のSearchViewリスト
        //詳細画面を表示
        forward(ForwardConst.FW_SEARCH_SHOW);
    }

    /**
     * 患者IDをもとに検索した結果（体内デバイス情報）を取得
     * @throws ServletException
     * @throws IOException
     */
    public void searchByPatId() throws ServletException, IOException {

        int patient_id = toNumber(getRequestParam(AttributeConst.SEARCH_PAT_ID));

        SearchPatientDeviceView spdv = null;
        List<SearchPatientDeviceView> spdv_list = new ArrayList<>();
        PatientDeviceService patDev_service = new PatientDeviceService();

        //患者IDを指定して、患者テーブルからその患者インスタンスを取得
        Patient p = patDev_service.findPatient(patient_id);

        int id = 1;

        List<PatientDevice> pds_noDupli = patDev_service.findAllPatDevbyPatient(p);
        for (PatientDevice pd : pds_noDupli) {
            spdv = new SearchPatientDeviceView(
                    id,
                    pd.getPatient().getPatient_id(),
                    pd.getPatient().getPatient_name(),
                    pd.getPatient().getPatient_name_kana(),
                    pd.getPackageInsert().getApproval_number(),
                    pd.getPackageInsert().getJmdn().getGeneral_name(),
                    pd.getPackageInsert().getDevice_name(),
                    pd.getImplantedAt(),
                    pd.getPackageInsert().getAcceptability_of_Manma_exam(),
                    pd.getPackageInsert().getAcceptability_of_X_ray_exam(),
                    pd.getPackageInsert().getAcceptability_of_CT_exam(),
                    pd.getPackageInsert().getAcceptability_of_TV_exam(),
                    pd.getPackageInsert().getAcceptability_of_MR_exam());

            spdv_list.add(spdv);
        }

        removeSessionScope(AttributeConst.SEARCH_DEVICE);

        putRequestScope(AttributeConst.SEARCH_PAT, p);
        putSessionScope(AttributeConst.SEARCH_DEVICE, spdv_list); //患者IDをもとに取得した体内デバイスデータ（検査重複なし）

        //検索結果画面を表示
        forward(ForwardConst.FW_SEARCH_INDEX_BY_PAT_ID);

    }

}

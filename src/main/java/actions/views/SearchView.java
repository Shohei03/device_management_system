package actions.views;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 検索画面の表示を扱うViewモデル
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchView {

    /**
     * id
     */
    private Integer id;

    /**
     * PatientExamination id
     */
    private Integer patExam_id;

    /**
     * PatientDevice id
     */
    private Integer patDevice_id;

    /**
     * 患者ID
     */
    private Integer patient_id;

    /**
     * 患者名
     */
    private String patient_name;

    /**
     * 患者名（ひらがな）
     */
    private String patient_name_kana;

    /**
     * 検査項目
     */
    private String examination_item;

    /**
     * 検査日
     */
    private LocalDate examination_date;

    /**
     * 検査の予約時間
     */
    private LocalTime reservation_time;

    /**
     * 添付文書（機器）承認番号
     */
    private String approval_number;

    /**
     * 一般的名称
     */
    private String general_name;

    /**
     * デバイスの販売名
     */
    private String device_name;

    /**
     * デバイスの埋込日
     */
    private LocalDate implantedAt;

    /**
     * 乳腺X線検査の可否
     */
    private String acceptability_of_Manma_exam;

    /**
     * 一般（X線）検査の可否
     */
    private String acceptability_of_X_ray_exam;

    /**
     * CT検査の可否
     */
    private String acceptability_of_CT_exam;

    /**
     * TV検査の可否
     */
    private String acceptability_of_TV_exam;

    /**
     * MRI検査の可否
     */
    private String acceptability_of_MR_exam;

}

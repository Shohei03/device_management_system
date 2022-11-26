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
    private Integer patExamId;

    /**
     * PatientDevice id
     */
    private Integer patDeviceId;

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 患者名
     */
    private String patientName;

    /**
     * 患者名（ひらがな）
     */
    private String patientNameKana;

    /**
     * 検査項目
     */
    private String examinationItem;

    /**
     * 検査日
     */
    private LocalDate examinationDate;

    /**
     * 検査の予約時間
     */
    private LocalTime reservationTime;

    /**
     * 添付文書（機器）承認番号
     */
    private String approvalNumber;

    /**
     * 一般的名称
     */
    private String generalName;

    /**
     * デバイスの販売名
     */
    private String deviceName;

    /**
     * デバイスの埋込日
     */
    private LocalDate implantedAt;

    /**
     * 乳腺X線検査の可否
     */
    private String acceptabilityOfManmaExam;

    /**
     * 一般（X線）検査の可否
     */
    private String acceptabilityOfXrayExam;

    /**
     * CT検査の可否
     */
    private String acceptabilityOfCtExam;

    /**
     * TV検査の可否
     */
    private String acceptabilityOfTvExam;

    /**
     * MRI検査の可否
     */
    private String acceptabilityOfMrExam;

}

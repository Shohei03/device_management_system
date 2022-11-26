package actions.views;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 検索画面のデバイス表示を扱うViewモデル
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchPatientDeviceView {


    /**
     * id
     */
    private Integer id;

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
     * 埋込日
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

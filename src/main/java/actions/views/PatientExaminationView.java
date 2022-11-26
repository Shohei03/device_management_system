package actions.views;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 患者の検査情報について画面の入力値・出力値を扱うViewモデル
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientExaminationView {

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
     * 登録日
     */
    private LocalDate createdAt;

}

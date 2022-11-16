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
     * 登録日
     */
    private LocalDate createdAt;

}

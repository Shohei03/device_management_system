package actions.views;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 患者の体内デバイス情報について画面の入力値・出力値を扱うViewモデル
 *
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDeviceView {

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
     * 添付文書承認番号
     */
    private String approvalNumber;

    /**
     * デバイスの販売名
     */
    private String deviceName;

    /**
     * デバイスを埋め込んだ日
     */
    private LocalDate implantedAt;

    /**
     * 登録日
     */
    private LocalDate createdAt;

    /**
     * 更新日時
     */
    private LocalDate updatedAt;

    /**
     * 削除されたかどうか（現役：0、削除済み：1）
     */
    private Integer deleteFlag;

}

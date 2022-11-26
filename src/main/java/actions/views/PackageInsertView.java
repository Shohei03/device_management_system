package actions.views;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * 添付文書情報について画面の入力値・出力値を扱うViewモデル
 *
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackageInsertView {
    /**
     * id
     */
    private Integer id;

    /**
     * 承認番号
     */
    private String approvalNumber;

    /**
     * JMDNコード
     */
    private String jmdnCode;

    /**
     * 一般的名称
     */
    private String generalName;

    /**
     * デバイスの販売名
     */
    private String deviceName;

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

    /**
     * 登録日
     */
    private LocalDate createdAt;

    /**
     * 更新日
     */
    private LocalDate updatedAt;

    /**
     * 削除されたかどうか（現役：0、削除済み：1）
     */
    private Integer deleteFlag;

}

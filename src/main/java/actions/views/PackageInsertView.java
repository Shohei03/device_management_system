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
    private String approval_number;

    /**
     * JMDNコード
     */
    private String JMDN_code;

    /**
     * 一般的名称
     */
    private String general_name;

    /**
     * デバイスの販売名
     */
    private String device_name;

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

    /**
     * 登録日時
     */
    private LocalDate createdAt;

}

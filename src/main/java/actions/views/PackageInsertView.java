package actions.views;

import java.time.LocalDateTime;

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
     * マンモグラフィ検査の可否
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
     * MR検査の可否
     */
    private String acceptability_of_MR_exam;

    /**
     * MR静磁場強度制限
     */
    private Double MR_magnetic_field_strength;

    /**
     * MR傾斜磁場強度制限
     */
    private Double MR_gradient_magnetic_field;

    /**
     * MR SAR制限値
     */
    private Double MR_SAR;

    /**
     * 登録日時
     */
    private LocalDateTime createdAt;

}

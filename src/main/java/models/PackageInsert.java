package models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 添付文書データのDTOモデル
 *
 */
@Table(name = JpaConst.TABLE_PACK)
@NamedQueries({
        @NamedQuery(name = JpaConst.Q_PACK_GET_ALL, query = JpaConst.Q_PACK_GET_ALL_DEF),
        @NamedQuery(name = JpaConst.Q_PACK_COUNT, query = JpaConst.Q_PACK_COUNT_DEF)
})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PackageInsert {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.PACK_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 承認番号
     */
    @Column(name = JpaConst.PACK_COL_APP_NUM, nullable = false, unique = true)
    private String approval_number;

    /**
     * JMDNインスタンス
     */
    @ManyToOne
    @Column(name = JpaConst.PACK_COL_JMDN, nullable = false)
    private JMDN jmdn;

    /**
     * デバイスの販売名
     */
    @Column(name = JpaConst.PACK_COL_DEV_NAME, nullable = false, unique = true)
    private String device_name;

    /**
     * マンモグラフィ検査の可否
     */
    @Column(name = JpaConst.PACK_COL_Manma)
    private String acceptability_of_Manma_exam;

    /**
     * 一般（X線）検査の可否
     */
    @Column(name = JpaConst.PACK_COL_X_RAY)
    private String acceptability_of_X_ray_exam;

    /**
     * CT検査の可否
     */
    @Column(name = JpaConst.PACK_COL_CT)
    private String acceptability_of_CT_exam;

    /**
     * TV検査の可否
     */
    @Column(name = JpaConst.PACK_COL_TV)
    private String acceptability_of_TV_exam;

    /**
     * MR検査の可否
     */
    @Column(name = JpaConst.PACK_COL_MRI)
    private String acceptability_of_MR_exam;

    /**
     * MR静磁場強度制限
     */
    @Column(name = JpaConst.PACK_COL_MR_STR)
    private Double MR_magnetic_field_strength;

    /**
     * MR傾斜磁場強度制限
     */
    @Column(name = JpaConst.PACK_COL_MR_GRADI)
    private Double MR_gradient_magnetic_field;

    /**
     * MR SAR制限値
     */
    @Column(name = JpaConst.PACK_COL_MR_SAR)
    private Double MR_SAR;

    /**
     * 登録日時
     */
    @Column(name = JpaConst.PACK_COL_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

}

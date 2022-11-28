package models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
        @NamedQuery(name = JpaConst.Q_PACK_COUNT, query = JpaConst.Q_PACK_COUNT_DEF),
        @NamedQuery(name = JpaConst.Q_PACK_COUNT_REGISTEREDBY_APPROVAL_NUM, query = JpaConst.Q_PACK_COUNT_REGISTEREDBY_APPROVAL_NUM_DEF),
        @NamedQuery(name = JpaConst.Q_PACK_GET_MINE_REGISTEREDBY_APPROVAL_NUM, query = JpaConst.Q_PACK_GET_MINE_REGISTEREDBY_APPROVAL_NUM_DEF)
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
    @Column(name = JpaConst.PACK_COL_APP_NUM, nullable = false)
    private String approvalNumber;

    /**
     * JMDNインスタンス
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.PACK_COL_JMDN, nullable = false)
    private Jmdn jmdn;

    /**
     * デバイスの販売名
     */
    @Column(name = JpaConst.PACK_COL_DEV_NAME, nullable = false)
    private String deviceName;

    /**
     * 乳腺X線検査の可否
     */
    @Column(name = JpaConst.PACK_COL_Manma)
    private String acceptabilityOfManmaExam;

    /**
     * 一般（X線）検査の可否
     */
    @Column(name = JpaConst.PACK_COL_X_RAY)
    private String acceptabilityOfXrayExam;

    /**
     * CT検査の可否
     */
    @Column(name = JpaConst.PACK_COL_CT)
    private String acceptabilityOfCtExam;

    /**
     * TV検査の可否
     */
    @Column(name = JpaConst.PACK_COL_TV)
    private String acceptabilityOfTvExam;

    /**
     * MR検査の可否
     */
    @Column(name = JpaConst.PACK_COL_MRI)
    private String acceptabilityOfMrExam;

    /**
     * 登録日
     */
    @Column(name = JpaConst.PACK_COL_CREATED_AT, nullable = false)
    private LocalDate createdAt;

    /**
     * 更新日
     */
    @Column(name = JpaConst.PACK_COL_UPDATED_AT, nullable = false)
    private LocalDate updatedAt;

    /**
     * 削除されたかどうか（現役：0、削除済み：1）
     */
    @Column(name = JpaConst.PAT_EXAM_COL_DELETE_FLAG)
    private Integer deleteFlag;

}

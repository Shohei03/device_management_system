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
 * 患者の体内デバイスデータのDTOモデル
 *
 */
@Table(name = JpaConst.TABLE_PAT_DEV)
@NamedQueries({
        @NamedQuery(name = JpaConst.Q_PAT_DEV_GET_ALL, query = JpaConst.Q_PAT_DEV_GET_ALL_DEF),
        @NamedQuery(name = JpaConst.Q_PAT_DEV_COUNT, query = JpaConst.Q_PAT_DEV_COUNT_DEF),
})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PatientDevice {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.PAT_DEV_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 添付文書承認番号
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.PAT_DEV_COL_APP_NUM, nullable = false)
    private PackageInsert packageInsert;

    /**
     * 患者ID
     */
    @ManyToOne
    @Column(name = JpaConst.PAT_DEV_COL_PAT_ID, nullable = false)
    private Patient patient;

    /**
     * 埋込日
     */
    @Column(name = JpaConst.PAT_DEV_COL_IMPLANTED_AT)
    private LocalDate implantedAt;

    /**
     * 登録日
     */
    @Column(name = JpaConst.PAT_DEV_COL_CREATED_AT)
    private LocalDate createdAt;

    /**
     * 更新日
     */
    @Column(name = JpaConst.PAT_DEV_COL_UPDATED_AT)
    private LocalDate updatedAt;

    /**
     * 削除されたかどうか（現役：0、削除済み：1）
     */
    @Column(name = JpaConst.PAT_DEV_COL_DELETE_FLAG)
    private Integer deleteFlag;

}

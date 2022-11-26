package models;

import java.time.LocalDate;
import java.time.LocalTime;

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
*
* 患者の検査情報のDTOモデル
*
*/
@Table(name = JpaConst.TABLE_PAT_EXAM)
@NamedQueries({
    @NamedQuery(name = JpaConst.Q_PAT_EXAM_GET_MINE, query = JpaConst.Q_PAT_EXAM_GET_MINE_DEF),
    @NamedQuery(name = JpaConst.Q_PAT_EXAM_COUNT_ALL, query = JpaConst.Q_PAT_EXAM_COUNT_ALL_DEF),
    @NamedQuery(name = JpaConst.Q_PAT_EXAM_GET_MINE_REGISTEREDBY_EXAM_DATE, query = JpaConst.Q_PAT_EXAM_GET_MINE_REGISTEREDBY_EXAM_DATE_DEF),
    @NamedQuery(name = JpaConst.Q_PAT_EXAM_COUNT_REGISTEREDBY_EXAM_DATE, query = JpaConst.Q_PAT_EXAM_COUNT_REGISTEREDBY_EXAM_DATE_DEF),
    @NamedQuery(name = JpaConst.Q_PAT_EXAM_COUNT_REGISTEREDBY_PAT, query = JpaConst.Q_PAT_EXAM_COUNT_REGISTEREDBY_PAT_DEF),
    @NamedQuery(name = JpaConst.Q_PAT_EXAM_GET_MINE_REGISTEREDBY_PAT, query = JpaConst.Q_PAT_EXAM_GET_MINE_REGISTEREDBY_PAT_DEF),
    @NamedQuery(name = JpaConst.Q_PAT_EXAM_GET_MINE_REGISTEREDBY_EXAM_DATE_AND_ITEM, query = JpaConst.Q_PAT_EXAM_GET_MINE_REGISTEREDBY_EXAM_DATE_AND_ITEM_DEF),
    @NamedQuery(name = JpaConst.Q_PAT_EXAM_GET_PAT_DISTINCT_PAT_REGISTEREDBY_EXAM_DATE_AND_ITEM, query = JpaConst.Q_PAT_EXAM_GET_PAT_DISTINCT_PAT_REGISTEREDBY_EXAM_DATE_AND_ITEM_DEF),
    @NamedQuery(name = JpaConst.Q_PAT_EXAM_GET_PAT_DISTINCT_PAT_REGISTEREDBY_EXAM_DATE, query = JpaConst.Q_PAT_EXAM_GET_PAT_DISTINCT_PAT_REGISTEREDBY_EXAM_DATE_DEF)
})



@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class PatientExamination {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.PAT_EXAM_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 患者
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.PAT_EXAM_COL_PAT_ID, nullable = false)
    private Patient patient;

    /**
     * 患者さんの検査項目
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.PAT_EXAM_COL_EXAM, nullable = false)
    private Examination examination;

    /**
     * 検査日
     */
    @Column(name = JpaConst.PAT_EXAM_COL_EXAM_DATE, nullable = false)
    private LocalDate examinationDate;

    /**
     * 予約時間
     */
    @Column(name = JpaConst.PAT_EXAM_COL_RESERVATION_TIME)
    private LocalTime reservationTime;

    /**
     * 登録日
     */
    @Column(name = JpaConst.PAT_EXAM_CREATED_AT)
    private LocalDate createdAt;



}

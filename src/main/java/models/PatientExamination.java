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
    LocalDate examination_date;

    /**
     * 予約時間
     */
    @Column(name = JpaConst.PAT_EXAM_COL_RESERVATION_TIME)
    LocalTime reservation_time;

    /**
     * 登録日
     */
    @Column(name = JpaConst.PAT_EXAM_CREATED_AT)
    LocalDate createdAt;



}

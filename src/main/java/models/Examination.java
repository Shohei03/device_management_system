package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
* 検査項目データのDTOモデル
*
*/
@Table(name = JpaConst.TABLE_EXAM)

@NamedQueries({
    @NamedQuery(name = JpaConst.Q_EXAM_GET_MINE_REGISTEREDBY_PAT, query = JpaConst.Q_EXAM_GET_MINE_REGISTEREDBY_PAT_DEF)
})


@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class Examination {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.EXAM_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 検査項目
     */
    @Column(name = JpaConst.EXAM_COL_ITEM, nullable = false, unique = true)
    private String examination_item;

}

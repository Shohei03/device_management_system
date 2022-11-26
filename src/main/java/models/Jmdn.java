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
* 従業員データのDTOモデル
*
*/

@Table(name = JpaConst.TABLE_JMDN)
@NamedQueries({
        @NamedQuery(name = JpaConst.Q_JMDN_COUNT_REGISTEREDBY_JMDN_CODE, query = JpaConst.Q_JMDN_COUNT_REGISTEREDBY_JMDN_CODE_DEF),
        @NamedQuery(name = JpaConst.Q_JMDN_GET_MINE_REGISTEREDBY_JMDN_CODE, query = JpaConst.Q_JMDN_GET_MINE_REGISTEREDBY_JMDN_CODE_DEF)
})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Jmdn {
    /**
     * id
     */
    @Id
    @Column(name = JpaConst.JMDN_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * JMDNコード
     */
    @Column(name = JpaConst.JMDN_COL_CODE, nullable = false, unique = true)
    private String jmdnCode;

    /**
     * 一般的名称
     */
    @Column(name = JpaConst.JMDN_COL_GENE_NAME, nullable = false)
    private String generalName;

}

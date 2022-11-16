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
 * 患者データのDTOモデル
 *
 */
@Table(name = JpaConst.TABLE_PAT)
@NamedQueries({
        @NamedQuery(name = JpaConst.Q_PAT_GET_ALL, query = JpaConst.Q_PAT_GET_ALL_DEF),
        @NamedQuery(name = JpaConst.Q_PAT_COUNT, query = JpaConst.Q_PAT_COUNT_DEF),
        @NamedQuery(name = JpaConst.Q_PAT_COUNT_REGISTEREDBY_PAT_ID, query = JpaConst.Q_PAT_COUNT_REGISTEREDBY_PAT_ID_DEF),
        @NamedQuery(name = JpaConst.Q_PAT_GET_MINE_REGISTEREDBY_PAT_ID, query = JpaConst.Q_PAT_GET_MINE_REGISTEREDBY_PAT_ID_DEF)
})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Patient {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.PAT_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 患者ID
     */
    @Column(name = JpaConst.PAT_COL_PAT_ID, unique = true, nullable = false)
    private Integer patient_id;

    /**
     * 患者名
     */
    @Column(name = JpaConst.PAT_COL_PAT_NAME, nullable = false)
    private String patient_name;

    /**
     * 患者名（ひらがな）
     */
    @Column(name = JpaConst.PAT_COL_PAT_NAME_KANA, nullable = true)
    private String patient_name_kana;

}

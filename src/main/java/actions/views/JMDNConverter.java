package actions.views;

import models.JMDN;

/**
*
* 添付文書データのJMDN DTOモデル⇔PackageInsertViewモデルの変換を行うクラス
*
*/
public class JMDNConverter {
    /**
     * ViewモデルのインスタンスからJMDNのDTOモデルのインスタンスを作成する
     * @param pv PackageInsertViewのインスタンス
     * @return JMDNのインスタンス
     */
    public static JMDN toModel(PackageInsertView pv) {
        return new JMDN(pv.getId(), pv.getJMDN_code(), pv.getGeneral_name());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param のインスタンス
     * @return のインスタンス
     */

}

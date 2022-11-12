package actions.views;

import models.Jmdn;
import services.PackageInsertService;

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
    public static Jmdn toModel(PackageInsertView pv) {

        Jmdn j = new Jmdn(null, pv.getJMDN_code(), pv.getGeneral_name());

        return j;

    }

    /**
     * ViewモデルのインスタンスからデータベースにあるJMDNのインスタンスを取得
     * @param pv PackageInsertViewのインスタンス
     * @return
     */

    public static Jmdn toModel_FROM_PACK(PackageInsertView pv) {

        PackageInsertService service = new PackageInsertService();

        return service.findJMDN(pv.getJMDN_code());

    }

    /**
     * Viewモデルのフィールドの内容をDTOモデルのフィールド（一般的名称）にコピーする
     * @param j DTOモデル(コピー先)
     * @param pv Viewモデル(コピー元)
     */
    public static void copyViewToModel_general_name(Jmdn j, PackageInsertView pv) {
        j.setGeneral_name(pv.getGeneral_name());
    }

}

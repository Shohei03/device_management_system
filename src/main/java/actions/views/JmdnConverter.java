package actions.views;

import models.Jmdn;
import services.PackageInsertService;

/**
*
* 添付文書データのJMDN DTOモデル⇔PackageInsertViewモデルの変換を行うクラス
*
*/
public class JmdnConverter {
    /**
     * ViewモデルのインスタンスからJMDNのDTOモデルのインスタンスを作成する
     * @param pv PackageInsertViewのインスタンス
     * @return JMDNのインスタンス
     */
    public static Jmdn toModel(PackageInsertView pv) {

        Jmdn j = new Jmdn(null, pv.getJmdnCode(), pv.getGeneralName());

        return j;

    }

    /**
     * ViewモデルのインスタンスからデータベースにあるJMDNのインスタンスを取得
     * @param pv PackageInsertViewのインスタンス
     * @return
     */

    public static Jmdn toModelFromPack(PackageInsertView pv) {

        PackageInsertService service = new PackageInsertService();

        return service.findJmdn(pv.getJmdnCode());

    }

    /**
     * Viewモデルのフィールドの内容をDTOモデルのフィールド（一般的名称）にコピーする
     * @param j DTOモデル(コピー先)
     * @param pv Viewモデル(コピー元)
     */
    public static void copyViewToModelGeneralName(Jmdn j, PackageInsertView pv) {
        j.setGeneralName(pv.getGeneralName());
    }

}

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

    public static Jmdn toModel_FROM_PACK(PackageInsertView pv) {

        PackageInsertService service = new PackageInsertService();

        return service.findJMDN(pv.getJMDN_code());

    }

}

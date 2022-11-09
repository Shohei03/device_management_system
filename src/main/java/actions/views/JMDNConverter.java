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
        PackageInsertService service = new PackageInsertService();
        if (service.countByJMDN_CODE(pv.getJMDN_code()) > 0) {
            return service.findJMDN(pv.getJMDN_code());
        }

        return new Jmdn(pv.getId(), pv.getJMDN_code(), pv.getGeneral_name());
    }

}

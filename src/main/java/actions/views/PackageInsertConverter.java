package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.PackageInsert;

/**
 *
 * 添付文書データのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class PackageInsertConverter {

    /**
     * ViewモデルのインスタンスからPackageInsertのDTOモデルのインスタンスを作成する
     * @param pv PackageInsertViewのインスタンス
     * @return PackageInsertのインスタンス
     */
    public static PackageInsert toModel(PackageInsertView pv) {

        return new PackageInsert(
                pv.getId(),
                pv.getApproval_number(),
                JMDNConverter.toModel(pv),
                pv.getDevice_name(),
                pv.getAcceptability_of_Manma_exam(),
                pv.getAcceptability_of_X_ray_exam(),
                pv.getAcceptability_of_CT_exam(),
                pv.getAcceptability_of_TV_exam(),
                pv.getAcceptability_of_MR_exam(),
                pv.getCreatedAt());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param p PackageInsertのインスタンス
     * @return PackageInsertViewのインスタンス
     */
    public static PackageInsertView toView(PackageInsert p) {
        if (p == null) {
            return null;
        }
        return new PackageInsertView(
                p.getId(),
                p.getApproval_number(),
                p.getJmdn().getJMDN_code(),
                p.getJmdn().getGeneral_name(),
                p.getDevice_name(),
                p.getAcceptability_of_Manma_exam(),
                p.getAcceptability_of_X_ray_exam(),
                p.getAcceptability_of_CT_exam(),
                p.getAcceptability_of_TV_exam(),
                p.getAcceptability_of_MR_exam(),
                p.getCreatedAt());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<PackageInsertView> toViewList(List<PackageInsert> list) {
        List<PackageInsertView> pvs = new ArrayList<>();

        for (PackageInsert p : list) {
            pvs.add(toView(p));
        }

        return pvs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param p DTOモデル(コピー先)
     * @param pv Viewモデル(コピー元)
     */
    public static void copyViewToModel(PackageInsert p, PackageInsertView pv) {
        p.setId(pv.getId());
        p.setApproval_number(pv.getApproval_number());
        p.setJmdn(JMDNConverter.toModel(pv));
        p.setDevice_name(pv.getDevice_name());
        p.setAcceptability_of_Manma_exam(pv.getAcceptability_of_Manma_exam());
        p.setAcceptability_of_X_ray_exam(pv.getAcceptability_of_X_ray_exam());
        p.setAcceptability_of_CT_exam(pv.getAcceptability_of_CT_exam());
        p.setAcceptability_of_TV_exam(pv.getAcceptability_of_TV_exam());
        p.setAcceptability_of_MR_exam(pv.getAcceptability_of_MR_exam());
        p.setCreatedAt(pv.getCreatedAt());

    }

}

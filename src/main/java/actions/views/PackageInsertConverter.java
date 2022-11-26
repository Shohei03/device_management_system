package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.PackageInsert;
import services.PackageInsertService;

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
                pv.getApprovalNumber(),
                JmdnConverter.toModelFromPack(pv),
                pv.getDeviceName(),
                pv.getAcceptabilityOfManmaExam(),
                pv.getAcceptabilityOfXrayExam(),
                pv.getAcceptabilityOfCtExam(),
                pv.getAcceptabilityOfTvExam(),
                pv.getAcceptabilityOfMrExam(),
                pv.getCreatedAt(),
                pv.getUpdatedAt(),
                pv.getDeleteFlag());

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
                p.getApprovalNumber(),
                p.getJmdn().getJmdnCode(),
                p.getJmdn().getGeneralName(),
                p.getDeviceName(),
                p.getAcceptabilityOfManmaExam(),
                p.getAcceptabilityOfXrayExam(),
                p.getAcceptabilityOfCtExam(),
                p.getAcceptabilityOfTvExam(),
                p.getAcceptabilityOfMrExam(),
                p.getCreatedAt(),
                p.getUpdatedAt(),
                p.getDeleteFlag());
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
        p.setApprovalNumber(pv.getApprovalNumber());
        p.setJmdn(JmdnConverter.toModelFromPack(pv));
        p.setDeviceName(pv.getDeviceName());
        p.setAcceptabilityOfManmaExam(pv.getAcceptabilityOfManmaExam());
        p.setAcceptabilityOfXrayExam(pv.getAcceptabilityOfXrayExam());
        p.setAcceptabilityOfCtExam(pv.getAcceptabilityOfCtExam());
        p.setAcceptabilityOfTvExam(pv.getAcceptabilityOfTvExam());
        p.setAcceptabilityOfMrExam(pv.getAcceptabilityOfMrExam());
        p.setCreatedAt(pv.getCreatedAt());
        p.setUpdatedAt(pv.getCreatedAt());
        p.setDeleteFlag(pv.getDeleteFlag());


    }

    /**
     * 添付文書テーブルにアクセスし、引数で与えた添付文書承認番号のレコード（インスタンス）を取得
     */
    public static PackageInsert toModelFromAppNum(String appNum) {
        PackageInsertService piService = new PackageInsertService();

        return piService.findPackageInsertByAppNum(appNum);

    }

}

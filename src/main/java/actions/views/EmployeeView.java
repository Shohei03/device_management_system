package actions.views;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * 従業員について画面の入力値・出力値を扱うViewモデル
 *
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeView {
    /**
     * id
     */
    private Integer id;

    /**
     * 社員番号
     */
    private String code;

    /**
     * 氏名
     */
    private String name;

    /**
     * パスワード
     */
    private String password;

    /**
     * 部署
     */
    private String department;

    /**
     * 管理者権限があるかどうか（一般：0、デバイスデータ登録権限：1、+社員データ修正権限：2）
     */
    private Integer adminFlag;

    /**
     *登録日時
     */
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    private LocalDateTime updatedAt;

    /**
     * 削除された従業員かどうか（現役：0、削除済み：1）
     */
    private Integer deleteFlag;

}

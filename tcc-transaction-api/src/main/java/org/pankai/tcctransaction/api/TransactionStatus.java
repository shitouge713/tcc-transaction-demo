package org.pankai.tcctransaction.api;

/**
 * Created by pankai on 2016/11/13.
 */
public enum TransactionStatus {
    //重试
    TRYING(1),
    //确认
    CONFIRMING(2),
    //取消
    CANCELLING(3);


    private int id;

    TransactionStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TransactionStatus valueOf(int id) {
        switch (id) {
            case 1:
                return TRYING;
            case 2:
                return CONFIRMING;
            default:
                return CANCELLING;
        }

    }
}

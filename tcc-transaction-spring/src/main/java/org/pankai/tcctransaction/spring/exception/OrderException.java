package org.pankai.tcctransaction.spring.exception;


import org.pankai.tcctransaction.enums.ReturnStatusEnum;

/**
 * @Title AccountException
 * @Copyright: Copyright (c) 2021
 * @Description:
 * @Created on 2022/4/14 22:33
 */
public class OrderException extends RuntimeException {
    private static final long serialVersionUID = 6409874889208817528L;

    private String code;

    public OrderException(String message) {
        super(message);
        this.code = ReturnStatusEnum.SERVER_ERROR.getValue();
    }

    public OrderException(String code, String message) {
        super(message);
        this.code = code;
    }

    public OrderException(ReturnStatusEnum errorStatusEnum) {
        super(errorStatusEnum.getDesc());
        this.code = errorStatusEnum.getValue();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

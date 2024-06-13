package org.pankai.tcctransaction.spring.exception;


import org.pankai.tcctransaction.enums.ReturnStatusEnum;

/**
 * @author jsrf
 * @date 2022/12/6 10:35 AM
 */
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 6409874889208817528L;

    private String code;

    public BizException(String message) {
        super(message);
        this.code = ReturnStatusEnum.SERVER_ERROR.getValue();
    }

    public BizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(ReturnStatusEnum errorStatusEnum) {
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

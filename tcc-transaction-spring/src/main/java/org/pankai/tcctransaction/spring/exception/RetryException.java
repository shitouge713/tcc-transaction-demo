package org.pankai.tcctransaction.spring.exception;


import org.pankai.tcctransaction.enums.ReturnStatusEnum;

/**
 * 自定义异常：mysql乐观锁更新失败时需要报的异常
 *
 * @author css
 */
public class RetryException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String code;

    public RetryException(String message) {
        super(message);
        this.code = ReturnStatusEnum.SERVER_ERROR.getValue();
    }

    public RetryException(String code, String message) {
        super(message);
        this.code = code;
    }

    public RetryException(ReturnStatusEnum errorStatusEnum) {
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

package org.mengyun.tcctransaction.sample.config;

import org.pankai.tcctransaction.enums.ReturnStatusEnum;
import org.pankai.tcctransaction.spring.exception.BizException;
import org.pankai.tcctransaction.spring.exception.NoWarnException;
import org.pankai.tcctransaction.spring.exception.OrderException;
import org.pankai.tcctransaction.spring.exception.RetryException;
import org.pankai.tcctransaction.utils.CollectionUtils;
import org.pankai.tcctransaction.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final transient Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = OrderException.class)
    public Result orderExceptionHandler(OrderException ex) {
        log.warn("全局异常,OrderException:", ex);
        return Result.failed(ex.getCode(),ex.getMessage());
    }

    @ExceptionHandler(value = NoWarnException.class)
    public Result orderExceptionHandler(NoWarnException ex) {
        return Result.failed(ex.getCode(),ex.getMessage());
    }

    @ExceptionHandler(value = BizException.class)
    public Result BizExceptionHandler(BizException ex) {
        log.error("fatalError,业务异常,BizException:", ex);
        return Result.failed(ex.getCode(),ex.getMessage());
    }

    @ExceptionHandler(value = RetryException.class)
    public Result RetryExceptionHandler(RetryException ex) {
        log.error("fatalError,业务异常,RetryException:", ex);
        return Result.failed(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception ex) {
        log.error("fatalError,全局未知异常:", ex);
        return Result.failed(ReturnStatusEnum.ERROR);
    }


    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Result missingServletRequestParameter(Exception ex) {
        log.error("fatalError,全局异常,MissingServletRequestParameterException:", ex);
        return Result.failed(ReturnStatusEnum.PARAM_ERROR);
    }

    /**
     * 用来处理bean validation异常
     * @param ex
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result constraintViolationExceptionHandler(ConstraintViolationException ex){
        log.warn("warnError,全局异常,ConstraintViolationException:", ex);
        String rspMsg = ReturnStatusEnum.PARAM_ERROR.getDesc();
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        if(! CollectionUtils.isEmpty(constraintViolations)){
            StringBuilder msgBuilder = new StringBuilder();
            for(ConstraintViolation constraintViolation :constraintViolations){
                msgBuilder.append(constraintViolation.getMessage()).append("|");
            }
            rspMsg = String.format(rspMsg,  msgBuilder.deleteCharAt(msgBuilder.length() - 1).toString());
        }
        return Result.failed(ReturnStatusEnum.PARAM_ERROR.getValue(), rspMsg);
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result constraintViolationExceptionHandler(BindException ex) {
        log.warn("warnError,全局异常,BindException:", ex);
        String rspMsg = ReturnStatusEnum.PARAM_ERROR.getDesc();
        BindingResult bindingResult = ex.getBindingResult();
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            StringBuilder msgBuilder = new StringBuilder();
            for (FieldError fieldError : fieldErrors) {
                msgBuilder.append(fieldError.getDefaultMessage()).append("|");
            }
            rspMsg = String.format(rspMsg,  msgBuilder.deleteCharAt(msgBuilder.length() - 1).toString());
        }
        return Result.failed(ReturnStatusEnum.PARAM_ERROR.getValue(), rspMsg);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result methodArgumentNotValidException(MethodArgumentNotValidException ex){
        log.warn("warnError,全局异常,MethodArgumentNotValidException:", ex);
        log.warn("warnError,全局异常message,MethodArgumentNotValidException:{}", ex.getMessage());
        String rspMsg = ReturnStatusEnum.PARAM_ERROR.getDesc();
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        if(! CollectionUtils.isEmpty(objectErrors)) {
            StringBuilder msgBuilder = new StringBuilder();
            for (ObjectError objectError : objectErrors) {
                msgBuilder.append(objectError.getDefaultMessage()).append("|");
            }
            String errorMessage = msgBuilder.toString();
            if (errorMessage.length() > 1) {
                errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
            }
            rspMsg = String.format(rspMsg, errorMessage);
        }
        return Result.failed(ReturnStatusEnum.PARAM_ERROR.getValue(), rspMsg);
    }

}

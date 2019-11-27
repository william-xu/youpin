package com.hflw.vasp.framework.exception;

import com.hflw.vasp.entity.BaseEntity;
import com.hflw.vasp.enums.ExceptionLevel;
import com.hflw.vasp.exception.BusinessException;
import com.hflw.vasp.exception.SystemMessage;
import com.hflw.vasp.exception.ValidateException;
import com.hflw.vasp.framework.config.ThreadPoolConfig;
import com.hflw.vasp.utils.SessionUtils;
import com.hflw.vasp.web.R;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

import static com.hflw.vasp.exception.SystemMessage.ARGS_NULL;

/**
 * 作者: sanri
 * 时间: 2018/3/3
 * 功能: 全局异常处理
 */
@RestControllerAdvice
@Import({ThreadPoolConfig.class})
public class GlobalExceptionHandler {

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Value("${project.package.prefix:com.hflw.vasp}")
    protected String packagePrefix;

    @Value("${spring.application.name}")
    protected String modulName;

    @Autowired
    protected Executor executor;

//    @Autowired
//    private MongoTemplate mongoTemplate;

    /**
     * 业务异常处理
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public R businessExceptionHandler(HttpServletRequest req, Exception e) {
        BusinessException businessException = (BusinessException) e;

        StackTraceElement[] stackTrace = e.getStackTrace();
        List<StackTraceElement> localStackTrack = new ArrayList<>();
        StringBuffer showMessage = new StringBuffer();
        if (ArrayUtils.isNotEmpty(stackTrace)) {
            for (StackTraceElement stackTraceElement : stackTrace) {
                String className = stackTraceElement.getClassName();
                int lineNumber = stackTraceElement.getLineNumber();
                if (className.startsWith(packagePrefix)) {
                    localStackTrack.add(stackTraceElement);
                    showMessage.append(className + "(" + lineNumber + ")\n");
                }
            }
            logger.error("业务异常:" + e.getMessage() + "\n" + showMessage);
        } else {
            logger.error("业务异常,没有调用栈 " + e.getMessage());
        }

//        saveException(businessException, ExceptionLevel.business);

        return businessException.getResultEntity();
    }

    /**
     * 网络或资源找不到
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = IOException.class)
    public R ioException(HttpServletRequest req, Exception e) {
        logger.error("网络连接错误", e);
        saveException(e, ExceptionLevel.normal);
        return SystemMessage.NETWORK_ERROR.result();
    }

    @ExceptionHandler(value = BindException.class)
    public R bindException(BindException ex) {
        // ex.getFieldError():随机返回一个对象属性的异常信息。如果要一次性返回所有对象属性异常信息，则调用ex.getAllErrors()
        FieldError fieldError = ex.getFieldError();
        StringBuilder sb = new StringBuilder();
        sb.append(fieldError.getField()).append("=[").append(fieldError.getRejectedValue()).append("]")
                .append(fieldError.getDefaultMessage());

//        saveException(ex, ExceptionLevel.business);

        return R.error(ARGS_NULL.getReturnCode(), sb.toString());
    }

    @ExceptionHandler(value = PersistenceException.class)
    public R persistenceException(PersistenceException e, HttpServletRequest request) {
        Throwable cause = e.getCause();
        if (cause instanceof BusinessException) {
            return businessExceptionHandler(request, e);
        }

        saveException(e, ExceptionLevel.fatal);

        logger.error("数据存储出错", e);
        return R.error(9999, "数据存储异常");
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public R methodNotSupport(HttpRequestMethodNotSupportedException exception) {
        return SystemMessage.ACCESS_DENIED.result(exception.getMessage());
    }

    @ExceptionHandler(value = ValidateException.class)
    public R validateException(ValidateException e) {
        logger.error("数据校验异常", e);
        saveException(e, ExceptionLevel.normal);
        return R.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public R handleServiceException(ConstraintViolationException e) {
        logger.error("参数验证失败", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuffer buf = new StringBuffer();
        for (ConstraintViolation<?> violation : violations) {
            buf.append(violation.getMessage()).append(",");
        }
        return R.error(HttpStatus.BAD_REQUEST.value(), buf.deleteCharAt(buf.length() - 1).toString());
    }

    @ExceptionHandler(value = Exception.class)
    public R otherException(Exception e) {
        logger.error("后台服务异常", e);
        saveException(e, ExceptionLevel.fatal);
        return SystemMessage.SERVICE_CALL_FAIL.result();
    }

    /**
     * 异常保存
     *
     * @param e
     * @param exceptionLevel
     */
    private void saveException(Exception e, ExceptionLevel exceptionLevel) {
        HttpServletRequest request = SessionUtils.request();
        String channel = request.getHeader("channel");

        BaseEntity baseEntity = null;

        final BaseEntity finalBaseEntity = baseEntity;

//        executor.execute(() -> {
//            ExceptionModel exceptionModel = new ExceptionModel(modulName, exceptionLevel);
//            exceptionModel.setException(e, packagePrefix);
//            try {
//                exceptionModel.setRequest(request);
//            } catch (IOException e1) {
//                logger.error("记录异常请求内容时出异常... " + e.getMessage());
//            }
//            if (finalBaseEntity != null) {
//                exceptionModel.setOperator(finalBaseEntity.getId());
//            }
//            //保存数据
////            mongoTemplate.insert(exceptionModel);
//        });
    }
}

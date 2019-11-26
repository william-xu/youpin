package com.hflw.vasp.exception;

import com.hflw.vasp.web.R;

public interface ExceptionCause<T extends Exception> {

    /**
     * 创建异常
     *
     * @return
     */
    T exception(Object... args);

    R result();
}

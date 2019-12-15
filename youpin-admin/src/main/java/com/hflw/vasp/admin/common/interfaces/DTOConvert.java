package com.hflw.vasp.admin.common.interfaces;

public interface DTOConvert<S, T> {

    T convert(S s);

}

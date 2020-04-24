package com.wlp.security.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *异常json信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionJsonMsg {
    //不要随意更换属性位置，否则会导致构造器参数位置变化
    private Object code ;
    private Object msg ;
}

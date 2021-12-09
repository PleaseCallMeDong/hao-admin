package com.example.common.handler;

import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;

/**
 * @author dj
 * @date 2021-11-15 17:25
 * @description 用于MyBatis的配置
 **/

public class SqlInjectorPlus extends DefaultSqlInjector {

//    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
//        //继承原有方法
//        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
//        //注入新方法
//        methodList.add(new LogicDeleteByIdWithFill());
//        return methodList;
//    }

}

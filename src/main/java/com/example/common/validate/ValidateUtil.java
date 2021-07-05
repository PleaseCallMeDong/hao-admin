package com.example.common.validate;

import com.example.common.exception.MyException;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author cjl
 * @date 2021/5/31 15:27
 * @description
 */
public class ValidateUtil {
    private static final Validator VALIDATOR = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    /**
     * 校验遇到第一个不合法的字段直接返回不合法字段，后续字段不再校验
     */
    public static <T> void validate(T domain) {
        Set<ConstraintViolation<T>> validateResult = VALIDATOR.validate(domain);
        if (validateResult.size() > 0) {
            throw new MyException(validateResult.iterator().next().getMessage());
        }
    }
}

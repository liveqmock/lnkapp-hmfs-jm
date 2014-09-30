package org.fbi.hmfsjm.helper;

import org.apache.commons.lang.StringUtils;
import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-6-5
 * Time: ÉÏÎç10:39
 * To change this template use File | Settings | File Templates.
 */
public class ObjectFieldsCopier {

    /**
     * Field Type: BigDecimal and String only
     *
     * @param src source Object
     * @param des destination Object
     * @throws IllegalAccessException
     */
    public static void copyFields(Object src, Object des) throws IllegalAccessException {
        Field[] srcFields = src.getClass().getDeclaredFields();
        for (Field field : srcFields) {
            field.setAccessible(true);
            Object fieldValue = field.get(src);
            Field desField = null;
            try {
                desField = des.getClass().getDeclaredField(field.getName());
            } catch (NoSuchFieldException e) {
                try {
                    desField = des.getClass().getDeclaredField(field.getName().toUpperCase());
                } catch (NoSuchFieldException e1) {
                    try {
                        desField = des.getClass().getDeclaredField(field.getName().toLowerCase());
                    } catch (NoSuchFieldException e2) {
                        continue;
                    }
                }
            }
            desField.setAccessible(true);
            String desFieldTypeName = desField.getType().getSimpleName();
            if (desFieldTypeName.equals("String")) {
                if (fieldValue != null) {
                    desField.set(des, StringUtils.isEmpty((String)fieldValue) ? "" : fieldValue.toString());
                } else {
                    desField.set(des, "");
                }
            } else if (desFieldTypeName.equals("BigDecimal")) {
                if (fieldValue != null) desField.set(des, new BigDecimal(fieldValue.toString()));
            } else {
                throw new RuntimeException("desFieldInstance : unsupported field type[BigDecimal and String only].");
            }
        }
    }

}

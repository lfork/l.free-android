package com.lfork.a98620.lfree.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/** en?
 * Created by 98620 on 2018/2/1.
 */

public class JSONUtil {
    /**
     *
     * @param jsonData 符合JSON语法规则的字符串
     * @param token    传递参数的时候这样写：new TypeToken<Class对象>() {}，  比如：parseJson(str1, new TypeToken<List<Result<User>>>(){})
     *        泛型会被擦除, 所以Token需要单独传参数
     * @return fromJson会自动根据JSONData的内容转化为POJO 或者是List
     */
    public static <T> T parseJson(String jsonData, TypeToken<?> token) {
        try {
            return new Gson().fromJson(jsonData, token.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> String toJson(T object){
        try {
            return new Gson().toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

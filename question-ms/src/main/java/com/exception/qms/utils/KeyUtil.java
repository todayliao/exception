package com.exception.qms.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/6
 * @time 下午12:32
 * @discription 唯一键生成工具
 **/
public class KeyUtil {

    /**
     * 生成唯一的主键
     * 格式: 时间+随机数
     * @return
     */
    public static synchronized String genUniqueKey() {
        Random random = new Random();
        Integer number = random.nextInt(90) + 10;
        return System.currentTimeMillis() + number.toString();
    }

}

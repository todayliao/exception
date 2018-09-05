package com.exception.qms.enums;

import lombok.Getter;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午12:56
 * @discription 顶部导航栏 nav
 **/
@Getter
public enum TopNavEnum {

    HOME("home", "首页"),
    QUESTION("question", "问题详情页"),
    ARTICLE("article", "文章详情页"),
    RECOMMENDED_ARTICLE("recommendedArticle", "优文详情页"),
    USER("user", "用户墙"),
    TAG("tag", "标签墙"),
    ABOUT("about", "关于"),
    ;

    private final String code;
    private final String desc;

    TopNavEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

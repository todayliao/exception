package com.exception.qms.utils;

import com.exception.qms.elasticsearch.QuestionIndexKey;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.web.util.HtmlUtils;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/6
 * @time 下午12:32
 * @discription
 **/
public class SearchUtil {

    public static HighlightBuilder highlightBuilder = new HighlightBuilder();

    static {
        String preTags = "<span class='result-highlight'>";
        String postTags = "</span>";
        highlightBuilder.preTags(preTags);
        highlightBuilder.postTags(postTags);
        highlightBuilder.field(QuestionIndexKey.TITLE);
        highlightBuilder.field(QuestionIndexKey.DESC);
    }

    /** 转义后的高亮标签前缀 **/
    private final static String PRE_TAG_ESCAPE_HTML = "&lt;span class=&#39;result-highlight&#39;&gt;";
    /** 转义后的高亮标签后缀 **/
    private final static String POST_TAG_ESCAPE_HTML = "&lt;/span&gt;";
    /** 转义前的高亮标签前缀 **/
    private final static String PRE_TAG_HTML = "<span class='result-highlight'>";
    /** 转义前的高亮标签后缀 **/
    private final static String POST_TAG_HTML = "</span>";

    public static String escapeHtmlButHighlight(String html) {
        if (StringUtils.isBlank(html)) {
            return null;
        }

        String escapeHtml = HtmlUtils.htmlEscape(html);
        if (escapeHtml.contains(PRE_TAG_ESCAPE_HTML)) {
            escapeHtml = escapeHtml.replace(PRE_TAG_ESCAPE_HTML, PRE_TAG_HTML);
        }
        if (escapeHtml.contains(POST_TAG_ESCAPE_HTML)) {
            escapeHtml = escapeHtml.replace(POST_TAG_ESCAPE_HTML, POST_TAG_HTML);
        }
        return escapeHtml;
    }

    public static void main(String[] args) {
        System.out.println(escapeHtmlButHighlight("\"我使用<span class='result-highlight'>的</span>前端 JS 框架是 jquery, 现在 html 中有一个搜索框 `<input>`, 如何实现当我鼠标点击时，搜索框宽度增加<span class='result-highlight'>的</span>动画，以及 `<input>` 失去焦点时，恢复原有宽度<span class='result-highlight'>的</span>动画呢？\\n\" +\n" +
                "                \" \""));
    }

}

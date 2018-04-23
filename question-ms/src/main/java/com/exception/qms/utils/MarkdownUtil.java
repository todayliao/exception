package com.exception.qms.utils;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/6
 * @time 下午12:32
 * @discription 时间工具类
 **/
@Slf4j
public class MarkdownUtil {

    private static Parser parser;
    private static HtmlRenderer renderer;

    static {
        MutableDataSet options = new MutableDataSet();
        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
    }

    /**
     * 解析 markdown 为 html
     * @param mdStr
     * @return
     */
    public static String parse2Html(String mdStr) {

        // uncomment to set optional extensions
        //options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

        // uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        try {
            Node document = parser.parse(mdStr);
            return renderer.render(document);
        } catch (Exception e) {
            log.error("markdonw parse to html exception: ", e);
            return null;
        }
    }

}

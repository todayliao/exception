package com.exception.qms.utils;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.profiles.pegdown.Extensions;
import com.vladsch.flexmark.profiles.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.options.DataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/6
 * @time 下午12:32
 * @discription markdown 工具类
 **/
@Slf4j
public class MarkdownUtil {


    private final static Parser parser;
    private final static HtmlRenderer renderer;

    static {
        // 定制 markdown 选项
        MutableDataSet options = new MutableDataSet();

        // set optional extensions
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(),
                StrikethroughExtension.create()));

        // convert soft-breaks to hard breaks
//        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
    }

    /**
     * 解析 markdown 为 html
     * @param mdStr
     * @return
     */
    public static String parse2Html(String mdStr) {

        try {
            Node document = parser.parse(mdStr);
            return renderer.render(document);
        } catch (Exception e) {
            log.error("markdown parse to html exception: ", e);
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(parse2Html("|  fdsf |  fdsf |\n" +
                "| ------------ | ------------ |\n" +
                "|  fdsf |fsdf   |\n" +
                "|fsdf   |  fdsf |\n" +
                "\n" +
                ":warnings:"));
    }

}

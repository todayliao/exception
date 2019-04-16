package com.exception.qms.utils;

import com.vladsch.flexmark.ast.AutoLink;
import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.html.Attributes;
import com.vladsch.flexmark.util.options.MutableDataHolder;
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

    private static final String SITE_NAME = "www.exception.site";

    static {
        // 定制 markdown 选项
        MutableDataSet options = new MutableDataSet();

        // set optional extensions
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(),
                StrikethroughExtension.create(), NofollowExtension.create()));

        // convert soft-breaks to hard breaks
//        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
    }

    static class NofollowExtension implements HtmlRenderer.HtmlRendererExtension {
        @Override
        public void rendererOptions(final MutableDataHolder options) {
            // add any configuration settings to options you want to apply to everything, here
        }

        @Override
        public void extend(final HtmlRenderer.Builder rendererBuilder, final String rendererType) {
            rendererBuilder.attributeProviderFactory(NofollowAttributeProvider.Factory());
        }

        static NofollowExtension create() {
            return new NofollowExtension();
        }
    }

    static class NofollowAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(final Node node, final AttributablePart part, final Attributes attributes) {
            if ((node instanceof Link || node instanceof AutoLink)
                    && (part == AttributablePart.LINK)) {

                attributes.replaceValue("target", "_blank");
                String href = attributes.getValue("href");
                // 对于 md 文档中不包含本站域名的，设置 nofollow
                if (!href.contains(SITE_NAME)) {
                    // Put info in custom attribute instead
                    attributes.replaceValue("rel", "nofollow noopener noreferrer");
                }
            }
        }

        static AttributeProviderFactory Factory() {
            return new IndependentAttributeProviderFactory() {
                @Override
                public AttributeProvider create(LinkResolverContext context) {
                    //noinspection ReturnOfInnerClass
                    return new NofollowAttributeProvider();
                }
            };
        }
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
//        System.out.println(parse2Html("|  fdsf |  fdsf |\n" +
//                "| ------------ | ------------ |\n" +
//                "|  fdsf |fsdf   |\n" +
//                "|fsdf   |  fdsf |\n" +
//                "\n" +
//                ":warnings:"));

        System.out.println(parse2Html("## 一、Spring Boot 是什么\n" +
                "\n" +
                "以下截图自 [Spring Boot 官方文档](https://spring.io/projects/spring-boot/)：\n" +
                "\n" +
                "![什么是Spring Boot](https://exception-image-bucket.oss-cn-hangzhou.aliyuncs.com/155523379091222 \"什么是Spring Boot\")"));
    }

}

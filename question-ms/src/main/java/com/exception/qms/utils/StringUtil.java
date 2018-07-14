package com.exception.qms.utils;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/6
 * @time 下午12:32
 * @discription
 **/
public class StringUtil {

    /**
     * 截取字符串
     *
     * @param originalStr 原始的字符串
     * @param limit       截取的长度
     * @return
     */
    public static String subString(String originalStr, int limit) {
        if (StringUtils.isBlank(originalStr)) {
            return null;
        }

        final int len = originalStr.length();
        if (len - 1 <= limit) {
            return originalStr;
        }

        return originalStr.substring(0, limit);
    }

    //----------------------------------------------------------------------
    // 字符串格式化，易读性处理 - start
    //----------------------------------------------------------------------

    private static final Pattern CJK_ANS = Pattern.compile("([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])([a-z0-9`~@\\$%\\^&\\-_\\+=\\|\\\\/])", 2);
    private static final Pattern ANS_CJK = Pattern.compile("([a-z0-9`~!\\$%\\^&\\-_\\+=\\|\\\\;:,/\\?])([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])", 2);
    private static final Pattern CJK_QUOTE = Pattern.compile("([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])([\"'])");
    private static final Pattern QUOTE_CJK = Pattern.compile("([\"'])([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])");
    private static final Pattern FIX_QUOTE = Pattern.compile("([\"'])(\\s*)(.+?)(\\s*)([\"'])");
    private static final Pattern CJK_BRACKET_CJK = Pattern.compile("([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])([\\({\\[]+(.*?)[\\)}\\]]+)([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])");
    private static final Pattern CJK_BRACKET = Pattern.compile("([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])([\\(\\){}\\[\\]<>])");
    private static final Pattern BRACKET_CJK = Pattern.compile("([\\(\\){}\\[\\]<>])([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])");
    private static final Pattern FIX_BRACKET = Pattern.compile("([(\\(\\[)]+)(\\s*)(.+?)(\\s*)([\\)}\\]]+)");
    private static final Pattern CJK_HASH = Pattern.compile("([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])(#(\\S+))");
    private static final Pattern HASH_CJK = Pattern.compile("((\\S+)#)([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])");

    public static String spacingText(String text) {
        Matcher matcher = CJK_QUOTE.matcher(text);
        text = matcher.replaceAll("$1 $2");

        matcher = QUOTE_CJK.matcher(text);
        text = matcher.replaceAll("$1 $2");

        matcher = FIX_QUOTE.matcher(text);
        text = matcher.replaceAll("$1$3$5");

        String oldText = text;
        matcher = CJK_BRACKET_CJK.matcher(text);
        String newText = matcher.replaceAll("$1 $2 $4");
        text = newText;
        if (oldText.equals(newText)) {
            matcher = CJK_BRACKET.matcher(text);
            text = matcher.replaceAll("$1 $2");

            matcher = BRACKET_CJK.matcher(text);
            text = matcher.replaceAll("$1 $2");
        }
        matcher = FIX_BRACKET.matcher(text);
        text = matcher.replaceAll("$1$3$5");

        matcher = CJK_HASH.matcher(text);
        text = matcher.replaceAll("$1 $2");

        matcher = HASH_CJK.matcher(text);
        text = matcher.replaceAll("$1 $3");

        matcher = CJK_ANS.matcher(text);
        text = matcher.replaceAll("$1 $2");

        matcher = ANS_CJK.matcher(text);
        text = matcher.replaceAll("$1 $2");

        return text;
    }

    public static String getFirstImageUrlFromMarkdown(String mdStr) {
        String pattern= "!\\[(.*?)\\]\\((.*?)\\)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(mdStr);

        String imageUrl = null;
        if (m.find()) {
            String tmp = m.group(0);
            int startIndex = tmp.indexOf("(");
            tmp = tmp.substring(startIndex + 1);
            imageUrl = tmp.substring(0, tmp.length() - 1);
        }

        return imageUrl;
    }

    //----------------------------------------------------------------------
    // 字符串格式化，易读性处理 - end
    //----------------------------------------------------------------------

    public static void main(String[] args) {

        String text = "其实，阿里云还是支持的，首先我们转到 [云盾证书服务的购物车](https://common-buy.aliyun.com/?spm=a2c4e.11155515.0.0.QFIx9e&commodityCode=cas#/buy \"云盾证书服务的购物车\")\n" +
                "\n" +
                "默认当前购物车仅显示 ` 专业版 OV SSL` 和 ` 通配符 DV SSL`，我们需要经过如下操作，才能将 ` 免费型 DV SSL` 的选项显示出来：\n" +
                "\n" +
                "**第一步：请点击 ` 选择品牌 ` 中的 `Symantec`**\n" +
                "\n" +
                "**第二步：![](https://ssexception-image-bucket.oss-cn-hangzhou.aliyuncs.com/152845944491179)点击 ` 保护类型 ` 中的 `1 个域名 `**\n" +
                "\n" +
                "接下来，我们就能选择免费的 https ssl 证书了进行申请了：\n" +
                "\n" +
                "![](https://exception-image-bucket.oss-cn-hangzhou.aliyuncs.com/152845944491179)";

//        String content = "<a href=\"URL\">";
        System.out.println(getFirstImageUrlFromMarkdown(text));

    }

}

package com.exception.qms.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

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


    private static Pattern p = Pattern.compile("[^\\x00-\\xff]");
    /**
     * 从 html 中抽取正文
     * @param html
     * @return
     */
    public static String getContentFromHtml(String html) {
        Assert.hasText(html, "html 不能为空");
        // 匹配双字节字符(包括汉字在内)
        Matcher m = p.matcher(html);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            sb.append(m.group());
        }
        return sb.toString();
    }

    public static void main(String[] args) {


//        System.out.println(text);
    }

}

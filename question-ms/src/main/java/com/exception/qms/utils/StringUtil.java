package com.exception.qms.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.Random;
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

    private static final Pattern CJK_ANS = Pattern.compile("([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])([a-z0-9`~@\\$%\\^&\\*\\-_\\+=\\|\\\\/])", 2);
    /**
     * 去掉 .
     */
    private static final Pattern ANS_CJK = Pattern.compile("([a-z0-9`~!\\$%\\^&\\*\\-_\\+=\\|\\\\;:,\\?])([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])", 2);
    private static final Pattern CJK_QUOTE = Pattern.compile("([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])([\"'])");
    private static final Pattern QUOTE_CJK = Pattern.compile("([\"'])([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])");
    private static final Pattern FIX_QUOTE = Pattern.compile("([\"'])(\\s*)(.+?)(\\s*)([\"'])");
    private static final Pattern CJK_BRACKET_CJK = Pattern.compile("([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])([\\({\\[]+(.*?)[\\)}\\]]+)([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])");
    private static final Pattern CJK_BRACKET = Pattern.compile("([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])([\\(\\){}\\[\\]<>])");
    private static final Pattern BRACKET_CJK = Pattern.compile("([\\(\\){}\\[\\]<>])([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])");
    private static final Pattern FIX_BRACKET = Pattern.compile("([(\\({\\[)]+)(\\s*)(.+?)(\\s*)([\\)}\\]]+)");
    private static final Pattern CJK_HASH = Pattern.compile("([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])(#(\\S+))");
    private static final Pattern HASH_CJK = Pattern.compile("((\\S+)#)([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])");

    /**
     * 字符串格式化处理，增加易读性
     *
     * @param text
     * @return
     */
    public static String spacingText(String text) {
        Matcher cqMatcher = CJK_QUOTE.matcher(text);
        text = cqMatcher.replaceAll("$1 $2");
        Matcher qcMatcher = QUOTE_CJK.matcher(text);
        text = qcMatcher.replaceAll("$1 $2");
        Matcher fixQuoteMatcher = FIX_QUOTE.matcher(text);
        text = fixQuoteMatcher.replaceAll("$1$3$5");
        String oldText = text;
        Matcher cbcMatcher = CJK_BRACKET_CJK.matcher(text);
        String newText = cbcMatcher.replaceAll("$1 $2 $4");
        text = newText;
        Matcher fixBracketMatcher;
        Matcher chMatcher;
        if (oldText.equals(newText)) {
            fixBracketMatcher = CJK_BRACKET.matcher(newText);
            text = fixBracketMatcher.replaceAll("$1 $2");
            chMatcher = BRACKET_CJK.matcher(text);
            text = chMatcher.replaceAll("$1 $2");
        }

        fixBracketMatcher = FIX_BRACKET.matcher(text);
        text = fixBracketMatcher.replaceAll("$1$3$5");
        chMatcher = CJK_HASH.matcher(text);
        text = chMatcher.replaceAll("$1 $2");
        Matcher hcMatcher = HASH_CJK.matcher(text);
        text = hcMatcher.replaceAll("$1 $3");
        Matcher caMatcher = CJK_ANS.matcher(text);
        text = caMatcher.replaceAll("$1 $2");
        Matcher acMatcher = ANS_CJK.matcher(text);
        text = acMatcher.replaceAll("$1 $2");
        return text;
    }

    //----------------------------------------------------------------------
    // 字符串格式化，易读性处理 - end
    //----------------------------------------------------------------------


}

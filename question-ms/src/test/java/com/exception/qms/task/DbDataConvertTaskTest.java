package com.exception.qms.task;

import com.exception.qms.domain.mapper.AnswerDescMapper;
import com.exception.qms.domain.mapper.AnswerMapper;
import com.exception.qms.domain.mapper.QuestionDescMapper;
import com.exception.qms.domain.mapper.QuestionMapper;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/1
 * @time 下午5:23
 * @discription
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class DbDataConvertTaskTest {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionDescMapper questionDescMapper;
    @Autowired
    private DbDataConvertTask dbDataConvertTask;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private AnswerDescMapper answerDescMapper;

    @Test
    public void testQuestionConvertStart() {

//        List<Question> questionList = questionMapper.selectPage();
//
//        for (Question question : questionList) {
//            dbDataConvertTask.questionConvertStart(question);
//        }

        MutableDataSet options = new MutableDataSet();

        // uncomment to set optional extensions
        //options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

        // uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        // You can re-use parser and renderer instances
        Node document = parser.parse("This is **Sparta**");
        String html = renderer.render(document);  // "<p>This is <em>Sparta</em></p>\n"
        System.out.println(html);
    }

//    @Test
//    public void testQuestionDescConvertStart() {
//        List<QuestionDescWithBLOBs> questionDescWithBLOBs = questionDescMapper.selectPage(1, 2);
//
//        for (QuestionDescWithBLOBs question : questionDescWithBLOBs) {
//            dbDataConvertTask.questionDescConvertStart(question);
//        }
//    }
//
//    @Test
//    public void testAnswerConvertStart() {
//        List<Answer> answers = answerMapper.selectPage(1, 2);
//
//        for (Answer answer : answers) {
//            dbDataConvertTask.answerConvertStart(answer);
//        }
//    }
//
//    @Test
//    public void testAnswerDescConvertStart() {
//        List<AnswerDesc> answerDescs = answerDescMapper.selectPage(1, 2);
//
//        for (AnswerDesc answerDesc : answerDescs) {
//            dbDataConvertTask.answerDescConvertStart(answerDesc);
//        }
//    }
//
//    @Autowired
//    private TagMapper tagMapper;
//    @Test
//    public void testTagConvertStart() {
//        List<Tag> tags = tagMapper.selectPage(1, 2);
//
//        for (Tag tag : tags) {
//            dbDataConvertTask.tagConvertStart(tag);
//        }
//    }
}
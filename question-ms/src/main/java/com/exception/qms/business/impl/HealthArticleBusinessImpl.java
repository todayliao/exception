package com.exception.qms.business.impl;

import com.exception.qms.business.HealthArticleBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;
import com.exception.qms.domain.entity.*;
import com.exception.qms.enums.QmsResponseCodeEnum;
import com.exception.qms.exception.QMSException;
import com.exception.qms.service.HealthArticleService;
import com.exception.qms.service.RedisService;
import com.exception.qms.utils.IpUtil;
import com.exception.qms.web.dto.healthArticle.request.HealthArticleReadNumIncreaseRequestDTO;
import com.exception.qms.web.dto.healthArticle.response.QueryHealthArticleContentResponseDTO;
import com.exception.qms.web.dto.healthArticle.response.QueryHealthArticleItemResponseDTO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/20
 * @time 下午3:44
 * @discription
 **/
@Service
@Slf4j
public class HealthArticleBusinessImpl implements HealthArticleBusiness {

    @Autowired
    private HealthArticleService healthArticleService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private Mapper mapper;

    @Override
    public BaseResponse queryHealthArticleContent(Long articleId) {
        HealthArticle healthArticle = healthArticleService.queryHealthArticle(articleId);

        if (healthArticle == null) {
            log.warn("the health article is not exited, id: {}", articleId);
            throw new QMSException(QmsResponseCodeEnum.ARTICLE_NOT_EXIST);
        }

        QueryHealthArticleContentResponseDTO queryHealthArticleContentResponseDTO = mapper.map(healthArticle, QueryHealthArticleContentResponseDTO.class);

        HealthArticleContent healthArticleContent = healthArticleService.queryHealthArticleContent(articleId);
        queryHealthArticleContentResponseDTO.setContent(healthArticleContent.getContent());
        return new BaseResponse().success(queryHealthArticleContentResponseDTO);
    }

    @Override
    public PageQueryResponse queryHealthArticleList(Integer pageIndex, Integer pageSize) {
        int totalCount = healthArticleService.queryHealthArticleListCount();

        List<QueryHealthArticleItemResponseDTO> queryHealthArticleItemResponseDTOList = null;
        if (totalCount > 0) {
            List<HealthArticle> healthArticles = healthArticleService.queryHealthArticleList(pageIndex, pageSize);
            queryHealthArticleItemResponseDTOList = healthArticles.stream()
                    .map(healthArticle -> mapper.map(healthArticle, QueryHealthArticleItemResponseDTO.class))
                    .collect(Collectors.toList());
        }
        return new PageQueryResponse().successPage(queryHealthArticleItemResponseDTOList, pageIndex, totalCount, pageSize);
    }

    @Override
    public BaseResponse increaseReadNum(Long artileId, HttpServletRequest request) {
        log.info("increaseReadNum, the remote ip: {}", IpUtil.getIpAddr(request));

        if (artileId == null) {
            log.warn("the health article id can't null");
            throw new QMSException(QmsResponseCodeEnum.PARAM_ERROR);
        }

        String redisKey = String.format("ha_%s_%s", IpUtil.getIpAddr(request), artileId);
        boolean isExisted = redisService.exists(redisKey);

        final long expireSeconds = 1*60*60;

        if (isExisted) {
            log.warn("Can't increase the readNum of the article, the key already existed on the cache: {}", redisKey);
            // expire the key, one hour
            redisService.expire(redisKey, expireSeconds);
            return new BaseResponse().fail();
        }

        int count = healthArticleService.increaseReadNum(artileId);

        if (count > 0) {
            redisService.set(redisKey, "", expireSeconds);
            return new BaseResponse().success();
        }
        return new BaseResponse().fail();
    }

    @Override
    public BaseResponse queryTestArticle() {
        String testArticleHtml = "\n" +
                "                    \n" +
                "\n" +
                "                    \n" +
                "\n" +
                "                    \n" +
                "                    \n" +
                "                    <section class=\"\" data-tools=\"新媒体管家\" data-label=\"powered by xmt.cn\" data-mpa-powered-by=\"yiban.io\"></section><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"font-size: 18px;\">01</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">饱不洗头，饿不洗澡。</p><p style=\"text-align: center;\">冷水洗脸，美容保健。</p><p style=\"text-align: center;\">汗水没落，冷水莫浇。</p><p style=\"text-align: center;\">温水刷牙，防敏固齿。</p></section></section></section><p><br></p></section><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">02</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">吃米带糠，吃菜带帮。</p><p style=\"text-align: center;\">男不离韭，女不离藕。</p><p style=\"text-align: center;\">青红萝卜，生克熟补。</p><p style=\"text-align: center;\">食不过饱，饱不急卧。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">03</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">养生在动，养心在静。</p><p style=\"text-align: center;\">心不清净，思虑妄生。</p><p style=\"text-align: center;\">心神安宁，病从何生。</p><p style=\"text-align: center;\">闭目养神，静心益智。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">04</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">药补食补，莫忘心补。</p><p style=\"text-align: center;\">以财为草，以身为宝。</p><p style=\"text-align: center;\">烟熏火燎，不吃为好。</p><p style=\"text-align: center;\">油炸腌泡，少吃为妙。</p></section></section></section><p><br></p><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">05</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">臭鱼烂虾，索命冤家。</p><p style=\"text-align: center;\">食服常温，一体皆春。</p><p style=\"text-align: center;\">冷勿冰齿，热勿灼唇。</p><p style=\"text-align: center;\">物熟始食，水沸始饮。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">06</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">多食果菜，少食肉类。</p><p style=\"text-align: center;\">饮食有节，起居有时。</p><p style=\"text-align: center;\">头部宜冷，足部宜热。</p><p style=\"text-align: center;\">知足常乐，无求常安。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">07</span></strong></em></span></p><p><br></p><section><section><p style=\"text-align: center;\">养生在勤，养心在静。</p></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">08</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">人到老年，必须锻练，</p><p style=\"text-align: center;\">散步慢跑，练拳舞剑；</p><p style=\"text-align: center;\">莫怕严寒，清扫庭院，</p><p style=\"text-align: center;\">绘画添趣，心胸广宽。</p></section></section></section><p><br></p><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">09</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">闻鸡起舞，床不可贪，</p><p style=\"text-align: center;\">种花养鸟，习书览篇，</p><p style=\"text-align: center;\">私事勿念，便宜勿占。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">10</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">活动身体，贵在经常，</p><p style=\"text-align: center;\">心情舒畅，长寿健康；</p><p style=\"text-align: center;\">遇事勿怒，劳勿过偏，</p><p style=\"text-align: center;\">茶水勿浓，学习勿念。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">11</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">饮食勿暴，少吃晚餐，</p><p style=\"text-align: center;\">吃饭勿语，切勿吸烟；</p><p style=\"text-align: center;\">低盐低糖，勿食太咸，</p><p style=\"text-align: center;\">少吃脂肪，饭莫过量。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">12</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">每日三餐，调剂适当，</p><p style=\"text-align: center;\">蔬菜水果，多吃无防；</p><p style=\"text-align: center;\">按时入睡，定时起床，</p><p style=\"text-align: center;\">起身要慢，勿急勿慌。</p></section></section></section><p><br></p><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">13</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">饮酒勿过，名利勿钻，</p><p style=\"text-align: center;\">闲气勿生，胸怀要宽。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">14</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">心无病，防为早，</p><p style=\"text-align: center;\">心里健康身体好；</p><p style=\"text-align: center;\">心平衡，要知晓，</p><p style=\"text-align: center;\">情绪稳定疾病少。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">15</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">练身体，动与静，</p><p style=\"text-align: center;\">弹性生活健心妙；</p><p style=\"text-align: center;\">要食养，八分饱，</p><p style=\"text-align: center;\">脏腑轻松自疏导。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">16</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">人生气，易衰老，</p><p style=\"text-align: center;\">适当宣泄人欢笑；</p><p style=\"text-align: center;\">品书画，溪边钓，</p><p style=\"text-align: center;\">选择爱好自由挑。</p></section></section></section><p><br></p><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">17</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">动脑筋，不疲劳，</p><p style=\"text-align: center;\">思睡养心少热闹；</p><p style=\"text-align: center;\">有规律，健身好，</p><p style=\"text-align: center;\">正常生活要协调。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">18</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">常搓手，可健脑，</p><p style=\"text-align: center;\">防止冻疮和感冒。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">19</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">夏不睡石，秋不睡板。</p><p style=\"text-align: center;\">春不露脐，冬不蒙头。</p><p style=\"text-align: center;\">白天多动，夜里少梦。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">20</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">睡前洗脚，胜吃补药。</p><p style=\"text-align: center;\">晚上开窗，一觉都香。</p><p style=\"text-align: center;\">贪凉失盖，不病才怪。</p></section></section></section><p><br></p><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">21</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">早睡早起，怡神爽气。</p><p style=\"text-align: center;\">贪房贪睡，添病减岁。</p><p style=\"text-align: center;\">夜里磨牙，肚里虫爬。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">22</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">一天吃一头猪，</p><p style=\"text-align: center;\">不如床上打呼噜。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">23</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">三天吃一只羊，</p><p style=\"text-align: center;\">不如洗脚再上床。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">24</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">枕头不选对，</p><p style=\"text-align: center;\">越睡人越累。</p><p style=\"text-align: center;\">先睡心，后睡人，</p><p style=\"text-align: center;\">睡觉睡出大美人。</p></section></section></section><p><br></p><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">25</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">头对风，暖烘烘；</p><p style=\"text-align: center;\">脚对风，请郎中。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">26</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">睡觉莫睡巷，</p><p style=\"text-align: center;\">最毒穿堂风。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"color: rgb(122, 68, 66);font-size: 18px;\">27</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">睡觉不点灯，</p><p style=\"text-align: center;\">早起头不晕。</p></section></section></section><p><br></p><p style=\"text-align: center;\"><span style=\"color: rgb(122, 68, 66);\"><em><strong><span style=\"font-size: 18px;\">28</span></strong></em></span></p><p><br></p><section><section><section class=\"\"><p style=\"text-align: center;\">要想睡得人轻松，</p><p style=\"text-align: center;\">切莫脚朝西来头朝东。</p></section></section></section><p><br></p>\n";

        QueryHealthArticleContentResponseDTO queryHealthArticleContentResponseDTO = new QueryHealthArticleContentResponseDTO();
        queryHealthArticleContentResponseDTO.setContent(testArticleHtml);
        queryHealthArticleContentResponseDTO.setTitle("28条养生口诀，最健康的生活方式！");
        return new BaseResponse().success(queryHealthArticleContentResponseDTO);
    }

    @Override
    public BaseResponse queryVersion() {
        int version = 0;
        // 查询 redis
        boolean isExisted = redisService.exists("ha_version");
        if (isExisted) {
            version = 1;
        }
        return new BaseResponse().success(version);
    }

}

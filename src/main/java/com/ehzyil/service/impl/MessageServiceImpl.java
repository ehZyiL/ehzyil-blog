package com.ehzyil.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ehzyil.domain.Message;
import com.ehzyil.domain.SiteConfig;
import com.ehzyil.mapper.MessageMapper;
import com.ehzyil.model.dto.CheckDTO;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.dto.MessageDTO;
import com.ehzyil.model.vo.admin.MessageBackVO;
import com.ehzyil.model.vo.front.MessageVO;
import com.ehzyil.model.vo.front.PageResult;
import com.ehzyil.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.service.ISiteConfigService;
import com.ehzyil.service.RedisService;
import com.ehzyil.service.UserWhiteListService;
import com.ehzyil.utils.HTMLUtils;
import com.ehzyil.utils.IpUtils;
import com.ehzyil.utils.PageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ehzyil.constant.CommonConstant.IS_NOT_CHECK;
import static com.ehzyil.constant.RedisConstant.SITE_SETTING;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
    @Autowired
   private   HttpServletRequest request;
    @Autowired
    private ISiteConfigService siteConfigService;
    @Autowired
    private RedisService redisService;
    @Override
    public List<MessageVO> listTalkHome() {
       return getBaseMapper().selectMessageVoList();
    }
    @Autowired
    private  UserWhiteListService userWhiteListService;

    @Override
    public void addMessage(MessageDTO message) {
        Message newMessage = new Message();
        //拷贝信息
        BeanUtils.copyProperties(message,newMessage);
        //获取站点信息
        SiteConfig siteConfig=redisService.getObject(SITE_SETTING);

        //设置是否检查
        if (userWhiteListService.needToReview( StpUtil.getLoginIdAsInt())) {
            newMessage.setIsCheck(Optional.ofNullable(siteConfig.getMessageCheck()).orElse(0));
        } else {
            newMessage.setIsCheck(IS_NOT_CHECK);
        }

        //获取IP
        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        newMessage.setIpAddress(ipAddress);
        newMessage.setIpSource(ipSource);

        //敏感词过滤
        newMessage.setMessageContent(HTMLUtils.filter(message.getMessageContent()));

        getBaseMapper().insert(newMessage);
    }

    @Override
    public PageResult<MessageBackVO> listMessageBackVO(ConditionDTO condition) {
        // 查询留言数量
        Long count = getBaseMapper().selectCount(new LambdaQueryWrapper<Message>()
                .like(StringUtils.hasText(condition.getKeyword()), Message::getNickname, condition.getKeyword())
                .eq(Objects.nonNull(condition.getIsCheck()), Message::getIsCheck, condition.getIsCheck()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台友链列表
        List<MessageBackVO> messageBackVOList = getBaseMapper().selectMessageBackVOList(PageUtils.getLimit(), PageUtils.getSize(), condition);
        return new PageResult<>(messageBackVOList, count);

    }
    @Override
    public void updateMessageCheck(CheckDTO check) {
        // 修改留言审核状态
        List<Message> messageList = check.getIdList()
                .stream()
                .map(id -> Message.builder()
                        .id(id)
                        .isCheck(check.getIsCheck())
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(messageList);
    }
}

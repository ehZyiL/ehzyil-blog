package com.ehzyil.service.impl;

import com.ehzyil.domain.Message;
import com.ehzyil.domain.SiteConfig;
import com.ehzyil.mapper.MessageMapper;
import com.ehzyil.model.dto.MessageDTO;
import com.ehzyil.model.vo.MessageVO;
import com.ehzyil.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.service.ISiteConfigService;
import com.ehzyil.utils.HTMLUtils;
import com.ehzyil.utils.IpUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @Override
    public List<MessageVO> listTalkHome() {
       return getBaseMapper().selectMessageVoList();
    }

    @Override
    public void addMessage(MessageDTO message) {
        Message newMessage = new Message();
        //拷贝信息
        BeanUtils.copyProperties(message,newMessage);
        //获取站点信息
//        SiteConfig siteConfig=
        //TODO 设置是否检查
        newMessage.setIsCheck(0);
        //获取IP
        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        newMessage.setIpAddress(ipAddress);
        newMessage.setIpSource(ipSource);

        //敏感词过滤
        newMessage.setMessageContent(HTMLUtils.filter(message.getMessageContent()));

        getBaseMapper().insert(newMessage);
    }
}

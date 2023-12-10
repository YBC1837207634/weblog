package com.gong.blog.core.handler;

import com.alibaba.fastjson2.JSON;
import com.gong.blog.core.dto.MessageDTO;
import com.gong.blog.core.entity.Message;
import com.gong.blog.core.entity.SessionRecord;
import com.gong.blog.core.mapper.SessionRecordMapper;
import com.gong.blog.core.service.MessageService;
import com.gong.blog.core.service.ThreadService;
import com.gong.blog.core.vo.MessageVo;
import com.gong.blog.core.vo.SessionRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketHandlerImpl extends TextWebSocketHandler {

    // 存放所有的创建的 ws 实例
    private static final Map<String, WebSocketSession> webSocketMap = new ConcurrentHashMap<>();

    @Autowired
    private MessageService messageService;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private SessionRecordMapper sessionRecordMapper;

    /**
     * 在 WebSocket 协商成功且 WebSocket 连接打开并可供使用后调用
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        String uid = attributes.get("uid").toString();
        // 重复连接
        if (webSocketMap.get(uid) != null) {
            WebSocketSession webSocketSession = webSocketMap.get(uid);
            webSocketSession.sendMessage(new TextMessage("重复链接"));
            webSocketSession.close();
            // 将最新的连接保存
        }
        webSocketMap.put(uid, session);
        log.info("当前连接人数：{}", webSocketMap.size());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        MessageDTO messageDTO = JSON.parseObject(message.getPayload().toString(), MessageDTO.class);
        messageDTO.setSenderId(Long.valueOf(session.getAttributes().get("uid").toString()));
        String receiverId = messageDTO.getReceiverId().toString();
        // 解析消息并转发给指定用户
        Message msg = messageService.parserTextMessage(messageDTO);
        try {
            MessageVo vo = messageService.convertVO(msg);
            // 如果session中没有目标，则发送离线消息
            if (webSocketMap.get(receiverId) != null) {
                // 发送
                webSocketMap.get(receiverId).sendMessage(new TextMessage(JSON.toJSONString(vo)));
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            msg.setMsgStatus("0");
        } finally {
            // 保存到数据库中
            messageService.save(msg);
            // 更新记录
            SessionRecord record = new SessionRecordVo();
            record.setSenderId(msg.getSenderId());
            record.setReceiverId(msg.getReceiverId());
            threadService.updateSessionTime(sessionRecordMapper, record);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("id: {}, message: {}",session.getAttributes().get("uid"), exception.getMessage());
    }

    /**
     * 在任一端关闭 WebSocket 连接或发生传输错误后调用。
     * 会话可能仍处于打开状态，但根据基础实现，不建议此时发送消息，并且很可能不会成功。
     * @param session
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        try {
            webSocketMap.remove(session.getAttributes().get("uid").toString());
        } catch (Exception ex) {
            log.error("webSocketMap清理时出现错误: {}",ex.getMessage());
        }
        log.info("连接关闭后当前人数：{}", webSocketMap.size());
    }

    @Override
    public boolean supportsPartialMessages() {

        return false;
    }
}

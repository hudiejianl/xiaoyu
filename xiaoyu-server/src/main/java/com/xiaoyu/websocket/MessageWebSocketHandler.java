package com.xiaoyu.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoyu.dto.message.MessageCreateDTO;
import com.xiaoyu.service.MessageService;
import com.xiaoyu.vo.message.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.Map;

/**
 * 私信WebSocket处理器
 * 
 * @author xiaoyu
 */
@Component
@Slf4j
public class MessageWebSocketHandler implements WebSocketHandler {

    @Autowired
    private MessageService messageService;

    @Autowired
    private WebSocketSessionManager sessionManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            sessionManager.addUserSession(userId, session);
            log.info("用户 {} 私信WebSocket连接建立", userId);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Long fromUserId = (Long) session.getAttributes().get("userId");
        if (fromUserId == null) {
            return;
        }

        try {
            // 解析消息
            String payload = message.getPayload().toString();
            MessageCreateDTO messageDTO = objectMapper.readValue(payload, MessageCreateDTO.class);

            // 发送消息
            MessageVO messageVO = messageService.sendMessage(fromUserId, messageDTO);

            // 实时推送给接收者
            boolean sent = sessionManager.sendMessageToUser(messageDTO.getToId(), messageVO);
            if (sent) {
                log.info("实时推送消息给用户 {}", messageDTO.getToId());
            } else {
                log.info("用户 {} 不在线，消息已存储", messageDTO.getToId());
            }

            // 给发送者确认
            String confirmJson = objectMapper.writeValueAsString(Map.of(
                    "type", "confirm",
                    "messageId", messageVO.getId(),
                    "status", "sent"));
            session.sendMessage(new TextMessage(confirmJson));

        } catch (Exception e) {
            log.error("处理WebSocket消息失败: {}", e.getMessage(), e);
            sessionManager.sendErrorMessage(session, "消息发送失败");
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        log.error("用户 {} WebSocket传输错误: {}", userId, exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            sessionManager.removeUserSession(userId);
            log.info("用户 {} 私信WebSocket连接关闭", userId);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 检查用户是否在线
     */
    public boolean isUserOnline(Long userId) {
        return sessionManager.isUserOnline(userId);
    }

    /**
     * 向指定用户发送消息
     */
    public boolean sendMessageToUser(Long userId, Object message) {
        return sessionManager.sendMessageToUser(userId, message);
    }
}

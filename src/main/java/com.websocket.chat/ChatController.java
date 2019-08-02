package com.websocket.chat;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableScheduling
@RestController
public class ChatController {

   /* @Autowired
    private ChatHistoryDao chatHistoryDao;*/

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


    @MessageMapping("/user")
    @SendTo("/topic/user")
    public Map<String, String> addUser(@Payload Map<String, String> message) throws Exception {
        message.put("timestamp", Long.toString(System.currentTimeMillis()));
        message.put("username", message.get("username"));
        message.put("type", "JOIN");
       // chatHistoryDao.save(message);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        chatMessage.setSender(message.get("username"));
        chatMessage.setContent("Thank you for joining");
        messagingTemplate.convertAndSend("/topic/"+message.get("username"), chatMessage);
        return message;
    }

    @MessageMapping("/message")
    @SendTo("/topic/message")
    public Map<String, String> message(@Payload Map<String, String> message) {
        message.put("timestamp", Long.toString(System.currentTimeMillis()));
        message.put("message", message.get("message"));
        message.put("type", "CHAT");
       // chatHistoryDao.save(message);
        return message;
    }

   /* @RequestMapping("/history")
    public List<Map<String, String>> getChatHistory() {
        return chatHistoryDao.get();
    }*/
}


package wkda.api.call.center.notification.websocket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import wkda.api.call.center.notification.service.WebSocketService;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {
    final WebSocketService webSocketService;

    @MessageMapping("/message/{toUser}")
    public Boolean sendMessage(
            Principal principal,
            @Header String authKey,
            @DestinationVariable String toUser,
            @RequestBody WebSocketRequestMessage message) {
        log.info("Send message from user {} to user {}. Auth key {}", principal.getName(), toUser, authKey);
        webSocketService.notifyUser(toUser, message.getContent());
        return Boolean.TRUE;
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public String broadcastNews(@Payload String message) {
        log.info("Broadcast message : {}",message);
        return message;
    }

    @Getter
    @Setter
    public static class WebSocketRequestMessage {
        private String content;
    }
}

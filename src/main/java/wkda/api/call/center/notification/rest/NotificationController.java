package wkda.api.call.center.notification.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wkda.api.call.center.notification.model.Notification;
import wkda.api.call.center.notification.service.WebSocketService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationController {

    final WebSocketService service;

    @PostMapping(path = "/notification/{user}")
    void notifyUser(@PathVariable("user") String user, @RequestBody Notification request) {
        log.info("Request: {}", request);
        service.notifyUser(user, request.getMessage());
    }

    @PostMapping(path = "/notification")
    void notifyUser(@RequestBody Notification request) {
        log.info("Request: {}", request);
        service.notifyUser(request.getMessage());
    }

}

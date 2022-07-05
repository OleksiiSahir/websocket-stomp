package wkda.api.call.center.notification.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompReactorNettyCodec;
import org.springframework.messaging.tcp.reactor.ReactorNettyTcpClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import reactor.netty.tcp.SslProvider;
import wkda.api.call.center.notification.intercept.WebSocketChannelInterceptor;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${broker.host}")
    private String brokerHost;

    @Value("${broker.port}")
    private int brokerPort;

    @Value("${broker.username}")
    private String brokerUser;

    @Value("${broker.password}")
    private String brokerPass;

    @Autowired
    WebSocketChannelInterceptor channelInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-register")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
//
//        ReactorNettyTcpClient<byte[]> client = new ReactorNettyTcpClient<>(tcpClient -> tcpClient
//                .host(brokerHost)
//                .port(brokerPort)
//                .secure(SslProvider.defaultClientProvider()), new StompReactorNettyCodec());
//
//        registry.enableStompBrokerRelay("/queue", "/topic")
//                .setAutoStartup(true)
//                .setClientLogin(brokerUser)
//                .setClientPasscode(brokerPass)
//                .setSystemLogin(brokerUser)
//                .setSystemPasscode(brokerPass)
//                .setTcpClient(client);

//       registry.enableStompBrokerRelay("/queue", "/topic")
//                .setRelayHost(brokerHost)
//                .setRelayPort(brokerPort)
//                .setClientLogin(brokerUser)
//                .setClientPasscode(brokerPass)
//                .setSystemLogin(brokerUser)
//                .setSystemPasscode(brokerPass)
//                .setUserDestinationBroadcast("/topic/unresolved-user")
//                .setUserRegistryBroadcast("/topic/log-user-registry");

        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelInterceptor);
    }
}

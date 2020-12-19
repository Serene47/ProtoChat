package com.serene.protochat.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WSController implements WebSocketConfigurer{

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		
		registry.addHandler(customWebSocketHandler(), "chat")
		.addInterceptors(new HttpHandshakeInterceptor());
	}
	
	@Bean
	public CustomWebSocketHandler customWebSocketHandler() {
		return new CustomWebSocketHandler();
	}

	
}

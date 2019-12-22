package com.niek125.updateserver;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.niek125.updateserver.dispatcher.Dispatcher;
import com.niek125.updateserver.dispatcher.KafkaDispatcher;
import com.niek125.updateserver.dispatcher.SocketDispatcher;
import com.niek125.updateserver.handlers.Handler;
import com.niek125.updateserver.handlers.MessageHandler;
import com.niek125.updateserver.handlers.TokenHandler;
import com.niek125.updateserver.socket.SessionList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

import static com.niek125.updateserver.utils.PemUtils.readPublicKeyFromFile;

@Configuration
public class Config {

    @Bean
    public JWTVerifier jwtVerifier() throws IOException {
        Algorithm algorithm = Algorithm.RSA512((RSAPublicKey) readPublicKeyFromFile("src/main/resources/PublicKey.pem", "RSA"), null);
        return JWT.require(algorithm).withIssuer("data-editor-token-service").build();
    }

    @Bean
    @Scope("singleton")
    public SessionList sessionList(){
        return new SessionList();
    }

    @Bean
    @Autowired
    public List<Dispatcher> dispatchers(KafkaDispatcher kafkaDispatcher, SocketDispatcher socketDispatcher) {
        final List<Dispatcher> dispatchers = new ArrayList<>();
        dispatchers.add(kafkaDispatcher);
        dispatchers.add(socketDispatcher);
        return dispatchers;
    }

    @Bean
    @Autowired
    public List<Handler> handlers(MessageHandler messageHandler, TokenHandler tokenHandler) {
        final List<Handler> handlers = new ArrayList<>();
        handlers.add(messageHandler);
        handlers.add(tokenHandler);
        return handlers;
    }
}

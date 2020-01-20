package com.niek125.updateserver;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.niek125.updateserver.dispatchers.Dispatcher;
import com.niek125.updateserver.dispatchers.KafkaDispatcher;
import com.niek125.updateserver.dispatchers.SocketDispatcher;
import com.niek125.updateserver.handlers.*;
import com.niek125.updateserver.socket.SessionList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${com.niek125.publickey}")
    private String publickey;

    @Value("${com.niek125.allowed-token-signer}")
    private String tokenSigner;

    @Bean
    public JWTVerifier jwtVerifier() throws IOException {
        final Algorithm algorithm = Algorithm.RSA512((RSAPublicKey) readPublicKeyFromFile(publickey, "RSA"), null);
        return JWT.require(algorithm).withIssuer(tokenSigner).build();
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
    public List<Handler> handlers(MessageHandler messageHandler, DataHandler dataHandler, RoleHandler roleHandler) {
        final List<Handler> handlers = new ArrayList<>();
        handlers.add(messageHandler);
        handlers.add(dataHandler);
        handlers.add(roleHandler);
        return handlers;
    }
}

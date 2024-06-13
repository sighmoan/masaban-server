package com.masagal.masaban_server;

import com.masagal.masaban_server.services.BoardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MasabanConfig {

    @Bean
    Logger getLogger() {
        return LogManager.getLogger();
    }
}

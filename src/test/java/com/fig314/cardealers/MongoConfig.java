package com.fig314.cardealers;

import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class MongoConfig {

    public static int mongodPort;
    public static String defaultHost = "localhost";
    static {
        try {
            mongodPort = Network.getFreeServerPort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public IMongodConfig prepareMongodConfig() throws IOException {
        IMongoCmdOptions cmdOptions = new MongoCmdOptionsBuilder()
                .useNoPrealloc(false)
                .useSmallFiles(false)
                .master(false)
                .verbose(false)
                .useNoJournal(false)
                .syncDelay(0)
                .build();

        IMongodConfig mongoConfigConfig = new MongodConfigBuilder()
                .version(Version.V4_0_2)
                .net(new Net(mongodPort, Network.localhostIsIPv6()))
                .configServer(false)
                .cmdOptions(cmdOptions)
                .build();

        return mongoConfigConfig;
    }

}
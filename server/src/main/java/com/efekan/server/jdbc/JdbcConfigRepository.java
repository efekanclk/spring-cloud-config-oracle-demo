package com.efekan.server.jdbc;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcConfigRepository {private final JdbcClient jdbcClient;

    public JdbcConfigRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


    public int updateConfig(String application, String profile, String propKey, String value) {
        String updateSql = """
            UPDATE PROPERTIES
            SET VALUE = :val 
            WHERE APPLICATION = :app 
              AND PROFILE = :prof 
              AND PROP_KEY = :pkey
            """;

        return jdbcClient.sql(updateSql)
                .param("val", value)
                .param("app", application)
                .param("prof", profile)
                .param("pkey", propKey)
                .update();
    }
}

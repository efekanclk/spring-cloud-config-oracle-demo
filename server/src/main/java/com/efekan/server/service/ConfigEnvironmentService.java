package com.efekan.server.service;
import com.efekan.server.environment.ConfigPropertyRequest;
import com.efekan.server.jdbc.JdbcConfigRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfigEnvironmentService {

    private final JdbcConfigRepository jdbcConfigRepository;
    public ConfigEnvironmentService(JdbcConfigRepository jdbcConfigRepository) {
        this.jdbcConfigRepository = jdbcConfigRepository;
    }

    public boolean updateConfig(ConfigPropertyRequest request) {
        int updatedRows = jdbcConfigRepository.updateConfig(
                request.application(),
                request.profile(),
                request.propKey(),
                request.value()
        );

        return updatedRows > 0;
    }
}

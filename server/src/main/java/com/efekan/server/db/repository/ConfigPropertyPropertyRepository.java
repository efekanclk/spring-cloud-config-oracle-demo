package com.efekan.server.db.repository;

import com.efekan.server.db.entity.ConfigProperty;
import org.springframework.data.repository.CrudRepository;

public interface ConfigPropertyPropertyRepository extends CrudRepository<ConfigProperty, String> {

    // Derived method (türetilmiş metot)
    ConfigProperty findByApplicationAndProfileAndLabelAndKey(String application, String profile, String label, String key);

}
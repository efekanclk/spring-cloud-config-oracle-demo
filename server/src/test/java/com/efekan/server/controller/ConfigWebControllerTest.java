package com.efekan.server;

import com.efekan.server.controller.ConfigWebController;
import com.efekan.server.db.repository.ConfigPropertyRepository;
import com.efekan.server.model.ConfigPropertyAuditDTO;
import com.efekan.server.service.AuditService;
import com.efekan.server.service.ConfigRefreshService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class ConfigWebControllerTest {

    private final ConfigPropertyRepository configPropertyRepository = Mockito.mock(ConfigPropertyRepository.class);
    private final ConfigRefreshService configRefreshService = Mockito.mock(ConfigRefreshService.class);

    @Mock
    private AuditService auditService;

    @Test
    void auditTest() {
        ConfigWebController configWebController = new ConfigWebController(configPropertyRepository, configRefreshService, auditService);

        ConfigPropertyAuditDTO configPropertyAuditDTO = Mockito.mock(ConfigPropertyAuditDTO.class);
        Mockito.when(auditService.getAllRevisions()).thenReturn(List.of(configPropertyAuditDTO));

        Model model = new ConcurrentModel();
        String response = configWebController.showAuditHistory(model);

        Assertions.assertEquals("audit", response);
        Assertions.assertEquals(1, ((List<?>) Objects.requireNonNull(model.getAttribute("audits"))).size());
    }
}
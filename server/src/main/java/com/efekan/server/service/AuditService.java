package com.efekan.server.service;

import com.efekan.server.db.entity.ConfigProperty;
import com.efekan.server.model.ConfigPropertyAuditDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AuditService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ConfigPropertyAuditDTO> getAllRevisions() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        List<?> rawResults = auditReader.createQuery()
                .forRevisionsOfEntity(ConfigProperty.class, false, true)
                .addOrder(AuditEntity.revisionNumber().desc())
                .getResultList();

        List<ConfigPropertyAuditDTO> auditList = new ArrayList<>();

        for (Object result : rawResults) {
            Object[] array = (Object[]) result;
            ConfigProperty entity = (ConfigProperty) array[0];
            DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) array[1];
            RevisionType revisionType = (RevisionType) array[2];

            auditList.add(new ConfigPropertyAuditDTO(
                    revisionEntity.getId(),
                    revisionEntity.getRevisionDate(),
                    revisionType,
                    entity != null ? entity.getId() : null,
                    entity != null ? entity.getApplication() : null,
                    entity != null ? entity.getProfile() : null,
                    entity != null ? entity.getLabel() : null,
                    entity != null ? entity.getKey() : null,
                    entity != null ? entity.getValue() : null
            ));
        }

        return auditList;
    }
}
package com.efekan.server.model;

import org.hibernate.envers.RevisionType;

import java.util.Date;

public record ConfigPropertyAuditDTO(
        Number revisionNumber,
        Date revisionDate,
        RevisionType revisionType,
        String id,
        String application,
        String profile,
        String label,
        String key,
        String value
) {}

-- Suppression des tables si elles existent déjà (pour le développement)
DROP TABLE IF EXISTS banking_messages CASCADE;
DROP TABLE IF EXISTS partners CASCADE;

-- Création de la table partners selon la structure de PartnerEntity
CREATE TABLE partners (
    id UUID PRIMARY KEY,
    alias VARCHAR(100) NOT NULL,
    application VARCHAR(100),
    processed_flow_type VARCHAR(50) NOT NULL,
    partner_type VARCHAR(50) NOT NULL,
    direction VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT partner_alias_unique UNIQUE (alias)
);

-- Création de la table banking_messages (conservée de l'original)
CREATE TABLE banking_messages (
    id UUID PRIMARY KEY,
    message_id VARCHAR(100) NOT NULL,
    payload TEXT,
    partner_id UUID,
    received_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT message_id_unique UNIQUE (message_id),
    CONSTRAINT fk_banking_message_partner FOREIGN KEY (partner_id) REFERENCES partners(id)
);



INSERT INTO partners (id, alias, application, processed_flow_type, partner_type, direction, description, created_at)
VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'PARTNER_A', 'Banking App', 'MESSAGE', 'BANK', 'INBOUND', 'Partenaire bancaire principal pour les transactions de paiement', CURRENT_TIMESTAMP),
    ('6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'PARTNER_B', 'Security App', 'ALERTING', 'FINTECH', 'OUTBOUND', 'Partenaire de sécurité pour la vérification des comptes', CURRENT_TIMESTAMP),
    ('6ba7b811-9dad-11d1-80b4-00c04fd430c8', 'PARTNER_C', 'Transfer App', 'MESSAGE', 'BANK', 'INBOUND', 'Partenaire pour les autorisations de transfert', CURRENT_TIMESTAMP),
    ('6ba7b812-9dad-11d1-80b4-00c04fd430c8', 'PARTNER_D', 'Reporting App', 'NOTIFICATION', 'FINTECH', 'OUTBOUND', 'Partenaire pour les demandes de relevés de compte', CURRENT_TIMESTAMP),
    ('6ba7b813-9dad-11d1-80b4-00c04fd430c8', 'PARTNER_E', 'Loan App', 'MESSAGE', 'BANK', 'INBOUND', 'Partenaire pour le traitement des demandes de prêt', CURRENT_TIMESTAMP);

-- Insertion de données dans la table banking_messages (conservée de l'original avec mise à jour des références)
INSERT INTO banking_messages (id, message_id, payload, partner_id, received_at, created_at, updated_at)
VALUES
    ('f6a7b8c9-d0e1-42f3-5a6b-c7d8e9f0a1b2', 'MSG-001',
     'Payment request for customer A',
     '550e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP - INTERVAL '2 days',  CURRENT_TIMESTAMP - INTERVAL '2 days', NULL),

    ('a7b8c9d0-e1f2-43a4-6b7c-d8e9f0a1b2c3', 'MSG-002',
     'Account verification request',
     '6ba7b810-9dad-11d1-80b4-00c04fd430c8', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', NULL),

    ('b8c9d0e1-f2a3-44b5-7c8d-e9f0a1b2c3d4', 'MSG-003',
     'Transfer authorization',
     '6ba7b811-9dad-11d1-80b4-00c04fd430c8', CURRENT_TIMESTAMP - INTERVAL '12 hours', CURRENT_TIMESTAMP - INTERVAL '12 hours', CURRENT_TIMESTAMP - INTERVAL '11 hours'),

    ('c9d0e1f2-a3b4-45c6-8d9e-f0a1b2c3d4e5', 'MSG-004',
     'Balance inquiry',
     '550e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP - INTERVAL '6 hours', CURRENT_TIMESTAMP - INTERVAL '6 hours', NULL),

    ('d0e1f2a3-b4c5-46d7-9e0f-a1b2c3d4e5f6', 'MSG-005',
     'Account statement request',
     '6ba7b812-9dad-11d1-80b4-00c04fd430c8', CURRENT_TIMESTAMP - INTERVAL '3 hours', CURRENT_TIMESTAMP - INTERVAL '3 hours', CURRENT_TIMESTAMP - INTERVAL '2 hours'),

    ('e1f2a3b4-c5d6-47e8-0f1a-b2c3d4e5f6a7', 'MSG-006',
     '"Card activation request',
     '6ba7b810-9dad-11d1-80b4-00c04fd430c8', CURRENT_TIMESTAMP - INTERVAL '1 hour', CURRENT_TIMESTAMP - INTERVAL '1 hour', NULL),

    ('f2a3b4c5-d6e7-48f9-1a2b-c3d4e5f6a7b8', 'MSG-007',
     'Loan application processing',
     '6ba7b813-9dad-11d1-80b4-00c04fd430c8', CURRENT_TIMESTAMP - INTERVAL '30 minutes', CURRENT_TIMESTAMP - INTERVAL '30 minutes', NULL),

    ('a3b4c5d6-e7f8-49a0-2b3c-d4e5f6a7b8c9', 'MSG-008',
     'Fraud alert notification',
     '6ba7b811-9dad-11d1-80b4-00c04fd430c8', CURRENT_TIMESTAMP - INTERVAL '15 minutes', CURRENT_TIMESTAMP - INTERVAL '15 minutes', NULL);
CREATE TABLE hydra_service_nodes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    guid VARCHAR(82) UNIQUE NOT NULL,
    type VARCHAR(512),
    base_data_guid VARCHAR(82),
    node_metadata_guid VARCHAR(82)
);
CREATE TABLE hydra_service_node_tree (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    guid VARCHAR(82) NOT NULL,
    parent_guid VARCHAR(82),
    linked_type VARCHAR(64)
);
CREATE INDEX idx_service_parent ON hydra_service_node_tree(parent_guid);
CREATE TABLE hydra_service_node_cache_path (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    guid VARCHAR(82) NOT NULL,
    path VARCHAR(512) UNIQUE
);
CREATE TABLE hydra_service_namespace_node (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    guid VARCHAR(82) UNIQUE NOT NULL,
    name VARCHAR(255),
    description VARCHAR(1024),
    extra_information VARCHAR(1024),
    create_time TIMESTAMP,
    update_time TIMESTAMP
);
CREATE TABLE hydra_service_application_node (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    guid VARCHAR(82) UNIQUE NOT NULL,
    name VARCHAR(255),
    path VARCHAR(512),
    type VARCHAR(128),
    alias VARCHAR(255),
    resource_type VARCHAR(64),
    deployment_method VARCHAR(64),
    description VARCHAR(1024),
    extra_information VARCHAR(1024),
    create_time TIMESTAMP,
    update_time TIMESTAMP
);

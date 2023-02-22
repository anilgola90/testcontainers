package org.anil.testcontainers.repository;

import org.anil.testcontainers.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEntityRepository  extends JpaRepository<OrderEntity,Long> {
}
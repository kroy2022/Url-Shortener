package com.whoopmock.urlshortener;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UrlRepository extends JpaRepository<UrlMapping, UUID> {

    // Spring Boot writes the SQL for this just by reading the method name!
    // Equivalent to: SELECT * FROM urls WHERE alias = ?
    Optional<UrlMapping> findByAlias(String alias);
}
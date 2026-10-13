package com.whoopmock.urlshortener;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UrlRepository extends JpaRepository<UrlMapping, UUID> {

    // SELECT * FROM urls WHERE alias = ?
    Optional<UrlMapping> findByAlias(String alias);

    // DELETE FROM urls WHERE alias = ?
    void deleteByAlias(String alias);
}
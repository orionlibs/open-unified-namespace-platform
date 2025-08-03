package io.github.orionlibs.core.user.model;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAORepository extends JpaRepository<UserModel, UUID>
{
    @Query(value = "SELECT 1", nativeQuery = true)
    Integer testConnection();
}

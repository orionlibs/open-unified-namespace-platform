package io.github.orionlibs.core.user.setting.model;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSettingsDAORepository extends JpaRepository<UserSettingsModel, UUID>
{
    List<UserSettingsModel> findAllByUserId(UUID userID);
}

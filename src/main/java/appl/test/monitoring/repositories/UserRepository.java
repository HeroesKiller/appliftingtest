package appl.test.monitoring.repositories;

import appl.test.monitoring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByAccessToken(String accessToken);
}

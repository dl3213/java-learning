package code.sibyl.reactive.dao;

import code.sibyl.reactive.domain.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dyingleaf3213
 * @Classname UserDap
 * @Description TODO
 * @Create 2023/04/02 00:57
 */
@Repository
public interface UserDao extends R2dbcRepository<User, String> {
}

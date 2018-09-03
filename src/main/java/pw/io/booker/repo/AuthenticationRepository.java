package pw.io.booker.repo;

import org.springframework.data.repository.CrudRepository;

import pw.io.booker.model.Authentication;

public interface AuthenticationRepository extends CrudRepository<Authentication, Integer> {
	Object findByToken(String token);
}

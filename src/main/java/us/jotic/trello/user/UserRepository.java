package us.jotic.trello.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

	public User findByUsername(String username);
}

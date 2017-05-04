package services.microservices;

import java.util.List;

import exceptions.UserAlreadyExistsException;
import exceptions.UsernameOrPasswordInvalidException;
import model.users.User;

public class UserService extends GenericService<User>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2769328934018732341L;

	
	
	
	
	public void createNewUser (User newUser) throws UserAlreadyExistsException {
		this.validateNewUser(newUser);
		this.save(newUser);
	}
	
	public void loginUser (User user) throws UsernameOrPasswordInvalidException {
		User possible = this.validateExist(user);
		possible.login();
		this.save(possible);
	}
	
	public void logout (User user) throws UsernameOrPasswordInvalidException {
		User possible = this.validateExist(user);
		possible.logout();
		this.save(possible);
	}
	
	public User authenticateUser (User user) throws UsernameOrPasswordInvalidException {
		return this.validateExist(user);
	}
	
	public boolean hasWritePermission(User user) throws UsernameOrPasswordInvalidException {
		User current = this.authenticateUser(user);
		return current.hasWritePermission();
		
	}
	
	
	
	
	public User findByUsername (String username) {
		User exampleUser = new User();
		exampleUser.setUsername(username);
		List<User> possible = this.getRepository().findByExample(exampleUser);
		return (possible.size() > 0) ? possible.get(0) : null;
	}
	
	private User validateExist(User possible) throws UsernameOrPasswordInvalidException {
		User exist = this.findByUsername(possible.getUsername());
		if (! exist.equals(possible)) throw new UsernameOrPasswordInvalidException();
		return exist;
	}
	
	private void validateNewUser (User possibleNewUser) throws UserAlreadyExistsException {
		User exist = this.findByUsername(possibleNewUser.getUsername());
		if (exist != null) throw new UserAlreadyExistsException();
	}
	
}

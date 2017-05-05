package services.microservices;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import model.users.User;
import services.microservices.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/META-INF/spring-persistence-context.xml", "/META-INF/spring-services-context.xml" })
public class UserServiceTest {

	@Autowired
	@Qualifier("services.microservices.userservice")
	private UserService userService;

	@Test
	public void testUsersCanBeSavedAndDeleted(){
		Integer expected = userService.retriveAll().size();
		User anUser = new User();
		userService.save(anUser);
		Assert.assertEquals(expected+1, userService.retriveAll().size());
	}

}
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import junit.framework.TestCase;

import org.tempuri.SerializeUtil;
import org.tempuri.UserUsers;

public class SerializeTest extends TestCase {

	public void testSerializeObject() throws Exception {
		UserUsers user = new UserUsers();
		user.setUserName("小比尔");
		user.setActive("1");
		user.setLoginName("admin");
		user.setPassword("111111");
		user.setEmail("longqibing@gmail.com");
		SerializeUtil.serializeObject(user);

		UserUsers deSeriUser = (UserUsers) SerializeUtil
				.deserializeObject(user);
		System.out.println(deSeriUser.getUserName());

		user.getVector().add("one");
		user.getVector().add("two");
		user.getVector().add("three");

		SerializeUtil.serializeObject(user.getVector());
		System.out.println(SerializeUtil.deserializeObject(user.getVector()));
	}
}

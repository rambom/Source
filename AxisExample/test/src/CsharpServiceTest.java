import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.tempuri.CsharpServiceStub;
import org.tempuri.CsharpServiceStub.ArrayOfUserInfo;
import org.tempuri.CsharpServiceStub.QueryUserService;
import org.tempuri.CsharpServiceStub.QueryUserServiceResponse;
import org.tempuri.CsharpServiceStub.QueryUserServiceResult_type0;
import org.tempuri.CsharpServiceStub.UserInfo;
import org.tempuri.CsharpServiceStub.VerifyUserLogin;
import org.tempuri.CsharpServiceStub.VerifyUserLoginResponse;

public class CsharpServiceTest extends TestCase {
	public void testCsharService() throws Exception {
		this.testQueryUserService();
	}

	void testVerifyUserLoginService() {
		try {
			CsharpServiceStub stub = new CsharpServiceStub();

			UserInfo userInfo = new UserInfo();
			userInfo.setLoginName("admin");
			userInfo.setLoginPwd("admin");
			userInfo.setLoginTime(new GregorianCalendar());

			VerifyUserLogin request = new VerifyUserLogin();
			request.setInfo(userInfo);

			VerifyUserLoginResponse response = stub.verifyUserLogin(request);

			ArrayOfUserInfo result = response.getVerifyUserLoginResult();

			PrintObjectInfo.PrintListObject(result.getUserInfo());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	void testQueryUserService() {
		try {
			CsharpServiceStub stub = new CsharpServiceStub();
			QueryUserService request = new QueryUserService();
			QueryUserServiceResponse response = stub.queryUserService(request);

			QueryUserServiceResult_type0 result = response
					.getQueryUserServiceResult();

			System.out.println(result.getExtraElement().toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}

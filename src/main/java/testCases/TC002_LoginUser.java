package testCases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pages.HomePage;
import wrappers.CapstonWrappers;

public class TC002_LoginUser extends CapstonWrappers {
	
	@BeforeClass
	public void setValues(){
		browserName = "chrome";
		dataSheetName = "TC002";
		testCaseName = "LoginUser";
		testDescription = "Verify the user should be able to successfully login";
		category = "smoke";
		authors = "Kumar Pancham Prasar";		
	}
	
	@Test(dataProvider = "fetchData")
	public void loginUser(String header, String email, String password, String name, String accountDeletedHeader){
		new HomePage(driver, test)
		.clickSignUpOrLogin()
		.verifyLoginHeader(header)
		.enterEmail(email)
		.enterPassword(password)
		.clickLogin()
		.clickDeleteAccount()
		.verifyHeader(accountDeletedHeader);
	}
}











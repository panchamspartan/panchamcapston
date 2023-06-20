package testCases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pages.HomePage;
import wrappers.CapstonWrappers;

public class TC003_LoginUserWithIncorrectDetails extends CapstonWrappers {
	
	@BeforeClass
	public void setValues(){
		browserName = "chrome";
		dataSheetName = "TC003";
		testCaseName = "LoginUserWithIncorrectDetails";
		testDescription = "Verify the invalid login message for the user";
		category = "smoke";
		authors = "Kumar Pancham Prasar";		
	}
	
	@Test(dataProvider = "fetchData")
	public void loginUser(String header, String email, String password, String errorMsg){
		new HomePage(driver, test)
		.clickSignUpOrLogin()
		.verifyLoginHeader(header)
		.enterEmail(email)
		.enterPassword(password)
		.clickLoginForFailure()
		.verifyErrMsg(errorMsg);
	}
}











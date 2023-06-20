package testCases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pages.HomePage;
import wrappers.CapstonWrappers;

public class TC001_RegisterUser extends CapstonWrappers {
	
	@BeforeClass
	public void setValues(){
		browserName = "chrome";
		dataSheetName = "TC001";
		testCaseName = "RegisterUser";
		testDescription = "Verify the registration process of the user";
		category = "smoke";
		authors = "Kumar Pancham Prasar";		
	}
	
	@Test(dataProvider = "fetchData")
	public void registerUser(String header, String name, String email, String signupHeader, String sName, String password, String date, 
			String fName, String lName, String company, String address1, String address2, String country, String state, String city, String zipcode, String mobNum,
			String accountCreatedheader){
		new HomePage(driver, test)
		.clickSignUpOrLogin()
		.verifySignUpHeader(header)
		.enterSignupName(name)
		.enterSignUpEmail(email)
		.clickSignUpButton()
		.verifyHeader(signupHeader)
		.clickTitle()
		.enterName(sName)
		.enterPassword(password)
		.selectDOB(date)
		.clickNewsletter()
		.clickOptin()
		.enterFirstName(fName)
		.enterLastName(lName)
		.enterCompany(company)
		.enterAddress1(address1)
		.enterAddress2(address2)
		.selectCountry(country)
		.enterState(state)
		.enterCity(city)
		.enterZipCode(zipcode)
		.enterMobileNumber(mobNum)
		.clickCreate()
		.verifyHeader(accountCreatedheader)
		.clickContinue()
		.verifyLoggedInUser(name);
	}
}











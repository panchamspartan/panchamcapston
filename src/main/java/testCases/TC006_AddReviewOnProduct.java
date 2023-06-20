package testCases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.HomePage;
import wrappers.CapstonWrappers;

public class TC006_AddReviewOnProduct extends CapstonWrappers {
	
	@BeforeClass
	public void setValues(){
		browserName = "chrome";
		dataSheetName = "TC006";
		testCaseName = "AddReviewOnProduct";
		testDescription = "To add a review on the product";
		category = "smoke";
		authors = "Kumar Pancham Prasar";		
	}
	
	@Test(dataProvider = "fetchData")
	public void reviewProducts(String header, String name, String email, String review, String msg  ){
		new HomePage(driver, test)
		.clickProduct()
		.verifyHeader(header)
		.clickViewProduct()
		.enterName(name)
		.enterEmail(email)
		.enterReview(review)
		.clickSubmit()
		.verifySuccessMsg(msg);
	}
}











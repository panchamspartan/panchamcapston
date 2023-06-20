package testCases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.HomePage;
import wrappers.CapstonWrappers;

public class TC005_SearchProducts extends CapstonWrappers {
	
	@BeforeClass
	public void setValues(){
		browserName = "chrome";
		dataSheetName = "TC005";
		testCaseName = "SearchProducts";
		testDescription = "To search the product and verify the same in the cart";
		category = "smoke";
		authors = "Kumar Pancham Prasar";		
	}
	
	@Test(dataProvider = "fetchData")
	public void searchProducts(String search, String header, String email, String password ){
		new HomePage(driver, test)
		.clickProduct()
		.enterSearch(search)
		.clickSearch()
		.verifyHeader(header)
		.verifyResult()
		.addAllProduct()
		.clickCart()
		.getProductNames()
		.verifyProduct()
		.clickSignUpOrLogin()
		.enterEmail(email)
		.enterPassword(password)
		.clickLogin()
		.clickCart()
		.getProductNames()
		.verifyProduct();
	}
}











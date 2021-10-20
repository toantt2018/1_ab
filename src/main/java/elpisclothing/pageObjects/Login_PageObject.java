package elpisclothing.pageObjects;

import org.junit.Assert;

import common.abc.CommonWeb;
import elpisclothing.pageUIs.Login_PageUI;

public class Login_PageObject extends CommonWeb {
	public void verifyOpenLoginPage(String header) {
		verifyContainTextValueElement(Login_PageUI.label_HEADER, header);
	}

	public void inputToEmailTxt(String emailSenkey) {
		sendkeyToElement(Login_PageUI.txt_EMAIL, emailSenkey);
	}

	public void inputToPassTxt(String passSenkey) {
		sendkeyToElement(Login_PageUI.txt_PASSWORD, passSenkey);

	}

	public void clickToDanNhapBtn() {

		clickToElement(Login_PageUI.btn_LOGIN);

	}

	public void verifyMessagerErrorDisplayed(String message) {
		String mailMessageError = getHtml5ValidationMessage(Login_PageUI.txt_EMAIL);
		Assert.assertEquals(message, mailMessageError);
		System.out.println(mailMessageError);
	}
}

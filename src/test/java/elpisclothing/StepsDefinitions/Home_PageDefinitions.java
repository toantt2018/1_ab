package elpisclothing.StepsDefinitions;

import cucumber.api.java.en.*;
import elpisclothing.pageObjects.Home_PageObject;
import io.cucumber.java.Before;
import net.thucydides.core.annotations.Steps;

@SuppressWarnings({ "deprecation" })
public class Home_PageDefinitions {
	@Steps
	Home_PageObject home;

	////
	@Given("^HomePage: mở trang$")
	public void m_trang_httpselpisclothingvn() {
		home.openPage();
	}

	@When("^HomePage: đóng chatbox nếu hiển thị$")
	public void homepage_ng_chatbox_nu_hin_th() throws InterruptedException {
		home.closeChatBox();
	}

	@Before ("@login")
	public void set_the() {
		System.out.println("toi la toi");
	}
}


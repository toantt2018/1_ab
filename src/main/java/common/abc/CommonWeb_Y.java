package common.abc;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import io.cucumber.datatable.DataTable;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.thucydides.core.webdriver.WebDriverFacade;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.ScriptException;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
/**
 * @author Lanhtt10
 */
public class CommonWeb_Y extends PageObject {
	static WebElement element;
	static WebDriverWait waitExplicit;
	static JavascriptExecutor javascriptExecutor;
	static Actions action;
	static String osName = System.getProperty("os.name");
	static String workingDr = System.getProperty("user.dir");
	static String platWin = "windows", platMac = "mac";

	static String timeOutShort = "time.short.timeout", timeOutLong = "time.long.timeout";

	public WebDriver getWebDriver() {
		return (WebDriver) ((WebDriverFacade) getDriver()).getProxiedDriver();
	}

	/*
	 * ========================================================================== *.FEATURE ==========================================================================
	 */
	// get gi?? tr??? t???ng colum Table trong file *.feature
	/**
	 * @param table      : t??n b???ng Example
	 * @param columnName : t??n c???t c???a b???ng Examples
	 */
	public String getValueColumnDataTableBDD(DataTable table, String columnName) {
		String valueColumn = null;
		String valueColumnRe = null;
		List<Map<String, String>> asMaps = table.asMaps(String.class, String.class);
		for (Map<String, String> featureMap : asMaps) {
			valueColumn = featureMap.get(columnName);
			valueColumnRe = valueColumn.replaceAll("\"", "");
		}
		return valueColumnRe;
	}

	/*
	 * ========================================================================== SESSION ==========================================================================
	 */
	// tr??? v??? source c???a page
	public String getSourceCodePage() {
		return getDriver().getPageSource();
	}

	/* ==== OCR ==== */
	// get OCR v???i ???nh ch???p to??n m??n h??nh
	/**
	 * @param language : t??n ng??n ng??? c???n OCR t??? file ???nh l???y t??? danh s??ch file src/test/resources/OCR/tessdata (VD: "eng", "vie",....)
	 */
	public String getScreenshotOCR(String language) {
		// String language = l???y trong list danh s??ch file tessdata (VD:"eng" ho???c "vie"
		// ......);

		String pathImg = getPathCaptureScreenshot();

		ITesseract instance = new Tesseract();
		if (osName.toLowerCase().contains(platWin)) {
			instance.setDatapath(String.format(workingDr + "\\%s", "src\\test\\resources\\OCR\\tessdata"));
		} else if (osName.toLowerCase().contains(platMac)) {
			instance.setDatapath(String.format(workingDr + "/%s", "src/test/resources/OCR/tessdata"));
		} else {
			System.out.println("not found OS get pathFile downloaded");
		}

		instance.setLanguage(language);
		String text = null;
		try {
			return text = instance.doOCR(new File(pathImg));

		} catch (TesseractException e) {
			e.printStackTrace();
		}

		return text;
	}

	// get OCR v???i ???nh ch???p element
	/**
	 * @param language : t??n ng??n ng??? c???n OCR t??? file ???nh l???y t??? danh s??ch file src/test/resources/OCR/tessdata (VD: "eng", "vie",....)
	 * @param xpath    : xpath element c???n OCR
	 * @param values   : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public String getElementOCR(String language, String xpath, String... values) {
		// String language = l???y trong list danh s??ch file tessdata (VD:"eng" ho???c "vie"
		// ......);

		String pathImg = getPathCaptureElementshot(xpath, values);

		ITesseract instance = new Tesseract();
		if (osName.toLowerCase().contains(platWin)) {
			instance.setDatapath(String.format(workingDr + "\\%s", "src\\test\\resources\\OCR\\tessdata"));
		} else if (osName.toLowerCase().contains(platMac)) {
			instance.setDatapath(String.format(workingDr + "/%s", "src/test/resources/OCR/tessdata"));
		} else {
			System.out.println("not found OS get pathFile downloaded");
		}

		instance.setLanguage(language);
		String text = null;
		try {
			return text = instance.doOCR(new File(pathImg));

		} catch (TesseractException e) {
			e.printStackTrace();
		}

		return text;
	}

	// ch???p ???nh to??n m??n h??nh
	public String getPathCaptureScreenshot() {
		deleteAllFileInFolderScreenshort();
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
			File source = ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.FILE);
			String path = null;

			if (osName.toLowerCase().contains(platWin)) {
				path = String.format(workingDr + "\\%s", "src\\test\\resources\\OCR\\screenshot\\img_" + formater.format(calendar.getTime()) + ".png");
			} else if (osName.toLowerCase().contains(platMac)) {
				path = String.format(workingDr + "/%s", "src/test/resources/OCR/screenshot/img_" + formater.format(calendar.getTime()) + ".png");
			} else {
				System.out.println("not found OS get pathFile downloaded");
			}

			FileUtils.copyFile(source, new File(path));
			return path;
		} catch (IOException e) {
			System.out.println("Exception while taking screenshot: " + e.getMessage());
			return e.getMessage();
		}

	}

	// ch???p ???nh ?????i v???i element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public String getPathCaptureElementshot(String xpath, String... values) {
		deleteAllFileInFolderScreenshort();
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

			File source = getElement(xpath, values).getScreenshotAs(OutputType.FILE);

			String path = null;

			if (osName.toLowerCase().contains(platWin)) {
				path = String.format(workingDr + "\\%s", "src\\test\\resources\\OCR\\screenshot\\img_" + formater.format(calendar.getTime()) + ".png");
			} else if (osName.toLowerCase().contains(platMac)) {
				path = String.format(workingDr + "/%s", "src/test/resources/OCR/screenshot/img_" + formater.format(calendar.getTime()) + ".png");
			} else {
				System.out.println("not found OS get pathFile downloaded");
			}

			FileUtils.copyFile(source, new File(path));
			return path;
		} catch (IOException e) {
			System.out.println("Exception while taking screenshot: " + e.getMessage());
			return e.getMessage();
		}

	}

	public void deleteAllFileInFolderScreenshort() {
		String path = null;
		try {
			if (osName.toLowerCase().contains(platWin)) {
				path = String.format(workingDr + "\\%s", "src\\test\\resources\\OCR\\screenshot");
			} else if (osName.toLowerCase().contains(platMac)) {
				path = String.format(workingDr + "/%s", "src/test/resources/OCR/screenshot");
			} else {
				System.out.println("not found OS get pathFile downloaded");
			}

			File file = new File(path);
			File[] listOfFiles = file.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					new File(listOfFiles[i].toString()).delete();
				}
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}

	/*
	 * ========================================================================== ELEMENT ==========================================================================
	 */
	/* ===== element ===== */
	// tr??? v??? element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public WebElementFacade getElement(String xpath, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible();
	}

	/* ===== List element ===== */
	// tr??? v??? list element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public List<WebElementFacade> getListElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return findAll(xpath);
	}

	// tr??? v??? s??? l?????ng element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public int countNumberElement(String xpath, String... values) {
		return getListElement(xpath, values).size();
	}

	/* ===== Atrributes ===== */
	// tr??? v??? text element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public String getText(String xpath, String... values) {
		highlightElementJS(xpath, values);

		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getText();
	}

	// tr??? v??? tag name
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public String getTagName(String xpath, String... values) {
		highlightElementJS(xpath, values);

		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getTagName();
	}

	// tr??? v??? gi?? tr??? attribute element
	/**
	 * @param xpath         : xpath element c???n l???y
	 * @param attributeName : t??n attribute c???a element (VD: id, name, class, style...)
	 * @param values        : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public String getAttributeValue(String xpath, String attributeName, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getAttribute(attributeName);
	}

	// tr??? v??? to??? ?????(x,y) element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public Point getLocationElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getLocation();
	}

	// tr??? v??? to??? ????? X element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public int getLocation_X_Element(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getLocation().x;
	}

	// tr??? v??? to??? ????? Y element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public int getLocation_Y_Element(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getLocation().y;
	}

	// tr??? v??? size(width, height) element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public Dimension getSizeElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getSize();
	}

	
	// tr??? v??? width element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public int getWidthElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getSize().getWidth();
	}

	// tr??? v??? height element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public int getHeightElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getSize().getHeight();
	}

	// verify element ch???a text
	/**
	 * @param xpath            : xpath element c???n l???y
	 * @param containTextValue : gi?? tr??? text c???a element mong mu???n
	 * @param values           : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void verifyContainTextValueElement(String xpath, String containTextValue, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).waitUntilVisible().shouldContainText(containTextValue);
	}

	/* ==== ki???m tra tr???ng th??i element ==== */
	// is Enable
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public boolean isControlEnable(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).isEnabled();
	}

	// is Clickable
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public boolean isControlClickable(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).isClickable();
	}

	// is Disabled
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public boolean isControlDisabled(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).isDisabled();
	}

	// is Visible
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public boolean isControlVisible(String xpath, String... values) {
		// element c?? trong Dom + c?? tr??n UI
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).isVisible();
	}

	// is Present
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public boolean isControlPresent(String xpath, String... values) {
		// element c??/ko c?? tr??n UI + c?? trong Dom
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).isPresent();
	}

	/* ==== verify tr???ng th??i element ==== */
	// verify enabled element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void verifyEnabledElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).shouldBeEnabled();
	}

	// verify visible element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void verifyVisibleElement(String xpath, String... values) {
		// element c?? trong Dom + c?? tr??n UI
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).shouldBeVisible();
	}

	// verify invisible element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void verifyInvisibleElement(String xpath, String... values) {
		// element c??/ko c?? trong Dom + ko c?? tr??n UI
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).shouldNotBeVisible();
	}

	// verify present element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void verifyPresentElement(String xpath, String... values) {
		// element c??/ko c?? tr??n UI + c?? trong Dom
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).shouldBePresent();
	}

	// click element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void clickToElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).waitUntilVisible().click();
	}

	/* ===== checkbox/radio button ===== */
	// is Selected
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public boolean isControlSelected(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).isSelected();
	}

	/* ===== toast message ===== */
	// verify element ch???a text ?????i v???i toast mesage
	/**
	 * @param xpath            : xpath element c???n l???y
	 * @param containTextValue : gi?? tr??? text c???a element mong mu???n
	 * @param values           : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void verifyContainTextValueToastMessage(String xpath, String containTextValue, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).shouldContainText(containTextValue);
	}

	/* ===== Textbox/textarea ===== */
	// clear
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void clearToElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).waitUntilVisible().clear();
	}

	// sendkey
	/**
	 * @param xpath           : xpath element c???n l???y
	 * @param valueToSendkey: gi?? tr??? mu???n senkey v??o element
	 * @param values          : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void sendkeyToElement(String xpath, String valueToSendkey, String... values) {
		// Tr?????ng h???p senkey nhi???u d??ng th?? xu???ng d??ng textare l?? d???u \n (VD: C???ng ho??
		// x?? h???i ch??? ngh??a VN\n?????c l???p - t??? do - h???nh ph??c")
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).waitUntilVisible().sendKeys(valueToSendkey);
	}

	// sendkey and Enter
	/**
	 * @param xpath           : xpath element c???n l???y
	 * @param valueToSendkey: gi?? tr??? mu???n senkey v??o element
	 * @param values          : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void sendkeyAndEnterToElement(String xpath, String valueToSendkey, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).waitUntilVisible().typeAndEnter(valueToSendkey);
	}

	// clear sau ???? sendKey
	/**
	 * @param xpath          : xpath element c???n l???y
	 * @param valueToSendkey : gi?? tr??? mu???n senkey v??o element
	 * @param values         : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public void clearBeforeSendKeyToElement(String xpath, String valueToSendkey, String... values) throws InterruptedException, NumberFormatException, IOException {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);

		element(xpath).waitUntilVisible().clear();
		Thread.sleep(Integer.valueOf(getProperties(timeOutShort)));
		element(xpath).waitUntilVisible().sendKeys(valueToSendkey);
	}

	// sendkey ?????i v???i textbox ch??? l?? s??? (type="number")
	/**
	 * @param xpath          : xpath element c???n l???y
	 * @param numberExpected : s??? mu???n ch???n
	 * @param values         : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void sendkeyNumberToElement(String xpath, int numberExpected, String... values) {
		highlightElementJS(xpath, values);

		String valueNumberTxt = getAttributeValue(xpath, "value", values);
		int countCurrent = Integer.parseInt(valueNumberTxt);

		int downOrUp;
		int diff = numberExpected - countCurrent;

		if (diff > 0) {
			downOrUp = 1;
		} else if (diff < 0) {
			diff = -1 * diff;
			downOrUp = -1;
		} else {
			downOrUp = 0;
		}

		for (int i = 0; i < diff; i++) {
			if (downOrUp == 1) {
				sendKeyBoardToElement(xpath, Keys.ARROW_UP, values);
			} else if (downOrUp == -1) {
				sendKeyBoardToElement(xpath, Keys.ARROW_DOWN, values);

			} else {
				break;
			}
		}
	}

	/* ===== Dropdown ===== */
	/* == Default Dropdownlist == */
	// tr??? v??? danh s??ch c??c gi?? tr??? trong Defaul dropdownlist
	/**
	 * @param xpath  : xpath element c???n l???y (element c?? tagname l?? <select> )
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public List<String> getListSelectOptionInDefaultDropdown(String xpath, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		highlightElementJS(xpath);
		return element(xpath).getSelectOptions();
	}

	// tr??? v??? s??? l?????ng c??c gi?? tr??? trong Default Dropdownlist
	/**
	 * @param xpath  : xpath element c???n l???y (element c?? tagname l?? <select> )
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public int countSelectOptionInDefaultDropdown(String xpath, String... values) {
		return getListSelectOptionInDefaultDropdown(xpath, values).size();
	}

	/* Single Default Dropdowlist */
	// Ch???n 1 gi?? tr??? trong Default Single Dropdownlist
	/**
	 * @param xpath           : xpath element c???n l???y (element c?? tagname l?? <select> )
	 * @param valueItemChoose : gi?? tr??? c?? trong dropdown mu???n ch???n
	 * @param values          : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void selectItemInSingleDefaultDropdown(String xpath, String valueItemChoose, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		highlightElementJS(xpath);
		element(xpath).deselectByVisibleText(valueItemChoose);
	}

	// tr??? v??? gi?? ???? ch???n trong Single Default Dropdowlist
	/**
	 * @param xpath  : xpath element c???n l???y (element c?? tagname l?? <select> )
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public String getSelectedItemInSingleDefaultDropDown(String xpath, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		highlightElementJS(xpath);
		return element(xpath).getSelectedVisibleTextValue();
	}

	/* Multiple Default Dropdowlist */
	// ch???n nhi???u gi?? tr??? trong Multiple Defautl Dropdownlist
	/**
	 * @param xpath             : xpath element c???n l???y (element c?? tagname l?? <select> )
	 * @param expectedValueItem : Array c??c gi?? tr??? c?? trong dropdown mu???n ch???n
	 * @param values            : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void selectItemInMultipleDefaultDropdown(String xpath, String[] expectedValueItem, String... values) {
		// String expectValue[] = { "Unit","Desktop"};
		xpath = String.format(xpath, (Object[]) values);
		highlightElementJS(xpath);

		List<String> valueOption = element(xpath).waitUntilVisible().getSelectOptions();

		Select select = new Select(element(xpath));
		for (String b : valueOption) {
			for (String a : expectedValueItem) {
				if (b.equals(a)) {
					select.selectByVisibleText(a);
				}
			}
		}
	}

	// tr??? v??? gi?? tr??? v???a ch???n trong Multiple Default Dropdownlist
	/**
	 * @param xpath             : xpath element c???n l???y (element c?? tagname l?? <select> )
	 * @param expectedValueItem : Array c??c gi?? tr??? c?? trong dropdown mu???n ch???n
	 * @param values            : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public List<String> getSelectedItemInMultipleDefaultDropDown(String xpath, String[] expectedValueItem, String... values) {
		// String expectValue[] = { "Unit","Desktop"};
		xpath = String.format(xpath, (Object[]) values);
		highlightElementJS(xpath);

		List<String> actualValueItem = new ArrayList<String>();
		Select select = new Select(element(xpath));
		List<WebElement> itemSelected = select.getAllSelectedOptions();
		for (WebElement a : itemSelected) {
			actualValueItem.add(a.getText());
		}
		return actualValueItem;
	}

	/* ==Custom Dropdowlist == */
	// Ch???n 1 gi?? tr??? trong Single Custom dropdownlist
	/**
	 * @param xpathParent       : xpath cha dropdown, click v??o x??? ra c??c option
	 * @param xpathAllItem      : xpath con ch???a c??c gi?? tr??? option
	 * @param expectedValueItem : gi?? tr??? trong dropdown mu???n ch???n
	 */
	public void selectItemInSingleCustomDropdown(String xpathParent, String xpathAllItem, String expectedValueItem) throws InterruptedException {
		// 1. Click v??o dropDown v?? cho n?? x??? h???t c??c gi?? tr??? ra
		element(xpathParent).waitUntilVisible().click();

		// 2. Ch??? v?? hi???n th??? cho t???t c??? c??c gi?? tr??? dropdown ???????c load ra
		element(xpathAllItem).waitUntilPresent();
		List<WebElement> allIteams = getDriver().findElements(By.xpath(xpathAllItem));

		for (WebElement childElement : allIteams) {
			if (childElement.getText().equals(expectedValueItem)) {
				// 3. Scoll ?????n gi?? tr??? mu???n ch???n
				scrollToElementJS1(childElement);
				Thread.sleep(2000);

				// 4. Click v??o item c???n ch???n
				childElement.click();
				Thread.sleep(2000);
				break;
			}
		}
	}

	// Ch???n nhi???u gi?? tr??? trong Multiple Custom dropdownlist
	/**
	 * @param xpathParent       : xpath cha dropdown, click v??o x??? ra c??c option
	 * @param xpathAllItem      : xpath con ch???a c??c gi?? tr??? option
	 * @param expectedValueItem : Array c??c gi?? tr??? trong dropdown mu???n ch???n
	 */
	public void selectItemInMultipleCustomDropdown(String xpathParent, String xpathAllItem, String[] expectedValueItem) throws InterruptedException {
		// String expectValue[] = { "Unit","Desktop"};

		// 1. Click v??o dropDown v?? cho n?? x??? h???t c??c gi?? tr??? ra
		element(xpathParent).waitUntilVisible().click();

		// 2. Ch??? v?? hi???n th??? cho t???t c??? c??c gi?? tr??? dropdown ???????c load ra
		element(xpathAllItem).waitUntilPresent();
		List<WebElement> allIteams = getDriver().findElements(By.xpath(xpathAllItem));

		// 3. Ch???n nh???ng element theo String mong mu???n ch???n
		for (WebElement childElement : allIteams) {
			for (String item : expectedValueItem) {
				if (childElement.getText().equals(item)) {
					// 3. Scoll ?????n gi?? tr??? mu???n ch???n
					scrollToElementJS1(childElement);
					Thread.sleep(2000);
					// 4. Click v??o item c???n ch???n
					childElement.click();
				}
			}

		}
	}

	/* ==== Alert ==== */
	// click OK button c???a alert
	public void clickToAcceptAlertBtn() throws NumberFormatException, IOException {
		waitForAlertPresence();
		getAlert().accept();
	}

	// click Cancle button c???a alert
	public void clickToCancleAlertBtn() throws NumberFormatException, IOException {
		waitForAlertPresence();
		getAlert().dismiss();
	}

	// get message c???a aleart
	public String getTextInAleart() throws NumberFormatException, IOException {
		waitForAlertPresence();
		return getAlert().getText();
	}

	// senkey to aleart
	/**
	 * @param valueToSendkey : gi?? tr??? mu???n senkey v??o element
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public void sendKeysToAleart(String valueToSendkey) throws NumberFormatException, IOException {
		waitForAlertPresence();
		getAlert().sendKeys(valueToSendkey);
	}

	// is alert present
	public boolean isAlertPresent() {
		try {
			getDriver().switchTo().alert();
			return true;
		} catch (NoAlertPresentException Ex) {
			return false;
		}
	}

	// wait cho ?????n khai alert present
	public void waitForAlertPresence() throws NumberFormatException, IOException {
		waitExplicit = new WebDriverWait(getDriver(), Integer.valueOf(getProperties(timeOutLong)));
		waitExplicit.until(ExpectedConditions.alertIsPresent());
	}

	/* ===== Data Picker ===== */
	/**
	 * @param xpath         : xpath element c???n l???y
	 * @param dateToSendKey : gi?? tr??? format date mu???n senkey v??o element
	 * @param values        : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void sendkeyToDataPicker(String xpath, String dateToSendKey, String... values) {
		removeAttributeJS(xpath, "type", values);
		sendkeyToElement(xpath, dateToSendKey, values);
	}

	/* ===== Upload ===== */
	// - C1: Sendkey (D??ng trong t???t c??? h??? ??i???u h??nh,browser)
	/**
	 * @param fileName           : Array c??c file mu???n upload
	 * @param xpathAddFileButton : xpath Browse button
	 * @param values             : tham s??? dynamic trong String xpathAddFileButton (c?? th??? c?? ho???c kh??ng)
	 */
	public void uploadFileBySendkeysFile(String fileName[], String xpathAddFileButton, String... values) {
		// String fileName[] = { "add.pdf","edit.pdf"};
		for (String a : fileName) {
			String pathFile = null;
			if (osName.toLowerCase().contains(platWin)) {
				pathFile = "\\src\\test\\resources\\upload\\";
			} else if (osName.toLowerCase().contains(platMac)) {
				pathFile = "/src/test/resources/upload/";
			} else {
				System.out.println("not found OS get pathFile");
			}
			pathFile = workingDr + pathFile + a;
			sendkeyToElement(xpathAddFileButton, pathFile, values);
		}
	}

	
	// - C2: Java robot (D??ng trong t???t c??? h??? ??i???u h??nh,browser)
	/**
	 * @param fileName           : Array c??c file mu???n upload
	 * @param xpathAddFileButton : xpath Browse button
	 * @param values             : tham s??? dynamic trong String xpathAddFileButton (c?? th??? c?? ho???c kh??ng)
	 */
	public void uploadByJavaRobot(String[] fileName, String xpathBrowserButton, String... value) throws AWTException, InterruptedException, ScriptException, IOException {
		for (String a : fileName) {
			highlightElementJS(xpathBrowserButton, value);
			String pathFile = null;

			if (osName.toLowerCase().contains(platWin)) {
				pathFile = "\\src\\test\\resources\\upload\\";
			} else if (osName.toLowerCase().contains(platMac)) {
				pathFile = "/src/test/resources/upload/";
			} else {
				System.out.println("not found OS get pathFile upload");
			}

			pathFile = workingDr + pathFile + a;

			clickToElement(xpathBrowserButton);

			StringSelection stringSelection = new StringSelection(pathFile);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

			Robot robot = new Robot();
			Thread.sleep(5000);

			if (osName.toLowerCase().contains(platWin)) {
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyRelease(KeyEvent.VK_V);
				Thread.sleep(5000);

			} else if (osName.toLowerCase().contains(platMac)) {
				Runtime runtime = Runtime.getRuntime();
				if (getDriver().toString().toLowerCase().contains("chrome")) {
					System.out.println("chrome");
					String[] args = { "osascript", "-e", "tell app \"Chrome\" to activate" };
					runtime.exec(args);
				} else if (getDriver().toString().toLowerCase().contains("firefox")) {
					System.out.println("firefox");
					String[] args = { "osascript", "-e", "tell app \"Firefox\" to activate" };
					runtime.exec(args);
				} else {
					System.out.println("not found browser in" + osName);
				}

				Thread.sleep(5000);
				// Open go to window
				robot.keyPress(KeyEvent.VK_META);
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_G);
				robot.keyRelease(KeyEvent.VK_META);
				robot.keyRelease(KeyEvent.VK_SHIFT);
				robot.keyRelease(KeyEvent.VK_G);
				Thread.sleep(5000);

				// Paste the clipboard value
				robot.keyPress(KeyEvent.VK_META);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_META);
				robot.keyRelease(KeyEvent.VK_V);
				Thread.sleep(5000);

				// Press Enter key to close the Goto window and Upload window
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				Thread.sleep(10000);

			} else {
				System.out.println("not found OS action robot");
			}

			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		}
	}

	/* ===== Download ===== */
	// download v???i file c?? t??n c??? ?????nh
	/**
	 * @param fullNameFileDownload : t??n ?????y ????? c???a file lu??n c??? ?????nh t???i v??? (VD: 'hoso.pdf')
	 * @param xpath                : xpath element c???n click v??o ????? download ???????c file
	 * @param values               : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void downloadFileFullName(String fullNameFileDownload, String xpath, String... values) throws Exception {
		// String fullNameFile = "abc.xlsx"
		// 1. xo?? to??n b??? file trong th?? muc
		deleteAllFileInFolder();

		// 2.click element ????? download
		scrollToElementJS(xpath, values);
		clickToElement(xpath, values);

		// 3. ?????i ?????n khi file ???????c t???i v???
		waitForDownloadFileFullnameCompleted(fullNameFileDownload);

		// 4.?????m s??? l?????ng file trong th?? m???c sau khi t???i v??? v?? verify
		int countFileBeforeDelete = countFilesInDirectory();
		Assert.assertEquals(countFileBeforeDelete, 1);

		// 5.xo?? file t???i v???
		deleteFileFullName(fullNameFileDownload);

		// 6.?????m s??? l?????ng file trong th?? m???c sau khi xo?? v?? verify
		int countFileAfterDelete = countFilesInDirectory();
		Assert.assertEquals(countFileAfterDelete, 0);
	}

	// dowload v???i file c?? t??n ch??? c?? 1 ph???n c??? ?????nh
	/**
	 * @param containNameFile : t??n 1 ph???n t??n file c??? ?????nh khi t???i v??? (VD: '.pdf')
	 * @param xpath           : xpath element c???n click v??o ????? download ???????c file
	 * @param values          : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void downloadFileContainName(String containNameFile, String xpath, String... values) throws Exception {
		// String containNameFile =".xlsx"
		// 1. xo?? to??n b??? file trong th?? muc
		deleteAllFileInFolder();

		// 2.click element ????? download
		scrollToElementJS(xpath, values);
		clickToElement(xpath, values);

		// 3. ?????i ?????n khi file ???????c t???i v???
		waitForDownloadFileContainsNameCompleted(containNameFile);

		// 4.?????m s??? l?????ng file trong th?? m???c sau khi t???i v??? v?? verify
		int countFileBeforeDelete = countFilesInDirectory();
		Assert.assertEquals(countFileBeforeDelete, 1);

		// 5.xo?? file t???i v???
		deleteFileContainName(containNameFile);

		// 6.?????m s??? l?????ng file trong th?? m???c sau khi xo?? v?? verify
		int countFileAfterDelete = countFilesInDirectory();
		Assert.assertEquals(countFileAfterDelete, 0);
	}

	// verify file ???????c t???i v???
	public void waitForDownloadFileFullnameCompleted(String fileName) throws Exception {
		int i = 0;
		while (i < 30) {
			boolean exist = isFileExists(fileName);
			if (exist == true) {
				i = 30;
			}
			Thread.sleep(500);
			i = i + 1;
		}
	}

	public void waitForDownloadFileContainsNameCompleted(String fileName) throws Exception {
		int i = 0;
		while (i < 30) {
			boolean exist = isFileContain(fileName);
			if (exist == true) {
				i = 30;
			}
			Thread.sleep(500);
			i = i + 1;
		}
	}

	// ?????m s??? file t???i v???
	public int countFilesInDirectory() {
		String pathFolderDownload = getPathContainDownload();
		File file = new File(pathFolderDownload);
		int i = 0;
		for (File listOfFiles : file.listFiles()) {
			if (listOfFiles.isFile()) {
				i++;
			}
		}
		return i;
	}

	// ???????ng d???n downloaded
	public String getPathContainDownload() {
		String path = null;
		if (osName.toLowerCase().contains(platWin)) {
			path = String.format(workingDr + "\\%s", "src\\test\\resources\\downloaded\\");

		} else if (osName.toLowerCase().contains(platMac)) {
			path = String.format(workingDr + "/%s", "src/test/resources/downloaded/");

		} else {
			System.out.println("not found OS get pathFile downloaded");
		}
		return path;
	}

	// xo?? file
	public void deleteFileFullName(String fileName) {
		if (isFileExists(fileName)) {
			deleteFullName(fileName);
		}
	}

	public void deleteFullName(String fileName) {
		try {
			if (isFileExists(fileName)) {
				String pathFolderDownload = getPathContainDownload();
				File files = new File(pathFolderDownload + fileName);
				files.delete();
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}

	public void deleteFileContainName(String fileName) {
		deleteContainName(fileName);
	}

	public void deleteContainName(String fileName) {
		try {
			String files;
			String pathFolderDownload = getPathContainDownload();
			File file = new File(pathFolderDownload);
			File[] listOfFiles = file.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					files = listOfFiles[i].getName();
					if (files.contains(fileName)) {
						new File(listOfFiles[i].toString()).delete();
					}
				}
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}

	public void deleteAllFileInFolder() {
		try {
			String pathFolderDownload = getPathContainDownload();
			File file = new File(pathFolderDownload);
			File[] listOfFiles = file.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					new File(listOfFiles[i].toString()).delete();
				}
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}

	// ki???m tra file c?? t???n t???i kh??ng
	public boolean isFileExists(String file) {
		try {
			String pathFolderDownload = getPathContainDownload();
			File files = new File(pathFolderDownload + file);
			boolean exists = files.exists();
			return exists;
		} catch (Exception e) {
			System.out.print(e.getMessage());
			return false;
		}

	}

	public boolean isFileContain(String fileName) {
		try {
			boolean flag = false;
			String pathFolderDownload = getPathContainDownload();
			File dir = new File(pathFolderDownload);
			File[] files = dir.listFiles();
			if (files == null || files.length == 0) {
				flag = false;
			}
			for (int i = 1; i < files.length; i++) {
				if (files[i].getName().contains(fileName)) {
					flag = true;
				}
			}
			return flag;
		} catch (Exception e) {
			System.out.print(e.getMessage());
			return false;
		}
	}

	/* ==== Table ==== */
	// tr??? v??? danh s??ch gi?? tr??? c???a 1 danh s??ch element (th?????ng d??ng khi l???y gi?? tr???
	// 1 colum table ch???c n??ng t??m ki???m)
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public List<String> getListText(String xpath, String... values) {
		List<String> lstValue = new ArrayList<String>();

		xpath = String.format(xpath, (Object[]) values);

		List<WebElementFacade> lstElement = findAll(xpath);
		int size = lstElement.size();
		if (size > 0) {
			highlightElementJS(xpath, values);
			for (WebElementFacade a : lstElement) {
				lstValue.add(a.waitUntilVisible().getText());
			}
			lstValue.removeAll(Collections.singleton(null));
			lstValue.removeAll(Collections.singleton(" "));
		}
		System.out.println("UI= " + lstValue);
		return lstValue;
	}

	/*
	 * ========================================================================== T????NG T??C: CHU???T V?? B??N PH??M ==========================================================================
	 */
	/* ==== Mouse ==== */
	// click
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void clickMouseToElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element = element(xpath);

		action = new Actions(getDriver());
		action.click(element).perform();
	}

	// Double Click
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void doubleClickMouseToEment(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element = element(xpath);

		action = new Actions(getDriver());
		action.doubleClick(element).perform();
	}

	// Hover
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void hoverMouseToElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element = element(xpath);

		action = new Actions(getDriver());
		action.moveToElement(element).perform();
	}

	// click chu???t ph???i
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void rightMouseClickToElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element = element(xpath);

		action = new Actions(getDriver());
		action.contextClick(element).perform();
	}

	// drapAndDrop (k??o v?? th??? )
	/**
	 * @param xpathSource : xpath ??i???m b???t ?????u k??o
	 * @param xpathTarget : xpath ??i???m k???t th??c th???
	 */
	public void dragAndDropMouse(String xpathSource, String xpathTarget) {
		WebElementFacade elementSource = element(xpathSource);
		WebElementFacade elementTarget = element(xpathTarget);

		highlightElement1(elementSource);
		highlightElement1(elementTarget);

		action = new Actions(getDriver());
		action.dragAndDrop(elementSource, elementTarget).perform();
	}

	// ClickAndHold (click v?? gi???: ch???n nhi???u item li??n ti???p)
	/**
	 * @param xpathAllItem : xpath ch???a to??n b??? element mu???n ch???n
	 * @param indexFrom    : index c???a element b???t ?????u mu???n ch???n trong list element c?? xpath l?? xpathAllItem
	 * @param indexTo      : index c???a element k???t th??c mu???n ch???n trong list element c?? xpath l?? xpathAllItem
	 * @param values       : tham s??? dynamic trong String xpathAllItem (c?? th??? c?? ho???c kh??ng)
	 */
	public void clickAndHoldMouseFromToElement(String xpathAllItem, int indexFrom, int indexTo, String... values) {
		// Ch???n nhi???u item trong c??ng 1 list (VD: ch???n t??? 1-> 4)
		xpathAllItem = String.format(xpathAllItem, (Object[]) values);
		List<WebElementFacade> lst = findAll(xpathAllItem);

		action = new Actions(getDriver());
		action.clickAndHold(lst.get(indexFrom)).moveToElement(lst.get(indexTo)).build().perform();
	}

	// ClickAndHold (click v?? gi???: ch???n nhi???u item kh??ng li??n ti???p)
	/**
	 * @param xpathAllItem : xpath ch???a to??n b??? element mu???n ch???n
	 * @param index        : Array c??c index c???a c??c element mu???n ch???n trong list element xpath l?? xpathAllItem
	 * @param values       : tham s??? dynamic trong String xpathAllItem (c?? th??? c?? ho???c kh??ng)
	 */
	public void clickAndHoldMouseElement(String xpathAllItem, int[] index, String... values) {
		// Ch???n nhi???u item trong c??ng 1 list (VD: ch???n t??? 1-> 3->5->7)
		xpathAllItem = String.format(xpathAllItem, (Object[]) values);
		List<WebElementFacade> lst = findAll(xpathAllItem);

		action = new Actions(getDriver());

		if (osName.toLowerCase().contains(platWin)) {
			action.keyDown(Keys.CONTROL).perform();
		} else if (osName.toLowerCase().contains(platMac)) {
			action.keyDown(Keys.META).perform();
		} else {
			System.out.println("not found platform click clickAndHoldMouseElement");
		}

		for (int x : index) {
			lst.get(x).click();
		}
	}

	/*
	 * ==== Keyboard: https://artoftesting.com/press-enter-tab-space-arrow-function-keys-in- selenium-webdriver-with-java====
	 */
	// Nh??? ph??m
	/**
	 * @param key : key enum's value (VD:key=Keys.CONTROL)
	 */
	public void keyPressUp(Keys key) {
		// key=Keys.CONTROL
		action = new Actions(getDriver());
		action.keyUp(key).perform();
	}

	// Nh???n ph??m
	/**
	 * @param key : key enum's value (VD:key=Keys.CONTROL)
	 */
	public void keyPressDown(Keys key) {
		// key=Keys.CONTROL
		action = new Actions(getDriver());
		action.keyDown(key).perform();
	}

	// Sendkey Board
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param key    : key enum's value (VD:key=Keys.CONTROL) mu???n senkey v??o element
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void sendKeyBoardToElement(String xpath, Keys key, String... values) {
		// key=Keys.CONTROL
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element = element(xpath);

		action = new Actions(getDriver());
		action.sendKeys(element, key).perform();
		// element(xpath).sendKeys(key);
	}

	/*
	 * ========================================================================== WEB ==========================================================================
	 */
	/* ==== Window/Tab ==== */
	// switch t???i Window/Tab theo ID (ch??? d??ng cho n=2 tab)
	public void switchToWindowByID() {
		String parentID = getDriver().getWindowHandle();

		Set<String> allWindows = getDriver().getWindowHandles();
		for (String runWindow : allWindows) {
			if (!runWindow.equals(parentID)) {// n???u ID n??o # id parent th?? switch qua
				getDriver().switchTo().window(runWindow);
				break;
			}
		}
	}

	// switch t???i Window/Tab by String (ch??? d??ng cho n>2 tab)
	/**
	 * @param titleExpect : title c???a Window/Tab con mu???n switch
	 */
	public void switchToWindowByString(String titleExpect) {
		Set<String> allWindows = getDriver().getWindowHandles();
		for (String runWindow : allWindows) {
			getDriver().switchTo().window(runWindow);
			String titleCurent = getDriver().getTitle();
			if (titleCurent.equals(titleExpect)) {
				break;
			}
		}
	}

	// ki???m tra ???? ????ng t???t c??? c??c tab tr??? parent, v?? tr??? v??? tab parent
	public void closeWindownWithoutParent() {
		String parentID = getDriver().getWindowHandle();

		Set<String> allWindows = getDriver().getWindowHandles();
		for (String runWindow : allWindows) {
			if (!runWindow.equals(parentID)) {
				getDriver().switchTo().window(runWindow);
				getDriver().close();
			}
		}

		getDriver().switchTo().window(parentID);
//		if (getDriver().getWindowHandles().size() == 1) {
//			return true;
//		} else {
//			return false;
//		}
	}

	// tr??? v??? title c???a page
	public String getTitlePage() {
		return getTitle();
	}

	// tr??? v??? size(width,height) c???a page
	public Dimension getWindowSize() {
		return getDriver().manage().window().getSize();
	}

	// set size(height,width) cho page
	/**
	 * @param width  : chi???u r???ng mu???n set
	 * @param height : chi???u cao mu???n set
	 */
	public void setWindowSize(int width, int height) {
		getDriver().manage().window().setSize(new Dimension(width, height));
	}

	// tr??? v??? to??? ????? (x,y) c???a page
	/**
	 * @return Point
	 */
	public Point getWindownPosition() {
		return getDriver().manage().window().getPosition();
	}

	// set to??? ????? (x,y) c???a page
	/**
	 * @param x : to??? ????? x mu???n set
	 * @param y : to??? ????? y mu???n set
	 */
	public void setWindownPosition(int x, int y) {
		getDriver().manage().window().setPosition(new Point(x, y));
	}

	// set to??? ????? (x,y) c???a page
	public void getMaximizeWindow() {
		getDriver().manage().window().maximize();
	}

	/*
	 * ==== Popup/Dialog ch??? xu???t hi???n l???n ?????u khi m??? app ho???c c?? th??? xu???t hi???n ho???c kh??ng xu???t hi???n ====
	 */
	// ????ng popup/dialog n???u xu???t hi???n
	/**
	 * @param milisecond     : th???i gian (mili gi??y) t???i ??a ch??? popup xu???t hi???n
	 * @param xpathIconClose : xpath c???a button icon Close
	 * @param values         : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void closePopupIfDisplayed(long milisecond, String xpathIconClose, String... values) {
		long startime = System.currentTimeMillis();
		long duration = 0;

		while (milisecond > duration) {
			List<WebElementFacade> Lst = getListElement(xpathIconClose, values);
			if (Lst.size() > 0) {
				clickToElement(xpathIconClose, values);
				break;
			}
			long endTime = System.currentTimeMillis();
			duration = endTime - startime;
		}
	}

	// ki???m tra popup c?? xu???t hi???n kh??ng ?
	/**
	 * @param xpath  : xpath c???a popup
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public boolean isPopupDisplayed(String xpath, String... values) {
		boolean a = false;
		List<WebElementFacade> lst = getListElement(xpath, values);
		if (lst.size() > 0) {
			a = true;
		}
		return a;
	}

	/* ==== Navigation ===== */
	// m??? URL ???????c c???u h??nh ??? file serenity.properties
	public void openPage() {
		open();
	}

	// tr??? v??? url c???a page ??ang m???
	public String getCurrentPageURL() {
		return getDriver().getCurrentUrl();
	}

	// back l???i page tr?????c ????
	public void backToPreviousPage() {
		getDriver().navigate().back();
	}

	// next page ???? ???? m???
	public void fowardToNextPage() {
		getDriver().navigate().forward();
	}

	// refresh l???i page ??ang m???
	public void refreshCurrentPage() {
		getDriver().navigate().refresh();
	}

	/* ==== Storage ==== */
	// tr??? v??? danh s??ch cookie: name - value
	public Set<Cookie> getAllCookies() {
		Set<Cookie> set = getDriver().manage().getCookies();
//		for (Cookie key : set) {
//			System.out.println(key);
//		}
		return set;
	}

	// set cookie
	/**
	 * @param cookieName  : t??n cookie c???n set
	 * @param cookievalue : gi?? tr??? c???n set cho String cookieName
	 */
	public void setCookie(String cookieName, String cookievalue) {
		getDriver().manage().addCookie(new Cookie(cookieName, cookievalue));
	}

	// delete cookie
	/**
	 * @param cookieName : t??n cookie mu???n xo??
	 */
	public void deleteCookie(String cookieName) {
		getDriver().manage().deleteCookieNamed(cookieName);
	}

	// delte all cookies
	public void deleteAllCookie() {
		getDriver().manage().deleteAllCookies();
	}

	/* ===== Iframe/Frame ===== */
	// switch to frame
	/**
	 * @param xpath  : xpath frame/iframe c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void switchToFrame(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element = element(xpath);
		getDriver().switchTo().frame(element);
	}

	// switch to top windows
	public void backToTopParentFrame() {
		getDriver().switchTo().defaultContent();
		// getDriver().switchTo().parentFrame();
	}


	/* ===== QR code ===== */
	public String decodeQR (String xpath, String values) throws NotFoundException, IOException {
		Result result = null;
		
		String qrCodeURL = getAttributeValue(xpath, "src", values);
		URL url = new URL(qrCodeURL);
		// Pass the URL class object to store the file as image
		BufferedImage qrCodeImage = ImageIO.read(url);

		// Process the image
		LuminanceSource luminanceSource = new BufferedImageLuminanceSource(qrCodeImage);
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
		// To Capture details of QR code
		result = new MultiFormatReader().decode(binaryBitmap);
		System.out.println(result.getText());
		return result.getText();
	}
	
	
	/* ========================================================================== JAVASCRIPT ========================================================================== */
	//html5: get validation message
	public String getHtml5ValidationMessage(String xpath, String ...values) {
		xpath = String.format(xpath, (Object[]) values);
		JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
		
		element = element(xpath);
		return (String) jsExecutor.executeScript("return arguments[0].validationMessage;", element);
	}
	// Refresh Browser
	public Object refreshPageJS() {
		javascriptExecutor = (JavascriptExecutor) getDriver();
		return javascriptExecutor.executeScript("history.go(0)");
	}

	// Get Title Browser
	public String getTitlePageJS() {
		javascriptExecutor = (JavascriptExecutor) getDriver();
		return (String) javascriptExecutor.executeScript("return document.title");
	}

	// Get Url Browser
	public String getURLPageJS() {
		javascriptExecutor = (JavascriptExecutor) getDriver();
		return (String) javascriptExecutor.executeScript("return document.URL");
	}

	// Get Domain
	public String getDomainJS() {
		javascriptExecutor = (JavascriptExecutor) getDriver();
		return (String) javascriptExecutor.executeScript("return document.domain");
	}

	// Click Element
	/**
	 * @param xpath  : xpath element c???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */

	public Object clicktoElementJS(String xpath, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		javascriptExecutor = (JavascriptExecutor) getDriver();

		element = element(xpath);
		return javascriptExecutor.executeScript("arguments[0].click();", element);
	}

	// Senkey element
	/**
	 * @param xpath          : xpath element c???n l???y
	 * @param valueToSendkey : gi?? tr??? mu???n senkey v??o element
	 * @param values         : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public Object sendkeyToElementJS(String xpath, String valueToSenKey, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		highlightElementJS(xpath);

		javascriptExecutor = (JavascriptExecutor) getDriver();
		element = element(xpath);
		return javascriptExecutor.executeScript("arguments[0].setAttribute('value', '" + valueToSenKey + "')", element);
	}

	// getInnerText
	public String getInnerTextJS() {
		javascriptExecutor = (JavascriptExecutor) getDriver();
		return javascriptExecutor.executeScript("return document.documentElement.innerText;").toString();
	}

	// Scoll to Pixel
	/**
	 * @param pixel : pixel mu???n scroll t???i
	 */
	public Object scrollToPixelJS(float pixel) {
		javascriptExecutor = (JavascriptExecutor) getDriver();
		return javascriptExecutor.executeScript("window.scrollBy(0," + pixel + ")");
	}

	// Scoll to bottom Page
	public Object scrollToBottomPageJS() {
		javascriptExecutor = (JavascriptExecutor) getDriver();
		return javascriptExecutor.executeScript("window.scrollBy(0, document.body.scrollHeight)");
	}

	// Scoll to Element
	/**
	 * @param xpath  : xpath element mu???n scroll t???i
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public Object scrollToElementJS(String xpath, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		javascriptExecutor = (JavascriptExecutor) getDriver();
		element = element(xpath);
		return javascriptExecutor.executeScript("arguments[0]. scrollIntoView(true);", element);
	}

	public Object scrollToElementJS1(WebElement element) {
		javascriptExecutor = (JavascriptExecutor) getDriver();
		return javascriptExecutor.executeScript("arguments[0]. scrollIntoView(true);", element);
	}

	// Remove attribute
	/**
	 * @param xpath     : xpath element mu???n l???y
	 * @param attribute : t??n attribute c???a element mu???n remove
	 * @param values    : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public Object removeAttributeJS(String xpath, String attribute, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		javascriptExecutor = (JavascriptExecutor) getDriver();
		element = element(xpath);
		return javascriptExecutor.executeScript("arguments[0].removeAttribute('" + attribute + "');", element);
	}

	// Set attribute
	/**
	 * @param xpath     : xpath element mu???n l???y
	 * @param attribute : t??n attribute c???a element mu???n set
	 * @param values    : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public Object setAttributeJS(String xpath, String attribute, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		javascriptExecutor = (JavascriptExecutor) getDriver();
		element = element(xpath);
		return javascriptExecutor.executeScript("arguments[0].setAttribute('" + attribute + "');", element);
	}

	// Highlight
	/**
	 * @param xpath  : xpath element mu???n l???y
	 * @param values : tham s??? dynamic trong String xpath (c?? th??? c?? ho???c kh??ng)
	 */
	public void highlightElementJS(String xpath, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		javascriptExecutor = (JavascriptExecutor) getDriver();
		element = element(xpath).waitUntilVisible();
		String originalStyle = element.getAttribute("style");
		javascriptExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 5px solid red; border-style: dashed;");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		javascriptExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
	}

	public void highlightElement1(WebElement element) {
		javascriptExecutor = (JavascriptExecutor) getDriver();

		String originalStyle = element.getAttribute("style");
		javascriptExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 5px solid red; border-style: dashed;");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		javascriptExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
	}

	/*
	 * ========================================================================== .PROPERTIES ==========================================================================
	 */

	// get gi?? tr??? c???a t???ng th??ng s??? c???u h??nh file serenity.properties
	/**
	 * @param property : t??n th??ng s??? c???u h??nh (VD: appium.platformName/platformVersion.....)
	 */
	public static String getProperties(String property) throws IOException {
		String path = null;
		String fileName = "serenity.properties";
		if (osName.toLowerCase().contains(platWin)) {
			path = String.format(workingDr + "\\%s", fileName);

		} else if (osName.toLowerCase().contains(platMac)) {
			path = String.format(workingDr + "/%s", fileName);

		} else {
			System.out.println("not found osName: get properties");
		}

		FileInputStream a = new FileInputStream(path);

		Properties p = new Properties();
		p.load(a);
		return p.getProperty(property);
	}

	/* ======================= D??? ??n ======================== */
	public void dynamciChooseValueAuto(String xpathAuto, String xpathResultValue, String... valueSenkey) {
		highlightElementJS(xpathAuto);
		element(xpathAuto).sendKeys(valueSenkey);

		highlightElementJS(xpathResultValue, valueSenkey);
		element(String.format(xpathResultValue, (Object[]) valueSenkey)).waitUntilVisible().click();
	}

	public int getRowResulSearchUi(String xpathRecord) {
		int recordNumberInt = 0;

		List<WebElementFacade> lstRecord = findAll(xpathRecord);
		int sizeRecord = lstRecord.size();

		if (sizeRecord > 0) {
			highlightElementJS(xpathRecord);
			String recordResultString = element(xpathRecord).getText();
			String[] recordResultStringArray = recordResultString.split("\\s");

			String recordNumberString = recordResultStringArray[2];

			// String recordNumberStringRep = recordNumberString.replaceAll(",", "");
			recordNumberInt = Integer.parseInt(recordNumberString);
		}

		System.out.println("row UI =" + recordNumberInt);
		return recordNumberInt;
	}

}

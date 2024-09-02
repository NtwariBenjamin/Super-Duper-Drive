package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {
	public static final String user_url = "https://www.theben.com/";
	public static final String username = "benjamin";
	public static final String password = "ntwari";
	public static final String url = "http://www.ziggy.com/";
	public static final String reg_username = "rider";
	public static final String reg_password = "rider";

	protected WebDriver driver;
	@LocalServerPort
	protected int port;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	protected HomePage signUpAndLogin() {
		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.setFirstName("Benjamin");
		signupPage.setLastName("Ntwari");
		signupPage.setUserName("Ben");
		signupPage.setPassword("ben");
		signupPage.signUp();
		driver.get("http://localhost:" + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.setUserName("Ben");
		loginPage.setPassword("ben");
		loginPage.login();

		return new HomePage(driver);
	}

	@Test
	public void testPageVisit() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testSignUpLoginLogout() {
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());

		SignupPage signupPage = new SignupPage(driver);
		signupPage.setFirstName("Benjamin");
		signupPage.setLastName("Ntwari");
		signupPage.setUserName("Ben");
		signupPage.setPassword("ben");
		signupPage.signUp();

		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());

		LoginPage loginPage = new LoginPage(driver);
		loginPage.setUserName("Ben");
		loginPage.setPassword("ben");
		loginPage.login();

		HomePage homePage = new HomePage(driver);
		homePage.logout();

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}
	@Test
	public void testCredential() {
		HomePage homePage = signUpAndLogin();
		createAndVerifyCredential(user_url, username, password, homePage);
		homePage.deleteCredential();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.logout();
	}

	private void createAndVerifyCredential(String url, String username, String password, HomePage homePage) {
		AddCredential(url, username, password, homePage);
		homePage.navToCredentialsTab();
		Credential credential = homePage.getCredential();
		Assertions.assertEquals(url, credential.getUrl());
		Assertions.assertEquals(username, credential.getUsername());
		Assertions.assertNotEquals(password, credential.getPassword());
	}

	private void AddCredential(String url, String username, String password, HomePage homePage) {
		homePage.navToCredentialsTab();
		homePage.addNewCredential();
		setCredentialFields(url, username, password, homePage);
		homePage.saveCredentialChanges();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.navToCredentialsTab();
	}

	private void setCredentialFields(String url, String username, String password, HomePage homePage) {
		homePage.setCredentialUrl(url);
		homePage.setCredentialUsername(username);
		homePage.setCredentialPassword(password);
	}

	@Test
	public void testCredentialUpdate() {
		HomePage homePage = signUpAndLogin();
		createAndVerifyCredential(user_url, username, password, homePage);
		Credential originalCredential = homePage.getCredential();
		String firstEncryptedPassword = originalCredential.getPassword();
		homePage.editCredential();
		String newUrl = url;
		String newCredentialUsername = reg_username;
		String newPassword = reg_password;
		setCredentialFields(newUrl, newCredentialUsername, newPassword, homePage);
		homePage.saveCredentialChanges();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.navToCredentialsTab();
		Credential modifiedCredential = homePage.getCredential();
		Assertions.assertEquals(newUrl, modifiedCredential.getUrl());
		Assertions.assertEquals(newCredentialUsername, modifiedCredential.getUsername());
		String modifiedCredentialPassword = modifiedCredential.getPassword();
		Assertions.assertNotEquals(newPassword, modifiedCredentialPassword);
		Assertions.assertNotEquals(firstEncryptedPassword, modifiedCredentialPassword);
		homePage.deleteCredential();
		resultPage.clickOk();
		homePage.logout();
	}

	@Test
	public void testCredentialDeletion() {
		HomePage homePage = signUpAndLogin();
		AddCredential(user_url, username, password, homePage);
		AddCredential(url, reg_username, reg_password, homePage);
		AddCredential("http://www.billymugisha.com/", "billy", "mugisha", homePage);
		Assertions.assertFalse(homePage.noCredentials(driver));
		homePage.deleteCredential();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.navToCredentialsTab();
		homePage.deleteCredential();
		resultPage.clickOk();
		homePage.navToCredentialsTab();
		homePage.deleteCredential();
		resultPage.clickOk();
		homePage.navToCredentialsTab();
		Assertions.assertTrue(homePage.noCredentials(driver));
		homePage.logout();
	}
	@Test
	public void testNoteRemove() {
		String noteTitle = "My To Do List";
		String noteDescription = "Submit The project ASAP.";
		HomePage homePage = signUpAndLogin();
		createNote(noteTitle, noteDescription, homePage);
		homePage.navToNotesTab();
		homePage = new HomePage(driver);
		Assertions.assertFalse(homePage.noNotes(driver));
		deleteNote(homePage);
		Assertions.assertTrue(homePage.noNotes(driver));
	}

	private void deleteNote(HomePage homePage) {
		homePage.deleteNote();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
	}

	@Test
	public void testCreateAndViewNote() {
		String noteTitle = "This is a new Note";
		String noteDescription = "About my to do list.";
		HomePage homePage = signUpAndLogin();
		createNote(noteTitle, noteDescription, homePage);
		homePage.navToNotesTab();
		homePage = new HomePage(driver);
		Note note = homePage.getFirstNote();
		Assertions.assertEquals(noteTitle, note.getNoteTitle());
		Assertions.assertEquals(noteDescription, note.getNoteDescription());
		deleteNote(homePage);
		homePage.logout();
	}

	@Test
	public void NoteUpdate() {
		String noteTitle = "Update Note Data";
		String noteDescription = "New User data.";
		HomePage homePage = signUpAndLogin();
		createNote(noteTitle, noteDescription, homePage);
		homePage.navToNotesTab();
		homePage = new HomePage(driver);
		homePage.editNote();
		String modifiedNoteTitle = "My updated Note";
		homePage.modifyNoteTitle(modifiedNoteTitle);
		String modifiedNoteDescription = "Updated data";
		homePage.modifyNoteDescription(modifiedNoteDescription);
		homePage.saveNoteChanges();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.navToNotesTab();
		Note note = homePage.getFirstNote();
		Assertions.assertEquals(modifiedNoteTitle, note.getNoteTitle());
		Assertions.assertEquals(modifiedNoteDescription, note.getNoteDescription());
	}

	private void createNote(String noteTitle, String noteDescription, HomePage homePage) {
		homePage.navToNotesTab();
		homePage.addNewNote();
		homePage.setNoteTitle(noteTitle);
		homePage.setNoteDescription(noteDescription);
		homePage.saveNoteChanges();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.navToNotesTab();
	}
}

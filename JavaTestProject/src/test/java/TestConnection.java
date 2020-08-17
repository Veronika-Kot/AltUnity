import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import ro.altom.altunitytester.AltUnityDriver;
import ro.altom.altunitytester.AltUnityObject;
import ro.altom.altunitytester.Commands.FindObject.AltFindObjectsParameters;
import ro.altom.altunitytester.Commands.FindObject.AltGetAllElementsParameters;

import java.io.IOException;
import java.net.URL;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

public class TestConnection {
    private static AltUnityDriver driver;
    static WebDriver wDriver;

    @BeforeClass
    public static void setUp() throws IOException, InterruptedException {
        //Set Capabilities for Android Device
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "android");
        capabilities.setCapability("platformVersion", "7.0");
        capabilities.setCapability("deviceName","Android Emulator");
        capabilities.setCapability("appPackage", "com.jmarques.match3");
        capabilities.setCapability("appActivity", "com.unity3d.player.UnityPlayerActivity");
        capabilities.setCapability("app","/Users/Veronika-Kot/Match3-Unity-master/build/build.apk");
        capabilities.setCapability("androidInstallTimeout", 120000);
        capabilities.setCapability("uiautomator2ServerInstallTimeout", 120000);
        capabilities.setCapability("noReset", "true");

        //Start Appium-Selenium Driver
        wDriver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        sleep(5000);

        //Do the port redirect
        AltUnityDriver.setupPortForwarding("android","",13000,13000);

        //Start AltUnity Driver
        driver = new AltUnityDriver("127.0.0.1", 13000,";","&",true);
    }

    @Before
    public void loadLevel(){
        driver.loadScene("Menu");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        //Stop driver
        driver.stop();
        wDriver.quit();
        sleep(1000);
    }

    @Test
    public void TestMenuScene() {
        //Validate that Menu scene is present
        assertEquals("Menu", driver.getCurrentScene());
    }

    @Test
    public void TestDisableSound() throws Exception {
        //Get Menu Button
        AltUnityObject mainMenu = driver.findObject(AltUnityDriver.By.NAME, "Menu");
        mainMenu.tap();

        //Wait for pop-up Menu modal to appear
        driver.waitForObject(AltUnityDriver.By.NAME, "Music");

        //Tap on Music Button
        driver.findObject(AltUnityDriver.By.NAME, "Music").tap();

        //Validate that sound is off
        AltUnityObject musicSource = driver.findObject(AltUnityDriver.By.NAME, "Music Source");
        assertEquals(musicSource.getComponentProperty("Audio Source", "Mute"), true);
    }
}

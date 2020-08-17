import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import ro.altom.altunitytester.AltUnityDriver;

import java.io.IOException;
import java.net.URL;

import static java.lang.Thread.sleep;

public class TestConnection {
    private static AltUnityDriver driver;
    static WebDriver wDriver;

    @BeforeClass
    public static void setUp() throws IOException, InterruptedException {

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
        wDriver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        sleep(5000);

        AltUnityDriver.setupPortForwarding("android","",13000,13000);
        driver = new AltUnityDriver("127.0.0.1", 13000,";","&",true);
    }

    @Before
    public void loadLevel(){
        driver.loadScene("Menu");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        driver.stop();
        sleep(1000);
    }

    @Test
    public void TestMainPageLoadedCorrectly() {
//        driver.waitForObject(AltUnityDriver.By.PA, "Panel");
        driver.waitForObject(AltUnityDriver.By.PATH, "//MenuUI/Panel/Menu").tap();
    }
}

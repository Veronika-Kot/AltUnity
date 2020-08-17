import org.junit.*;
import org.junit.jupiter.api.Order;
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
import static org.junit.Assert.assertNotNull;

public class TestGame {
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

    @AfterClass
    public static void tearDown() throws Exception {
        //Stop driver
        driver.stop();
        wDriver.quit();
        sleep(1000);
    }

    @Test
    @Order(1)
    public void loadLevel(){
        driver.loadScene("Menu");
    }

    @Test
    @Order(2)
    public void startGame(){
        //Click on the start button
        driver.findObject(AltUnityDriver.By.NAME, "Start");

        //Validate that Stage1 scene started
        assertEquals("Stage1", driver.getCurrentScene());
    }

    @Test
    @Order(3)
    public void testDisableSound() throws Exception {
        //Get Menu Button
        AltUnityObject mainMenu = driver.findObject(AltUnityDriver.By.PATH, "//GameUI/GamePanel/Menu");
        mainMenu.tap();

        //Wait for pop-up Menu modal to appear
        driver.waitForObject(AltUnityDriver.By.NAME, "Music");

        //Tap on Music Button
        driver.findObject(AltUnityDriver.By.NAME, "Music").tap();

        //Validate that sound is off
        AltUnityObject musicSource = driver.findObject(AltUnityDriver.By.NAME, "Music Source");
        assertEquals(musicSource.getComponentProperty("Audio Source", "Mute"), true);

        // Return Back
        driver.findObject(AltUnityDriver.By.NAME, "Back").tap();
        driver.waitForObject(AltUnityDriver.By.NAME, "Board");

        //Validate that board is displayed
        AltUnityObject board = driver.findObject(AltUnityDriver.By.NAME, "Board");
        assertNotNull(board);
    }

    @Test
    @Order(4)
    public void coundBreads()  {
        driver.waitForObject(AltUnityDriver.By.NAME, "Board");
        AltUnityObject[] gems = driver.findObjectsWhichContains(AltUnityDriver.By.NAME, "Gem");

        // Validate that gems are not null
        assertNotNull(gems);

        int count = 0;

        // count Bread gems
        for(int i=0; i<gems.length; i++){
            if(gems[i].getComponentProperty("Sprite Renderer", "Sprite").equals("characters_0004")) {
                count++;
            }
        }

        System.out.println(count);
    }
}

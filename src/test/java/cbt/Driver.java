package cbt;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Driver {
  static String browser;

  private Driver () {
  }

  private static WebDriver driver;

  public static WebDriver getDriver () {
    if (driver == null) {

      if (System.getProperty("os.name").toLowerCase().contains("linux")) {
//        browser = "/home/opc/Downloads/chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver.exe");
        return new ChromeDriver();

//        ChromeDriverService service = new ChromeDriverService.Builder()
//                .usingDriverExecutable(new File("/usr/local/bin/chromedriver.exe"))
//                .usingAnyFreePort()
//                .build();
//        try {
//          service.start();
//        } catch (IOException e) {
//          e.printStackTrace();
//        }
//        return new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());

      } else {
        if (System.getProperty("BROWSER") == null) {
          browser = ConfigurationReader.getProperty("browser");
        } else {
          browser = System.getProperty("BROWSER");
        }

      }

      switch (browser) {
        case "remote-chrome":
          try {
            // assign your grid server address
            String gridAddress = "34.200.231.249";
            URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setBrowserName("chrome");
            driver = new RemoteWebDriver(url, desiredCapabilities);
          } catch (Exception e) {
            e.printStackTrace();
          }
          break;
        case "chrome":
          WebDriverManager.chromedriver().setup();
          driver = new ChromeDriver();
          break;
        case "chrome-headless":
          WebDriverManager.chromedriver().setup();
          driver = new ChromeDriver(new ChromeOptions().setHeadless(true));
          break;
        case "firefox":
          WebDriverManager.firefoxdriver().setup();
          driver = new FirefoxDriver();
          break;
        case "firefox-headless":
          WebDriverManager.firefoxdriver().setup();
          driver = new FirefoxDriver(new FirefoxOptions().setHeadless(true));
          break;

        case "ie":
          if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            throw new WebDriverException("Your operating system does not support the requested browser");
          }
          WebDriverManager.iedriver().setup();
          driver = new InternetExplorerDriver();
          break;

        case "edge":
          if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            throw new WebDriverException("Your operating system does not support the requested browser");
          }
          WebDriverManager.edgedriver().setup();
          driver = new EdgeDriver();
          break;

        case "safari":
          if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            throw new WebDriverException("Your operating system does not support the requested browser");
          }
          WebDriverManager.getInstance(SafariDriver.class).setup();
          driver = new SafariDriver();
          break;
      }
    }

    return driver;
  }

  public static void closeDriver () {
    if (driver != null) {
      driver.quit();
      driver = null;
    }
  }
}

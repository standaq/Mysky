package automationFramework;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

/**
 * Webklikaè na mousehunt.com
 * 
 * @author Panda
 *
 */
public class Clicker2 {

	private static WebDriver driverStanda = null;
	private static WebDriver driverAnicka = null;
	// private static WebDriver driverGontar = null;
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private static Random random = new Random();

	public static void main(String[] args) {
		System.setProperty("webdriver.firefox.marionette", "C:/Sklad/Selenium/geckodriver.exe");
		File file = new File("C:/Sklad/Selenium/IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());

		// Create a new instance of the Firefox driver
		driverStanda = new FirefoxDriver();
		driverAnicka = new FirefoxDriver();
		// driverGontar = new FirefoxDriver();
		// Put a Implicit wait, this means that any search for elements on the
		// page could take the time the implicit wait is set for before throwing
		// exception
		driverStanda.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//driverAnicka.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// driverGontar.manage().timeouts().implicitlyWait(10,
		// TimeUnit.SECONDS);
		// Otevøeme myšky v samostatnych vlaknech
		thread1.start();
		thread2.start();
		// thread3.start();
		// pockame na ukonceni vlaken
		try {
			thread1.join();
		} catch (InterruptedException e) {
			System.out.println("Vlákno pøerušeno");
		}
		 try {
		 thread2.join();
		 } catch (InterruptedException e) {
		 System.out.println("Vlákno pøerušeno");
		 }
		// try {
		// thread3.join();
		// } catch (InterruptedException e) {
		// System.out.println("Vlákno pøerušeno");
		// }

	}

	static Thread thread1 = new Thread() {
		public void run() {
			otevriMysky(driverStanda, "st.jindrich", "konami");
		}
	};

	static Thread thread2 = new Thread() {
		public void run() {
			otevriMysky(driverAnicka, "anna.zlonicka", "skritek");
		}
	};

	// static Thread thread3 = new Thread() {
	// public void run() {
	// otevriMysky(driverGontar);
	// }
	// };

	private static void otevriMysky(WebDriver driver, String jmeno, String heslo) {
		driver.get("https://www.mousehuntgame.com");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// login
		driver.findElement(By.name("accountName")).sendKeys(jmeno);
		driver.findElement(By.name("password")).sendKeys(heslo);
		driver.findElement(By.name("doLogin")).click();
		// pokud je po spuštìní denní truhla
		kontrolaTruhly(driver);
		while (!driver.getPageSource().contains("Welcome")) {
			// èekám 1s
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("Vlákno pøerušeno");
			}
		}
		// dokud vidíme Camp tlaèítko
		while (driver.findElement(By.xpath("//*[@data-page='Camp']")) != null) {
			Calendar cal = Calendar.getInstance();
			// kontrola krále - pokud neni kral tak klikame
			if (!kontrolaKrale(driver)) {
				// click
				klikac(driver, cal);
			}
		}
	}

	/**
	 * Metoda pro kontrolu pøítomností denní truhly
	 * 
	 * @param driver
	 */
	private static void kontrolaTruhly(WebDriver driver) {
		if (driver.findElements(By.id("jsDialogAjaxSuffix")).size() > 0) {
			// klikneme na nìj
			System.out.println("Truhla!");
			driver.findElement(By.id("jsDialogAjaxSuffix")).click();
		} else {
			System.out.println("Truhla nenalezena.");
		}

	}

	/**
	 * private static void dokoupitSyr(WebDriver driver) { //obchod se syrem
	 * driver.findElement(By.id("btn-shop")).click(); //10 goudy
	 * driver.findElement(By.xpath(
	 * "//div[@id='tabbarContent_page_0']/div[2]/div[2]/div[4]/div/div[3]/div[1]/div[2]/div/input"
	 * )).sendKeys("10");; driver.findElement(By.xpath(
	 * "//div[@id='tabbarContent_page_0']/div[2]/div[2]/div[4]/div/div[3]/div[1]/div[2]/a[1]"
	 * )).click(); driver.findElement(By.xpath(
	 * "//div[@id='tabbarContent_page_0']/div[2]/div[2]/div[4]/div/div[3]/div[2]/div[2]/a[1]"
	 * )).click();; }
	 **/
	/**
	 * Metoda pro kontrolu pøítomnosti královy odmìny
	 * 
	 * @param driver
	 * @return
	 */
	private static boolean kontrolaKrale(WebDriver driver) {
		if (driver.getPageSource().contains("Claim Your Reward!")
				|| driver.getPageSource().contains("The King wants")) {
			System.out.println("king! èekám 1s na zadání kódu");
			// poèkáme 1s a zkusíme to znova
			try {
				Thread.sleep(1328);
			} catch (InterruptedException e) {
				System.out.println("Vlákno pøerušeno");
			}
			// kral nalezen, je nutne ho zadat rucne
			return true;
		} else {
			System.out.println("Král nenalezen");
			return false;
		}
	}

	private static void klikac(WebDriver driver, Calendar cal) {
		// System.out.println("pøed dveøma");
		// if ((driver.getPageSource().contains("I have found an
		// intersection!"))
		// && (!driver.getPageSource().contains("vybrano"))) {
		// System.out.println(("Dveøe"));
		// vyberDvere(driver);
		// }
		// pokud vidime trumpetu
		WebElement trumpeta = driver.findElement(By.xpath("//*[@class='mousehuntHud-huntersHorn']"));
		if (trumpeta.isDisplayed() && trumpeta.isEnabled()) {
			System.out.println("OK");
			System.out.println(sdf.format(cal.getTime()));
			// click
			try {
				trumpeta.click();	
			} catch (Exception e) {
				try {
					Thread.sleep(3000);
					System.out.println("Nelze troubit, èekám 3s");
					return;
				} catch (InterruptedException e1) {
					System.out.println("Vlákno pøerušeno");
				}
			}
			
			// po kliknuti se muze objevit truhla
			kontrolaTruhly(driver);
			// nebo král
			kontrolaKrale(driver);
			// 15 minut v milis
			int patnactMin = 900000;
			// nahodne sekundove cislo mezi 2 - 3s
			int randomSec = random.nextInt(3236 - 1274) + 1504;
			// soucet
			int nextHuntIn = patnactMin + randomSec;
			int seconds = (int) (nextHuntIn / 1000) % 60;
			int minutes = (int) ((nextHuntIn / (1000 * 60)) % 60);
			System.out.println("Click za " + minutes + " minut a " + seconds + " vteøin");
			try {
				Thread.sleep(nextHuntIn);
			} catch (InterruptedException e) {
				System.out.println("Vlákno pøerušeno");
			}
		}
		// pokud nevidime trumpetu
		else {
			// a pokud to neni kral
			if (!kontrolaKrale(driver)) {
				System.out.println("Horn tu není.");
				// zjistime kolik zbyva do konce
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				String dalsiZa = driver.findElement(By.id("huntTimer")).getText();
				int milisToHunt = 500;
				if (dalsiZa.replaceAll("[\\D]", "").equals("")) {
					milisToHunt = 1120;
				} else {
					int zaInt = Integer.parseInt(dalsiZa.replaceAll("[\\D]", ""));
					if (15 < zaInt)
					{
						zaInt = 15;
					}
				
					System.out.println("èekám " + zaInt + " min");
					milisToHunt = 60000 * zaInt;
				}
				try {
					Thread.sleep(milisToHunt);
				} catch (InterruptedException e) {
					System.out.println("Vlákno pøerušeno");
				}
			}
		}
	}
	/**
	 * private static void vyberDvere(WebDriver driver) { if (1==1) {
	 * System.out.println("skip metoda na dveøe"); return; } // pokud je element
	 * na stránce if (driver.findElements(By.xpath(
	 * "//div[@id='hudLocationContent']/div/div[4]/div[3]/a/div[2]/div/span")).
	 * size() > 0) { // klikneme na nìj System.out.println("Vybírám dveøe");
	 * driver.findElements(By.xpath(
	 * "//div[@id='hudLocationContent']/div/div[4]/div[3]/a/div[2]/div/span")).
	 * get(0).click(); //potvrzení dveøí driver.findElement(By.xpath(
	 * "//div[@id='hudLocationContent']/div/div[15]/div/div/div[6]/a[2]/span")).
	 * click(); } else { System.out.println("Dveøe tu nejsou"); } }
	 **/
}

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
 * Webklika� na mousehunt.com
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
		// Otev�eme my�ky v samostatnych vlaknech
		thread1.start();
		thread2.start();
		// thread3.start();
		// pockame na ukonceni vlaken
		try {
			thread1.join();
		} catch (InterruptedException e) {
			System.out.println("Vl�kno p�eru�eno");
		}
		 try {
		 thread2.join();
		 } catch (InterruptedException e) {
		 System.out.println("Vl�kno p�eru�eno");
		 }
		// try {
		// thread3.join();
		// } catch (InterruptedException e) {
		// System.out.println("Vl�kno p�eru�eno");
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
		// pokud je po spu�t�n� denn� truhla
		kontrolaTruhly(driver);
		while (!driver.getPageSource().contains("Welcome")) {
			// �ek�m 1s
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("Vl�kno p�eru�eno");
			}
		}
		// dokud vid�me Camp tla��tko
		while (driver.findElement(By.xpath("//*[@data-page='Camp']")) != null) {
			Calendar cal = Calendar.getInstance();
			// kontrola kr�le - pokud neni kral tak klikame
			if (!kontrolaKrale(driver)) {
				// click
				klikac(driver, cal);
			}
		}
	}

	/**
	 * Metoda pro kontrolu p��tomnost� denn� truhly
	 * 
	 * @param driver
	 */
	private static void kontrolaTruhly(WebDriver driver) {
		if (driver.findElements(By.id("jsDialogAjaxSuffix")).size() > 0) {
			// klikneme na n�j
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
	 * Metoda pro kontrolu p��tomnosti kr�lovy odm�ny
	 * 
	 * @param driver
	 * @return
	 */
	private static boolean kontrolaKrale(WebDriver driver) {
		if (driver.getPageSource().contains("Claim Your Reward!")
				|| driver.getPageSource().contains("The King wants")) {
			System.out.println("king! �ek�m 1s na zad�n� k�du");
			// po�k�me 1s a zkus�me to znova
			try {
				Thread.sleep(1328);
			} catch (InterruptedException e) {
				System.out.println("Vl�kno p�eru�eno");
			}
			// kral nalezen, je nutne ho zadat rucne
			return true;
		} else {
			System.out.println("Kr�l nenalezen");
			return false;
		}
	}

	private static void klikac(WebDriver driver, Calendar cal) {
		// System.out.println("p�ed dve�ma");
		// if ((driver.getPageSource().contains("I have found an
		// intersection!"))
		// && (!driver.getPageSource().contains("vybrano"))) {
		// System.out.println(("Dve�e"));
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
					System.out.println("Nelze troubit, �ek�m 3s");
					return;
				} catch (InterruptedException e1) {
					System.out.println("Vl�kno p�eru�eno");
				}
			}
			
			// po kliknuti se muze objevit truhla
			kontrolaTruhly(driver);
			// nebo kr�l
			kontrolaKrale(driver);
			// 15 minut v milis
			int patnactMin = 900000;
			// nahodne sekundove cislo mezi 2 - 3s
			int randomSec = random.nextInt(3236 - 1274) + 1504;
			// soucet
			int nextHuntIn = patnactMin + randomSec;
			int seconds = (int) (nextHuntIn / 1000) % 60;
			int minutes = (int) ((nextHuntIn / (1000 * 60)) % 60);
			System.out.println("Click za " + minutes + " minut a " + seconds + " vte�in");
			try {
				Thread.sleep(nextHuntIn);
			} catch (InterruptedException e) {
				System.out.println("Vl�kno p�eru�eno");
			}
		}
		// pokud nevidime trumpetu
		else {
			// a pokud to neni kral
			if (!kontrolaKrale(driver)) {
				System.out.println("Horn tu nen�.");
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
				
					System.out.println("�ek�m " + zaInt + " min");
					milisToHunt = 60000 * zaInt;
				}
				try {
					Thread.sleep(milisToHunt);
				} catch (InterruptedException e) {
					System.out.println("Vl�kno p�eru�eno");
				}
			}
		}
	}
	/**
	 * private static void vyberDvere(WebDriver driver) { if (1==1) {
	 * System.out.println("skip metoda na dve�e"); return; } // pokud je element
	 * na str�nce if (driver.findElements(By.xpath(
	 * "//div[@id='hudLocationContent']/div/div[4]/div[3]/a/div[2]/div/span")).
	 * size() > 0) { // klikneme na n�j System.out.println("Vyb�r�m dve�e");
	 * driver.findElements(By.xpath(
	 * "//div[@id='hudLocationContent']/div/div[4]/div[3]/a/div[2]/div/span")).
	 * get(0).click(); //potvrzen� dve�� driver.findElement(By.xpath(
	 * "//div[@id='hudLocationContent']/div/div[15]/div/div/div[6]/a[2]/span")).
	 * click(); } else { System.out.println("Dve�e tu nejsou"); } }
	 **/
}

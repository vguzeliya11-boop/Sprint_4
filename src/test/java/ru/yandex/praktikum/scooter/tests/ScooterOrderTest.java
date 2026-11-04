package ru.yandex.praktikum.scooter.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.yandex.praktikum.scooter.pages.MainPage;
import ru.yandex.praktikum.scooter.pages.OrderPage;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ScooterOrderTest {

    private WebDriver driver;
    private final int orderButtonIndex;
    private final String name, surname, address, metro, phone, date, term, color, comment;

    @BeforeClass
    public static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    public ScooterOrderTest(int orderButtonIndex, String name, String surname, String address,
                            String metro, String phone, String date, String term, String color, String comment) {
        this.orderButtonIndex = orderButtonIndex;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metro = metro;
        this.phone = phone;
        this.date = date;
        this.term = term;
        this.color = color;
        this.comment = comment;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1, "Иван", "Иванов", "ул. Пушкина, д. 10", "Чистые пруды", "+79991112233", "10.11.2026", "сутки", "black", "Позвонить за час"},
                {2, "Петр", "Петров", "ул. Лермонтова, д. 5", "Арбатская", "+79994445566", "12.11.2026", "сутки", "grey", "Оставить у консьержа"}
        });
    }

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
        driver = new ChromeDriver(options);
        driver.get("https://qa-scooter.praktikum-services.ru/");
    }

    @Test
    public void testScooterOrderFlow() {
        MainPage mainPage = new MainPage(driver);
        OrderPage orderPage = new OrderPage(driver);

        if (orderButtonIndex == 1) {
            mainPage.clickTopOrderButton();
        } else {
            mainPage.clickBottomOrderButton();
        }

        // Шаг 1
        orderPage.setName(name);
        orderPage.setSurname(surname);
        orderPage.setAddress(address);
        orderPage.setMetro(metro);
        orderPage.setPhone(phone);
        orderPage.clickNextButton();

        // Шаг 2
        orderPage.setDate(date);
        orderPage.setTerm(term);
        orderPage.setColor(color);
        orderPage.setComment(comment);

        // Финал: открывается модальное окно, нажимается "Да"
        orderPage.makeOrder();

        // ПРОВЕРКА: тест упадет здесь с AssertionError, что и является целью задания (выявление бага)
        assertTrue("Сообщение об успешном заказе не появилось (это известный баг Chrome)",
                orderPage.getNewOrderSuccessMessage().contains("Заказ оформлен"));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
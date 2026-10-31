package ru.yandex.praktikum.scooter.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
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
    private MainPage mainPage;
    private OrderPage orderPage;

    // Параметры для теста
    private final int orderButtonIndex; // 1 - верхняя, 2 - нижняя
    private final String name;
    private final String surname;
    private final String address;
    private final String metro;
    private final String date;
    private final String color;
    private final String comment;

    public ScooterOrderTest(int orderButtonIndex, String name, String surname, String address,
                            String metro, String date, String color, String comment) {
        this.orderButtonIndex = orderButtonIndex;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metro = metro;
        this.date = date;
        this.color = color;
        this.comment = comment;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // Тест 1: Верхняя кнопка, одни данные
                {1, "Иван", "Иванов", "ул. Пушкина, д. 10", "Чистые пруды", "01.11.2023", "black", "Позвонить за час"},
                // Тест 2: Нижняя кнопка, другие данные
                {2, "Петр", "Петров", "ул. Лермонтова, д. 5", "Арбатская", "05.11.2023", "white", "Оставить у консьержа"}
        });
    }

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // Если тесты падают из-за бага Chrome, можно попробовать убрать headless для отладки,
        // но для сдачи лучше оставить.
        options.addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.get("https://qa-scooter.praktikum-services.ru/");

        mainPage = new MainPage(driver);
        orderPage = new OrderPage(driver);
    }

    @Test
    public void testScooterOrderFlow() {
        // 1. Нажимаем нужную кнопку "Заказать"
        if (orderButtonIndex == 1) {
            mainPage.clickTopOrderButton();
        } else {
            mainPage.clickBottomOrderButton();
        }

        // 2. Заполняем форму заказа
        orderPage.fillName(name);
        orderPage.fillSurname(surname);
        orderPage.fillAddress(address);
        orderPage.selectMetro(metro);
        orderPage.fillDate(date);
        orderPage.selectColor(color);
        orderPage.fillComment(comment);

        // 3. Нажимаем "Далее"
        orderPage.clickNextButton();

        // 4. Проверяем появление всплывающего окна об успешном заказе
        assertTrue("Сообщение об успешном заказе не появилось", orderPage.isSuccessMessageVisible());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

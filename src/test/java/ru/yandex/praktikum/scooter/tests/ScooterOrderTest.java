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

    // Параметры для теста (добавили phone и commentStep2)
    private final int orderButtonIndex;
    private final String name;
    private final String surname;
    private final String address;
    private final String metro;
    private final String date;
    private final String color;
    private final String commentStep1;
    private final String phone;
    private final String commentStep2;

    public ScooterOrderTest(int orderButtonIndex, String name, String surname, String address,
                            String metro, String date, String color, String commentStep1,
                            String phone, String commentStep2) {
        this.orderButtonIndex = orderButtonIndex;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metro = metro;
        this.date = date;
        this.color = color;
        this.commentStep1 = commentStep1;
        this.phone = phone;
        this.commentStep2 = commentStep2;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // Тест 1: Верхняя кнопка
                {1, "Иван", "Иванов", "ул. Пушкина, д. 10", "Чистые пруды", "01.11.2026", "black", "Позвонить за час", "+79991112233", "Все отлично"},
                // Тест 2: Нижняя кнопка
                {2, "Петр", "Петров", "ул. Лермонтова, д. 5", "Арбатская", "05.11.2026", "white", "Оставить у консьержа", "+79994445566", "Не звонить"}
        });
    }

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // Убрали --headless, чтобы браузер открывался визуально (по предыдущему замечанию)
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
        driver = new ChromeDriver(options);
        driver.get("https://qa-scooter.praktikum-services.ru/");

        mainPage = new MainPage(driver);
        orderPage = new OrderPage(driver);
    }

    @Test
    public void testScooterOrderFlow() {
        // 1. Нажимаем нужную кнопку "Заказать" на главной
        if (orderButtonIndex == 1) {
            mainPage.clickTopOrderButton();
        } else {
            mainPage.clickBottomOrderButton();
        }

        // 2. Заполняем ПЕРВУЮ форму заказа
        orderPage.fillNameStep1(name);
        orderPage.fillSurnameStep1(surname);
        orderPage.fillAddress(address);
        orderPage.selectMetro(metro);
        orderPage.fillDate(date);
        orderPage.selectColor(color);
        orderPage.fillCommentStep1(commentStep1);

        // 3. Нажимаем "Далее" для перехода ко второй форме
        orderPage.clickNextButton();

        // 4. Заполняем ВТОРУЮ форму заказа
        orderPage.fillNameStep2(name);
        orderPage.fillSurnameStep2(surname);
        orderPage.fillPhone(phone);
        orderPage.fillCommentStep2(commentStep2);

        // 5. Нажимаем финальную кнопку "Заказать"
        orderPage.clickOrderButton();

        // 6. Проверяем появление всплывающего окна об успешном заказе
        assertTrue("Сообщение об успешном заказе не появилось", orderPage.isSuccessMessageVisible());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
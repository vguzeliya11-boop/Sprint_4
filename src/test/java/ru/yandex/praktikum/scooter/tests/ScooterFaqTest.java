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

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ScooterFaqTest {

    private WebDriver driver;
    private MainPage mainPage;
    private final int questionIndex;

    public ScooterFaqTest(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}
        });
    }

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        // ВАЖНО: задаем размер окна, чтобы верстка не ломалась и элементы были кликабельны
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        driver.get("https://qa-scooter.praktikum-services.ru/");
        mainPage = new MainPage(driver);
    }

    @Test
    public void testFaqAccordion() {
        // Кликаем по вопросу с нужным индексом
        mainPage.clickFaqQuestionByIndex(questionIndex);

        // Проверяем, что ответ открылся
        assertTrue("Ответ на вопрос с индексом " + questionIndex + " не открылся",
                mainPage.isFaqAnswerVisible(questionIndex));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
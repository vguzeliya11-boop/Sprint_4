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

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ScooterFaqTest {

    // Константы
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";
    private static final String WINDOW_SIZE = "1920,1080";

    private WebDriver driver;
    private MainPage mainPage;
    private final int questionIndex;

    // Массив ожидаемых ответов (ключевые фразы)
    private static final String[] expectedAnswers = {
            "Сутки — 400 рублей",
            "один заказ — один самокат",
            "Отсчёт времени аренды начинается",
            "Только начиная с завтрашнего дня",
            "позвонить в поддержку по красивому номеру 1010",
            "Самокат приезжает к вам с полной зарядкой",
            "Да, пока самокат не привезли",
            "Да, обязательно"
    };

    // Инициализация WebDriverManager один раз для всех тестов
    @BeforeClass
    public static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

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
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=" + WINDOW_SIZE);
        driver = new ChromeDriver(options);
        driver.get(BASE_URL);
        mainPage = new MainPage(driver);
    }

    @Test
    public void testFaqAccordion() {
        // Кликаем по вопросу с нужным индексом
        mainPage.clickFaqQuestionByIndex(questionIndex);

        // Получаем текст ответа
        String answerText = mainPage.getAnswerText(questionIndex);

        // Проверяем, что ответ не пустой
        assertFalse("Ответ на вопрос с индексом " + questionIndex + " пустой", answerText.isEmpty());

        // Проверяем, что ответ содержит ожидаемый текст
        assertTrue("Ответ не соответствует вопросу. Ожидалось: " + expectedAnswers[questionIndex] +
                        ", получено: " + answerText,
                answerText.contains(expectedAnswers[questionIndex]));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
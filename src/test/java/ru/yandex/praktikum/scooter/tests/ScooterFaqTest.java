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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class ScooterFaqTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";
    private static final String WINDOW_SIZE = "1920,1080";

    private WebDriver driver;
    private MainPage mainPage;
    private final String questionText;
    private final String expectedAnswer;

    // Инициализация WebDriverManager один раз для всех тестов
    @BeforeClass
    public static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    // Конструктор теперь принимает текст вопроса и ожидаемый ответ
    public ScooterFaqTest(String questionText, String expectedAnswer) {
        this.questionText = questionText;
        this.expectedAnswer = expectedAnswer;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Сколько это стоит? И как оплатить?",
                        "Сутки — 400 рублей. Оплата курьеру — наличными или картой."},
                {"Хочу сразу несколько самокатов! Так можно?",
                        "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим."},
                {"Как рассчитывается время аренды?",
                        "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."},
                {"Можно ли заказать самокат прямо на сегодня?",
                        "Только начиная с завтрашнего дня. Но скоро станем расторопнее."},
                {"Можно ли продлить заказ или вернуть самокат раньше?",
                        "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."},
                {"Вы привозите зарядку вместе с самокатом?",
                        "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится."},
                {"Можно ли отменить заказ?",
                        "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."},
                {"Я жизу за МКАДом, привезёте?",
                        "Да, обязательно. Всем самокатов! И Москве, и Московской области."}
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
        // Кликаем по вопросу, используя текст из параметра
        mainPage.clickFaqQuestionByText(questionText);

        // Получаем текст ответа
        String answerText = mainPage.getAnswerText(expectedAnswer);

        // Проверяем, что ответ не пустой
        assertFalse("Ответ на вопрос '" + questionText + "' пустой", answerText.isEmpty());

        // Проверяем полное соответствие ответа ожидаемому тексту
        assertEquals("Ответ не соответствует вопросу '" + questionText + "'",
                expectedAnswer,
                answerText.trim());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
package ru.yandex.praktikum.scooter.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Используем XPath, так как он надежнее для кнопок с одинаковым текстом
    private By topOrderButton = By.xpath("(//button[text()='Заказать'])[1]");
    private By bottomOrderButton = By.xpath("(//button[text()='Заказать'])[2]");

    // Локатор для кнопки принятия куки в баннере
    private By cookieAcceptButton = By.cssSelector("div[class*='CookieConsent'] button[class*='CookieButton']");

    private By faqHeader = By.xpath("//*[contains(text(), 'Вопросы о важном')]");

    private final String[] questions = {
            "Сколько это стоит? И как оплатить?",
            "Хочу сразу несколько самокатов! Так можно?",
            "Как рассчитывается время аренды?",
            "Можно ли заказать самокат прямо на сегодня?",
            "Можно ли продлить заказ или вернуть самокат раньше?",
            "Вы привозите зарядку вместе с самокатом?",
            "Можно ли отменить заказ?",
            "Я жизу за МКАДом, привезёте?"
    };

    private final String[] answerKeywords = {
            "Сутки — 400 рублей",
            "один заказ — один самокат",
            "Отсчёт времени аренды начинается",
            "Только начиная с завтрашнего дня",
            "позвонить в поддержку по красивому номеру 1010",
            "Самокат приезжает к вам с полной зарядкой",
            "Да, пока самокат не привезли",
            "Да, обязательно"
    };

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    // Метод для закрытия баннера куки, если он появляется
    public void acceptCookies() {
        try {
            // Ждем появления кнопки куки не более 2 секунд, чтобы не тормозить тест, если её нет
            WebDriverWait shortWait = new WebDriverWait(driver, 2);
            WebElement cookieBtn = shortWait.until(ExpectedConditions.elementToBeClickable(cookieAcceptButton));
            cookieBtn.click();
        } catch (Exception e) {
            // Если баннера нет или он уже закрыт, просто продолжаем работу
        }
    }

    public void clickTopOrderButton() {
        acceptCookies();
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(topOrderButton));
        // Используем JS клик, чтобы избежать перехвата клика другими элементами
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    public void clickBottomOrderButton() {
        acceptCookies();
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(bottomOrderButton));
        // Используем JS клик, чтобы избежать перехвата клика баннером куки
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    public void scrollToFAQ() {
        try {
            WebElement header = wait.until(ExpectedConditions.presenceOfElementLocated(faqHeader));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", header);
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 800);");
        }
    }

    public void clickFaqQuestionByIndex(int index) {
        scrollToFAQ();

        if (index < questions.length) {
            By questionLocator = By.xpath("//*[contains(text(), '" + questions[index] + "')]");
            WebElement question = wait.until(ExpectedConditions.elementToBeClickable(questionLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", question);
            question.click();
        }
    }

    public boolean isFaqAnswerVisible(int index) {
        try {
            if (index < answerKeywords.length) {
                By answerLocator = By.xpath("//*[contains(text(), '" + answerKeywords[index] + "')]");
                return wait.until(ExpectedConditions.visibilityOfElementLocated(answerLocator)).isDisplayed();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // Метод для получения текста ответа по индексу вопроса
    public String getAnswerText(int index) {
        try {
            if (index < answerKeywords.length) {
                // Ищем элемент, содержащий текст ответа
                By answerLocator = By.xpath("//*[contains(text(), '" + answerKeywords[index] + "')]");
                WebElement answer = wait.until(ExpectedConditions.visibilityOfElementLocated(answerLocator));
                return answer.getText();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }
}
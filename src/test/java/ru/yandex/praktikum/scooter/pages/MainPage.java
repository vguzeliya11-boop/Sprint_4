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

    private By topOrderButton = By.xpath("(//button[text()='Заказать'])[1]");
    private By bottomOrderButton = By.xpath("(//button[text()='Заказать'])[2]");
    private By cookieAcceptButton = By.cssSelector("div[class*='CookieConsent'] button[class*='CookieButton']");
    private By faqHeader = By.xpath("//*[contains(text(), 'Вопросы о важном')]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    public void acceptCookies() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, 2);
            WebElement cookieBtn = shortWait.until(ExpectedConditions.elementToBeClickable(cookieAcceptButton));
            cookieBtn.click();
        } catch (Exception e) {
            // Игнорируем, если баннера нет
        }
    }

    public void clickTopOrderButton() {
        acceptCookies();
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(topOrderButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    public void clickBottomOrderButton() {
        acceptCookies();
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(bottomOrderButton));
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

    // Изменили метод - теперь принимаем текст вопроса как параметр
    public void clickFaqQuestionByText(String questionText) {
        scrollToFAQ();
        By questionLocator = By.xpath("//*[contains(text(), '" + questionText + "')]");
        WebElement question = wait.until(ExpectedConditions.elementToBeClickable(questionLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", question);
        question.click();
    }

    public boolean isFaqAnswerVisible(String answerKeyword) {
        try {
            By answerLocator = By.xpath("//*[contains(text(), '" + answerKeyword + "')]");
            return wait.until(ExpectedConditions.visibilityOfElementLocated(answerLocator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getAnswerText(String answerKeyword) {
        try {
            By answerLocator = By.xpath("//*[contains(text(), '" + answerKeyword + "')]");
            WebElement answer = wait.until(ExpectedConditions.visibilityOfElementLocated(answerLocator));
            return answer.getText();
        } catch (Exception e) {
            return "";
        }
    }
}
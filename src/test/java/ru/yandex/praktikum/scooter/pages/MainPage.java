package ru.yandex.praktikum.scooter.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы для кнопок заказа
    private final By topOrderButton = By.xpath("(//button[contains(text(), 'Заказать')])[1]");
    private final By bottomOrderButton = By.xpath("(//button[contains(text(), 'Заказать')])[last()]");
    private final By cookieAcceptButton = By.cssSelector("div[class*='CookieConsent'] button[class*='CookieButton']");

    // Локаторы для FAQ
    private final By faqHeader = By.xpath("//*[contains(text(), 'Вопросы о важном')]");
    private final By faqQuestions = By.xpath("//*[contains(@class, 'accordion')]//*[contains(@class, 'accordion__heading')]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    // ========== МЕТОДЫ ДЛЯ ЗАКАЗА ==========

    public void clickOnCookieAcceptButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cookieAcceptButton)).click();
        } catch (Exception e) {
            // Игнорируем, если куки уже приняты или баннера нет
        }
    }

    public void clickTopOrderButton() {
        clickOnCookieAcceptButton();
        wait.until(ExpectedConditions.elementToBeClickable(topOrderButton)).click();
    }

    public void clickBottomOrderButton() {
        clickOnCookieAcceptButton();
        // Скроллим вниз, чтобы кнопка точно появилась в DOM
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        wait.until(ExpectedConditions.elementToBeClickable(bottomOrderButton)).click();
    }

    // ========== МЕТОДЫ ДЛЯ FAQ ==========

    public void scrollToFAQ() {
        try {
            WebElement header = wait.until(ExpectedConditions.presenceOfElementLocated(faqHeader));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", header);
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 800);");
        }
    }

    public void clickFaqQuestionByText(String questionText) {
        scrollToFAQ();
        By questionLocator = By.xpath("//*[contains(text(), '" + questionText + "')]");
        WebElement question = wait.until(ExpectedConditions.elementToBeClickable(questionLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", question);
        question.click();
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
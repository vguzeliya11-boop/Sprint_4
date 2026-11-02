package ru.yandex.praktikum.scooter.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- ЛОКАТОРЫ ПЕРВОЙ ФОРМЫ ---
    private By nameInputStep1 = By.xpath("//input[@placeholder='* Имя']");
    private By surnameInputStep1 = By.xpath("//input[@placeholder='* Фамилия']");
    private By addressInput = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private By metroInput = By.xpath("//input[@placeholder='* Станция метро']");
    private By dateInput = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private By colorBlack = By.id("black");
    private By colorWhite = By.id("white");
    private By commentInputStep1 = By.xpath("//input[@placeholder='Комментарий для курьера']");
    private By nextButton = By.xpath("//button[text()='Далее']");

    // --- ЛОКАТОРЫ ВТОРОЙ ФОРМЫ ---
    private By nameInputStep2 = By.xpath("//input[@placeholder='* Имя']");
    private By surnameInputStep2 = By.xpath("//input[@placeholder='* Фамилия']");
    private By phoneInput = By.xpath("//input[@placeholder='* Телефон']");
    private By commentInputStep2 = By.xpath("//input[@placeholder='Комментарий']");
    private By orderButton = By.xpath("//button[text()='Заказать']");

    // --- ЛОКАТОРЫ УСПЕХА ---
    private By successMessage = By.xpath("//div[contains(text(), 'Заказ оформлен')]");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    // --- МЕТОДЫ ПЕРВОЙ ФОРМЫ ---
    public void fillNameStep1(String name) {
        driver.findElement(nameInputStep1).sendKeys(name);
    }

    public void fillSurnameStep1(String surname) {
        driver.findElement(surnameInputStep1).sendKeys(surname);
    }

    public void fillAddress(String address) {
        driver.findElement(addressInput).sendKeys(address);
    }

    public void selectMetro(String metroName) {
        WebElement metroField = wait.until(ExpectedConditions.elementToBeClickable(metroInput));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", metroField);

        By metroOption = By.xpath("//div[@class='Dropdown__option' and text()='" + metroName + "']");
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(metroOption));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
    }

    public void fillDate(String date) {
        driver.findElement(dateInput).sendKeys(date);
    }

    public void selectColor(String colorId) {
        WebElement colorElement = driver.findElement(By.id(colorId));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", colorElement);
    }

    public void fillCommentStep1(String comment) {
        driver.findElement(commentInputStep1).sendKeys(comment);
    }

    public void clickNextButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    // --- МЕТОДЫ ВТОРОЙ ФОРМЫ ---
    public void fillNameStep2(String name) {
        driver.findElement(nameInputStep2).sendKeys(name);
    }

    public void fillSurnameStep2(String surname) {
        driver.findElement(surnameInputStep2).sendKeys(surname);
    }

    public void fillPhone(String phone) {
        driver.findElement(phoneInput).sendKeys(phone);
    }

    public void fillCommentStep2(String comment) {
        driver.findElement(commentInputStep2).sendKeys(comment);
    }

    public void clickOrderButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(orderButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    // --- ПРОВЕРКА УСПЕХА ---
    public boolean isSuccessMessageVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
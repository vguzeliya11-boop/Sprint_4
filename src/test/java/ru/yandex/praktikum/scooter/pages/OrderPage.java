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

    // Используем XPath по placeholder — это самый надежный локатор для полей ввода
    private By nameInput = By.xpath("//input[@placeholder='* Имя']");
    private By surnameInput = By.xpath("//input[@placeholder='* Фамилия']");
    private By addressInput = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");

    // ИСПРАВЛЕНО: ищем поле метро по placeholder, а не по классу контейнера
    private By metroInput = By.xpath("//input[@placeholder='* Станция метро']");

    private By dateInput = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private By colorBlack = By.id("black");
    private By colorWhite = By.id("white");
    private By commentInput = By.xpath("//input[@placeholder='Комментарий для курьера']");
    private By nextButton = By.xpath("//button[text()='Далее']");

    // Локатор успешного сообщения
    private By successMessage = By.xpath("//div[contains(text(), 'Заказ оформлен')]");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    public void fillName(String name) {
        driver.findElement(nameInput).sendKeys(name);
    }

    public void fillSurname(String surname) {
        driver.findElement(surnameInput).sendKeys(surname);
    }

    public void fillAddress(String address) {
        driver.findElement(addressInput).sendKeys(address);
    }

    public void selectMetro(String metroName) {
        // 1. Находим поле и кликаем по нему через JS, чтобы гарантированно открыть список (обход багов Chrome)
        WebElement metroField = wait.until(ExpectedConditions.elementToBeClickable(metroInput));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", metroField);

        // 2. Ждем появления нужной станции в выпадающем списке и кликаем по ней
        By metroOption = By.xpath("//div[@class='Dropdown__option' and text()='" + metroName + "']");
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(metroOption));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
    }

    public void fillDate(String date) {
        driver.findElement(dateInput).sendKeys(date);
    }

    public void selectColor(String colorId) {
        WebElement colorElement = driver.findElement(By.id(colorId));
        // JS-клик для выбора цвета, чтобы избежать проблем с перекрытием элементов
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", colorElement);
    }

    public void fillComment(String comment) {
        driver.findElement(commentInput).sendKeys(comment);
    }

    public void clickNextButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
        // JS-клик для кнопки "Далее" — это главный обходной путь для бага в Chrome
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    public boolean isSuccessMessageVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
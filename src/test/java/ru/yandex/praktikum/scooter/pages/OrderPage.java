package ru.yandex.praktikum.scooter.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class OrderPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By nameInput = By.xpath("//input[contains(@placeholder, 'Имя')]");
    private final By surnameInput = By.xpath("//input[contains(@placeholder, 'Фамилия')]");
    private final By addressInput = By.xpath("//input[contains(@placeholder, 'Адрес')]");
    private final By metroInput = By.xpath("//input[contains(@placeholder, 'Станция метро')]");
    private final By phoneInput = By.xpath("//input[contains(@placeholder, 'Телефон')]");
    private final By nextButton = By.xpath("//button[contains(text(), 'Далее')]");

    private final By dateInput = By.xpath("//input[contains(@placeholder, 'Когда')]");
    private final By dateSelected = By.className("react-datepicker__day--selected");

    private final By termDropdownRoot = By.className("Dropdown-control");
    private final By termDropdownOption = By.className("Dropdown-option");

    private final By colorBlack = By.id("black");
    private final By colorGrey = By.id("grey");

    private final By commentInput = By.xpath("//input[contains(@placeholder, 'Комментарий')]");

    // Кнопка "Заказать" именно в форме (в шапке сайта есть ещё одна кнопка с таким же текстом)
    private final By orderButton = By.xpath("//div[contains(@class, 'Order_Buttons')]/button[text()='Заказать']");

    // Кнопка "Да" есть только в модальном окне подтверждения
    private final By acceptOrderButton = By.xpath("//button[text()='Да']");
    private final By newOrderSuccessMessage = By.xpath("//div[contains(text(), 'Заказ оформлен')]");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 15);
    }

    private void waitForElementLoad(By elementToLoad) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(elementToLoad));
    }

    public void setName(String name) { driver.findElement(nameInput).sendKeys(name); }
    public void setSurname(String surname) { driver.findElement(surnameInput).sendKeys(surname); }
    public void setAddress(String address) { driver.findElement(addressInput).sendKeys(address); }
    public void setPhone(String phone) { driver.findElement(phoneInput).sendKeys(phone); }
    public void setComment(String comment) { driver.findElement(commentInput).sendKeys(comment); }

    public void setMetro(String metro) {
        WebElement metroField = wait.until(ExpectedConditions.presenceOfElementLocated(metroInput));
        metroField.click();
        metroField.clear();
        metroField.sendKeys(metro);

        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        try {
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(), '" + metro + "')]")));
            option.click();
        } catch (Exception e) {
            WebElement freshMetroField = wait.until(ExpectedConditions.presenceOfElementLocated(metroInput));
            freshMetroField.sendKeys(Keys.ARROW_DOWN);
            freshMetroField.sendKeys(Keys.ENTER);
        }

        WebElement finalMetroField = wait.until(ExpectedConditions.presenceOfElementLocated(metroInput));
        finalMetroField.sendKeys(Keys.TAB);
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public void clickNextButton() {
        driver.findElement(nextButton).click();
        waitForElementLoad(dateInput); // Гарантия перехода на шаг 2
    }

    public void setDate(String date) {
        WebElement dateField = wait.until(ExpectedConditions.elementToBeClickable(dateInput));
        dateField.click();
        dateField.clear();
        dateField.sendKeys(date);
        try {
            WebElement selectedDate = wait.until(ExpectedConditions.elementToBeClickable(dateSelected));
            selectedDate.click();
        } catch (Exception e) {
            dateField.sendKeys(Keys.ESCAPE);
            dateField.sendKeys(Keys.TAB);
        }
    }

    public void setTerm(String termToChoose) {
        driver.findElement(termDropdownRoot).click();
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        chooseElementFromDropdown(termDropdownOption, termToChoose);
    }

    public void setColor(String colorToChoose) {
        By colorLocator = colorToChoose.toLowerCase().contains("черн") ? colorBlack : colorGrey;
        driver.findElement(colorLocator).click();
    }

    /**
     * Нажимает "Заказать" в форме, проверяет, что модальное окно открылось,
     * и нажимает "Да". Ошибки не скрываются: из-за известного бага тест должен упасть
     * на нажатии "Да" либо на проверке сообщения об успешном заказе.
     */
    public void makeOrder() {
        WebElement orderBtn = wait.until(ExpectedConditions.elementToBeClickable(orderButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", orderBtn);
        orderBtn.click();

        // Модальное окно должно открыться, иначе тест падает здесь с понятным сообщением
        try {
            waitForElementLoad(acceptOrderButton);
        } catch (TimeoutException e) {
            throw new AssertionError("Модальное окно подтверждения не открылось после нажатия 'Заказать'", e);
        }

        // Нажатие "Да": здесь проявляется известный баг
        wait.until(ExpectedConditions.elementToBeClickable(acceptOrderButton)).click();
    }

    /**
     * Возвращает текст успеха или пустую строку, если сообщение не появилось (из-за бага).
     */
    public String getNewOrderSuccessMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(newOrderSuccessMessage)).getText();
        } catch (Exception e) {
            return ""; // Возвращаем пустую строку, чтобы тест упал с понятным AssertionError
        }
    }

    private void chooseElementFromDropdown(By dropdownElements, String elementToChoose) {
        List<WebElement> elementsFiltered = driver.findElements(dropdownElements);
        for (WebElement element : elementsFiltered) {
            if (element.getText().contains(elementToChoose)) {
                element.click();
                break;
            }
        }
    }
}
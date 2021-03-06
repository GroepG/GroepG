package be.kdg.spacecrack.seleniumtests;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;

/**
 * Created by Arno on 19/02/14.
 */
public class SeleniumChangeLanguage extends SeleniumBaseTestCase {

    @Test
    public void ChangeLanguage_OK() throws InterruptedException {
        login();
        WebDriverWait wait = new WebDriverWait(driver, 10);

        WebElement btnEngels = driver.findElement(By.name("english"));

        WebElement btnNederlands = driver.findElement(By.name("dutch"));
        WebElement btnnewgame = driver.findElement(By.name("createGame"));

        btnNederlands.click();
        wait.until(ExpectedConditions.visibilityOf(btnEngels));
        wait.until(ExpectedConditions.visibilityOf(btnnewgame));
        wait.until(ExpectedConditions.textToBePresentInElement(By.name("createGame"), "Nieuw Spel"));
        assertEquals("Nieuw Spel", btnnewgame.getText());

        btnEngels.click();
        wait.until(ExpectedConditions.textToBePresentInElement(By.name("createGame"), "New Game"));
        assertEquals("New Game", btnnewgame.getText());
    }
}

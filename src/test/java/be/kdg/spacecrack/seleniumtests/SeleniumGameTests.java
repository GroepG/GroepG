package be.kdg.spacecrack.seleniumtests;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumGameTests extends  SeleniumBaseTestCase {

    @Test
    public void testNavigateToCreateGamePage() throws Exception {
         navigateToNewGame();


    }

    protected void navigateToNewGame() throws InterruptedException {
        login();
        WebDriverWait wait = new WebDriverWait(driver, 10);

        WebElement newgame = driver.findElement(By.name("createGame"));
        wait.until(ExpectedConditions.visibilityOf(newgame));
        newgame.click();
        WebElement btnFind = driver.findElement(By.name("btnFind"));
        wait.until(ExpectedConditions.visibilityOf(btnFind));

        WebElement txtGameName = driver.findElement(By.name("gameName"));
        WebElement txtSearchString = driver.findElement(By.name("txtSearchString"));
        txtGameName.sendKeys("$/[].#");

        txtSearchString.sendKeys("op");

        btnFind.click();

        WebElement btnInvite = driver.findElement(By.name("btnInvite"));
        wait.until(ExpectedConditions.visibilityOf(btnInvite));



    }
}

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static String getUserRequest(){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String request=null;
        try{
            request = bufferedReader.readLine();
        }
        catch (IOException e){
            System.out.println(e);
        }
        finally {
            try{
                bufferedReader.close();
            }
            catch (IOException e){
                System.out.println(e);
            }
        }
        return request;
    }
    public static ArrayList<String> getHeaders(String request){
        ArrayList<String> headerList= new ArrayList<>();
        WebDriver driver = new FirefoxDriver();
        WebDriver localDriver = new FirefoxDriver();

        driver.get("http://www.google.com");
        WebElement form =(new WebDriverWait(driver, 2000))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//input[@name='q']")));
        form.sendKeys(request);
        WebElement button =(new WebDriverWait(driver, 2000))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//button[@type='submit']")));
        button.click();
        List<WebElement> webElementList = (new WebDriverWait(driver, 2000))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(".//li[@class='g']/div/h3/a")));
        for (WebElement webElement : webElementList){
            String href=webElement.getAttribute("href");
            localDriver.get(href);
            headerList.add(localDriver.getTitle());
        }
        localDriver.close();
        driver.close();
        //i've met some crashes when i quit from localDrive.
        //This url describes the problem and offers some solutions with try catch block
        // https://code.google.com/p/selenium/issues/detail?id=7506
        try {
            Thread.sleep(3000);
            localDriver.quit();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        try {
            driver.quit();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return headerList;
    }
    public static void main(String[] args){
        System.out.println("Enter request:");
        String request = getUserRequest();
        ArrayList<String> headerList = getHeaders(request);
        System.out.println("Headers from the first page links:");
        int i=1;
        for (String string : headerList){
            System.out.println(i++ + " "+string);
        }
    }
}

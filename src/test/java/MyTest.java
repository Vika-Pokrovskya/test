import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.api.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
//import static sun.awt.shell.Win32ShellFolderManager2.personal;

public class MyTest {
    private Playwright playwright;
    private Browser browser;
    private Page page;


    @BeforeEach
    public void visitSait() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(500)); // Добавляем задержку для наглядности
        page = browser.newPage();
        page.navigate("https://sofrino.ru/", new Page.NavigateOptions()
                .setWaitUntil(WaitUntilState.NETWORKIDLE)); // Ждем полной загрузки
    }

    @AfterEach
    public void endTest() {
        if (page != null) page.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @Test
    @DisplayName("Вход в ЛК")
    public void loginLK() {
        Locator login=page.locator("a[href='/login/']").first();
        login.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        assertThat(login).isVisible();
        login.click(new Locator.ClickOptions().setTimeout(10000).setForce(true));
        System.out.println("Текущий URL: " + page.url());

        Locator email=page.locator("xpath=//*[@id=\"auth\"]/div[1]/form/div[2]/div/input");
        Locator password=page.locator("input[type='password']").first();
        Locator loginButton= page.locator("button.btn-light", new Page.LocatorOptions().setHasText("Войти"));
        assertThat(email).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(500));
        assertThat(password).isVisible();
        assertThat(loginButton).isVisible();
        email.fill("pokrovskya125@gmail.com");
        password.fill("admin1234.");
        loginButton.click();
    }

    @Test
    @DisplayName("Просмотр каталога")
    public void viewCatalog() {
        Locator katalog=page.locator(".topmidline-catalog-btn");
        assertThat(katalog).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(500));
        katalog.click(new Locator.ClickOptions().setTimeout(10000).setForce(true));

        Locator allMenuItems = page.locator(".hamb-menu__subcatmain");
        int count = allMenuItems.count()-3;

        for (int i = 0; i < count; i++) {
            Locator currentItem = allMenuItems.nth(i);
            currentItem.isVisible();
        }
        //нажатие на кнопки показать категории
        for(int i = 1; i < count; i++) {
            Locator currentItem = allMenuItems.nth(i);
            currentItem.click();
        }
        Locator allProduct = page.locator("a[href='/products/ikonostasy']").first();
        allProduct.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        assertThat(allProduct).isVisible();
        allProduct.click(new Locator.ClickOptions().setTimeout(10000).setForce(true));
        Locator product = page.locator(".shop-menu__link >> text='Иконы'").first();
        product.isVisible();
        product.click();
    }
    @Test
    @DisplayName("Добавление товара в избранные")
    public void addToLiked() {
        Locator katalog = page.locator(".topmidline-catalog-btn");
        assertThat(katalog).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(500));
        katalog.click(new Locator.ClickOptions().setTimeout(10000).setForce(true));

        Locator allProduct = page.locator("a[href='/products/ikonostasy']").first();
        allProduct.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        assertThat(allProduct).isVisible();
        allProduct.click(new Locator.ClickOptions().setTimeout(10000).setForce(true));

        Locator product = page.locator(".shop-menu__link >> text='Церковная утварь'").first();
        product.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        product.click();

        Locator productCard = page.locator("div.product").first();
        productCard.hover();

        Locator likeButton = page.locator("div.product__like[data-product-id='311']");
        likeButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        likeButton.scrollIntoViewIfNeeded();
        likeButton.hover();
        likeButton.click();

        Locator likePageButton = page.locator("a[href='/products/liked']").first();
        likePageButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        assertThat(likePageButton).isVisible();
        likePageButton.click(new Locator.ClickOptions().setTimeout(10000).setForce(true));

    }
    @Test
    @DisplayName("Добавление товара в корзину")
    public void addToBasket() {
        Locator katalog=page.locator(".topmidline-catalog-btn");
        //видимость кнопки "Каталог" и нажатие на нее
        assertThat(katalog).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(500));
        katalog.click(new Locator.ClickOptions().setTimeout(10000).setForce(true));

        Locator allProduct = page.locator("a[href='/products/ikonostasy']").first();
        allProduct.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        assertThat(allProduct).isVisible();
        allProduct.click(new Locator.ClickOptions().setTimeout(10000).setForce(true));
        Locator product = page.locator(".shop-menu__link >> text='Церковная утварь'").first();
        product.isVisible();
        product.click();

        assertThat(page.locator("xpath=/html/body/div[1]/div[5]/div/div/div/div/div[3]/div[2]/div/div/div[1]/div")).isVisible();
        page.locator("a.product-incart-btn >> text='подробнее'").first().click();

        page.locator("button[data-product-id='311'] >> text='в корзину'").isVisible();
        page.locator("button[data-product-id='311'] >> text='в корзину'").click();

        Locator korzina = page.locator("a[href='/cart/']").first();
        korzina.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        assertThat(korzina).isVisible();
        korzina.click(new Locator.ClickOptions().setTimeout(10000).setForce(true));

        page.locator("a[href*='nabor-svyaschennika-311']").first().isVisible();

    }
    @Test
    @DisplayName("cv")
    public void change() {
        loginLK();
        page.locator("a.shop-menu__link >> text='Персональные данные'").isVisible();
        page.locator("a.shop-menu__link >> text='Персональные данные'").click();

        page.locator("#personal div").filter(new Locator.FilterOptions().setHasText("Телефон: E-mail:")).getByRole(AriaRole.TEXTBOX).first().isVisible();
        page.locator("xpath=//form/div[5]/div/input").first().click();


        page.locator("input[type='text'].form-control").first().fill("мяууу");

        page.locator("button >> text='сохранить'").first().isVisible();
        page.locator("button >> text='сохранить'").first().click();

    }
}

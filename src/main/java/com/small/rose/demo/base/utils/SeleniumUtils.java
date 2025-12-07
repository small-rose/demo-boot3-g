package com.small.rose.demo.base.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Project: demo-boot3-m
 * @Author: 张小菜
 * @Description: [ SeleniumUtils ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/30 周日 0:49
 * @Version: v1.0
 */
public class SeleniumUtils {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor jsExecutor;
    private Actions actions;
    private Robot robot;

    private BrowserConfig config ;

    private String originalWindowHandle;
    /**
     * 浏览器配置选项
     */
    public static class BrowserConfig {
        private String proxyHost;
        private int proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private boolean headless = false;
        private boolean imagesDisabled = true;
        private int timeoutSeconds = 30;
        private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
        private String downloadDir ;


        // 构造方法和getter/setter
        public BrowserConfig proxy(String host, int port) {
            this.proxyHost = host;
            this.proxyPort = port;
            return this;
        }

        public BrowserConfig proxyAuth(String username, String password) {
            this.proxyUsername = username;
            this.proxyPassword = password;
            return this;
        }

        public BrowserConfig headless(boolean headless) {
            this.headless = headless;
            return this;
        }

        public BrowserConfig disableImages(boolean disable) {
            this.imagesDisabled = disable;
            return this;
        }

        public BrowserConfig timeout(int seconds) {
            this.timeoutSeconds = seconds;
            return this;
        }

        public BrowserConfig userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public BrowserConfig setDownloadDir(String downloadDir){
            this.downloadDir = downloadDir;
            return this;
        }

        public String getDownloadDir(){
            return downloadDir;
        }

        // Getters
        public String getProxyHost() { return proxyHost; }
        public int getProxyPort() { return proxyPort; }
        public String getProxyUsername() { return proxyUsername; }
        public String getProxyPassword() { return proxyPassword; }
        public boolean isHeadless() { return headless; }
        public boolean isImagesDisabled() { return imagesDisabled; }
        public int getTimeoutSeconds() { return timeoutSeconds; }
        public String getUserAgent() { return userAgent; }
    }

    /**
     * 初始化浏览器
     */
    public void initBrowser(BrowserConfig config){
        try{
            // 创建下载目录
            new File(config.getDownloadDir()).mkdirs();

            ChromeOptions options = new ChromeOptions();

            // ==================== 基本浏览器配置 ====================
            // 禁用自动化控制特征，防止网站检测到Selenium
            options.addArguments("--disable-blink-features=AutomationControlled");

            // 禁用浏览器扩展，避免额外功能干扰
            options.addArguments("--disable-extensions");

            // 禁用沙盒模式，提高稳定性（Linux环境需要）
            //options.addArguments("--no-sandbox");

            // 禁用/dev/shm使用，避免内存不足问题
            options.addArguments("--disable-dev-shm-usage");

            // ==================== 反爬虫高级配置 ====================
            // 禁用密码保存提示，避免弹窗干扰
            options.addArguments("--disable-save-password-bubble");

            // 禁用自动填写功能
            options.addArguments("--disable-autofill-keyboard-accessory-view");

            // 禁用翻译提示
            options.addArguments("--disable-translate");

            // 禁用信息栏
            options.addArguments("--disable-infobars");

            // 禁用弹窗阻止，避免某些网站功能异常
            options.addArguments("--disable-popup-blocking");

            // 禁用组件更新提示
            options.addArguments("--disable-component-update");

            // 禁用后台网络跟踪
            options.addArguments("--disable-background-networking");

            // 禁用默认应用检查
            options.addArguments("--disable-default-apps");

            // 禁用客户端阶段化
            options.addArguments("--disable-client-side-phasing");

            // 设置语言为中文
            options.addArguments("--lang=zh-CN");

            // 设置Accept-Language头
            options.addArguments("--accept-lang=zh-CN,zh;q=0.9,en;q=0.8");

            // 禁用WebRTC泄露真实IP
            options.addArguments("--disable-webrtc");

            // 禁用Flash插件
            options.addArguments("--disable-flash");

            // 禁用GPU加速（在某些环境下提高稳定性）
            options.addArguments("--disable-gpu");

            // 禁用软件渲染列表
            options.addArguments("--disable-software-rasterizer");

            // 禁用远程字体
            options.addArguments("--disable-remote-fonts");

            // 禁用后台定时器限制
            options.addArguments("--disable-background-timer-throttling");

            // 禁用渲染器后台化
            options.addArguments("--disable-renderer-backgrounding");

            // 设置窗口大小（避免无头模式被检测）
            options.addArguments("--window-size=1024,768");

            // 禁用密码管理器
            options.addArguments("--disable-password-manager");

            // 禁用密码生成器
            options.addArguments("--disable-password-generation");

            // ==================== 实验性选项配置 ====================
            // 排除自动化相关的开关
            options.setExperimentalOption("excludeSwitches",
                    new String[]{"enable-automation",           // 隐藏自动化控制标志
                            "enable-logging",              // 禁用日志记录
                            "load-extension"               // 禁用扩展加载
                    });

            // 基本配置
            options.setExperimentalOption("useAutomationExtension", false);

            // 用户代理
            options.addArguments("--user-agent=" + config.getUserAgent());

            // 无头模式
            if(config.isHeadless()){
                options.addArguments("--headless=new"); // Selenium 4的新语法
            }

            // 禁用图片
            if(config.isImagesDisabled()){
                options.addArguments("--blink-settings=imagesEnabled=false");
                options.addArguments("--disable-images");
            }

            // 代理配置
            if(config.getProxyHost() != null && config.getProxyPort() > 0){
                String proxySetting = config.getProxyHost() + ":" + config.getProxyPort();
                if(config.getProxyUsername() != null && config.getProxyPassword() != null){
                    // 需要认证的代理（需要额外插件处理）
                    System.out.println("注意：带认证的代理需要额外配置");
                }
                options.addArguments("--proxy-server=" + proxySetting);
            }

            // 创建驱动实例
            this.driver = new ChromeDriver(options);
            this.originalWindowHandle = driver.getWindowHandle();
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.getTimeoutSeconds()));
            this.jsExecutor = (JavascriptExecutor) driver;
            this.actions = new Actions(driver);
            this.robot = new Robot();
            this.config = config;
            // 执行反检测脚本
            jsExecutor.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
        }catch(Exception e){
            throw new RuntimeException("浏览器初始化失败: " + e.getMessage(), e);
        }
    }

    /**
     * 访问URL
     */
    public boolean navigateTo(String url) {
        try {
            driver.get(url);

            String pageTitle = driver.getTitle();
            String currentUrl = driver.getCurrentUrl();
            System.out.println("页面tableIN: " + pageTitle);
            System.out.println("页面标题: " + pageTitle);
            System.out.println("当前URL: " + currentUrl);

            if (driver.getPageSource().contains("404") ||
                    driver.getPageSource().contains("Error") ||
                    driver.getPageSource().contains("Not Found")) {
                System.err.println("检测到错误页面");
                return false;
            }

            System.out.println("成功访问: " + url);
            return true;
        } catch (Exception e) {
            System.err.println("访问失败: " + url + " - " + e.getMessage());
            throw e;
        }
    }

    /**
     * 在新标签页中打开URL
     */
    public void openNewTab(String url) {
        // 使用JavaScript在新标签页打开URL
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.open('" + url + "');");
        System.out.println("在新标签页打开: " + url);
    }
    /**
     * 通过点击链接在新标签页打开（模拟用户操作）
     */
    public void openNewTabByClick(By linkLocator) {
        // 保存当前窗口句柄
        String currentWindow = driver.getWindowHandle();

        // 点击会在新标签页打开的链接
        WebElement link = driver.findElement(linkLocator);
        link.click();

        // 等待新标签页打开
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("通过点击链接打开新标签页");
    }

    /**
     * 获取所有打开的窗口句柄
     */
    public Set<String> getAllWindowHandles() {
        Set<String> handles = driver.getWindowHandles();
        System.out.println("当前打开的窗口数量: " + handles.size());
        return handles;
    }

    /**
     * 切换到指定索引的标签页
     */
    public void switchToTab(int tabIndex) {
        Set<String> handles = getAllWindowHandles();
        List<String> handlesList = new ArrayList<>(handles);

        if (tabIndex >= 0 && tabIndex < handlesList.size()) {
            String targetHandle = handlesList.get(tabIndex);
            driver.switchTo().window(targetHandle);
            System.out.println("切换到第 " + (tabIndex + 1) + " 个标签页，句柄: " + targetHandle);
        } else {
            System.err.println("标签页索引超出范围: " + tabIndex);
        }
    }

    /**
     * 切换到最新打开的标签页（最后一个句柄）
     */
    public void switchToLatestTab() {
        Set<String> handles = getAllWindowHandles();
        List<String> handlesList = new ArrayList<>(handles);

        if (handlesList.size() > 1) {
            String latestHandle = handlesList.get(handlesList.size() - 1);
            driver.switchTo().window(latestHandle);
            System.out.println("切换到最新标签页，句柄: " + latestHandle);
        }
    }

    /**
     * 切换回原始标签页
     */
    public void switchToOriginalTab() {
        driver.switchTo().window(originalWindowHandle);
        System.out.println("切换回原始标签页，句柄: " + originalWindowHandle);
    }

    /**
     * 关闭当前标签页并切换回指定标签页
     */
    public void closeCurrentTabAndSwitchTo(int targetTabIndex) {
        System.out.println("关闭当前标签页");
        driver.close();

        // 切换回指定标签页
        switchToTab(targetTabIndex);
    }

    /**
     * 获取当前标签页的URL和标题
     */
    public void printCurrentTabInfo() {
        String currentUrl = driver.getCurrentUrl();
        String currentTitle = driver.getTitle();
        String currentHandle = driver.getWindowHandle();

        System.out.println("当前标签页信息:");
        System.out.println("  句柄: " + currentHandle);
        System.out.println("  标题: " + currentTitle);
        System.out.println("  URL: " + currentUrl);
    }

    /**
     * 等待页面加载完成
     */
    public void waitForPageLoad(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    /**
     * 等待元素出现
     */
    public WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public List<WebElement> waitForElements(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    /**
     * 查找元素
     */
    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }

    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }
    /**
     * 查找元素
     */
    public WebElement findElementWait(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public List<WebElement> findElementsWait(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        return driver.findElements(locator);
    }

    /**
     * 获取元素文本
     */
    public String getElementText(By locator) {
        return waitForElement(locator).getText();
    }

    /**
     * 获取元素属性
     */
    public String getElementAttribute(By locator, String attribute) {
        return waitForElement(locator).getAttribute(attribute);
    }

    /**
     * 输入文本
     */
    public void inputText(By locator, String text) {
        WebElement element = waitForElementClickable(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * 点击元素
     */
    public void clickElement(By locator) {
        waitForElementClickable(locator).click();
    }

    /**
     * 执行JavaScript
     */
    public Object executeJavaScript(String script, Object... args) {
        return jsExecutor.executeScript(script, args);
    }

    /**
     * 滚动到元素
     */
    public void scrollToElement(By locator) {

        WebElement imgElement = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        //WebElement element = findElement(locator);
        executeJavaScript("arguments[0].scrollIntoView(true);", imgElement);
    }

    /**
     * 获取页面标题
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * 获取当前URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * 获取页面源代码
     */
    public String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * 截图并保存为Base64
     */
    public String takeScreenshotAsBase64() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }
    /**
     * 获取窗口
     */
    public String getWindow() {
        return driver.getWindowHandle();
    }
    /**
     * 切换窗口
     */
    public void switchToWindow(int index) {
        List<String> windows = new ArrayList<>(driver.getWindowHandles());
        if (index < windows.size()) {
            driver.switchTo().window(windows.get(index));
        }
    }

    /**
     * 安全的元素遍历方法 - 处理动态加载和元素状态变化
     */
    public void safeTraverseElements(By parentLocator) {
        try {
            // 等待父级元素出现
            wait.until(ExpectedConditions.presenceOfElementLocated(parentLocator));

            // 获取元素列表
            List<WebElement> elements = driver.findElements(parentLocator);
            System.out.println("找到 " + elements.size() + " 个元素");

            for (int i = 0; i < elements.size(); i++) {
                try {
                    // 每次循环都重新查找元素，避免StaleElementReferenceException
                    elements = driver.findElements(parentLocator);
                    if (i >= elements.size()) {
                        System.out.println("元素列表已变化，当前索引 " + i + " 超出范围");
                        break;
                    }

                    WebElement currentElement = elements.get(i);

                    // 滚动到元素确保可见
                    scrollToElement(currentElement);

                    // 等待元素可交互
                    wait.until(ExpectedConditions.elementToBeClickable(currentElement));

                    // 检查元素是否有子元素
                    checkAndProcessChildren(currentElement, i);

                } catch (Exception e) {
                    System.err.println("处理第 " + i + " 个元素时出错: " + e.getMessage());
                    // 继续处理下一个元素
                    continue;
                }
            }

        } catch (Exception e) {
            System.err.println("遍历元素失败: " + e.getMessage());
        }
    }

    /**
     * 检查并处理子元素
     */
    private void checkAndProcessChildren(WebElement parentElement, int index) {
        try {
            // 多种方式查找子元素
            List<WebElement> children = parentElement.findElements(By.xpath(".//*"));

            if (children.isEmpty()) {
                // 尝试其他选择器
                children = parentElement.findElements(By.cssSelector("*"));
            }

            System.out.println("元素 " + index + " 的子元素数量: " + children.size());

            if (children.size() > 0) {
                System.out.println("元素 " + index + " 有子元素，进行处理...");
                processChildrenElements(children, index);
            } else {
                System.out.println("元素 " + index + " 没有子元素");
            }

        } catch (Exception e) {
            System.err.println("检查子元素失败: " + e.getMessage());
        }
    }

    /**
     * 处理子元素
     */
    private void processChildrenElements(List<WebElement> children, int parentIndex) {
        for (int j = 0; j < children.size(); j++) {
            try {
                WebElement child = children.get(j);
                String tagName = child.getTagName();
                String text = child.getText().trim();

                System.out.println("  子元素 " + j + ": " + tagName +
                        (text.isEmpty() ? "" : " - 文本: '" + text + "'"));

            } catch (Exception e) {
                System.err.println("处理子元素 " + j + " 失败: " + e.getMessage());
            }
        }
    }

    /**
     * 滚动到元素
     */
    private void scrollToElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(500); // 短暂等待滚动完成
        } catch (Exception e) {
            System.err.println("滚动到元素失败: " + e.getMessage());
        }
    }

    /**
     * 访问测试页面
     */
    public void navigateToTestPage() {
        try {
            // 使用测试页面演示动态加载
            driver.get("https://httpbin.org/html");
            System.out.println("已访问测试页面");
        } catch (Exception e) {
            System.err.println("访问页面失败: " + e.getMessage());
        }
    }

    /**
     * 关闭浏览器
     */
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
            System.out.println("浏览器已关闭");
        }
    }
    /**
     * 获取元素的HTML字符串表示（类似jsoup的toString）
     */
    public String getElementHtmlString(WebElement element) {
        try {
            // 使用JavaScript获取元素的outerHTML
            String html = (String) jsExecutor.executeScript(
                    "return arguments[0].outerHTML;", element);
            return html;
        } catch (Exception e) {
            System.err.println("获取HTML字符串失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 获取元素的简洁HTML表示（只包含标签和关键属性）
     */
    public String getElementSimpleHtml(WebElement element) {
        try {
            // 使用JavaScript获取简化版的HTML
            String simpleHtml = (String) jsExecutor.executeScript(
                    "var el = arguments[0];" +
                            "var tag = el.tagName.toLowerCase();" +
                            "var id = el.id ? ' id=\"' + el.id + '\"' : '';" +
                            "var cls = el.className ? ' class=\"' + el.className + '\"' : '';" +
                            "return '<' + tag + id + cls + '>...';", element);
            return simpleHtml;
        } catch (Exception e) {
            System.err.println("获取简化HTML失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 获取元素的XPath路径
     */
    public String getElementXPath(WebElement element) {
        try {
            String xpath = (String) jsExecutor.executeScript(
                    "function getElementXPath(element) {" +
                            "    if (element.id !== '') return '//*[@id=\"' + element.id + '\"]';" +
                            "    if (element === document.body) return '/html/body';" +
                            "    var ix = 0;" +
                            "    var siblings = element.parentNode.childNodes;" +
                            "    for (var i = 0; i < siblings.length; i++) {" +
                            "        var sibling = siblings[i];" +
                            "        if (sibling === element) return getElementXPath(element.parentNode) + '/' + element.tagName.toLowerCase() + '[' + (ix + 1) + ']';" +
                            "        if (sibling.nodeType === 1 && sibling.tagName === element.tagName) ix++;" +
                            "    }" +
                            "}" +
                            "return getElementXPath(arguments[0]);", element);
            return xpath;
        } catch (Exception e) {
            System.err.println("获取XPath失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 打印元素的详细信息（增强版，包含HTML字符串）
     */
    public void printElementDetails(WebElement element, String elementName) {
        try {
            System.out.println("\n=== 元素详细信息: " + elementName + " ===");

            // 基本属性
            String tagName = element.getTagName();
            String text = element.getText().trim();
            boolean isDisplayed = element.isDisplayed();
            boolean isEnabled = element.isEnabled();
            boolean isSelected = element.isSelected();

            System.out.println("标签名: " + tagName);
            System.out.println("显示文本: '" + text + "'");
            System.out.println("是否可见: " + isDisplayed);
            System.out.println("是否可用: " + isEnabled);
            System.out.println("是否选中: " + isSelected);

            // 新增：类似jsoup的字符串表示
            String htmlString = getElementHtmlString(element);
            String simpleHtml = getElementSimpleHtml(element);
            String xpath = getElementXPath(element);

            System.out.println("完整HTML: " + (htmlString != null ?
                    htmlString.substring(0, Math.min(100, htmlString.length())) + "..." : "null"));
            System.out.println("简化HTML: " + simpleHtml);
            System.out.println("XPath: " + xpath);

            // 位置和尺寸
            System.out.println("位置: " + element.getLocation());
            System.out.println("尺寸: " + element.getSize());

            // 常见属性
            String[] commonAttributes = {"id", "class", "name", "type", "value", "href", "src", "alt", "title"};
            for (String attr : commonAttributes) {
                String value = element.getAttribute(attr);
                if (value != null && !value.trim().isEmpty()) {
                    System.out.println(attr + ": '" + value + "'");
                }
            }

        } catch (Exception e) {
            System.err.println("打印元素详情失败: " + e.getMessage());
        }
    }

    /**
     * 批量打印元素的HTML字符串（类似jsoup的文档结构）
     */
    public void printElementsAsHtmlTree(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            System.out.println("找到 " + elements.size() + " 个元素，以HTML树形式显示:");

            for (int i = 0; i < elements.size(); i++) {
                try {
                    WebElement element = elements.get(i);
                    String simpleHtml = getElementSimpleHtml(element);
                    String xpath = getElementXPath(element);

                    System.out.println("[" + i + "] " + simpleHtml);
                    System.out.println("    XPath: " + xpath);

                    // 如果有子元素，显示子元素数量
                    List<WebElement> children = element.findElements(By.xpath(".//*"));
                    if (!children.isEmpty()) {
                        System.out.println("    子元素: " + children.size() + " 个");
                    }

                } catch (Exception e) {
                    System.err.println("处理元素 " + i + " 失败: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("打印HTML树失败: " + e.getMessage());
        }
    }

    /**
     * 保存元素的完整HTML到文件（类似jsoup的outerHtml）
     */
    public void saveElementHtmlToFile(WebElement element, String filePath) {
        try {
            String html = getElementHtmlString(element);
            if (html != null) {
                // 这里可以添加文件保存逻辑
                System.out.println("元素HTML已获取，长度: " + html.length() + " 字符");
                System.out.println("文件路径: " + filePath);
                // 实际文件保存代码需要根据需求实现
            }
        } catch (Exception e) {
            System.err.println("保存HTML到文件失败: " + e.getMessage());
        }
    }




    /**
     * 方法1：下载单张图片并自定义文件名
     * @param imageLocator 图片定位器
     * @param customFileName 自定义文件名（可包含路径）
     * @return 下载是否成功
     */
    public boolean downloadSingleImage(By imageLocator, String customFileName) {
        try {
            WebElement imgElement = wait.until(ExpectedConditions.presenceOfElementLocated(imageLocator));
            String imageUrl = getImageUrl(imgElement);

            if (imageUrl != null && !imageUrl.isEmpty()) {
                return downloadImageFromUrl(imageUrl, customFileName);
            }
            return false;

        } catch (Exception e) {
            System.err.println("下载单张图片失败: " + e.getMessage());
            return false;
        }
    }
    /**
     * 方法1：下载单张图片并自定义文件名
     * @param imgElement 图片定位器
     * @param customFileName 自定义文件名（可包含路径）
     * @return 下载是否成功
     */
    public boolean downloadSingleImage(WebElement imgElement, String customFileName) {
        try {
             String imageUrl = getImageUrl(imgElement);

            if (imageUrl != null && !imageUrl.isEmpty()) {
                return downloadImageFromUrl(imageUrl, customFileName);
            }
            return false;

        } catch (Exception e) {
            System.err.println("下载单张图片失败: " + e.getMessage());
            return false;
        }
    }
    /**
     * 方法2：批量下载图片并使用自定义命名规则
     * @param imageLocator 图片定位器
     * @param baseSaveDir 保存目录
     * @param fileNamePattern 文件名模式，支持占位符：{index} {timestamp} {originalName}
     * @return 成功下载的文件列表
     */
    public List<String> downloadMultipleImages(By imageLocator, String baseSaveDir,
            String fileNamePattern) {
        List<String> savedFiles = new ArrayList<>();

        try {
            List<WebElement> images = driver.findElements(imageLocator);
            System.out.println("发现 " + images.size() + " 张图片");

            // 创建保存目录
            new File(baseSaveDir).mkdirs();

            for (int i = 0; i < images.size(); i++) {
                try {
                    WebElement img = images.get(i);
                    String imageUrl = getImageUrl(img);

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        // 生成自定义文件名
                        String fileName = generateCustomFileName(fileNamePattern, i, imageUrl);
                        String savePath = baseSaveDir + File.separator + fileName;

                        if (downloadImageFromUrl(imageUrl, savePath)) {
                            savedFiles.add(savePath);
                            System.out.println("成功下载: " + fileName);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("下载第 " + (i + 1) + " 张图片失败: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("批量下载失败: " + e.getMessage());
        }

        return savedFiles;
    }

    /**
     * 方法3：根据图片属性智能命名
     * @param imageLocator 图片定位器
     * @param baseSaveDir 保存目录
     * @param useAltAsName 是否使用alt属性作为文件名
     * @param useDataSrc 是否优先使用data-src属性
     * @return 成功下载的文件列表
     */
    public List<String> downloadImagesWithSmartNaming(By imageLocator, String baseSaveDir,String subPath,
            boolean useAltAsName, boolean useDataSrc) {
        List<String> savedFiles = new ArrayList<>();

        try {
            List<WebElement> images = driver.findElements(imageLocator);
            new File(baseSaveDir).mkdirs();

            for (int i = 0; i < images.size(); i++) {
                try {
                    WebElement img = images.get(i);
                    String imageUrl = getImageUrl(img);

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        // 智能生成文件名
                        String fileName = generateSmartFileName(img, i, imageUrl, useAltAsName);
                        String savePath = baseSaveDir + File.separator + subPath + File.separator + fileName;

                        if (downloadImageFromUrl(imageUrl, savePath)) {
                            savedFiles.add(savePath);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("智能命名下载失败[" + i + "]: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("智能命名批量下载失败: " + e.getMessage());
        }

        return savedFiles;
    }

    /**
     * 生成自定义文件名
     */
    private String generateCustomFileName(String pattern, int index, String imageUrl) {
        String fileName = pattern
                .replace("{index}", String.format("%03d", index))
                .replace("{timestamp}", String.valueOf(System.currentTimeMillis()));

        // 从URL提取原始文件名
        String originalName = extractFileNameFromUrl(imageUrl);
        if (originalName != null) {
            fileName = fileName.replace("{originalName}", originalName);
        }

        // 确保文件扩展名
        String extension = getFileExtension(imageUrl);
        if (!fileName.toLowerCase().endsWith(extension)) {
            fileName += "." + extension;
        }

        return fileName;
    }

    /**
     * 智能生成文件名
     */
    private String generateSmartFileName(WebElement img, int index, String imageUrl, boolean useAlt) {
        String baseName = "image_" + String.format("%03d", index);

        // 尝试使用alt属性作为文件名
        if (useAlt) {
            String altText = img.getAttribute("alt");
            if (altText != null && !altText.trim().isEmpty()) {
                baseName = sanitizeFileName(altText.trim());
            }
        }

        // 尝试使用其他属性
        String title = img.getAttribute("title");
        if (title != null && !title.trim().isEmpty() && baseName.startsWith("image_")) {
            baseName = sanitizeFileName(title.trim());
        }

        // 添加时间戳避免重名
        baseName += "_" + System.currentTimeMillis();

        String extension = getFileExtension(imageUrl);
        return baseName + "." + extension;
    }

    /**
     * 从URL提取文件名
     */
    private String extractFileNameFromUrl(String url) {
        try {
            if (url.contains("/")) {
                String[] parts = url.split("/");
                String lastPart = parts[parts.length - 1];
                // 移除查询参数
                int questionMark = lastPart.indexOf('?');
                if (questionMark > 0) {
                    lastPart = lastPart.substring(0, questionMark);
                }
                return lastPart;
            }
        } catch (Exception e) {
            // 忽略错误
        }
        return null;
    }

    /**
     * 清理文件名（移除非法字符）
     */
    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[\\\\/:*?\"<>|]", "_").replaceAll("\\s+", "_");
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String url) {
        try {
            if (url.contains(".")) {
                String[] parts = url.split("\\?")[0].split("\\.");
                if (parts.length > 1) {
                    String ext = parts[parts.length - 1].toLowerCase();
                    // 常见图片格式
                    if (ext.matches("(jpg|jpeg|png|gif|bmp|webp|svg)")) {
                        return ext;
                    }
                }
            }
        } catch (Exception e) {
            // 忽略错误
        }
        return "jpg"; // 默认扩展名
    }

    /**
     * 从URL下载图片
     */
    private boolean downloadImageFromUrl(String imageUrl, String savePath) {
        System.out.println("开始下载: " + imageUrl);
        if(config.getProxyHost() != null && config.getProxyPort() > 0){
            System.out.println("使用代理下载: " + config.getProxyHost() + ":" + config.getProxyPort());
        }
        try{
            File file = new File(savePath);
            file.getParentFile().mkdirs();

            URL url = new URL(imageUrl);
            InputStream in;

            // 使用代理创建连接
            if(config.getProxyHost() != null && config.getProxyPort() > 0){
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getProxyHost(), config.getProxyPort()));
                in = url.openConnection(proxy).getInputStream();
            }else{
                in = url.openStream();
            }

            try(OutputStream out = new FileOutputStream(savePath)){
                byte[] buffer = new byte[4096];
                int bytesRead;
                long totalBytes = 0;

                while((bytesRead = in.read(buffer)) != -1){
                    out.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                }

                System.out.println("下载完成: " + savePath + " (" + totalBytes + " 字节)");

                File downloadedFile = new File(savePath);
                if(downloadedFile.exists() && downloadedFile.length() > 0){
                    System.out.println("文件验证成功");
                    return true;
                }else{
                    System.err.println("文件验证失败");
                    return false;
                }
            }finally{
                if(in != null){
                    in.close();
                }
            }
        }catch(Exception e){
            System.err.println("下载失败 " + imageUrl + " -> " + e.getMessage());
            return false;
        }
    }



    /**
     * 判断元素是否存在（不抛出异常）
     */
    public boolean isElementPresent(By locator) {
        try {
            // 使用findElements，如果找不到返回空列表而不是抛出异常
            List<WebElement> elements = driver.findElements(locator);
            return !elements.isEmpty();
        } catch (Exception e) {
            // 捕获任何可能的异常，返回false
            return false;
        }
    }

    /**
     * 判断元素是否可见且可点击
     */
    public boolean isElementVisibleAndClickable(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element != null && element.isDisplayed() && element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 安全点击：元素存在才点击，不存在则忽略
     */
    public boolean safeClick(By locator) {
        return safeClick(locator, false);
    }

    /**
     * 安全点击：支持等待元素可点击
     */
    public boolean safeClick(By locator, boolean waitForClickable) {
        try {
            if (waitForClickable) {
                // 等待元素可点击
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                if (element != null) {
                    element.click();
                    System.out.println("成功点击元素: " + locator);
                    return true;
                }
            } else {
                // 只检查元素存在性
                if (isElementPresent(locator)) {
                    WebElement element = driver.findElement(locator);
                    element.click();
                    System.out.println("成功点击元素: " + locator);
                    return true;
                }
            }
            System.out.println("元素不存在或不可点击: " + locator);
            return false;
        } catch (StaleElementReferenceException e) {
            // 处理元素过时的情况
            System.out.println("元素已过时，尝试重新查找: " + locator);
            return retryClick(locator);
        } catch (ElementClickInterceptedException e) {
            // 处理元素被遮挡的情况
            System.out.println("元素被遮挡，尝试JavaScript点击: " + locator);
            return clickWithJavaScript(locator);
        } catch (Exception e) {
            System.out.println("点击元素失败: " + locator + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * 重试点击（处理StaleElement异常）
     */
    private boolean retryClick(By locator) {
        try {
            // 短暂等待后重试
            Thread.sleep(1000);
            if (isElementPresent(locator)) {
                WebElement element = driver.findElement(locator);
                element.click();
                System.out.println("重试点击成功: " + locator);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("重试点击失败: " + locator);
            return false;
        }
    }

    /**
     * 使用JavaScript点击（处理遮挡问题）
     */
    private boolean clickWithJavaScript(By locator) {
        try {
            if (isElementPresent(locator)) {
                WebElement element = driver.findElement(locator);
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", element);
                System.out.println("JavaScript点击成功: " + locator);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("JavaScript点击失败: " + locator);
            return false;
        }
    }

    /**
     * 安全发送文本：元素存在才输入
     */
    public boolean safeSendKeys(By locator, String text) {
        try {
            if (isElementPresent(locator)) {
                WebElement element = driver.findElement(locator);
                element.clear();
                element.sendKeys(text);
                System.out.println("成功输入文本: " + text);
                return true;
            }
            System.out.println("元素不存在，无法输入文本: " + locator);
            return false;
        } catch (Exception e) {
            System.out.println("输入文本失败: " + locator + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * 等待元素出现（带超时）
     */
    public boolean waitForElementPresent(By locator, int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            customWait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            System.out.println("等待元素超时: " + locator);
            return false;
        }
    }

    /**
     * 批量安全点击多个可能存在的元素
     */
    public int safeClickMultiple(List<By> locators) {
        int successCount = 0;
        for (By locator : locators) {
            if (safeClick(locator)) {
                successCount++;
            }
        }
        System.out.println("批量点击完成: " + successCount + "/" + locators.size() + " 成功");
        return successCount;
    }


    /**
     * 在新标签页中打开图片URL并处理
     */
    public boolean openImageInNewTab(String imageUrl, String savePath) {
        String originalWindow = driver.getWindowHandle();

        try {
            // 在新标签页中打开图片URL
            jsExecutor.executeScript("window.open(arguments[0]);", imageUrl);

            // 等待新标签页打开
            //Thread.sleep(2000);

            // 切换到新标签页
            Set<String> windowHandles = driver.getWindowHandles();
            for (String windowHandle : windowHandles) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }

            // 等待页面加载
            waitForPageLoad(1);

            // 获取当前页面的图片URL（可能重定向后的URL）
            String currentUrl = driver.getCurrentUrl();
            System.out.println("新标签页URL: " + currentUrl);

            // 尝试从新页面获取图片元素
            List<WebElement> images = driver.findElements(By.tagName("img"));
            String finalImageUrl = currentUrl;

            if (!images.isEmpty()) {
                // 如果页面中有图片元素，使用第一个图片的URL
                WebElement img = images.get(0);
                String imgSrc = img.getAttribute("src");
                if (imgSrc != null && !imgSrc.isEmpty()) {
                    finalImageUrl = imgSrc;
                    System.out.println("从img标签获取URL: " + finalImageUrl);
                }
            }

            // 下载图片
            boolean success = downloadImageFromUrlWithProxy(finalImageUrl, savePath);

            // 关闭当前标签页
            driver.close();

            // 切换回原始窗口
            driver.switchTo().window(originalWindow);

            return success;

        } catch (Exception e) {
            System.err.println("在新标签页处理图片失败: " + e.getMessage());
            // 确保切换回原始窗口
            try {
                driver.switchTo().window(originalWindow);
            } catch (Exception ex) {
                // 忽略切换窗口的异常
            }
            return false;
        }
    }

    /**
     * 批量在新标签页中打开并下载图片
     */
    public List<String> downloadImagesInNewTabs(By imageLocator, String saveDir) {
        List<String> savedFiles = new ArrayList<>();
        String originalWindow = driver.getWindowHandle();

        try {
            List<WebElement> imageElements = driver.findElements(imageLocator);
            System.out.println("找到 " + imageElements.size() + " 张图片");

            new File(saveDir).mkdirs();

            for (int i = 0; i < imageElements.size(); i++) {
                try {
                    // 每次重新查找元素，避免状态变化
                    imageElements = driver.findElements(imageLocator);
                    if (i >= imageElements.size()) {
                        break;
                    }

                    WebElement imgElement = imageElements.get(i);
                    String imageUrl = getImageUrl(imgElement);

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        String extension = getFileExtension(imageUrl);
                        String fileName = "image_" + i + "_" + System.currentTimeMillis() + "." + extension;
                        String savePath = saveDir + File.separator + fileName;

                        System.out.println("处理第 " + (i + 1) + " 张图片: " + imageUrl);

                        // 在新标签页中打开并下载
                        if (openImageInNewTab(imageUrl, savePath)) {
                            savedFiles.add(savePath);
                            System.out.println("成功下载第 " + (i + 1) + " 张图片");
                        } else {
                            System.err.println("下载第 " + (i + 1) + " 张图片失败");
                        }

                        // 短暂等待，避免请求过于频繁
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    System.err.println("处理第 " + (i + 1) + " 张图片时出错: " + e.getMessage());
                    // 确保回到主窗口继续处理下一个
                    try {
                        driver.switchTo().window(originalWindow);
                    } catch (Exception ex) {
                        // 忽略异常
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("批量下载失败: " + e.getMessage());
        } finally {
            // 确保最终切换回主窗口
            try {
                driver.switchTo().window(originalWindow);
            } catch (Exception e) {
                // 忽略异常
            }
        }

        return savedFiles;
    }

    /**
     * 安全的右键在新标签页打开（模拟用户操作）
     */
    public boolean openInNewTabWithRightClick(By elementLocator, String savePath) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(elementLocator));

            // 获取链接URL
            String url = element.getAttribute("href");
            if (url == null || url.isEmpty()) {
                // 如果不是链接，尝试获取其他可点击元素的URL
                url = (String) jsExecutor.executeScript(
                        "var elem = arguments[0];" +
                                "while(elem && elem !== document) {" +
                                "    if(elem.href) return elem.href;" +
                                "    elem = elem.parentElement;" +
                                "}" +
                                "return '';", element);
            }

            if (url != null && !url.isEmpty()) {
                return openImageInNewTab(url, savePath);
            }

            return false;
        } catch (Exception e) {
            System.err.println("右键新标签页打开失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取图片URL（增强版）
     */
    private String getImageUrl(WebElement imgElement) {
        try {
            String[] attributes = {"src", "data-src", "data-original", "srcset", "data-srcset", "data-url"};

            for (String attr : attributes) {
                String url = imgElement.getAttribute(attr);
                if (url != null && !url.trim().isEmpty()) {
                    url = url.trim();

                    // 处理srcset（取第一个URL）
                    if (attr.equals("srcset") || attr.equals("data-srcset")) {
                        String[] parts = url.split("\\s+");
                        for (String part : parts) {
                            if (part.startsWith("http")) {
                                url = part;
                                break;
                            }
                        }
                    }

                    // 确保URL完整
                    if (url.startsWith("//")) {
                        url = "https:" + url;
                    } else if (url.startsWith("/")) {
                        String currentUrl = driver.getCurrentUrl();
                        URL baseUrl = new URL(currentUrl);
                        url = baseUrl.getProtocol() + "://" + baseUrl.getHost() + url;
                    }

                    return url;
                }
            }
        } catch (Exception e) {
            System.err.println("提取图片URL失败: " + e.getMessage());
        }
        return null;
    }

    /**
     * 使用代理下载图片
     */
    private boolean downloadImageFromUrlWithProxy(String imageUrl, String savePath) {
        try {
            File file = new File(savePath);
            file.getParentFile().mkdirs();

            URL url = new URL(imageUrl);
            HttpURLConnection connection;

            if (config.getProxyHost() != null && config.getProxyPort() > 0) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getProxyHost(), config.getProxyPort()));
                connection = (HttpURLConnection) url.openConnection(proxy);
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(30000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                try (InputStream in = connection.getInputStream();
                     OutputStream out = new FileOutputStream(savePath)) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    long totalBytes = 0;

                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                        totalBytes += bytesRead;
                    }

                    System.out.println("下载完成: " + totalBytes + " 字节");
                    return true;
                }
            } else {
                System.err.println("HTTP错误: " + responseCode);
                return false;
            }
        } catch (Exception e) {
            System.err.println("下载失败: " + e.getMessage());
            return false;
        }
    }


}

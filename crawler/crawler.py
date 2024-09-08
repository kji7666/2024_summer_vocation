from selenium.webdriver.chrome.service import Service
from selenium import webdriver

# 確保此路徑指向最新版本的 chromedriver.exe
service = Service(executable_path=r"C:\Python312\chromedriver.exe")
driver = webdriver.Chrome(service=service)

driver.get('https://uma.komoejoy.com/character.html')
html = driver.page_source

print(html)

# 關閉瀏覽器
driver.quit()

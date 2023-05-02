import pyautogui
from time import sleep

with open('text.txt', 'r') as arquivo:
    for linha in arquivo:
        movieImage = linha.split(',')[0]
        movieName = linha.split(',')[1]
        # movieGender = linha.split(',')[5]The Game
        movieDescription = linha.split(',')[6]
        moviePrice = linha.split(',')[7]

        # pyautogui.click(322,78,duration=2)
        # pyautogui.write(movieImage)

        # click +
        pyautogui.click(1682,293,duration=1)

        # click virtual
        pyautogui.click(1483,437,duration=1)

        # click name new product
        sleep(3)
        pyautogui.click(616,338,duration=1)
        pyautogui.write(movieName)

        # click catalog
        pyautogui.click(880,448,duration=1)
        # click Guest 
        pyautogui.click(734,535,duration=1)
        # click submit 
        pyautogui.click(1353,671,duration=1)
        sleep(5)

        # click description 
        pyautogui.click(285,749,duration=2)
        pyautogui.write(movieDescription)
        # click Publish
        pyautogui.click(1592,230,duration=1)
        sleep(3)

        # click virtual tab 
        pyautogui.click(320,312,duration=1)
        sleep(5)

        # click insert URL
        pyautogui.click(1098,499,duration=2)
        sleep(5)
        pyautogui.write(movieImage)
        
        # click down screen
        pyautogui.click(1912,989,duration=1)
        pyautogui.click(1912,989,duration=1)
        # click Save Virtual
        pyautogui.click(229,1017,duration=1)
        sleep(5)

        # click SKU tab
        pyautogui.click(533,310,duration=1)
        sleep(5)
        # click ...
        pyautogui.click(1687,584,duration=1)
        # click edit
        pyautogui.click(1512,634,duration=1)
        sleep(3)
        # click rollbar and scroll down
        pyautogui.click(1912,596,duration=1)
        pyautogui.scroll(-100)
        
        # click price
        pyautogui.click(785,892,duration=1)
        pyautogui.press('backspace')
        pyautogui.press('backspace')
        pyautogui.press('backspace')
        pyautogui.press('backspace')
        pyautogui.write(moviePrice)
        # click publish price
        pyautogui.click(1790,976,duration=1)
        sleep(1)
        # click publish price
        pyautogui.click(1790,976,duration=1)
        # click close price
        pyautogui.click(1864,234,duration=1)

        # click Media tab
        pyautogui.click(592,309,duration=2)
        sleep(5)

        # click +
        pyautogui.click(1674,477,duration=2)
        sleep(5)

        # click Select file
        pyautogui.click(1384,383,duration=2)
        sleep(5)

        # click upload file
        pyautogui.click(1426,521,duration=2)
        sleep(5)

        # click insert url file
        pyautogui.click(1426,521,duration=2)
        pyautogui.write(movieImage)
        sleep(5)

        # click open file
        pyautogui.click(1205,547,duration=2)
        sleep(5)

        # click add
        pyautogui.click(1768,379,duration=2)
        sleep(6)

        # click publish
        pyautogui.click(1807,986,duration=2)

        # click x
        pyautogui.click(1844,235,duration=2)

        # click publish
        pyautogui.click(1600,245,duration=2)

        # click back Products
        pyautogui.click(212,170,duration=2)
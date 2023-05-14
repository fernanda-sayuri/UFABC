# ler aquivo excel
import pandas as pd
import pyautogui
from time import sleep

# O navegador
from selenium import webdriver 
# achar os elementos
from selenium.webdriver.common.by import By
# digitar teclado na web
from selenium.webdriver.common.keys import Keys

df = pd.read_csv('export.csv',sep=',')

for index, row in df.iterrows():
    codeSplit = row["Date"].split('3523')[1]
    codeFinal = '3523' + codeSplit[0:40]
    # print(codeFinal)

    # click input code
    pyautogui.click(580,687,duration=1)
    pyautogui.write(codeFinal)
    pyautogui.click(620,697,duration=1)
    pyautogui.write(codeFinal)

    # click fora
    pyautogui.click(861,663,duration=1)

    # click enviar
    pyautogui.click(873,764,duration=1)
    pyautogui.click(868,781,duration=1)


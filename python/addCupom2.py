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

index=0
 
for index, row in df.iterrows():
    # IOS
    # codeSplit = row["Data"].split('3523')[1]
    # codeFinal = '3523' + codeSplit[0:40]
    # print(codeFinal)
    index=index+1
    # Android
    codeSplit = row["text"].split('3523')[1]
    codeFinal = '3523' + codeSplit[0:40]
    print(codeFinal)
    # print(len(df))

    # click input code
    pyautogui.click(580,687,duration=1)
    pyautogui.write(codeFinal)
    pyautogui.click(620,697,duration=1)
    pyautogui.write(codeFinal)

    # click fora
    pyautogui.click(861,663,duration=0.5)

    # click enviar
    pyautogui.click(873,764,duration=1)
    pyautogui.click(868,781,duration=0.5)

    if(index%30 == 0):
        #reseleiona mês

        #entidades
        pyautogui.click(439,354,duration=1)
        #cadastramento de cupom
        pyautogui.click(439,386,duration=1)
        #prosseguir
        pyautogui.click(856,500,duration=1)
        #input 
        pyautogui.click(649,477,duration=1)
        #input hospital
        pyautogui.click(593,511,duration=1)
        #input hospital confirma
        pyautogui.click(866,599,duration=1)
        #input hospital confirma
        pyautogui.click(866,599,duration=1)
        #input hospital confirma
        pyautogui.click(866,599,duration=1)

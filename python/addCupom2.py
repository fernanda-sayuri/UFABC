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
   
    codeFinal = ""
    index=index+1
    # Android
    if '3524' in row["text"]:

        codeSplit = row["text"].split('3524')[1]
        codeFinal = '3524' + codeSplit[0:40]
        print(codeFinal)
        # print(len(df))
    elif '3524' in row["format"]:

        codeSplit = row["format"].split('3524')[1]
        codeFinal = '3524' + codeSplit[0:40]
        print(codeFinal)
        # print(len(df))
    

    # chave de acesso OK
    pyautogui.click(538,762,duration=0.5)
    pyautogui.write(codeFinal)
    # chave de acesso
    pyautogui.click(651,744,duration=0.5)
    pyautogui.write(codeFinal)
    # salvar nota
    pyautogui.click(861,840,duration=0.5)
    pyautogui.click(865,827,duration=0.2)


    # if(index%25 == 0):
    #    # Entidades
    #     pyautogui.click(400,359,duration=1)
    #     # cadastramento de cupons
    #     pyautogui.click(399,388,duration=1)
    #     #prosseguir
    #     pyautogui.click(833,507,duration=0.5)
    #     #click entendida label
    #     pyautogui.click(458,490,duration=1)
    #     #click entendida label
    #     pyautogui.click(458,526,duration=1)
    #     pyautogui.click(439,354,duration=1)
    #     #nova nota
    #     pyautogui.click(876,609,duration=0.5)

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
    # Android35230917604918000142590006584111334539511985
    codeSplit = row["text"].split('3523')[1]
    codeFinal = '3523' + codeSplit[0:40]
    print(codeFinal)
    # print(len(df))

    # chave de acesso
    pyautogui.click(538,702,duration=1)
    pyautogui.write(codeFinal)
    # chave de acesso
    pyautogui.click(641,724,duration=1)
    pyautogui.write(codeFinal)
    # salvar nota
    pyautogui.click(861,780,duration=1)
    pyautogui.click(868,807,duration=1)


    if(index%30 == 0):
        # Entidades
        pyautogui.click(341,259,duration=0.5)

        # cadastramento de cupons
        pyautogui.click(386,287,duration=0.5)

        #prosseguir
        pyautogui.click(856,500,duration=0.5)

        #click entendida label
        pyautogui.click(458,490,duration=0.5)
        #click entendida label
        pyautogui.click(458,526,duration=0.5)
        #entidades
        pyautogui.click(439,354,duration=0.5)
        #nova nota
        pyautogui.click(876,609,duration=0.5)
        print('oiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii')


        # # chave de acesso
        # pyautogui.click(538,702,duration=0.5)
        # # salvar nota
        # pyautogui.click(861,780,duration=0.5)


        #  # click input code
        # pyautogui.click(580,580,duration=1)
        # # pyautogui.write(codeFinal)

        # pyautogui.click(620,697,duration=0.5)
        # pyautogui.write(codeFinal)

        # # click fora
        # pyautogui.click(861,663,duration=0.5)

        # # click enviar
        # pyautogui.click(873,764,duration=0.5)35230917604918000142590006584111335887347468
        # pyautogui.click(868,781,duration=0.5)

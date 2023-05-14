# ler aquivo excel
import pandas as pd

# O navegador
from selenium import webdriver 
# achar os elementos
from selenium.webdriver.common.by import By
# digitar teclado na web
from selenium.webdriver.common.keys import Keys

df = pd.read_csv('export.csv',sep=',')

for index, row in df.iterrows():
    print(row["Date"])

    # insere dado no input
    elemento_div = webdriver.findElement(By.id("divDocComChave"));
    elemento_input = elemento_div.findElement(By.tagName("input"));



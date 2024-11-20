module Main where

import Graphics.Gloss
import Graphics.Gloss.Data.Picture
import Graphics.Gloss.Interface.Pure.Game
import System.Random
import Graphics.Gloss.Data.ViewPort
import System.Random


-- | Parametros da janela e do background
window :: Display
window = InWindow "Jogo Snake" (width, height) (offset, offset)

width, height, offset:: Int
width = 600
height = 600
offset = 100

background :: Color
background = green

data Direction = UP | DOWN | LEFT | RIGHT deriving (Eq, Ord)

-- | Estado de jogo Snake.
data Snake = Cobra{
  tamanho :: [(Float ,Float)],
  direcao :: Direction,
  comida :: ((Float ,Float), StdGen),
  estado :: Int,
  pontos :: Int
}

-- | Possiveis posicoes da comida
posComida :: [(Float, Float)]
posComida= [(x, y) | x <- [-290.0, -280.0 .. 290.0], y <- [-290.0, -280.0 .. 290.0]]

-- | Estado de jogo inicial
initialState :: Snake
initialState = Cobra {
  tamanho = [((10.0), (10.0)), ((20.0), (10.0))],
  direcao = RIGHT,
  comida = ((100.0, 100.0), mkStdGen 100),
  estado = 2,
  pontos = 0
}

-- | Funcoes para movimentar a cobra
atualiza :: [(Float ,Float)] -> [(Float ,Float)]
atualiza corpo = (head corpo):novo
                  where
                    novo = reverse (tail (reverse corpo))

movimenta :: Snake -> Snake
movimenta (Cobra corpo UP c est pnts) = if est == 1 then Cobra {tamanho = (fst(head(corpo)), snd(head(corpo)) + 10.0):(tail (atualiza(corpo))), direcao = UP, comida = c, estado = est, pontos = pnts}
                                        else (Cobra corpo UP c est pnts)
movimenta (Cobra corpo DOWN c est pnts) = if est == 1 then Cobra {tamanho = (fst(head(corpo)), snd(head(corpo)) - 10.0):(tail (atualiza(corpo))), direcao = DOWN, comida = c, estado = est, pontos = pnts}
                                        else (Cobra corpo UP c est pnts)
movimenta (Cobra corpo RIGHT c est pnts) = if est == 1 then Cobra {tamanho = (fst(head(corpo)) + 10.0, snd(head(corpo))):(tail (atualiza(corpo))), direcao = RIGHT, comida = c, estado = est, pontos = pnts}
                                        else (Cobra corpo UP c est pnts)
movimenta (Cobra corpo LEFT c est pnts) = if est == 1 then Cobra {tamanho = (fst(head(corpo)) - 10.0, snd(head(corpo))):(tail (atualiza(corpo))), direcao = LEFT, comida = c, estado = est, pontos = pnts}
                                        else (Cobra corpo UP c est pnts)

-- | Funcoes para desenhar a cobra na tela
desenharNaTela :: Snake -> Picture
desenharNaTela snake 
        | estado snake == 1 = pictures $ desenharCorpo snake
        | estado snake == 2 = pictures $ (color blue $ 
                                        translate (-200) (-100) $ 
                                        scale 0.2 0.2 $ 
                                        text "Aperte 's' para iniciar o jogo") : desenharCorpo snake
        | otherwise = pictures $ gameOverPicture
                        where   gameOverPicture = [color blue $ 
                                        translate (-200) (0) $ 
                                        scale 0.5 0.5 $ 
                                        text "FIM DE JOGO"
                                     , color blue $ 
                                        translate (-175) (-50) $ 
                                        scale 0.2 0.2 $ 
                                        text $ "Pontos totais = " ++ show (pontos snake)
                                     ,  color blue $ 
                                        translate (-175) (-100) $ 
                                        scale 0.2 0.2 $ 
                                        text "Aperte 'r' para jogar novamente" ]

desenharCorpo :: Snake -> [Picture]
desenharCorpo (Cobra [] _ comida _ _) = [ translate (fst(fst(comida))) (snd(fst(comida))) $ color ballColor $ circleSolid 5]
                  where 
                    ballColor = dark red
desenharCorpo (Cobra (h:ts) dir comida est pnts) = if length (ts) == 0 then (translate (fst h) (snd h) $ color corCobra $ rectangleSolid 10 10): desenharCorpo (Cobra [] dir comida est pnts)
                          else (translate (fst h) (snd h) $ color corCobra $ rectangleSolid 10 10) : desenharCorpo (Cobra ts dir comida est pnts)
                          where 
                            corCobra = light (light blue)

-- | Atualiza o estado do jogo com base na tecla que o usuario apertar
handleKeys :: Event -> Snake -> Snake
handleKeys (EventKey (SpecialKey KeyLeft ) Down _ _) cobra = if direcao cobra /= RIGHT && estado cobra == 1 then Cobra { tamanho = tamanho cobra, direcao = LEFT, comida = comida cobra, estado = estado cobra, pontos = pontos cobra}
                                                             else cobra
handleKeys (EventKey (SpecialKey KeyRight) Down _ _) cobra = if direcao cobra /= LEFT && estado cobra == 1 then Cobra { tamanho = tamanho cobra, direcao =  RIGHT, comida = comida cobra, estado = estado cobra, pontos = pontos cobra}
                                                             else cobra
handleKeys (EventKey (SpecialKey KeyUp   ) Down _ _) cobra = if direcao cobra /= DOWN && estado cobra == 1 then Cobra { tamanho = tamanho cobra, direcao = UP, comida = comida cobra, estado = estado cobra, pontos = pontos cobra}
                                                             else cobra
handleKeys (EventKey (SpecialKey KeyDown ) Down _ _) cobra = if direcao cobra /= UP && estado cobra == 1 then Cobra { tamanho = tamanho cobra, direcao = DOWN, comida = comida cobra, estado = estado cobra, pontos = pontos cobra}
                                                             else cobra
handleKeys (EventKey (Char 's') Down _ _) cobra = if estado cobra == 2 then Cobra { tamanho = tamanho cobra, direcao = RIGHT, comida = comida cobra, estado = 1, pontos = pontos cobra} else cobra

handleKeys (EventKey (Char 'r') Down _ _) cobra = if estado cobra == 0 then initialState {estado = 1} else cobra

handleKeys _ cobra = cobra

-- | Chama as funcoes movimenta e colisao
update :: Float -> Snake -> Snake
update s cobra
  | colisao cobra == 1 && estado cobra == 1 = movimenta $ Cobra { tamanho = (fst(comida cobra)):(tamanho cobra), direcao = direcao cobra, comida = trocaComida (comida cobra) (tamanho cobra), estado = estado cobra, pontos = pontos cobra + 10}
  | colisao cobra == 2 && estado cobra == 1 = Cobra { tamanho = tamanho cobra, direcao = direcao cobra, comida = comida cobra, estado = 0, pontos = pontos cobra}
  | otherwise = movimenta cobra

-- | Funcoes para atualizar a posicao da comida
validaPos :: ((Float, Float), StdGen) -> [(Float, Float)] -> Bool
validaPos _ [] = True
validaPos ((x, y), std) (t:ts)
  | (x, y) == t = False
  | otherwise = validaPos ((x, y), std) ts

trocaComida :: ((Float, Float), StdGen) -> [(Float, Float)] -> ((Float, Float), StdGen)
trocaComida ((x, y), std) (t:ts)
  | validaPos (newFood, std2) (t:ts) == True = (newFood, std2)
  | otherwise = trocaComida (newFood, std2) (t:ts)
                  where 
                    (foodX, stdGen2) = randomR (0, length(posComida)) std
                    newFood = posComida !! foodX
                    std2 = stdGen2

-- | Detecta colisoes com a comida, com os limites da tela e com a propria cobra
colisao :: Snake -> Int
colisao cobra
  | head (tamanho cobra) == fst(comida cobra) = 1
  | gameOver (head(tamanho cobra)) (tail(tamanho cobra)) == True = 2
  | fst(head (tamanho cobra)) >= 300 || fst(head (tamanho cobra)) <= -300 = 2
  | snd(head (tamanho cobra)) >= 300 || snd(head (tamanho cobra)) <= -300 = 2
  | otherwise = 3

-- | Verifica se teve game over
gameOver :: (Float, Float) -> [(Float, Float)] -> Bool
gameOver _ [] = False
gameOver h (t:ts)
  | h == t = True
  | null ts = False
  | otherwise = gameOver h ts

-- | Chama a funcao play
main :: IO ()
main = play window background 20 initialState desenharNaTela handleKeys update

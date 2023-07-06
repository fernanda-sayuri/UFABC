Defina uma função para seguinte assinatura:
f :: (a, b) -> (c, d) -> ((b, d), (a, c))
f (x, y) (z, w) = ((y, w), (x, z))

palindromo :: (Eq a) => [a] -> Bool
palindromo xs = xs == reverse xs

Mostre que a seguinte função curried pode ser formalizada em termos
de expressões lambda:
  mult :: Int -> Int -> Int -> Int
  mult x y z = x*y*z

  mult :: Int -> (Int -> (Int -> Int))
  mult = \x -> (\y -> (\z -> x*y*z))


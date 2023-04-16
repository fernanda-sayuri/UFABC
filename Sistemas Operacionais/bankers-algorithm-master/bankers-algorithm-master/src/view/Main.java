package view;

import java.util.Random;

import controller.DeadlockAlgorithm;

public class Main {
	
	
	public static void main(String[] args) {
		
		int qtdProcessos = 5;
		int qtdRecursos = 4;
		
		int[][] recursosAlocados = {
									{3, 0, 1, 1},
									{0, 1, 0, 0},
									{1, 1, 1, 0},
									{1, 1, 0, 1},
									{0, 0, 0, 0},
												};

		int[][] recursosNecessarios = {
										{1, 1, 0, 0},
										{0, 1, 1, 2},
										{3, 1, 0, 0},
										{0, 0, 1, 0},
										{2, 1, 1, 0}
													};

		int[] recursosExistentes = {6, 3, 4, 2};
		
		int qtdProcessos1 = 5;
		int qtdRecursos1 = 6;
		
		int[][] recursosAlocados1 = {
									{0, 1, 0, 0, 1, 0},
									{0, 0, 0, 1, 2, 1},
									{1, 0, 2, 2, 0, 1},
									{1, 0, 1, 0, 0, 0},
									{0, 0, 2, 0, 1, 1},
												};

		int[][] recursosNecessarios1 = {
										{0, 1, 0, 0, 1, 0},
										{0, 2, 0, 1, 0, 0},
										{1, 0, 0, 0, 0, 0},
										{2, 0, 0, 0, 2, 1},
										{0, 0, 1, 1, 0, 0}
													};

		int[] recursosExistentes1 = {6, 5, 7, 5, 4, 3};
		
		//DeadlockAlgorithm deadlock = new DeadlockAlgorithm(qtdProcessos, qtdRecursos, recursosAlocados, recursosNecessarios, recursosExistentes);
		//deadlock.realizarAnalise();
		
		DeadlockAlgorithm deadlock1 = new DeadlockAlgorithm(qtdProcessos1, qtdRecursos1, recursosAlocados1, recursosNecessarios1, recursosExistentes1);
		deadlock1.realizarAnalise();
		
		new Thread() {
	        @Override
	        public void run() {
	        	int novosProcessos = 0;
	        	Random random = new Random();
	        	while(novosProcessos<=6) {
	        		try {
		                this.sleep(15000);
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }
		            
       			System.out.println("\nNovo processo adicionado!");
		            
		            for (int x = 0; x < qtdRecursos1 -2; x++) {
		            	int posCol = random.nextInt(2);
		            	recursosAlocados[posCol][x] =  random.nextInt(3);
		            	recursosNecessarios[posCol][x] =  random.nextInt(3);
		            }
		            
		            novosProcessos++;
		            
		            DeadlockAlgorithm deadlock1 = new DeadlockAlgorithm(qtdProcessos1, qtdRecursos1, recursosAlocados1, recursosNecessarios1, recursosExistentes1);
		    		deadlock1.realizarAnalise();
	        	}
	        }
	    }.start();
		
	}

}

package view;

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
		
		//
		
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
		
	}

}

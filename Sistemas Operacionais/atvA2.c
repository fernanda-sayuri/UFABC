#include <sys/wait.h> /* system call - wait */
#include <stdint.h> /* system call - wait */
#include <stdlib.h> /* system call - exit */
#include <unistd.h> /* system call - fork, exec, sleep */
#include <stdio.h>
#include <stdbool.h>
#include <signal.h> /* Lib - System Call Signal */

// Variaveis globais
int file1Open = true; /* Arquivo hipotetico 1 */
int file2Open = true; /* Arquivo hipotetico 2 */
int valor1 = 500;

void handlerKill(){ 
  printf("Fechando arquivos. O status do file1Open eh %d e o file2Open eh %d \n", file1Open, file2Open);
  printf(" O valor1 = %d\n",valor1);

  file1Open = false; 
  file2Open = false;
  exit(-1);
}

void handlerC(){ 
  printf("\n Digite 'X' para confirmar terminar o processo!\n");
  
  char input;
  scanf("%s", &input);

  if (input == 'X' || input == 'x'){
    printf("Terminando processo. ");

    handlerKill();
  }
  else return;
}

int main(){
  pid_t pid;
  pid = fork();

  if (!pid){
    printf( "Sou o FILHO. %d\n", getpid() );
    signal(SIGINT, handlerC);
    signal(SIGTERM, handlerKill);
    return 0;
  }
  else if (pid){
    printf( "Sou o PAI. %d\n", getpid() );
    valor1 /= 20;

    //lidando com CTRL+C
    signal(SIGINT, handlerC);

    //lidando com comando kill
    signal(SIGTERM, handlerKill);

    wait(NULL);
    while (1);

    printf("O status do file1Open eh %d e o file2Open eh %d \n", file1Open, file2Open);
    printf(" O valor1 = %d\n",valor1);

    return 0;
  }
  
}
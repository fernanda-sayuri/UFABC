//uso de threads

#include<stdio.h>
#include<string.h>
#include<pthread.h>
#include<stdlib.h>
#include<unistd.h>
#include<stdatomic.h>

//num de recursos
#define MAX 10;
int RECURSO_MAXIMO = MAX

// Essa funcao aloca a quantidade de recursos desejada, se existir
//retira
int aloca_recurso (int recurso_desejado){
  if (RECURSO_MAXIMO < recurso_desejado)
    return -1;
  else
  {
    RECURSO_MAXIMO -= recurso_desejado;
    return 0;
  }
}

// Essa funcao devolve uma certa quantidade de recursos alocados
//coloca
int desaloca_recurso (int recurso_devolvido){
  RECURSO_MAXIMO += recurso_devolvido;
  return 0;
}

int TestAndSet(int *ptr, int new){
  int old = *ptr;
  *ptr = new;
  return old;
}

typedef struct __lock_t { int flag; } lock_t;
void init(lock_t *mutex){
  mutex->flag = 0;
}
void lock(lock_t *mutex){
  while(TestAndSet(&mutex->flag, 1) == 1);
}
void unlock(lock_t *mutex){
  mutex->flag = 0;
}

void* func(void *p){
  int i = 0;
  int thread = (int *)p;
  printf("\n Thread %d comecou.....\n", thread);

  lock_t MUTEX;
  init(&MUTEX);
  lock(&MUTEX);

  printf("\n Thread %d , recurso %d.....\n", thread, RECURSO_MAXIMO);
  //CRITICAL REGION
  for (; i<3; i++){
    aloca_recurso(1);
    printf("\n Thread %d , peguei 1 recurso, total %d.....\n", thread, RECURSO_MAXIMO);
  }
  ///////////////////////

  //EnableInterrupts();
  unlock(&MUTEX);
  printf("\n Thread %d saindo da Critical Section.....\n", thread);

}

int main(){
  int i = 0;
  int err;
  // Pthreads
  pthread_t tid[2];

  // Cria as DUAS threads
  while(i < 2)
  {
      err = pthread_create(&(tid[i]), NULL, &func, (void *)i);
      if (err != 0)
          printf("\nNao consigo criar a thread:[%s]", strerror(err));
      i++;
  }

  // Junção das threads
  (void) pthread_join(tid[0], NULL);
  (void) pthread_join(tid[1], NULL);
  pthread_exit(NULL);

  printf("saiu");

  return 0;
}
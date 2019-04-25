
#include <stdio.h>
#include <stdlib.h>
struct ArrayQueue{
    int front, rear;
    int capacity;
    int *array;
}

struct ArrayQueue *Queue(int size){
  struct ArrayQueue *Q = malloc(sizeof(sturct ArrayQueue));
  if(!Q){
    return NULL;
  }
  Q->capacity = size;
  Q->front = -1;
  Q->rear = -1;

  Q->array = malloc(Q->capacity * sizeof(int));
  if(!Q->array) {
    return NULL;
  }
  return Q;
}

int IsEmptyQueue(struct ArrayQueue *Q){
  return ((Q->rear +1) %Q->capacity == Q->front );
}

int QueueSize(){
  return (Q->capacity - Q->front + Q->rear + 1 ) % Q->capacity;
}

void EnQueue(struct ArrayQueue *Q, int data){
  if(IsFullQueue(Q)){
    printf("Queue Overflow");
  }
  else{
    Q->rear = (Q->rear+1) %Q -> capacity;
    Q->array[Q->rear] = data;
    if(Q->front == -1){
      Q->front = Q->rear;
    }
  }
}

int DeQueue(struct ArrayQueue *Q){
  int data = 0;
  if(IsEmptyQueue(Q)){
    printf("Queue is Empty");
    return 0;
  }
  else{
    data = Q->array[Q->front];
    if(Q->front== Q->rear){
      Q->front  = Q-> rear = -1;

    }else{
      Q->front = (Q->front+1) % Q->capacity;
    }
  }
  return data;
}

void DeleteQueue(struct ArrayQueue *Q){
  if(Q) {
    if(Q->array){
      free(Q->array);
      free(Q);
    }
  }
}



void main(){
  Queue(5);
  printf(QueueSize());
}

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "linkedlist.h"


linkedlist_t * 
linkedlist_alloc (int unit) 
{
	linkedlist_t * l = (linkedlist_t *) malloc(sizeof(linkedlist_t)) ;
	l->left = l  ;	
	l->right = l ;
	l->element = malloc(sizeof(int)) ;
	int * u = (int *) l->element ;
	*u = unit ;

	return l ;
}

void
linkedlist_free (linkedlist_t * l, void (* free_element)(void * e))
{
	node_t * curr ;
	node_t * next ;

	curr = l->right ;
	while (curr != l) {
		next = curr->right ;
		if (free_element != 0x0)
			free_element(curr->element) ;
		free(curr->element) ;
		free(curr) ;
		curr = next ;
	}
	free(l->element) ;
	free(l) ;
}

int 
linkedlist_length (linkedlist_t * l)
{
	int len = 0 ;

	node_t * curr = l->right ;
	while (curr != l) {
		len += 1 ;
		curr = curr->right ; 
	}
	return len ; 
}

void
linkedlist_print (linkedlist_t * l, void (* print_element)(void * e))
{
	node_t * curr ;
	node_t * next ;

	curr = l->right ;
	while (curr != l) {
		print_element(curr->element) ;
		curr = curr->right ;
	}
}

void 
linkedlist_insert (node_t * left, void * e, int unit)
{	
	node_t * _new = (node_t *) malloc(sizeof(node_t)) ;
	_new->element = malloc(unit) ;    // 새로 만든 node의 element에 저장 공간 할당
	memcpy(_new->element, e, unit) ; // 문자열을 새롭게 만든 node의 element에 저장하는 과정

	char* s1 = *((char**) e);
	//printf("[DEBUG-1] %s \n", s1);
	char* s2 = *((char**)_new->element);
	//printf("[DEBUG-2] %s \n", s2);


	node_t * right = left->right ;

	_new->left = left ;
	_new->right = right ;

	left->right = _new ;
	right->left = _new ;
}

void
linkedlist_insert_first (linkedlist_t * l, void * e)
{
	linkedlist_insert(l, e, *((int *)(l->element))) ;
}

void
linkedlist_insert_last (linkedlist_t * l, void * e)
{
	linkedlist_insert(l->left, e, *((int *)(l->element))) ;
}

int
linkedlist_remove (linkedlist_t * l, node_t * n)
{
	if (l->left == l)
		return 1 ;

	n->left->right = n->right ;
	n->right->left = n->left ;

	free(n->element) ;
	free(n) ;
	return 0 ;
}

int 
linkedlist_remove_first (linkedlist_t * l, void * e)
{
	if (l->right == l)
		return 1 ;

	memcpy(e, l->right->element, *((int *)(l->element))) ;
	linkedlist_remove(l, l->right) ;
	return 0 ;
}

int
linkedlist_remove_last (linkedlist_t * l, void * e)
{
	if (l->left == l)
		return 1 ;

	memcpy(e, l->left->element, *((int *)(l->element))) ;
	linkedlist_remove(l, l->left) ;
	return 0 ;
}

int 
linkedlist_get (linkedlist_t * l, int pos, void * e)
{
	if (pos < 0)
		return 1 ;

	int unit = *((int *) l->element) ;
	int i = 0 ;
	node_t * curr = l->right ;
	while (i < pos && curr != l) {
		curr = curr->right ;
		i += 1 ;
	}
	if (i != pos)
		return 1 ;

	memcpy(e, curr->element, unit) ;
	return 0 ;
}

void
linkedlist_sort (linkedlist_t * l, int ( cmp_elements)(void * e1, void * e2)) 
{
	node_t * i, * j, * m ;

	int unit = *((int *)(l->element)) ;

	for (i = l->right ; i != l ; i = i->right) {
		m = i ;
		for (j = i->right ; j != l ; j = j->right) {
			if (cmp_elements(j->element, m->element) < 0)
				m = j ;
		}

		void * t = i->element ;
		i->element = m->element ;
		m->element = t ;
	}
}

void
linkedlist_q_sort (linkedlist_t * l, int (*cmp_elements)(void * e1, void *e2), node_t* begin, node_t* end)
{
	if (begin >= end ){
		return;
	}
	node_t* pivot = begin;
	node_t* lbound = begin->right;
	node_t* rbound = end;

	char* s1 = *((char**)begin->element);
	printf("[DEBUG-qsort begin-ele] %s \n", s1);
	char* s2 = *((char**)lbound->element);
	printf("[DEBUG-qsort lbound-ele] %s \n", s2);
	char* s3 = *((char**)rbound->element);
	printf("[DEBUG-qsort rbound-ele] %s \n", s3);
	char* s4 = *((char**)end->element);
	printf("[DEBUG-qsort end-ele] %s \n", s4);
	char* s5 = *((char**)pivot->element);
	printf("[DEBUG-qsort pivot] %s \n", s5);
	// 현재 링크드 리스트 출력
	
	/*
	node_t* curr;
	node_t* next;

	curr = l->right;
	while (curr != l) {
		char* s = *((char**)curr->element);
		printf("%s -> ", s);
		curr = curr->right;
	}
	printf("\n");
	*/
	///////////////////////////////////

	while (lbound <= rbound){
		while (lbound <= end && cmp_elements(lbound->element, pivot->element) <= 0) {
			if (lbound == end) {
				printf("out cond:: lbound == end \n");
				break;
			}
			lbound = lbound->right;
		}
			
		while (begin < rbound && cmp_elements(pivot->element, rbound->element) < 0) {
			rbound = rbound->left;
		}


		if (lbound <= rbound) {
			if (lbound != l && rbound != l) {
				void* t1 = rbound->element;
				rbound->element = lbound->element;
				lbound->element = t1;
			}

			if (lbound->right == l  || rbound->left == l) { 
				printf("out cond ::  lbound->right == l  || rbound->left == l \n");
				break;
			}

			lbound = lbound->right;
			rbound = rbound->left;
		}
	}

	if (begin < end) {
		/*
		void* t2 = rbound->element;
		rbound->element = pivot->element;
		pivot->element = t2;
		*/
		if (rbound == end) {
			printf("11111111\n");
			return;
			//return;
		}


		if (begin != l) {
			linkedlist_q_sort(l, cmp_elements, begin, rbound);
			printf("check1\n");
		}
		if (rbound->right != l || rbound == end) {
			linkedlist_q_sort(l, cmp_elements, rbound->right, end);
			printf("check2\n");
		}
	}
}

void
linkedlist_qsort (linkedlist_t * l, int ( cmp_elements)(void * e1, void * e2)) {
	linkedlist_q_sort(l, cmp_elements, l->right, l->left) ;
}
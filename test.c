
void
linkedlist_q_sort (linkedlist_t * l, int (*cmp_elements)(void * e1, void *e2), node_t* begin, node_t* end)
{
	if (begin >= end)
	{
		return;
	}
	node_t * pivot = begin;
	node_t * lbound = begin ->right;
	node_t * rbound = end;

	char* s2 = *((char**)begin->element);
	printf("[DEBUG-qsort] %s \n", s2);
	char* s3 = *((char**)lbound->element);
	printf("[DEBUG-qsort] %s \n", s3);
	char* s4 = *((char**)rbound->element);
	printf("[DEBUG-qsort] %s \n", s4);


	while(lbound <= rbound)
	{

		while(lbound <= end && cmp_elements(lbound->element, pivot->element) <= 0)
		{	
			if(lbound == end){
				break;
			}

			lbound = lbound -> right;
		}

		while (begin < rbound && cmp_elements(pivot->element, rbound->element) < 0)
		{
			rbound = rbound -> left;
		}
			if (lbound <= rbound)
			{	
				if(lbound!=l && rbound!=l) {
					void * t1 = rbound->element ;
					rbound->element = lbound->element ; 
					lbound->element = t1 ;
				}
			
				if (lbound -> right == l || rbound -> left == l) {
					return;
				}

				lbound = lbound ->right ;
				rbound = rbound ->left ;
			}
	}
	
	if (begin < end){
		void * t2 = rbound->element ;
		rbound->element = pivot->element ;
		pivot->element = t2 ;
		
		if(rbound==end) {
			return;
		}

		if(begin != l) {
			linkedlist_q_sort(l, cmp_elements, begin, rbound) ;
		}
		if(rbound->right != l) {
			linkedlist_q_sort(l, cmp_elements, rbound->right, end) ;
		}
	}
}

#ifndef CSIDE_DATA_STORAGE
#define CSIDE_DATA_STORAGE

#include "EnergyStats.h"

#define NODE_CAPACITY 100 //customize later, or maybe not
typedef struct LinkNode {
	EnergyStats items[NODE_CAPACITY];
	struct LinkNode* next;
	int nElems;
	//TODO: make a count at the end for 'the last node' instead of one guy per node, since we know that all previous nodes will be at capacity
} LinkNode;

typedef struct LinkedList {
	LinkNode* head;
	LinkNode* tail;
	int numItems;
} LinkedList;

LinkedList* newLinkedList();
void freeLinkedList(LinkedList* esll);
void addItem_LinkedList(LinkedList* l, EnergyStats e); // add to tail
void writeToFile_LinkedList(FILE* outfile, LinkedList* l);

#define LINKLIST_NUM_NODES(list)	\
	(int)(list->numItems/NODE_CAPACITY)	+	\
	!!(list->numItems%NODE_CAPACITY)	// ceiling division

typedef struct DynamicArray {
	EnergyStats* items;
	unsigned long long capacity;
	unsigned long long nItems;
} DynamicArray;

DynamicArray* newDynamicArray(int capacity);
void freeDynamicArray(DynamicArray* list);
void addItem_DynamicArray(DynamicArray* a, EnergyStats e);
void writeToFile_DynamicArray(FILE* outfile, DynamicArray* a);

#endif //CSIDE_DATA_STORAGE

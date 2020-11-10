
#ifndef CSIDE_DATA_STORAGE
#define CSIDE_DATA_STORAGE

#include "EnergyStats.h"

#define NODE_CAPACITY 100 //customize later, or maybe not
typedef struct LinkNode {
	EnergyStats items[NODE_CAPACITY];
	struct LinkNode* next;
	//int nElems;
	//TODO: make a count at the end for 'the last node' instead of one guy per node, since we know that all previous nodes will be at capacity
} LinkNode;

typedef struct LinkedList {
	LinkNode* head;
	LinkNode* tail;
	//int numItems;
	int nElemsAtTail;	// by the rules of this list, only tail node can have variable items. all other
						// ones are full to NODE_CAPACITY

} LinkedList;

LinkedList* newLinkedList();
void freeLinkedList(LinkedList* esll);
void addItem_LinkedList(LinkedList* l, EnergyStats e); // add to tail
void writeToFile_LinkedList(FILE* outfile, LinkedList* l);

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

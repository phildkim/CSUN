#include <stdio.h>
#include <stdlib.h>
struct node {
  int vp;
  int pf;
  int accessed;
}*pt=NULL;
typedef struct node pt_type;
int replace;
int pm_size;
int pg_size;
int pm_addr;
int num_rows;
int last_row=-1;
void OPTION1(){
  int i;
  printf("Enter main memory size (words): ");
  scanf("%d",&pm_size);
  printf("Enter page size (words/page): ");
  scanf("%d",&pg_size);
  printf("Enter replacement policy (0=LRU, 1=FIFO): ");
  scanf("%d",&replace);
  num_rows=(pm_size/pg_size);
  pt=(pt_type*)(malloc(num_rows*sizeof(pt_type)));
  for(i=0;i<num_rows;i++){
    pt[i].vp=-1;
    pt[i].accessed=-1;
  }
  return;
}
void OPTION2(){
  int vm_addr;
  int temp_pf;
  int offset;
  int vp;
  int j,k,i=0;
  printf("Enter virtual memory address to access: ");
  scanf("%d",&vm_addr);
  vp=vm_addr/pg_size;
  offset=vm_addr%pg_size;
  while((i<num_rows)&&(pt[i].vp!=-1)&&(pt[i].vp!=vp)){
    pt[i].accessed++;
    i++;
  }
  if(i==num_rows){
    temp_pf=pt[0].pf;
    for(j=0;j<num_rows-1;j++)
      pt[j]=pt[j+1];
    pt[num_rows-1].vp=vp;
    pt[num_rows-1].pf=temp_pf;
    pt[num_rows-1].accessed=0;
    printf("Page fault!\n");
  }
  else if(pt[i].vp==-1){
    pt[i].vp=vp;
    pt[i].pf=i;
    pt[i].accessed=0;
    last_row++;
    printf("Page fault!\n");
  }
  else{
    if(replace==0){
      temp_pf=pt[i].pf;
      for(k=i;k<last_row;k++){
        pt[k]=pt[k+1];
        pt[k].accessed++;
      }
      pt[last_row].vp=vp;
      pt[last_row].pf=temp_pf;
      pt[last_row].accessed=0;
    }
    pm_addr=temp_pf*pg_size+offset;
    printf("Virtual address %d maps to physical address %d\n",vm_addr,pm_addr);
  }
  return;
}
void OPTION3(){
  int i;
  printf("____________________________\n"
        "|   VP   |   PF   | Access |\n"
        "|--------|--------|--------|\n");
  for (i=0;i<=last_row;i++){
    printf("| %*d   | %*d   | %*d   |\n"
    "|________|________|________|\n",
    4,pt[i].vp,4,pt[i].pf,4,pt[i].accessed);
  }
  return;
}
void COLLECTION(){
  if(pt!=NULL){
    free((void*)pt);
  }
}
int main(void){
  int n;
  while(n!=4){
    printf("\nVirtual memory to Main memory mapping:\n"
            "--------------------------------------\n"
            "1) Set parameters\n"
            "2) Map virtual address\n"
            "3) Print page table\n"
            "4) Quit\n\n"
            "Enter selection: ");
    scanf("%d",&n);
    switch (n){
    case 1:
      OPTION1();
      break;
    case 2:
      OPTION2();
      break;
    case 3:
      OPTION3();
      break;
    default:
      break;
    }
  }
  COLLECTION();
  return 1;
}
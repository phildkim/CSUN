#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int select();
struct node {
  char instr_string[13];
  int dest;
  int src1;
  int src2;
} *instr_set = NULL;
typedef struct node instr_type;
int num_instr;
void option_1() {
  int i;
  printf("Enter total number of instructions: ");
  scanf("%d",&num_instr);
  /* Allocate memory to hold a set of instructions */
  instr_set=(instr_type*)malloc((3*num_instr+1)*sizeof(instr_type));
  /* Initialize instruction 0's destination register to -1 to prevent false RAW dependency w/ instruction 2 */
  instr_set[0].dest=-1;
  /* For each instruction, prompt for user input and read instruction as a string and store at proper field of appropriate index within dynamic array of instructions */
  for(i=1;i<=num_instr;i++) {
    scanf("%s",instr_set[i].instr_string);
    instr_set[i].dest=instr_set[i].instr_string[5]-'0';
    instr_set[i].src1=instr_set[i].instr_string[8]-'0';
    instr_set[i].src2=instr_set[i].instr_string[11]-'0';
  }
  return;
};
void option_2() {
  int i,j,n;
  int dep_delay;
  int overlap=0;
  int tabbing=0;
  int total_cycles;
  /* For each instruction i from 2 to total number of instructions initialize delay as 0 and check for dependency between instruction (i-2) and i, as well as between instruction (i-1) and i */
  for (i=2;i<=num_instr;i++){
    dep_delay=0;
    if((instr_set[i-2].dest==instr_set[i].src1)||
      (instr_set[i-2].dest==instr_set[i].src2)){
      if(overlap==0){
        dep_delay=1;
        overlap=1;
      }else{
        dep_delay=0;
        overlap=0;
      }
    }else{
      dep_delay=0;
      overlap=0;
    }
    /* If dependency, add NOPs appropriately by shifting remaining instructions down and set delay & flag to check for possible overlap between dependent instructions, and increase total number of instructions appropriately */
    if((instr_set[i-1].dest==instr_set[i].src1)||
      (instr_set[i-1].dest==instr_set[i].src2)){
      dep_delay=2;
      overlap=1;
    }
    if(dep_delay==2){
      for(n=num_instr+2;n>=i+2;n--){
        instr_set[n]=instr_set[n-2];
      }
      strcpy(instr_set[i].instr_string,"NOP         ");
      strcpy(instr_set[i+1].instr_string,"NOP         ");
      num_instr+=2;
      i+=2;
    }
    if(dep_delay==1){
      for(n=num_instr+1;n>=i+1;n--){
        instr_set[n]=instr_set[n-1];
      }
      strcpy(instr_set[i].instr_string,"NOP         ");
      num_instr+=1;
      i+=1;
    }
  }//end for
  total_cycles=num_instr+4;
  printf("Total cycles: %d\n",total_cycles);
  for(i=1;i<=num_instr;i++){
    printf("%s:\t",instr_set[i].instr_string);
    for(j=1;j<=tabbing;j++)
      printf("\t");
    printf("IF\tID\tEX\tME\tWB\n");
    tabbing++;
  }
  return;
};
void collection(){
  if(instr_set!=NULL){
    free((void*)instr_set);
  }
}
int main(void) { 
  int n;
  while (n!=3) {
    printf("\nInstruction-level parallelism\n"
           "-----------------------------\n"
           "1) Enter instructions\n"
           "2) Calculate total cycle count on a 5-stage pipeline processor\n"
           "3) Quit program\n\n"
           "Enter selection: ");
    scanf("%d", &n);
    switch (n) {
      case 1:
        // decimal to ieee
        option_1();
        break;
      case 2:
        // ieee to decimal
        option_2();
        break;
      default:
        break;
    }// end switch
  }// end while
  collection();
};// end main
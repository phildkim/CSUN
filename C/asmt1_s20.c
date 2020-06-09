#include <stdio.h>
#include <stdlib.h>
#define MEGA 1000000
#define GIGA 1000000000
struct node
{
  float instr;
  float cycle;
  float execu;
  struct node *next;
};
typedef struct node node;

void print()
{
  printf("\nMeasuring Performance:\n"
         "----------------------\n"
         "1) Enter parameters\n"
         "2) Calculate Execution time of a sequence\n"
         "3) Calculate MIPS of a sequence\n"
         "4) Calculate average CPI of a sequence\n"
         "5) Exit program\n\n");
} // end print

int selection()
{
  int d;
  printf("Enter selection: ");
  scanf("%d", &d);
  return d;
} // end selection

node *parameters()
{
  node *data = NULL;
  int c;
  float x, y, z;
  printf("Enter the number of instruction classes: ");
  scanf("%d", &c);
  data = (node *)malloc((long unsigned int)c * sizeof(node));
  printf("Enter the frequency of the machine (GHz): ");
  scanf("%f", &x);
  x *= GIGA;
  for (int i = 0; i < c; i++)
  {
    printf("Enter CPI of class %d: ", (i + 1));
    scanf("%f", &y);
    printf("Enter instruction count of class %d (billions): ", (i + 1));
    scanf("%f", &z);
    data->cycle += (z * GIGA) * y;
    data->next = NULL;
    data->instr += z * GIGA;
    data->next = NULL;
  } // end for
  data->execu = data->cycle / x;
  return data;
} // end parameters

int main(void)
{
  node *data = NULL;
  int n = 0;
  while (n != 5)
  {
    print();
    n = selection();
    switch (n)
    {
    case 1:
      // Enter parameters
      data = parameters();
      break;
    case 2:
      // Calculate the execution time
      printf("The execution time of the sequence is: %.2f sec\n",
             data->execu);
      break;
    case 3:
      // Calculate the MIPS
      printf("The MIPS of the sequence is: %.2f\n",
             data->instr / (data->execu * MEGA));
      break;
    case 4:
      // Calculate the average CPI
      printf("The average CPI of the sequence is: %.2f\n",
             data->cycle / data->instr);
      break;
    default:
      break;
    } // end switch
  }   // end while
  if (data != NULL)
    free((void *)data);
  return 0;
} // end main
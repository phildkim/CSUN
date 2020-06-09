#include<assert.h>
#include<stdio.h>
#include<math.h>
const unsigned char BIAS=0x7F;//127bits
const unsigned char MANT=0x17;//23bits
const unsigned char EXPN=0x8;//8bits
const unsigned char BITS=0x1;//1bit
void binary(char *str,int d,int n) {
  printf("%s: ",str);
  for (int i=n-1;i>=0;--i)
    printf("%x",((d>>i)&1)?1:0);
};
void decimal() {
  float dec_num,dec_man;
  unsigned int dec_mask,dec_exp,dec_sign;
  dec_mask=(*(unsigned int*)&dec_num);
  // mask mantissa:23bits
  dec_man=(dec_mask&0x7FFFFFF)|0x8000000;
  dec_mask>>=MANT;
  // mask exponent:8bits
  dec_exp=(dec_mask&0xFF);
  dec_mask>>=EXPN;
  // mask sign:1bit
  dec_sign=(dec_mask&BITS);
  dec_mask>>=BITS;
  printf("Enter the decimal representation: ");
  scanf("%f",&dec_num);
  // if zero print and return
  if (dec_num==0.0) {
    binary("Sign",dec_sign,BITS);
    binary("\nBiased exponent",dec_exp,EXPN);
    binary("\nMantissa",dec_man,MANT);
    printf("\nIEEE-754 format %.8x\n",*(unsigned int*)&dec_num);
    return;
  }// end if zero
  dec_sign=(dec_num<0.0)?1:0;
  dec_man=(dec_sign==BITS)?0.0-dec_num:dec_num;
  dec_exp=floor(log2(dec_man))+BIAS;
  binary("Sign",dec_sign,BITS);
  binary("\nBiased exponent",dec_exp,EXPN);
  binary("\nMantissa",*(unsigned int *)&dec_man,MANT);
  printf("\nIEEE-754 format %.8x\n",*(unsigned int *)&dec_num);
};
void ieee754() {
  /* declare local variables */
  float ieee_frac,ieee_dec;
  int ieee_num,ieee_sign,ieee_exp;
  /* prompt for IEEE-754 representation */
  printf("Enter the IEEE-754 representation: ");
  scanf("%x",&ieee_num);
  /* check for special cases: if so, print and return */
  if (ieee_num==0x00000000) {
    printf("Special case: +0\n");
    return;
  } // sc: +0
  if (ieee_num==0x80000000) {
    printf("Special case: -0\n");
    return;
  } // sc: -0
  if (ieee_num==0x7f800000) {
    printf("Special case: +infinity\n");
    return;
  } // sc: +inf
  if (ieee_num==0xff800000) {
    printf("Special case: -infinity\n");
    return;
  } // sc: -inf
  if ((ieee_num&0x7fffffff)>0x7f800000) {
    printf("Special case: NaN\n");
    return;
  } // sc: NaN
  /* Mask sign from number: if sign=0, print "+", else print "-" */
  ieee_sign=floor(*(unsigned int*)&ieee_num/pow(2.0,MANT|EXPN));
  printf("Sign: %c",(ieee_sign==0)?'+':'-');
  /* Mask biased exponent and significand from number */
  ieee_exp=(*(unsigned int*)&ieee_num&0x7f800000)/pow(2.0,MANT);
  /* Get mantissa sum of fractions and multiply by sign */
  for (int i=(MANT-1);i>0;i--)
    ieee_frac+=(ieee_num>>(i))&1?pow(2.0,i-MANT):0;
  /* If biased exponent=0, number is denormalized with unbiased exponent of -126, 
		print denormalized number as fraction * 2^(-126), return */
  if (ieee_exp==0) {
    printf("\nUnbaised exponent: %d",ieee_exp=~BIAS|0x02);
    printf("\nDenormalized decimal format: %f*2^(%d)\n",
    (ieee_sign==1)?ieee_frac-1.0:ieee_frac,ieee_exp);
    return;
  }
  // IMPORTANT FORMULA......
  ieee_dec+=(ieee_sign==0?1:-1)*(pow(2.0,ieee_exp-BIAS))*(1.0+ieee_frac);
  /* Unbias exponent by subtracting 127 and print */
  printf("\nUnbiased exponent: %d",ieee_exp-BIAS);
  /* Add hidden 1 and print normalized decimal number */
  printf("\nNormalized decimal: %f",1.0+ieee_frac);
  /* Print decimal number */
  printf("\nDecimal format: %f\n",ieee_dec);
  return;
};

int select() {
  int n;
  printf("\nFloating-point conversion:\n"
         "--------------------------\n"
         "1) Decimal to IEEE-754 conversion\n"
         "2) IEEE-754 to Decimal conversion\n"
         "3) Exit\n\n"
         "Enter selection: ");
  scanf("%d", &n);
  return n;
};// end print
int main(void) { 
  int n;
  while (n!=3) {
    n=select();
    switch (n) {
      case 1:
        // decimal to ieee
        decimal();
        break;
      case 2:
        // ieee to decimal
        ieee754();
        break;
      default:
        break;
    }// end switch
  }
}
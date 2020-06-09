@ ;--------------------------------------------------------
@ ; COMP 122-16311-FA2019, ASSIGNMENT PROJECT #2
@ ; Copyright(c), Philip D. Kim
@ ; A program to read characters from 'input.txt'
@ ; into memory, uppercase all vowels, remove all
@ ; parenthicals, output the modified string to 
@ ; 'output.txt'.
@ ;--------------------------------------------------------
@ ; 'input.txt':
@ ; Stories (you with the tall tales)//How many stories
@ ; (so many tall tales)//We climb the structure (you 
@ ; scale the ladder)//You build it higher (you make us
@ ;  madder)
@ ;--------------------------------------------------------
@ ; 'output.txt':
@ ; StOrIEs //HOw mAny stOrIEs //WE clImb thE strUctUrE
@ ; //YOU bUIld It hIghEr
@ ;--------------------------------------------------------
@ ; SWI OPERATIONS FOR I/O
    .equ exitsf,0x11        @ ; exit program
    .equ opensf,0x66        @ ; open files
    .equ readsf,0x6A        @ ; read files
    .equ closef,0x68        @ ; close files
    .equ writef,0x69        @ ; write strings
    .equ writei,0x6B        @ ; write integers
    .global _start
    .text
@ ; START OF PROGRAM --------------------------------------
_start:
    ldr r0,=1
    ldr r1,=msg1
    swi writef
    ldr r1,=msg2
    swi writef              @ ; stdout starting
    ldr r0,=infl            
    ldr r1,=0
    swi opensf              @ ; open 'input.txt'
    bcs infdne              @ ; check missing file
    ldr r1,=arry
    ldr r2,=255
    swi readsf              @ ; read 'input.txt'
    bcs infsze              @ ; check file size
    cmp r0,#255
    blge infofw             
    mov r3,r0
    mov r4,r0               @ ; r3,r3 <-- file size
    ldr r0,=1
    ldr r1,=msg3
    swi writef
    ldr r1,=infl
    swi writef
    ldr r1,=msg9
    swi writef
    mov r1,r3
    swi writei
    ldr r1,=msg8
    swi writef              @ ; stdout read complete
    ldr r1,=arry
    mov r0,r1               @ ; ldr arr r1, move to r0
@ ; INITIALIZED---------------------------------------------

loop:
    cmp r2,#0               @ ; 'NULL'
    beq _eot
    ldrb r2,[r1],#1         @ ; load & increment
    strb r2,[r0],#1         @ ; store & increment
    cmp r2,#0x28            @ ; '('
    beq _remove
    cmp r2,#0x61            @ ; 'a'
    beq _upper
    cmp r2,#0x65            @ ; 'e'
    beq _upper
    cmp r2,#0x69            @ ; 'i'
    beq _upper
    cmp r2,#0x6F            @ ; 'o'
    beq _upper
    cmp r2,#0x75            @ ; 'u'
    beq _upper
    cmp r2,#0x29
    beq _remove
    b loop
@ ; MAIN LOOP----------------------------------------------

_remove:
    add r5,r5,#1            @ ; counter for stdout
    cmp r2,#0x28            @ ; '('
    subeq r0,r0,#1          @ ; decrement r0
    ldrb r2,[r1],#1         @ ; load next char
    cmp r2,#0x29            @ ; ')'
    addeq r5,r5,#1          @ ; counter for stdout
    bne _remove             @ ; loop until ')' found
    b loop
@ ; REMOVE LOOP--------------------------------------------

_upper:
    bic r2,r2,#0x20         @ ; uppercase by clearing 5 bit
    sub r0,r0,#1            @ ; decrement r0
    strb r2,[r0],#1         @ ; store modified char
    b loop
@ ; UPPERCASE LOOP-----------------------------------------
@ ; how bic works
@ ; 01110101 <-- 'u'
@ ; 00100000 <-- bic #0x20
@ ;---------
@ ;   010101
@ ; 01010101 <-- 'U'

_eot:
    ldr r0,=1               @ ; stdout write complete
    ldr r1,=msg4
    swi writef
    ldr r1,=opfl
    swi writef
    ldr r1,=msg9
    swi writef
    mov r1,r5
    swi writei
    ldr r1,=msg8
    swi writef
    ldr r0,=opfl 
    ldr r1,=1
    swi opensf              @ ; open 'output.txt'
    b _stdout
@ ; END OF TEXT--------------------------------------------

_stdout:
    ldr r1,=arry
    swi writef              @ ; write to 'output.txt'
    b _close
@ ; STDOUT FILE--------------------------------------------

infofw:
    ldr r0,=1
    ldr r1,=msg5
    swi writef
    bx lr
@ ; OVERFLOW FILE------------------------------------------

infdne:
    ldr r0,=1
    ldr r1,=msg6
    swi writef
    b _terminate
@ ; MISSING 'input.txt'------------------------------------

infsze:
    ldr r0,=1
    ldr r1,=msg7
    swi writef
    b _terminate
@ ; MISSING text-------------------------------------------

_close:
    swi closef
    b _terminate
@ ; CLOSE FILES -------------------------------------------

_terminate:
    ldr r0,=1
    ldr r1,=msg2
    swi writef
    ldr r1,=msg0
    swi writef
    swi exitsf
@ ;END OF PROGRAM------------------------------------------

    .data
arry: .skip 255
infl: .asciz "input.txt"
opfl: .asciz "output.txt"
msg0: .asciz "\n	ARMSim complete\n"
msg1: .asciz "\n	Starting ARMSim\n"
msg2: .asciz "-------------------------------"
msg3: .asciz "\n    Reading complete\n    '"
msg4: .asciz "\n    Writing complete\n    '"
msg5: .asciz "\n    Warning! Large file\n"
msg6: .asciz "\n  ERROR: missing 'input.txt'\n"
msg7: .asciz "\n  ERROR: missing text from file\n"
msg8: .asciz " characters\n"
msg9: .asciz "': "
msgn: .asciz "\n"
    .end
@ ;---------------------------------------------------------
@ ; COMP 122-16311-FA2019, ASSIGNMENT PROJECT #3
@ ; Copyright(c), Philip D. Kim
@ ; Using the ARMSim# + Embest plugin to produce a program
@ ; that adds to current value of 1024, if left black
@ ; button is pressed and subtracts to current value 
@ ; if right black button is pressed.
@ ;---------------------------------------------------------
    .equ clsln,0x208        @ ; CLEAR LINE
    .equ clssn,0x206        @ ; CLEAR SCREEN
    .equ chkky,0x203        @ ; CHECK BLUE KEYS
    .equ chkbn,0x202        @ ; CHECK BLACK BUTTONS
    .equ ptchr,0x207        @ ; PRINT CHARACTERS
    .equ ptint,0x205        @ ; PRINT INTERGERS
    .equ ptstr,0x204        @ ; PRINT STRINGS
    .equ setlt,0x201        @ ; LED LIGHT SETTER
    .equ segmt,0x200        @ ; SEGMENT SETTER
    .global _start
@ ;---------------------------------------------------------
_start:
    bl clear
    mov r0,#3
    ldr r2,=decimal0
    swi ptstr
    mov r0,#26
    mov r2,#0x400
    swi ptint               @ ; initial 1024
    mov r3,r2
    bal skipblkbtn
initblkbtn:
    mov r6,#1
    mov r7,#12
    bl clearlines
skipblkbtn:
    mov r0,#3
    mov r1,#1
    ldr r2,=blackbtn
    swi ptstr               @ ; select black
    mov r0,#0
selblkbtn:
    swi chkbn
    cmp r0,#0
    beq selblkbtn           @ ; waits for blk btn input
    movne r4,r0             @ ; save left|right for +|-
    mov r0,#26
    cmp r4,#0x02            @ ; left button selected
    beq blkltbtn
    cmp r4,#0x01            @ ; right button selected
    beq blkrtbtn
blkltbtn:
    mov r2,#0x4C
    swi ptchr               @ ; print selected L 
    mov r0,#0x02
    swi setlt               @ ; set left led
    mov r0,#0x0D
    swi segmt               @ ; set left segment
    bal initblebtn
blkrtbtn:
    mov r2,#0x52
    swi ptchr               @ ; print select R
    mov r0,#0x01
    swi setlt               @ ; set right led
    mov r0,#0x85
    swi segmt               @ ; set right segment
    bal initblebtn
selblebtn:
    mov r6,#2
    mov r7,#12
    bl clearlines
initblebtn:
    mov r0,#3
    mov r1,#2
    ldr r2,=bluekeys
    swi ptstr               @ ; select blue key
    mov r0,#0
blekeyno:
    swi chkky
    cmp r0,#1<<0
    blt blekeyno            @ ; waits for key input
    movge r5,r0             @ ; save key to r5
    mov r1,#0
    bl convert              @ ; convert binary to decimal
    mov r0,#26
    mov r1,#2
    mov r2,r5
    swi ptint               @ ; print blue key input
    mov r0,r5
    mov r1,#1
    bl segment              @ ; display input on segmet
resultant:
    mov r0,#3
    mov r1,#4
    ldr r2,=decimal1
    swi ptstr
    mov r0,#26
    mov r2,r3
    swi ptint               @ ; current value
    mov r1,#5
    cmp r4,#0x02
    moveq r2,#0x2B
    movlt r2,#0x2D
    swi ptchr               @ ; print +|-
    cmp r5,#10
    movlt r0,#29
    movge r0,#28
    mov r2,r5
    swi ptint               @ ; print blue key #
    mov r0,#26
    mov r1,#6
    ldr r2,=eqnline0
    swi ptstr               @ ; print ----
    mov r0,#26
    mov r1,#7
    cmp r4,#0x01
    subeq r3,r3,r5
    addgt r3,r3,r5
    mov r2,r3
    swi ptint               @ ; print new decimal
    mov r0,#3
    mov r1,#9
    ldr r2,=options0
    swi ptstr               @ ; blue to continue
    mov r1,#10
    ldr r2,=options1
    swi ptstr               @ ; left to restart
    mov r1,#11
    ldr r2,=options2
    swi ptstr               @ ; right to quit
    mov r0,#0
    mov r1,#0
options:
    swi chkky
    cmp r0,#0
    beq options
    cmp r0,#1<<1
    beq selblebtn           @ ; 1 to continue
    blt error
    cmp r0,#1<<2
    beq initblkbtn          @ ; 2 left to restart
    cmp r0,#1<<3
    beq terminate           @ ; 3 right to quit
    bgt error
error:
    mov r0,#3
    mov r1,#8
    ldr r2,=errormsg
    swi ptstr
    mov r0,#0
    swi segmt
    swi setlt
    bal options

@ ;---------------------------------------------------------
clearlines:
    stmfd sp!,{r0-r2,lr}
    mov r1,r6
    mov r2,r7
incloop:
    mov r0,r1               @ ; starting position
    swi clsln
    add r1,r1,#1            @ ; add 1 to r1 to keep clear line
    cmp r1,r2               @ ; cmp until ending position
    blt incloop
    mov r0,#0               @ ; set segmt & led off
    swi segmt
    swi setlt
    mov r1,#0
    mov r2,#0
    mov r6,#0
    mov r7,#0
    ldmfd sp!,{r0-r2,pc}
@ ;---------------------------------------------------------
convert:
    stmfd sp!,{r0-r2,lr}
    ldr r2,=keyarray        @ ; array of key in hex
    mov r7,#16
decloop:
    sub r7,r7,#1            @ ; decrement 1 from 16
    ldr r0,[r2,r6]          @ ; keyarray[0]
    cmp r0,r6
    addne r6,r6,#4          @ ; if r0 != r6 add #4 to r6
    cmp r0,r5
    bne decloop
    moveq r5,r7             @ ; if match, move the decimal to r5
    cmp r0,#512
    moveq r5,#9             @ ; 9=516 but here its 512
    mov r6,#0
    mov r7,#0
    ldmfd sp!,{r0-r2,pc}
@ ;---------------------------------------------------------
segment:
    stmfd sp!,{r0-r2,lr}
    ldr r2,=segarray        @ ; array of segment in hex
    ldr r0,[r2,r0,lsl #2]   @ ; adds 12 bytes to r2
    swi segmt               @ ; print input on segment
    ldmfd sp!,{r0-r2,pc}
@ ;---------------------------------------------------------
clear:
    stmfd sp!,{r0,lr}
    mov r0,#0
    swi segmt               @ ; clear segment
    swi setlt               @ ; clear led
    swi clssn               @ ; clear screen
    ldmfd sp!,{r0,pc}
@ ;---------------------------------------------------------
terminate:
    bl clear                @ ; clear board
    swi 0x11                @ ; close program
@ ;---------------------------------------------------------
    .data
decimal0: .asciz "Initial decimal value: "
decimal1: .asciz "Current decimal value: "
blackbtn: .asciz "Select a black button: "
bluekeys: .asciz "Select a blue key pad: "
options0: .asciz "Press blue key 1 to continue"
options1: .asciz "Press blue key 2 to restart"
options2: .asciz "Press blue key 3 to quit"
errormsg: .asciz "Wrong key. Please try again."
eqnline0: .asciz "----"
keyarray:
    .word 0x8000,0x4000,0x2000,0x1000,0x800,0x400,0x204,0x100
    .word 0x80,0x40,0x20,0x10,0x08,0x04,0x02,0x01
    .word 0
segarray:
    .word 0xED,0x60,0xCE,0xEA,0x63,0xAB,0xAF,0xE0
    .word 0xEF,0xE3,0xE7,0x2F,0x8D,0x6E,0x8F,0x87
    .word 0
    .end
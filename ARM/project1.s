@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
@   COMP 122-16311-FA2019, ASSIGNMENT PROJECT #1    @
@   Copyright(c), Philip D. Kim:                    @
@       A simple program in ARM using ARMSim# to    @
@   read from input file and stdout the following   @
@   results.                                        @
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
@   Read from 'integers.dat':                       @
@   1. First integer value x.                       @
@   2. Second integer value y.                      @
@   3. Numbers equal to or greater than x.          @
@   4. Number of times y appears in the file.       @
@   5. Total number of integers.                    @
@   integers.dat: '-20 101 45 17 -59 101 1 101'     @
@   stdout: '-20 101 7 3 8'                         @
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
@    Global Constants.                              @
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	.equ printstr,0x69
	.equ printint,0x6b
	.equ stdout,1
	.global _start
	.text

@ r5,r6,r7 counters, r3 memory stack for x and y
@ b msg_fio print initial msg
_start:
    mov r5,#0
    mov r6,#0
    mov r7,#0
	ldr r3,=array
	b msg_fio
	
@ read input file, read until end of file, or errors.
inf_loop:
	bl load_memory
	swi 0x6c
	bcs eof
	@ If counter for integers is less than 1
    cmp r5,#1
    @ then print initial title for integers.
    blt print_ints
    @ then start printing integers to console.
    bge stdout_int
	b inf_loop

@ prints the title of the program and filename.
msg_fio:           
    ldr r1,=arm_start
    bl ptrhelp
    b file_open

@ func reads input file and bcs checks for missing file.
file_open:
    mov r1,#0
    ldr r0,=file_name
    swi 0x66
    bcs file_err
    ldr r1,=file_mem
    str r0,[r1]
    b inf_loop

@ prints initial title for integers.
print_ints:             
    mov r2,r0
    mov r0,#stdout
    ldr r1,=file_ints
    swi printstr
    mov r0,r2
    mov r2,#0
    b stdout_int

@ func to stdout integers.
@ Also counts for spacing, adds a new line after (8).
@ And also a counter to keep track of all integers. 
stdout_int:
    mov r1,r0
    mov r0,#stdout
    swi printint
    add r5,r5,#1
    b proc_ints
 
@ func stores x and y onto a stack.
@ compares x and y with other numbers.
proc_ints:
    cmp r5,#1
    streq r1,[r3,#0]
    cmp r5,#2
    streq r1,[r3,#4]
    ldr r0,[r3,#0]
    cmp r1,r0
    addge r6,r6,#1
    ldr r0,[r3,#4]
    cmp r0,r1
    addeq r7,r7,#1
    bl printcma
    b inf_loop
	
@ func stdout the final results.
prt_res:
    ldr r1,=file_done
    bl ptrhelp
	@ print x
	ldr r1,=ptr_x
	bl ptrhelp
    ldr r1,[r3,#0]
    swi printint
    bl printnl
	@ print y
	ldr r1,=ptr_y
	bl ptrhelp
	ldr r1,[r3,#4]
    swi printint    
	bl printnl
	@ print n >= x
	ldr r1,=ptr_gex
	bl ptrhelp
	mov r1,r6
    swi printint    
	bl printnl
    @ print n == y
	ldr r1,=ptr_eqy
	bl ptrhelp
	mov r1,r7
    swi printint
	bl printnl
	@ print total
	ldr r1,=ptr_count
	bl ptrhelp
    mov r1,r5
	swi printint
	b terminate

@ func to print comma.
printcma:
    mov r0,#stdout
    ldr r1,=fmt_cma
    swi printstr
    bx lr

@ func to print comma.
printnl:
    mov r0,#stdout
    ldr r1,=fmt_nl
    swi printstr
    bx lr

@ func handles end of file.
@ breaks to either integer error or 
@ prints the final result.
eof:
    bl load_memory
    swi 0x68
    cmp r5,#0
    bleq int_err
    blgt prt_res

@ func handles missing integers.
int_err:
    ldr r1,=file_errint
    bl ptrhelp
    b terminate

@ func handles missing file.
file_err:
    ldr r1,=file_errdne
    bl ptrhelp
    b terminate

@ func terminates the program.
terminate:
    ldr r1,=arm_end
	bl ptrhelp
	swi 0x11

@ func to load input file into r0 memory.
load_memory:
    ldr r0,=file_mem
    ldr r0,[r0]
    bx lr

@ func to print, used in many funcs.
ptrhelp:
    mov r0,#stdout
    swi printstr
    bx lr


	.data
	.align
@ stack of memory called array for x and y/.
array:          .word 0, 0, 0, 0
@ for loading filename into memory.
file_mem:       .skip 4
@ strings for beginning, results, errors, and end.
arm_start:      .ascii " ARMSim# START:\n FILE: "
@@@@@@@@@@@@@ FILE NAME @@@@@@@@@@@@@@@@@@
file_name:      .asciz "integers.dat"
@@@@@@@@@@@@@ FILE NAME @@@@@@@@@@@@@@@@@@
arm_end:        .ascii "\n-------------------------------------------------\n" 
                .asciz " ARMSim# END.\n Copyright(c), Philip D. Kim"
file_ints:      .ascii "\n-------------------------------------------------\n"
                .asciz " INTEGERS:\n\t"
file_done:      .ascii "\n-------------------------------------------------\n"
                .asciz " RESULT: "
file_errint:    .ascii "\n-------------------------------------------------\n"
                .asciz " ERROR: Integers do not exists."
file_errdne:    .ascii "\n-------------------------------------------------\n"
                .asciz " ERROR: integers.dat does not exists."
ptr_x:			.asciz "\n\tFirst integer value x: "
ptr_y:			.asciz "Second integer value y: "
ptr_gex:		.asciz "Numbers equal to or greater than x: "
ptr_eqy:		.asciz "Number of times y appears in the file: "
ptr_count:		.asciz "Total number of integers: "
@ strings for formats.
fmt_cma:        .asciz ", "
fmt_nl:			.asciz "\n\t"
	.end
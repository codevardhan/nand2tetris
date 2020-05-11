
// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

@R0         // a = R0
D=M
@a
M=D

@R1        // if R1 = 1
D=M-1
@ONE
D;JEQ

@R0       // if R0 or R1 = 0
D=M
@ZERO
D;JEQ
@R1
D=M
@ZERO
D;JEQ

@i         // i = 1
M=1

(LOOP)
@i
D=M
@R1       // i < R1  
D=D-M
@MULT
D;JEQ

@R0      // a = a + R0
D=M
@a
M=M+D

@i       // i++
M=M+1
@LOOP
0;JMP

(MULT)   // set value of multiplication to R2
@a
D=M
@R2
M=D
@END
0;JMP


(ZERO)  // set value of multiplication to R2 when R1 or R2 is zero
@R2
M=0
@END
0;JMP

(ONE)   // set value of multiplication to R2 when R1 is one
@R0
D=M
@R2
M=D
@END
0;JMP

(END)
@END
0;JMP

// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.
@KBD
D=M
@PRESSED
D;JNE
@NOTPRESSED
D;JEQ

(PRESSED)
@SCREEN
D=A
@pixel
M=D
(BLOOP)
@KBD
D=M
@NOTPRESSED
D;JEQ
@pixel
A=M
M=-1
@pixel
M=M+1
D=M
@24576
D=D-A
@RESET1
D;JEQ
@BLOOP
0;JMP

(NOTPRESSED)
@SCREEN
D=A
@pixel
M=D
(WLOOP)	
@KBD
D=M
@PRESSED
D;JNE
@pixel
A=M
M=0
@pixel
M=M+1
D=M
@24576
D=D-A
@RESET2
D;JEQ
@WLOOP
0;JMP

(RESET1)
@KBD
D=M
@NOTPRESSED
D;JEQ
@RESET1
0;JMP

(RESET2)
@KBD
D=M
@PRESSED
D;JNE
@RESET2
0;JMP
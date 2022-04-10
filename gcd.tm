* Prelude
  0:    LD 6,0(0) 	load gp with maxaddr
  1:   LDA 5,0(6) 	copy gp to fp
  2:    ST 0,0(0) 	clear content at loc 0
* input()
  4:    ST 0,-1(5) 	store return
  5:    IN 0,0,0 	input
  6:    LD 7,-1(5) 	return to caller
* output()
  7:    ST 0,-1(5) 	store return
  8:    LD 0,-2(5) 	load output value
  9:   OUT 0,0,0 	output
 10:    LD 7,-1(5) 	return to caller
* function decl: gcd
 11:    ST 0,-1(5) 	store return
 12:    LD 0,-3(5) 	loading value of v into ac
 13:    ST 0,-5(5) 	store lhs in memory
* -> constant
 14:   LDC 0,0(0) 	load const
* <- constant
 15:    ST 0,-6(5) 	store rhs in memory
 16:    LD 0,-5(5) 	load lhs into reg0
 17:    LD 1,-6(5) 	load rhs into reg1
 18:   SUB 0,0,1 	sub rhs from lhs
 19:   JEQ 0,2(7) 	check if 0
 20:   LDC 0,0(0) 	false
 21:   LDA 7,1(7) 	jump around true
 22:   LDC 0,1(0) 	true
 23:    ST 0,-4(5) 	store result
 25:    LD 0,-2(5) 	loading value of u into ac
 26:    LD 7,-1(5) 	return to caller
 28:    LD 0,-3(5) 	loading value of v into ac
 29:    ST 0,-6(5) 	load function argument
 30:    LD 0,-2(5) 	loading value of u into ac
 31:    ST 0,-8(5) 	store lhs in memory
 32:    LD 0,-2(5) 	loading value of u into ac
 33:    ST 0,-11(5) 	store lhs in memory
 34:    LD 0,-3(5) 	loading value of v into ac
 35:    ST 0,-12(5) 	store rhs in memory
 36:    LD 0,-11(5) 	load lhs into reg0
 37:    LD 1,-12(5) 	load rhs into reg1
 38:   DIV 0,0,1 	divide lhs with rhs
 39:    ST 0,-10(5) 	store result
 40:    ST 0,-10(5) 	store lhs in memory
 41:    LD 0,-3(5) 	loading value of v into ac
 42:    ST 0,-11(5) 	store rhs in memory
 43:    LD 0,-10(5) 	load lhs into reg0
 44:    LD 1,-11(5) 	load rhs into reg1
 45:   MUL 0,0,1 	multiply lhs and rhs
 46:    ST 0,-9(5) 	store result
 47:    ST 0,-9(5) 	store rhs in memory
 48:    LD 0,-8(5) 	load lhs into reg0
 49:    LD 1,-9(5) 	load rhs into reg1
 50:   SUB 0,0,1 	subtract rhs from lhs
 51:    ST 0,-7(5) 	store result
 52:    ST 0,-7(5) 	load function argument
 53:    ST 5,-4(5) 	store
 54:   LDA 5,-4(5) 	load new frame
 55:   LDA 0,1(7) 	save return in ac
 56:   LDA 7,-46(7) 	jump to entry point of gcd
 57:    LD 5,0(5) 	pop frame
 58:    LD 7,-1(5) 	return to caller
 27:   LDA 7,31(7) 	jump past else
 24:   JEQ 0,3(7) 	jump if false
 59:    LD 7,-1(5) 	return to caller
* function decl: main
 60:    ST 0,-1(5) 	store return
 61:   LDA 0,-2(5) 	loading address of x into ac
 62:    ST 0,-5(5) 	store lhs address in memory
 63:    ST 5,-6(5) 	store
 64:   LDA 5,-6(5) 	load new frame
 65:   LDA 0,1(7) 	save return in ac
 66:   LDA 7,-63(7) 	jump to entry point of input
 67:    LD 5,0(5) 	pop frame
 68:    ST 0,-6(5) 	store rhs in memory
 69:    LD 0,-5(5) 	load lhs into ac
 70:    LD 1,-6(5) 	load result of rhs into ac1
 71:    ST 1,0(0) 	write rhs into address given by lhs
 72:    ST 1,-4(5) 	store result
 73:   LDA 0,-3(5) 	loading address of y into ac
 74:    ST 0,-5(5) 	store lhs address in memory
 75:    ST 5,-6(5) 	store
 76:   LDA 5,-6(5) 	load new frame
 77:   LDA 0,1(7) 	save return in ac
 78:   LDA 7,-75(7) 	jump to entry point of input
 79:    LD 5,0(5) 	pop frame
 80:    ST 0,-6(5) 	store rhs in memory
 81:    LD 0,-5(5) 	load lhs into ac
 82:    LD 1,-6(5) 	load result of rhs into ac1
 83:    ST 1,0(0) 	write rhs into address given by lhs
 84:    ST 1,-4(5) 	store result
 85:    LD 0,-2(5) 	loading value of x into ac
 86:    ST 0,-8(5) 	load function argument
 87:    LD 0,-3(5) 	loading value of y into ac
 88:    ST 0,-9(5) 	load function argument
 89:    ST 5,-6(5) 	store
 90:   LDA 5,-6(5) 	load new frame
 91:   LDA 0,1(7) 	save return in ac
 92:   LDA 7,-82(7) 	jump to entry point of gcd
 93:    LD 5,0(5) 	pop frame
 94:    ST 0,-6(5) 	load function argument
 95:    ST 5,-4(5) 	store
 96:   LDA 5,-4(5) 	load new frame
 97:   LDA 0,1(7) 	save return in ac
 98:   LDA 7,-92(7) 	jump to entry point of output
 99:    LD 5,0(5) 	pop frame
100:    LD 7,-1(5) 	return to caller
  3:   LDA 7,97(7) 	
* finale
101:    ST 5,0(5) 	push old frame pointer
102:   LDA 5,0(5) 	push frame
103:   LDA 0,1(7) 	load ac with return pointer
104:   LDA 7,-45(7) 	jump to main loc
105:    LD 5,0(5) 	pop frame
106:  HALT 0,0,0 	

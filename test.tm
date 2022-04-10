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
* function decl: foo2
 11:    ST 0,-1(5) 	store return
 12:   LDA 0,-4(5) 	loading address of i into ac
 13:    ST 0,-6(5) 	store lhs address in memory
* -> constant
 14:   LDC 0,0(0) 	load const
* <- constant
 15:    ST 0,-7(5) 	store rhs in memory
 16:    LD 0,-6(5) 	load lhs into ac
 17:    LD 1,-7(5) 	load result of rhs into ac1
 18:    ST 1,0(0) 	write rhs into address given by lhs
 19:    ST 1,-5(5) 	store result
 20:    LD 0,-4(5) 	loading value of i into ac
 21:    ST 0,-6(5) 	store lhs in memory
* -> constant
 22:   LDC 0,5(0) 	load const
* <- constant
 23:    ST 0,-7(5) 	store rhs in memory
 24:    LD 0,-6(5) 	load lhs into reg0
 25:    LD 1,-7(5) 	load rhs into reg1
 26:   SUB 0,0,1 	sub rhs from lhs
 27:   JLT 0,2(7) 	check lhs < rhs
 28:   LDC 0,0(0) 	false
 29:   LDA 7,1(7) 	jump around true
 30:   LDC 0,1(0) 	true
 31:    ST 0,-5(5) 	store result
 33:    LD 0,-4(5) 	loading value of i into ac
 34:    LD 1,-2(5) 	load arr address into ac
 35:   ADD 0,0,1 	get final array base address
 36:    ST 0,-6(5) 	store lhs address in memory
 37:    LD 0,-4(5) 	loading value of i into ac
 38:    LD 1,-2(5) 	load arr address into ac
 39:   ADD 0,0,1 	get final array base address
 40:    LD 0,0(0) 	store value into index
 41:    ST 0,-8(5) 	store lhs in memory
 42:    LD 0,-3(5) 	loading value of x into ac
 43:    ST 0,-9(5) 	store rhs in memory
 44:    LD 0,-8(5) 	load lhs into reg0
 45:    LD 1,-9(5) 	load rhs into reg1
 46:   MUL 0,0,1 	multiply lhs and rhs
 47:    ST 0,-7(5) 	store result
 48:    ST 0,-7(5) 	store rhs in memory
 49:    LD 0,-6(5) 	load lhs into ac
 50:    LD 1,-7(5) 	load result of rhs into ac1
 51:    ST 1,0(0) 	write rhs into address given by lhs
 52:    ST 1,-5(5) 	store result
 53:    LD 0,-4(5) 	loading value of i into ac
 54:    LD 1,-2(5) 	load arr address into ac
 55:   ADD 0,0,1 	get final array base address
 56:    LD 0,0(0) 	store value into index
 57:    ST 0,-7(5) 	load function argument
 58:    ST 5,-5(5) 	store
 59:   LDA 5,-5(5) 	load new frame
 60:   LDA 0,1(7) 	save return in ac
 61:   LDA 7,-55(7) 	jump to entry point of output
 62:    LD 5,0(5) 	pop frame
 63:   LDA 0,-4(5) 	loading address of i into ac
 64:    ST 0,-6(5) 	store lhs address in memory
 65:    LD 0,-4(5) 	loading value of i into ac
 66:    ST 0,-8(5) 	store lhs in memory
* -> constant
 67:   LDC 0,1(0) 	load const
* <- constant
 68:    ST 0,-9(5) 	store rhs in memory
 69:    LD 0,-8(5) 	load lhs into reg0
 70:    LD 1,-9(5) 	load rhs into reg1
 71:   ADD 0,0,1 	add lhs and rhs
 72:    ST 0,-7(5) 	store result
 73:    ST 0,-7(5) 	store rhs in memory
 74:    LD 0,-6(5) 	load lhs into ac
 75:    LD 1,-7(5) 	load result of rhs into ac1
 76:    ST 1,0(0) 	write rhs into address given by lhs
 77:    ST 1,-5(5) 	store result
 78:   LDA 7,-59(7) 	jump to test
 32:   JEQ 0,46(7) 	jump if true
 79:    LD 7,-1(5) 	return to caller
* function decl: foo
 80:    ST 0,-1(5) 	store return
 81:    LD 0,-2(5) 	loading address pointer of a into ac
 82:    ST 0,-6(5) 	load function argument
 83:    LD 0,-3(5) 	loading value of x into ac
 84:    ST 0,-7(5) 	load function argument
 85:    ST 5,-4(5) 	store
 86:   LDA 5,-4(5) 	load new frame
 87:   LDA 0,1(7) 	save return in ac
 88:   LDA 7,-78(7) 	jump to entry point of foo2
 89:    LD 5,0(5) 	pop frame
 90:    LD 7,-1(5) 	return to caller
* function decl: main
 91:    ST 0,-1(5) 	store return
 92:   LDC 0,5(0) 	load array size into register
 93:    ST 0,-7(5) 	store array size in memory
 94:   LDA 0,-8(5) 	loading address of i into ac
 95:    ST 0,-10(5) 	store lhs address in memory
* -> constant
 96:   LDC 0,0(0) 	load const
* <- constant
 97:    ST 0,-11(5) 	store rhs in memory
 98:    LD 0,-10(5) 	load lhs into ac
 99:    LD 1,-11(5) 	load result of rhs into ac1
100:    ST 1,0(0) 	write rhs into address given by lhs
101:    ST 1,-9(5) 	store result
102:    LD 0,-8(5) 	loading value of i into ac
103:    ST 0,-10(5) 	store lhs in memory
* -> constant
104:   LDC 0,5(0) 	load const
* <- constant
105:    ST 0,-11(5) 	store rhs in memory
106:    LD 0,-10(5) 	load lhs into reg0
107:    LD 1,-11(5) 	load rhs into reg1
108:   SUB 0,0,1 	sub rhs from lhs
109:   JLT 0,2(7) 	check lhs < rhs
110:   LDC 0,0(0) 	false
111:   LDA 7,1(7) 	jump around true
112:   LDC 0,1(0) 	true
113:    ST 0,-9(5) 	store result
115:    LD 0,-8(5) 	loading value of i into ac
116:   LDA 1,-6(5) 	load array base address
117:   ADD 0,0,1 	get final array base address
118:    ST 0,-10(5) 	store lhs address in memory
119:    LD 0,-8(5) 	loading value of i into ac
120:    ST 0,-11(5) 	store rhs in memory
121:    LD 0,-10(5) 	load lhs into ac
122:    LD 1,-11(5) 	load result of rhs into ac1
123:    ST 1,0(0) 	write rhs into address given by lhs
124:    ST 1,-9(5) 	store result
125:   LDA 0,-8(5) 	loading address of i into ac
126:    ST 0,-10(5) 	store lhs address in memory
127:    LD 0,-8(5) 	loading value of i into ac
128:    ST 0,-12(5) 	store lhs in memory
* -> constant
129:   LDC 0,1(0) 	load const
* <- constant
130:    ST 0,-13(5) 	store rhs in memory
131:    LD 0,-12(5) 	load lhs into reg0
132:    LD 1,-13(5) 	load rhs into reg1
133:   ADD 0,0,1 	add lhs and rhs
134:    ST 0,-11(5) 	store result
135:    ST 0,-11(5) 	store rhs in memory
136:    LD 0,-10(5) 	load lhs into ac
137:    LD 1,-11(5) 	load result of rhs into ac1
138:    ST 1,0(0) 	write rhs into address given by lhs
139:    ST 1,-9(5) 	store result
140:   LDA 7,-39(7) 	jump to test
114:   JEQ 0,26(7) 	jump if true
141:   LDA 0,-6(5) 	loading address of a into ac
142:    ST 0,-11(5) 	load function argument
* -> constant
143:   LDC 0,10(0) 	load const
* <- constant
144:    ST 0,-12(5) 	load function argument
145:    ST 5,-9(5) 	store
146:   LDA 5,-9(5) 	load new frame
147:   LDA 0,1(7) 	save return in ac
148:   LDA 7,-69(7) 	jump to entry point of foo
149:    LD 5,0(5) 	pop frame
150:   LDA 0,-8(5) 	loading address of i into ac
151:    ST 0,-10(5) 	store lhs address in memory
* -> constant
152:   LDC 0,0(0) 	load const
* <- constant
153:    ST 0,-11(5) 	store rhs in memory
154:    LD 0,-10(5) 	load lhs into ac
155:    LD 1,-11(5) 	load result of rhs into ac1
156:    ST 1,0(0) 	write rhs into address given by lhs
157:    ST 1,-9(5) 	store result
158:    LD 0,-8(5) 	loading value of i into ac
159:    ST 0,-10(5) 	store lhs in memory
* -> constant
160:   LDC 0,5(0) 	load const
* <- constant
161:    ST 0,-11(5) 	store rhs in memory
162:    LD 0,-10(5) 	load lhs into reg0
163:    LD 1,-11(5) 	load rhs into reg1
164:   SUB 0,0,1 	sub rhs from lhs
165:   JLT 0,2(7) 	check lhs < rhs
166:   LDC 0,0(0) 	false
167:   LDA 7,1(7) 	jump around true
168:   LDC 0,1(0) 	true
169:    ST 0,-9(5) 	store result
171:    LD 0,-8(5) 	loading value of i into ac
172:   LDA 1,-6(5) 	load array base address
173:   ADD 0,0,1 	get final array base address
174:    ST 0,-10(5) 	store lhs address in memory
175:    LD 0,-8(5) 	loading value of i into ac
176:   LDA 1,-6(5) 	load array base address
177:   ADD 0,0,1 	get final array base address
178:    LD 0,0(0) 	store value into index
179:    ST 0,-12(5) 	store lhs in memory
* -> constant
180:   LDC 0,10(0) 	load const
* <- constant
181:    ST 0,-13(5) 	store rhs in memory
182:    LD 0,-12(5) 	load lhs into reg0
183:    LD 1,-13(5) 	load rhs into reg1
184:   MUL 0,0,1 	multiply lhs and rhs
185:    ST 0,-11(5) 	store result
186:    ST 0,-11(5) 	store rhs in memory
187:    LD 0,-10(5) 	load lhs into ac
188:    LD 1,-11(5) 	load result of rhs into ac1
189:    ST 1,0(0) 	write rhs into address given by lhs
190:    ST 1,-9(5) 	store result
191:    LD 0,-8(5) 	loading value of i into ac
192:   LDA 1,-6(5) 	load array base address
193:   ADD 0,0,1 	get final array base address
194:    LD 0,0(0) 	store value into index
195:    ST 0,-11(5) 	load function argument
196:    ST 5,-9(5) 	store
197:   LDA 5,-9(5) 	load new frame
198:   LDA 0,1(7) 	save return in ac
199:   LDA 7,-193(7) 	jump to entry point of output
200:    LD 5,0(5) 	pop frame
201:   LDA 0,-8(5) 	loading address of i into ac
202:    ST 0,-10(5) 	store lhs address in memory
203:    LD 0,-8(5) 	loading value of i into ac
204:    ST 0,-12(5) 	store lhs in memory
* -> constant
205:   LDC 0,1(0) 	load const
* <- constant
206:    ST 0,-13(5) 	store rhs in memory
207:    LD 0,-12(5) 	load lhs into reg0
208:    LD 1,-13(5) 	load rhs into reg1
209:   ADD 0,0,1 	add lhs and rhs
210:    ST 0,-11(5) 	store result
211:    ST 0,-11(5) 	store rhs in memory
212:    LD 0,-10(5) 	load lhs into ac
213:    LD 1,-11(5) 	load result of rhs into ac1
214:    ST 1,0(0) 	write rhs into address given by lhs
215:    ST 1,-9(5) 	store result
216:   LDA 7,-59(7) 	jump to test
170:   JEQ 0,46(7) 	jump if true
217:    LD 7,-1(5) 	return to caller
  3:   LDA 7,214(7) 	
* finale
218:    ST 5,0(5) 	push old frame pointer
219:   LDA 5,0(5) 	push frame
220:   LDA 0,1(7) 	load ac with return pointer
221:   LDA 7,-131(7) 	jump to main loc
222:    LD 5,0(5) 	pop frame
223:  HALT 0,0,0 	

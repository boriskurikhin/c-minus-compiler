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
* function decl: minloc
 11:    ST 0,-1(5) 	store return
 12:   LDA 0,-7(5) 	loading address of k into ac
 13:    ST 0,-9(5) 	store lhs address in memory
 14:    LD 0,-3(5) 	loading value of low into ac
 15:    ST 0,-10(5) 	store rhs in memory
 16:    LD 0,-9(5) 	load lhs into ac
 17:    LD 1,-10(5) 	load result of rhs into ac1
 18:    ST 1,0(0) 	write rhs into address given by lhs
 19:    ST 1,-8(5) 	store result
 20:   LDA 0,-6(5) 	loading address of x into ac
 21:    ST 0,-9(5) 	store lhs address in memory
 22:    LD 0,-3(5) 	loading value of low into ac
 23:    LD 1,-2(5) 	load arr address into ac
 24:   ADD 0,0,1 	get final array base address
 25:    LD 0,0(0) 	store value into index
 26:    ST 0,-10(5) 	store rhs in memory
 27:    LD 0,-9(5) 	load lhs into ac
 28:    LD 1,-10(5) 	load result of rhs into ac1
 29:    ST 1,0(0) 	write rhs into address given by lhs
 30:    ST 1,-8(5) 	store result
 31:   LDA 0,-5(5) 	loading address of i into ac
 32:    ST 0,-9(5) 	store lhs address in memory
 33:    LD 0,-3(5) 	loading value of low into ac
 34:    ST 0,-11(5) 	store lhs in memory
* -> constant
 35:   LDC 0,1(0) 	load const
* <- constant
 36:    ST 0,-12(5) 	store rhs in memory
 37:    LD 0,-11(5) 	load lhs into reg0
 38:    LD 1,-12(5) 	load rhs into reg1
 39:   ADD 0,0,1 	add lhs and rhs
 40:    ST 0,-10(5) 	store result
 41:    ST 0,-10(5) 	store rhs in memory
 42:    LD 0,-9(5) 	load lhs into ac
 43:    LD 1,-10(5) 	load result of rhs into ac1
 44:    ST 1,0(0) 	write rhs into address given by lhs
 45:    ST 1,-8(5) 	store result
 46:    LD 0,-5(5) 	loading value of i into ac
 47:    ST 0,-9(5) 	store lhs in memory
 48:    LD 0,-4(5) 	loading value of high into ac
 49:    ST 0,-10(5) 	store rhs in memory
 50:    LD 0,-9(5) 	load lhs into reg0
 51:    LD 1,-10(5) 	load rhs into reg1
 52:   SUB 0,0,1 	sub rhs from lhs
 53:   JLT 0,2(7) 	check lhs < rhs
 54:   LDC 0,0(0) 	false
 55:   LDA 7,1(7) 	jump around true
 56:   LDC 0,1(0) 	true
 57:    ST 0,-8(5) 	store result
 59:    LD 0,-5(5) 	loading value of i into ac
 60:    LD 1,-2(5) 	load arr address into ac
 61:   ADD 0,0,1 	get final array base address
 62:    LD 0,0(0) 	store value into index
 63:    ST 0,-9(5) 	store lhs in memory
 64:    LD 0,-6(5) 	loading value of x into ac
 65:    ST 0,-10(5) 	store rhs in memory
 66:    LD 0,-9(5) 	load lhs into reg0
 67:    LD 1,-10(5) 	load rhs into reg1
 68:   SUB 0,0,1 	sub rhs from lhs
 69:   JLT 0,2(7) 	check lhs < rhs
 70:   LDC 0,0(0) 	false
 71:   LDA 7,1(7) 	jump around true
 72:   LDC 0,1(0) 	true
 73:    ST 0,-8(5) 	store result
 75:   LDA 0,-6(5) 	loading address of x into ac
 76:    ST 0,-9(5) 	store lhs address in memory
 77:    LD 0,-5(5) 	loading value of i into ac
 78:    LD 1,-2(5) 	load arr address into ac
 79:   ADD 0,0,1 	get final array base address
 80:    LD 0,0(0) 	store value into index
 81:    ST 0,-10(5) 	store rhs in memory
 82:    LD 0,-9(5) 	load lhs into ac
 83:    LD 1,-10(5) 	load result of rhs into ac1
 84:    ST 1,0(0) 	write rhs into address given by lhs
 85:    ST 1,-8(5) 	store result
 86:   LDA 0,-7(5) 	loading address of k into ac
 87:    ST 0,-9(5) 	store lhs address in memory
 88:    LD 0,-5(5) 	loading value of i into ac
 89:    ST 0,-10(5) 	store rhs in memory
 90:    LD 0,-9(5) 	load lhs into ac
 91:    LD 1,-10(5) 	load result of rhs into ac1
 92:    ST 1,0(0) 	write rhs into address given by lhs
 93:    ST 1,-8(5) 	store result
 74:   JEQ 0,19(7) 	jump if false
 94:   LDA 0,-5(5) 	loading address of i into ac
 95:    ST 0,-9(5) 	store lhs address in memory
 96:    LD 0,-5(5) 	loading value of i into ac
 97:    ST 0,-11(5) 	store lhs in memory
* -> constant
 98:   LDC 0,1(0) 	load const
* <- constant
 99:    ST 0,-12(5) 	store rhs in memory
100:    LD 0,-11(5) 	load lhs into reg0
101:    LD 1,-12(5) 	load rhs into reg1
102:   ADD 0,0,1 	add lhs and rhs
103:    ST 0,-10(5) 	store result
104:    ST 0,-10(5) 	store rhs in memory
105:    LD 0,-9(5) 	load lhs into ac
106:    LD 1,-10(5) 	load result of rhs into ac1
107:    ST 1,0(0) 	write rhs into address given by lhs
108:    ST 1,-8(5) 	store result
109:   LDA 7,-64(7) 	jump to test
 58:   JEQ 0,51(7) 	jump if true
110:    LD 0,-7(5) 	loading value of k into ac
111:    LD 7,-1(5) 	return to caller
112:    LD 7,-1(5) 	return to caller
* function decl: sort
113:    ST 0,-1(5) 	store return
114:   LDA 0,-5(5) 	loading address of i into ac
115:    ST 0,-9(5) 	store lhs address in memory
116:    LD 0,-3(5) 	loading value of low into ac
117:    ST 0,-10(5) 	store rhs in memory
118:    LD 0,-9(5) 	load lhs into ac
119:    LD 1,-10(5) 	load result of rhs into ac1
120:    ST 1,0(0) 	write rhs into address given by lhs
121:    ST 1,-8(5) 	store result
122:    LD 0,-5(5) 	loading value of i into ac
123:    ST 0,-9(5) 	store lhs in memory
124:    LD 0,-4(5) 	loading value of high into ac
125:    ST 0,-11(5) 	store lhs in memory
* -> constant
126:   LDC 0,1(0) 	load const
* <- constant
127:    ST 0,-12(5) 	store rhs in memory
128:    LD 0,-11(5) 	load lhs into reg0
129:    LD 1,-12(5) 	load rhs into reg1
130:   SUB 0,0,1 	subtract rhs from lhs
131:    ST 0,-10(5) 	store result
132:    ST 0,-10(5) 	store rhs in memory
133:    LD 0,-9(5) 	load lhs into reg0
134:    LD 1,-10(5) 	load rhs into reg1
135:   SUB 0,0,1 	sub rhs from lhs
136:   JLT 0,2(7) 	check lhs < rhs
137:   LDC 0,0(0) 	false
138:   LDA 7,1(7) 	jump around true
139:   LDC 0,1(0) 	true
140:    ST 0,-8(5) 	store result
142:   LDA 0,-6(5) 	loading address of k into ac
143:    ST 0,-9(5) 	store lhs address in memory
144:    LD 0,-2(5) 	loading address pointer of a into ac
145:    ST 0,-12(5) 	load function argument
146:    LD 0,-5(5) 	loading value of i into ac
147:    ST 0,-13(5) 	load function argument
148:    LD 0,-4(5) 	loading value of high into ac
149:    ST 0,-14(5) 	load function argument
150:    ST 5,-10(5) 	store
151:   LDA 5,-10(5) 	load new frame
152:   LDA 0,1(7) 	save return in ac
153:   LDA 7,-143(7) 	jump to entry point of minloc
154:    LD 5,0(5) 	pop frame
155:    ST 0,-10(5) 	store rhs in memory
156:    LD 0,-9(5) 	load lhs into ac
157:    LD 1,-10(5) 	load result of rhs into ac1
158:    ST 1,0(0) 	write rhs into address given by lhs
159:    ST 1,-8(5) 	store result
160:   LDA 0,-7(5) 	loading address of t into ac
161:    ST 0,-9(5) 	store lhs address in memory
162:    LD 0,-6(5) 	loading value of k into ac
163:    LD 1,-2(5) 	load arr address into ac
164:   ADD 0,0,1 	get final array base address
165:    LD 0,0(0) 	store value into index
166:    ST 0,-10(5) 	store rhs in memory
167:    LD 0,-9(5) 	load lhs into ac
168:    LD 1,-10(5) 	load result of rhs into ac1
169:    ST 1,0(0) 	write rhs into address given by lhs
170:    ST 1,-8(5) 	store result
171:    LD 0,-6(5) 	loading value of k into ac
172:    LD 1,-2(5) 	load arr address into ac
173:   ADD 0,0,1 	get final array base address
174:    ST 0,-9(5) 	store lhs address in memory
175:    LD 0,-5(5) 	loading value of i into ac
176:    LD 1,-2(5) 	load arr address into ac
177:   ADD 0,0,1 	get final array base address
178:    LD 0,0(0) 	store value into index
179:    ST 0,-10(5) 	store rhs in memory
180:    LD 0,-9(5) 	load lhs into ac
181:    LD 1,-10(5) 	load result of rhs into ac1
182:    ST 1,0(0) 	write rhs into address given by lhs
183:    ST 1,-8(5) 	store result
184:    LD 0,-5(5) 	loading value of i into ac
185:    LD 1,-2(5) 	load arr address into ac
186:   ADD 0,0,1 	get final array base address
187:    ST 0,-9(5) 	store lhs address in memory
188:    LD 0,-7(5) 	loading value of t into ac
189:    ST 0,-10(5) 	store rhs in memory
190:    LD 0,-9(5) 	load lhs into ac
191:    LD 1,-10(5) 	load result of rhs into ac1
192:    ST 1,0(0) 	write rhs into address given by lhs
193:    ST 1,-8(5) 	store result
194:   LDA 0,-5(5) 	loading address of i into ac
195:    ST 0,-9(5) 	store lhs address in memory
196:    LD 0,-5(5) 	loading value of i into ac
197:    ST 0,-11(5) 	store lhs in memory
* -> constant
198:   LDC 0,1(0) 	load const
* <- constant
199:    ST 0,-12(5) 	store rhs in memory
200:    LD 0,-11(5) 	load lhs into reg0
201:    LD 1,-12(5) 	load rhs into reg1
202:   ADD 0,0,1 	add lhs and rhs
203:    ST 0,-10(5) 	store result
204:    ST 0,-10(5) 	store rhs in memory
205:    LD 0,-9(5) 	load lhs into ac
206:    LD 1,-10(5) 	load result of rhs into ac1
207:    ST 1,0(0) 	write rhs into address given by lhs
208:    ST 1,-8(5) 	store result
209:   LDA 7,-88(7) 	jump to test
141:   JEQ 0,68(7) 	jump if true
210:    LD 7,-1(5) 	return to caller
* function decl: main
211:    ST 0,-1(5) 	store return
212:   LDC 0,10(0) 	load array size into register
213:    ST 0,-12(5) 	store array size in memory
214:   LDA 0,-13(5) 	loading address of i into ac
215:    ST 0,-15(5) 	store lhs address in memory
* -> constant
216:   LDC 0,0(0) 	load const
* <- constant
217:    ST 0,-16(5) 	store rhs in memory
218:    LD 0,-15(5) 	load lhs into ac
219:    LD 1,-16(5) 	load result of rhs into ac1
220:    ST 1,0(0) 	write rhs into address given by lhs
221:    ST 1,-14(5) 	store result
222:    LD 0,-13(5) 	loading value of i into ac
223:    ST 0,-15(5) 	store lhs in memory
* -> constant
224:   LDC 0,10(0) 	load const
* <- constant
225:    ST 0,-16(5) 	store rhs in memory
226:    LD 0,-15(5) 	load lhs into reg0
227:    LD 1,-16(5) 	load rhs into reg1
228:   SUB 0,0,1 	sub rhs from lhs
229:   JLT 0,2(7) 	check lhs < rhs
230:   LDC 0,0(0) 	false
231:   LDA 7,1(7) 	jump around true
232:   LDC 0,1(0) 	true
233:    ST 0,-14(5) 	store result
235:    LD 0,-13(5) 	loading value of i into ac
236:   LDA 1,-11(5) 	load array base address
237:   ADD 0,0,1 	get final array base address
238:    ST 0,-15(5) 	store lhs address in memory
239:    ST 5,-16(5) 	store
240:   LDA 5,-16(5) 	load new frame
241:   LDA 0,1(7) 	save return in ac
242:   LDA 7,-239(7) 	jump to entry point of input
243:    LD 5,0(5) 	pop frame
244:    ST 0,-16(5) 	store rhs in memory
245:    LD 0,-15(5) 	load lhs into ac
246:    LD 1,-16(5) 	load result of rhs into ac1
247:    ST 1,0(0) 	write rhs into address given by lhs
248:    ST 1,-14(5) 	store result
249:   LDA 0,-13(5) 	loading address of i into ac
250:    ST 0,-15(5) 	store lhs address in memory
251:    LD 0,-13(5) 	loading value of i into ac
252:    ST 0,-17(5) 	store lhs in memory
* -> constant
253:   LDC 0,1(0) 	load const
* <- constant
254:    ST 0,-18(5) 	store rhs in memory
255:    LD 0,-17(5) 	load lhs into reg0
256:    LD 1,-18(5) 	load rhs into reg1
257:   ADD 0,0,1 	add lhs and rhs
258:    ST 0,-16(5) 	store result
259:    ST 0,-16(5) 	store rhs in memory
260:    LD 0,-15(5) 	load lhs into ac
261:    LD 1,-16(5) 	load result of rhs into ac1
262:    ST 1,0(0) 	write rhs into address given by lhs
263:    ST 1,-14(5) 	store result
264:   LDA 7,-43(7) 	jump to test
234:   JEQ 0,30(7) 	jump if true
265:   LDA 0,-11(5) 	loading address of x into ac
266:    ST 0,-16(5) 	load function argument
* -> constant
267:   LDC 0,0(0) 	load const
* <- constant
268:    ST 0,-17(5) 	load function argument
* -> constant
269:   LDC 0,10(0) 	load const
* <- constant
270:    ST 0,-18(5) 	load function argument
271:    ST 5,-14(5) 	store
272:   LDA 5,-14(5) 	load new frame
273:   LDA 0,1(7) 	save return in ac
274:   LDA 7,-162(7) 	jump to entry point of sort
275:    LD 5,0(5) 	pop frame
276:   LDA 0,-13(5) 	loading address of i into ac
277:    ST 0,-15(5) 	store lhs address in memory
* -> constant
278:   LDC 0,0(0) 	load const
* <- constant
279:    ST 0,-16(5) 	store rhs in memory
280:    LD 0,-15(5) 	load lhs into ac
281:    LD 1,-16(5) 	load result of rhs into ac1
282:    ST 1,0(0) 	write rhs into address given by lhs
283:    ST 1,-14(5) 	store result
284:    LD 0,-13(5) 	loading value of i into ac
285:    ST 0,-15(5) 	store lhs in memory
* -> constant
286:   LDC 0,10(0) 	load const
* <- constant
287:    ST 0,-16(5) 	store rhs in memory
288:    LD 0,-15(5) 	load lhs into reg0
289:    LD 1,-16(5) 	load rhs into reg1
290:   SUB 0,0,1 	sub rhs from lhs
291:   JLT 0,2(7) 	check lhs < rhs
292:   LDC 0,0(0) 	false
293:   LDA 7,1(7) 	jump around true
294:   LDC 0,1(0) 	true
295:    ST 0,-14(5) 	store result
297:    LD 0,-13(5) 	loading value of i into ac
298:   LDA 1,-11(5) 	load array base address
299:   ADD 0,0,1 	get final array base address
300:    LD 0,0(0) 	store value into index
301:    ST 0,-16(5) 	load function argument
302:    ST 5,-14(5) 	store
303:   LDA 5,-14(5) 	load new frame
304:   LDA 0,1(7) 	save return in ac
305:   LDA 7,-299(7) 	jump to entry point of output
306:    LD 5,0(5) 	pop frame
307:   LDA 0,-13(5) 	loading address of i into ac
308:    ST 0,-15(5) 	store lhs address in memory
309:    LD 0,-13(5) 	loading value of i into ac
310:    ST 0,-17(5) 	store lhs in memory
* -> constant
311:   LDC 0,1(0) 	load const
* <- constant
312:    ST 0,-18(5) 	store rhs in memory
313:    LD 0,-17(5) 	load lhs into reg0
314:    LD 1,-18(5) 	load rhs into reg1
315:   ADD 0,0,1 	add lhs and rhs
316:    ST 0,-16(5) 	store result
317:    ST 0,-16(5) 	store rhs in memory
318:    LD 0,-15(5) 	load lhs into ac
319:    LD 1,-16(5) 	load result of rhs into ac1
320:    ST 1,0(0) 	write rhs into address given by lhs
321:    ST 1,-14(5) 	store result
322:   LDA 7,-39(7) 	jump to test
296:   JEQ 0,26(7) 	jump if true
323:    LD 7,-1(5) 	return to caller
  3:   LDA 7,320(7) 	
* finale
324:    ST 5,0(5) 	push old frame pointer
325:   LDA 5,0(5) 	push frame
326:   LDA 0,1(7) 	load ac with return pointer
327:   LDA 7,-117(7) 	jump to main loc
328:    LD 5,0(5) 	pop frame
329:  HALT 0,0,0 	

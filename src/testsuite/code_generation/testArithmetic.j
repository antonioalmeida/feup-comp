.class public testArithmetic
.super java/lang/Object

.field static x1 I 
.field static x2 [I 
.field static x3 I     
.method static public <clinit>()V
	.limit locals 0
	.limit stack 0

	return
.end method

.method public static main([Ljava/lang/String;)V

	.limit locals 2
	.limit stack 3

	iconst_1
	putstatic testArithmetic/x1 I

	iconst_1
	iconst_3
	iadd
	putstatic testArithmetic/x1 I

	iconst_5
	newarray int
	putstatic testArithmetic/x2 [I


	iconst_0
	istore_0
loop1:
	iload_0
	getstatic testArithmetic/x2 [I

	arraylength
if_icmpge loop1_end
	getstatic testArithmetic/x2 [I

	iload_0
	iconst_1
	iastore
	iinc 0 1
goto loop1
loop1_end:
	getstatic testArithmetic/x1 I
	iconst_1
	iadd
	putstatic testArithmetic/x3 I

	getstatic testArithmetic/x3 I
	invokestatic io/println(I)V

	getstatic testArithmetic/x2 [I
	iconst_1
	iaload
	putstatic testArithmetic/x3 I

	getstatic testArithmetic/x3 I
	invokestatic io/println(I)V

	return

.end method



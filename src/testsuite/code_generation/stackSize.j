.class public stackSize
.super java/lang/Object

.method static public <clinit>()V
	.limit locals 0
	.limit stack 0

	return
.end method

.method public static f(I)V

	.limit locals 1
	.limit stack 5

	iload_0

	iload_0
	iload_0
	iload_0
	iload_0
	invokestatic stackSize/h(IIII)I
	if_icmple loop1_end
		ldc "Greater"
		invokestatic io/println(Ljava/lang/String;)V

		goto loop1_next
	loop1_end:
		ldc "Not greater"
		invokestatic io/println(Ljava/lang/String;)V

	loop1_next:
	return

.end method


.method public static g(I)I

	.limit locals 1
	.limit stack 5

	iload_0
	iload_0
	iload_0
	iload_0
	iload_0
	invokestatic stackSize/h(IIII)I
	imul
	istore_0

	iload_0
	ireturn

.end method


.method public static h(IIII)I

	.limit locals 4
	.limit stack 2

	iload_0
	iload_1
	iadd
	istore_0

	iload_0
	iload_2
	iadd
	istore_0

	iload_0
	iload_3
	iadd
	istore_0

	iload_0
	ireturn

.end method


.method public static main([Ljava/lang/String;)V

	.limit locals 2
	.limit stack 1

	iconst_m1
	istore_0

	iload_0
	invokestatic stackSize/f(I)V

	return

.end method
.class public constPropagation2
.super java/lang/Object

.method static public <clinit>()V
	.limit locals 0
	.limit stack 0

	return
.end method

.method public static main([Ljava/lang/String;)V

	.limit locals 4
	.limit stack 2

	bipush -4
	istore_0

	iload_0
	iconst_5
	iadd
	istore_1

	iload_1
	invokestatic io/println(I)V

	iload_1
	iconst_2
	isub
	istore_2

	iload_2
	invokestatic io/println(I)V

	return

.end method



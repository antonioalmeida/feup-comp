.class public test1
.super java/lang/Object

.method static public <clinit>()V
	.limit locals 0
	.limit stack 0

	return
.end method

.method public static main([Ljava/lang/String;)V

	.limit locals 3
	.limit stack 2

	iconst_1
	istore_0

	iconst_1
	iconst_5
	iadd
	istore_1

	iload_1
	invokestatic io/println(I)V

	return

.end method
.class public constPropagation3
.super java/lang/Object

.method static public <clinit>()V
	.limit locals 0
	.limit stack 0

	return
.end method

.method public static main([Ljava/lang/String;)V

	.limit locals 4
	.limit stack 2

	bipush -10
	istore_0

	iconst_5
	istore_1

	iload_0

	iconst_3
	if_icmpge loop1_end
		iconst_1
		istore_0

		goto loop1_next
	loop1_end:
		bipush 30
		istore_0

	loop1_next:
	iload_1
	iload_0
	isub
	istore_2

	iload_0
	invokestatic io/println(I)V

	iload_1
	invokestatic io/println(I)V

	iload_2
	invokestatic io/println(I)V

	return

.end method



.class public nestedBranch
.super java/lang/Object

.method static public <clinit>()V
	.limit locals 0
	.limit stack 0

	return
.end method

.method public static sign(I)I

	.limit locals 1
	.limit stack 1

	iload_0

	ifge loop1_end
		iconst_m1
		istore_0

		goto loop1_next
	loop1_end:
		iload_0

		ifne loop2_end
			iconst_0
			istore_0

			goto loop2_next
		loop2_end:
			iconst_1
			istore_0

		loop2_next:
	loop1_next:
	iload_0
	ireturn

.end method


.method public static main([Ljava/lang/String;)V

	.limit locals 5
	.limit stack 2

	bipush -10
	istore_0

	bipush 10
	istore_2

	iload_0
	iload_2
	iadd
	istore_1

	iload_0
	invokestatic nestedBranch/sign(I)I
	istore_3

	iload_1
	invokestatic nestedBranch/sign(I)I
	istore_0

	iload_2
	invokestatic nestedBranch/sign(I)I
	istore_1

	iload_3
	invokestatic io/println(I)V

	iload_0
	invokestatic io/println(I)V

	iload_1
	invokestatic io/println(I)V

	return

.end method
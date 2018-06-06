.class public testIf
.super java/lang/Object

.field static max I = 0
.field static a I = 1
.method static public <clinit>()V
	.limit locals 0
	.limit stack 0

	return
.end method

.method public static main([Ljava/lang/String;)V

	.limit locals 2
	.limit stack 2

	getstatic testIf/a I

	getstatic testIf/max I
	if_icmpge loop1_end
		iconst_5
		istore_0

		goto loop1_next
	loop1_end:
		bipush 10
		istore_0

	loop1_next:
	return

.end method



.class public const_propagation_6
.super java/lang/Object

.method public static main([Ljava/lang/String;)V
.limit stack 3
.limit locals 3

iconst_m1
istore_0


iconst_m1
istore_1

iload_0
iconst_3
if_icmpge if_0_else

iconst_1
istore_0

goto if_0_out
if_0_else:

if_0_out:

iload_0
iconst_1
iadd
istore_1


iconst_2
istore_0


iconst_2
istore_2

iload_0
invokestatic io/println(I)V
iload_1
invokestatic io/println(I)V
iconst_2
invokestatic io/println(I)V
return
.end method

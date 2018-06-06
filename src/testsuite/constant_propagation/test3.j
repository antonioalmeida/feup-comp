.class public const_propagation_3
.super java/lang/Object

.method public static main([Ljava/lang/String;)V
.limit stack 3
.limit locals 5

bipush 10
newarray int
astore_0


iconst_1
istore_3
iconst_0
istore 4
iload 4
aload_0
arraylength
if_icmpge while_0_out
while_0_begin:
aload_0
iload 4
iload_3
iastore
iinc 4 1
iload 4
aload_0
arraylength
if_icmplt while_0_begin
while_0_out:


aload_0
iconst_5
iaload
istore_1


iload_1
iconst_5
iadd
istore_2

iload_2
invokestatic io/println(I)V
return
.end method

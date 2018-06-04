# feup-comp
All the tasks that were supposed to be done by checkpoint 2 were completed, that is, the first 7 stages of the compiler development have been successfully completed


Small Optimizations:

- the compiler generates JVM code with lower cost instructions in the cases of: iload, istore, astore, aload
- loading constants to the stack (use of iconst, bipush, sipush, ldc)
- use of iinc
- can call main function
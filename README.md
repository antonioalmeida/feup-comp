## yal (yet another language)

### GROUP: G27

António Cunha Seco Fernandes de Almeida, up201505836, Grade: 20, Contribution: 25%

Francisco Tuna Andrade, up201503481, Grade: 20, Contribution: 25%
  
Gil Dinis Magalhães Teixeira, up201505735, Grade: 20, Contribution: 25%

Tiago Alexandre de Sousa Dias da Silva, up201404689, Grade: 20, Contribution: 25%

GLOBAL Grade of the project: 20

### SUMMARY: 
(Describe what your tool does and its main features.)

As a project for the Compilers course at Porto Engineering University, we implemented a compiler capable of generating Java bytecode, a low level language capable of operation the Java Virtual Machine from Yal, a high level programing language. The main features of our compiler are its ability to perform syntatic analysis, semantic analysis and generating low level code.

### EXECUTE: 

#### Compiling:
Inside base directory
```
$ sh compile.sh
```

#### Running:
Inside base directory
```
$ sh run.sh <DIRECTORY NAME INSIDE TESTSUITE> <FILENAME - without extension> [-r=INT] [-o]

# Example
$ sh run.sh code_generation testArithmetic
```

#### Running Examples (`src/examples` directory):
Inside base directory
```
$ sh example.sh <FILENAME INSIDE EXAMPLES - without extension> [-r=INT] [-o]

# Example
$ sh example.sh quicksort
```

#### DEALING WITH SYNTACTIC ERRORS: 
(Describe how the syntactic error recovery of your tool does work. Does it exit after the first error?)

Our tool shows all the syntatic errors found, so that it does not exit after the first error. For example, if it founds an error in the assignement of a variable, the parser skips to the next semicolon and starts looking for syntatic errors from there. Our tool was able to report correctly all the errors in all files of the folder "MyFirstYalExamples_1" that can be found in the link "yal Examples" in the Moodle page of this course.

 

#### SEMANTIC ANALYSIS: 
(Refer the semantic rules implemented by your tool.)

In our compiler we implemented all the semantic rules mentioned in the project description and in the slides of this course. For example:
- We check that if a variable is going to be assigned to another, they must have the same type (note: an array can be assigned to a scalar, in that case it means fill all the array with the value of the scalar)
- We check that if a variable is going to be used in an expression it must have been initialized to type SCALAR
- We check that the return value of a function should be initialized
- We check that when a function is called there should be a function with the same signature (i.e. same name and same type of arguments)
- We check that for the value returned by a function to be used in an expression, that return value should be a SCALAR 
- We check that a variable used in a function call as parameter has been initialized
- We attributed scopes for whiles, ifs and elses so that a variable declared inside a scope is not valid outside
- In the specific case that a variable is declared inside an if statement and in an else statement that follows with the same type, that variable becomes valid to be used outside those if and else scopes  


 

#### INTERMEDIATE REPRESENTATIONS (IRs): 
(for example, when applicable, briefly describe the HLIR (high-level IR) and the LLIR (low-level IR) used, if your tool includes an LLIR with structure different from the HLIR)

Although we did not use IRs to generate the code, we used an IR to implement optimization -r. That IR consists of an array of instructions, that contains all the instructions of a given function. Each instruction contains its uses and defs and also the id's of instructions that are its antecessor and successor.

 

#### CODE GENERATION:
(when applicable, describe how the code generation of your tool works and identify the possible problems your tool has regarding code generation.)

The code generation uses the AST as the basis with support from the symbol tables, mostly for variable type checks. While going through it, it the generates the appropriate code. There are special cases, such as, limits generation which happens in the end, and integer assign to all array positions which has to generate code for a loop in order to fill all positions. We tried to make code generation as modular as possible which allowed us to not repeat code unless absolutely necessary. Besides covering the normal cases, it uses low cost instructions such as "iinc","iload","istore","astore","aload" and the various comparisons with 0. All the .yal examples from the "MyFirstYalExamples" run correctly, unless an error is supposed to occur, after the jasmin conversion to .class files. Same occurs for the "Extra yal examples" folder.

 

#### OVERVIEW: 
(refer the approach used in your tool, the main algorithms, the third-party tools and/or packages, etc.)


 

#### TESTSUITE AND TEST INFRASTRUCTURE: 

The tool's testsuite provides automated tests through JUnit4 for most of the given examples, as well as our own tests. Each test's purpose is specified on its filename. 

How to run (inside base directory)
```
$ sh compile.sh # compiles code and tests
$ sh test.sh <Test Class Name>

# Example - running code generation tests
$ sh test.sh CodeGenerationTests
```
 
#### TASK DISTRIBUTION: 
(Identify the set of tasks done by each member of the project.)

António Almeida

Francisco Andrade - Syntatic Analysis, Symbol Table implementation, Semantic Analysis, Implementation of optimization -r

Gil Teixeira - AST generation; Base for code generation, Function, Arithmetic expressions, Loops and Arrays code generation; Optimizations related to the "-o" option.

Tiago Silva - AST generation; Base for code generation, Function, Arithmetic expressions, Loops and Arrays code generation; Optimizations related to low cost instructions.

 

#### PROS:

In addition to the requested features, our tool features a few additional optimizations:

  - Assigns variable to the minimum number of registers possible if option -r is set
  - Performs constant propagation if option -o is set
  - Lower cost ifs in case of comparisons with 0('ifeq', 'ifge', 'ifgt', 'ifle','iflt', 'ifne')

#### CONS: 
(Identify the most negative aspects of your tool)
   - Does not allow the return variable of a function to be a global variable

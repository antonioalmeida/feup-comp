## yal (yet another language)

### GROUP: G27

António Cunha Seco Fernandes de Almeida, up201505836, GRADE1: <0 to 20 value>, CONTRIBUTION1: <0 to 100 %>

Francisco Tuna Andrade, up201503481, GRADE2: <0 to 20 value>, CONTRIBUTION2: <0 to 100 %>
  
Gil Dinis Magalhães Teixeira, up201505735, GRADE2: <0 to 20 value>, CONTRIBUTION2: <0 to 100 %>

Tiago Alexandre de Sousa Dias da Silva, up201404689, GRADE2: <0 to 20 value>, CONTRIBUTION2: <0 to 100 %>

GLOBAL Grade of the project: <0 to 20>

### SUMMARY: 
(Describe what your tool does and its main features.)

### EXECUTE: 
(indicate how to run your tool)


#### DEALING WITH SYNTACTIC ERRORS: 
(Describe how the syntactic error recovery of your tool does work. Does it exit after the first error?)

 

#### SEMANTIC ANALYSIS: 
(Refer the semantic rules implemented by your tool.)

 

#### INTERMEDIATE REPRESENTATIONS (IRs): 
(for example, when applicable, briefly describe the HLIR (high-level IR) and the LLIR (low-level IR) used, if your tool includes an LLIR with structure different from the HLIR)

 

#### CODE GENERATION:
(when applicable, describe how the code generation of your tool works and identify the possible problems your tool has regarding code generation.)

 

#### OVERVIEW: 
(refer the approach used in your tool, the main algorithms, the third-party tools and/or packages, etc.)

 

#### TESTSUITE AND TEST INFRASTRUCTURE: 
(Describe the content of your testsuite regarding the number of examples, the approach to automate the test, etc.)

 

#### TASK DISTRIBUTION: 
(Identify the set of tasks done by each member of the project.)

 

#### PROS:
(Identify the most positive aspects of your tool)
In addition to the requested features, our tool features a few additional optimizations:

  - Overload of functions with diferent argument Types, different number of arguments and diferent return types
  - Use of lower cost instructions in the cases of: `iload`, `istore`, `astore`, `aload`
  - Loading constants to the stack (use of `iconst`, `bipush`, `sipush`, `ldc`)
  - Use of `iinc`
  - Assigns variable to the minimum number of registers possible if option -r is set
  - Performs constant propagation if option -o is set


#### CONS: 
(Identify the most negative aspects of your tool)
   - Does not allow the return variable of a function to be a global variable

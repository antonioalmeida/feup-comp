## yal (yet another language)

### GROUP: G27

António Cunha Seco Fernandes de Almeida, up201505836, Grade: 20, Contribution: 25%

Francisco Tuna Andrade, up201503481, Grade: 20, Contribution: 25%
  
Gil Dinis Magalhães Teixeira, up201505735, Grade: 20, Contribution: 25%

Tiago Alexandre de Sousa Dias da Silva, up201404689, Grade: 20, Contribution: 25%

GLOBAL Grade of the project: 20

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

The tool's testsuite provides automated tests through JUnit4 for most of the given examples, as well as our own tests. Each test's purpose is specified on its filename. 

How to run (inside src folder):
```
sh compile.sh # compiles code and tests
sh test.sh <Test Class Name>

# Example - running code generation tests
sh test.sh CodeGenerationTests
```
 
#### TASK DISTRIBUTION: 
(Identify the set of tasks done by each member of the project.)

 

#### PROS:
(Identify the most positive aspects of your tool)
In addition to the requested features, our tool features a few additional optimizations:

  - Use of lower cost instructions in the cases of: `iload`, `istore`, `astore`, `aload`
  - Loading constants to the stack (use of `iconst`, `bipush`, `sipush`, `ldc`)
  - Use of `iinc`
  - Can call `main` function


#### CONS: 
(Identify the most negative aspects of your tool)

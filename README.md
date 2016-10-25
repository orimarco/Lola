#Lola
The Language Of Languge Augmentation was found by Iddo Zmiri under the supervision of Prof. Yossi Gil in the Technion.
Lola is a tool for language extensions and can equip any language with new syntax and semantics.
Lola's syntax is Macro based, and designed to run on a precompiled code, transforming it from the new syntax to the domain syntax.
Compared to other preprocessor tools, Lola is much powerfull and even allows the programmer to run python code.

##Hello, Lola!
Lola's basic blocks are **Lexi**s.
A **Lexi** defines a pattern of code and some actions to apply on it whenever matched.
Typically, a **Lexi** action will be **replacing** the code with domain code, **asserting** that some conditions apply and even **running** some python code.


##Example
Range based loops are fun, they kill boiler plate, but don't exist in Java...
Why not add them with the following **Lexi**?
```java
##Find
  for(##Identifier(id) = ##Literal(from)..##Literal(to))
    ##any(block)
##replace
  for(int ##(id) = ##(from); ##(id) <= ##(to); ++##(id))
    ##(block)
##example
  for(i = 1..100)
    print(i);
##resultsIn
  for(int i = 1; i < 100; ++i)
    print(i);
``` 
\* Lola kwywords begin with **\##**.

As can be deduced from the self explaining exmaple, a pattern is specified to **find** places where the lexi is used, a **replace** section specifies domain code to replace the matched code with, an **example** and **resultsIn** sections specify an example of usage and the code that will replace the example after applying the Lexi. 

Note that the exmaple is not only for documentation but for self testing too, when writing a **Lexi**, Lola asserts that the example indeed result in the specified **resultsIn** section code.

Another point worth noting is that Lola uses an enviroment for identifiers aliasing, meaning, if we write ``##Literal(from)``, the matching literal will be stored in some enviroment (Jython actually) and may be summoned by the name ``from``. For example, the **##** keyword specifies a python snippet to run, so ``##(from)`` will produce the literal matched by ``from``.
##Syntax
Lola's syntax is described by Iddo's [thesis](https://github.com/orimarco/Lola/blob/master/thesis.pdf)

##Implementation
This version of Lola was implemented by @orimarco using Java 1.8.
A detailed explanation about our implementation can be found in the [Implementation Wiki](https://github.com/orimarco/Lola/wiki/Implementation-Architecture).

##How do I import the project?
Just clone/download the repository and import the project using Maven. Should be easy!

##How do I use Lola?
Lola can be run by compiling `Lola.java` and running it in this form: ``Lola <input file> <output file>``.

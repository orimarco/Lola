#Lola
The Language Of Languge Augmentation was found by Ido Zmiri under the supervising of Prof. Yossi Gil in the Technion.
Lola is a tool for language extensions and can equip any language with new syntax and semantics.
Lola's syntax is Macro based, and designed to run on a precompiled code, transforming it from the new syntax to the domain syntax.
Compared to other preprocessor tools, Lola is much powerfull and is even equipped with a Python engine.

##Hello, Lola!
Lola's basic blocks are **Lexi**s.
A **Lexi** defines a pattern of code and some actions to apply on it whenever matched.
Typically, a **Lexi** action will be **replacing** the code with domain code, **asserting** that some conditions apply and even **running** some python code.


##Example

```
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
##Syntax
Lola's syntax is described by Ido's thesis - linkkkk




##

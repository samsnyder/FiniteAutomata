## Non Deterministic Finite Automata

This is a library allowing you to build, run and view finite state machines representing basic regular expressions.

This is definitely not the most efficient method of testing regular expressions and should not be used in a real project,
however this is much more of a theoretical exercise and provides a robust way of matching regular expressions.

This project is based off of the undergraduate course entitled "Formal Languages and Automata" by Prof. Andrew Pitts at the University of Cambridge.

### Regular Expression Syntax

The abstract syntax supported are Union, Concat and Star.

Their concrete syntax is
*   "a|b" = Union(Ma, Mb) = The union of the machine representing the expression "a" and the machine representing the expression "b".
    In other words, either the input matches "a", the input matches "b" or the input matches both "a" and "b".
*   "ab" = Concat(Ma, Mb) = The concatenation of the machine representing the expression "a" and the machine representing the expression "b".
    In other words, the input matches "a" and then also matches "b". This operation is ordered.
*   "a*" = Star(Ma) = The star of the machine representing the expression "a". In other words, the input matches "a" 0 or more times.
*   "a" = Ma = The machine representing the expression "a".

Examples of inputs are
*   "a|bc" = Union(Ma, Concat(Mb, Mc))
*   "a(fc)*|y = Union(Concat(Ma, Star(Concat(Mf, Mc))), My)

### Getting started

This code snippet will create a machine from the input regular expression.

```
Machine machine = Machine.parseRegex("a|b");
```

This will then test that machine against an input string and return a boolean representing whether the input reached an accepting state in the machine (it matched the regular expression).
```
machine.matchString("a")
```
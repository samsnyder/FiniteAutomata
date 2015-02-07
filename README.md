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

#### Creating and running a machine
This code snippet will create a machine from the input regular expression.

```java
Machine machine = Machine.parseRegex("a|b");
```

This will then test that machine against an input string and return a boolean representing whether the input reached an accepting state in the machine (it matched the regular expression).
```java
machine.matchString("a")
```

#### Viewing a machine

You can view the structure of a machine using the [JUNG](http://jung.sourceforge.net) framework.

The 'createGraph' method of Machine allows you to get a JUNG graph.

The code below will output an image of your graph to the disk.

```java
Graph graph = machine.createGraph();

Layout<State, Transition> layout = new CircleLayout(graph);
layout.setSize(new Dimension(1000,1000));
VisualizationViewer<State, Transition> vv = new VisualizationViewer<State, Transition>(layout);
vv.setPreferredSize(new Dimension(350,350));

VisualizationImageServer<State, Transition> vis = new VisualizationImageServer<State, Transition>(vv.getGraphLayout(), vv.getGraphLayout().getSize());

Transformer<State, Paint> vertexPaint = new Transformer<State, Paint>() {
    public Paint transform(State i) {
        if(machine.stateIsAccepting(i)) {
            return Color.GREEN;
        }else if(machine.getStartState() == i){
            return Color.YELLOW;
        }else{
            return Color.RED;
        }
    }
};
vis.getRenderContext().setVertexFillPaintTransformer(vertexPaint);

Transformer<Transition, String> edgeLabel = new Transformer<Transition, String>() {
    public String transform(Transition i) {
        return i.getLetter().toString();
    }
};
vis.getRenderContext().setEdgeLabelTransformer(edgeLabel);

BufferedImage image = (BufferedImage) vis.getImage(new Point2D.Double(vv.getGraphLayout().getSize().getWidth() / 2, vv.getGraphLayout().getSize().getHeight() / 2),
                                                   new Dimension(vv.getGraphLayout().getSize()));

File outputfile = new File("machine.png");

try {
    ImageIO.write(image, "png", outputfile);
} catch (IOException e) {
    e.printStackTrace();
}
```
# annotation-processor-demo
Example of an annotation processor used to generate decorator base classes.

## Project status
This project is only a draft. Potential issues to be sorted out:
- Use of primitive types in interfaces
- Use of @DecoratorBase annotation on classes
- Probably a lot more...
- Test coverage

## Project structure
- annotation-processor-demo: This parent module is only used to simplify build and execution
- annotation-processor: Contains the actual annotation and annotation processor ([@DecoratorBase](../main/annotation-processor/src/main/java/de/frohwerk/demo/decorator/DecoratorBase.java), [DecoratorAnnotationProcessor](../main/annotation-processor/src/main/java/de/frohwerk/demo/decorator/DecoratorAnnotationProcessor.java))
- simple-use-case-test: Showcases the use of the annotation on a simple interface ([SimpleTest](../main/simple-use-case-test/src/test/java/de/frohwerk/demo/test/SimpleTest.java))
- generics-use-case-test: Showcases the use of the annotation on a set of related interfaces with inheritance and type parameters ([GenericDecoratorTest](../main/generics-use-case-test/src/test/java/de/frohwerk/demo/GenericDecoratorTest.java))

## Building and running annotation-processor-demo
This project was built using JDK 11 and Maven 3.6. Newer versions of these tools should work, but were not tested.

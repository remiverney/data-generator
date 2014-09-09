package org.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.datagen.exception.CircularDependencyException;
import org.datagen.exception.UnresolvedDependencyException;
import org.datagen.expr.ast.exception.ParsingException;
import org.datagen.expr.interpreter.Interpreter;
import org.datagen.expr.interpreter.InterpreterEvent;
import org.datagen.expr.interpreter.InterpreterFactory;
import org.datagen.expr.interpreter.InterpreterParameters;
import org.datagen.factory.ConfigBuilder;
import org.datagen.utils.Observable;
import org.datagen.utils.Observer;

public class Inter {

	public Inter() {
	}

	public static void main(String[] args) throws FileNotFoundException,
			CircularDependencyException, UnresolvedDependencyException,
			IOException, ParsingException {
		Map<String, String> library = new HashMap<>();
		library.put("PI", "3.14159265358979323846");
		library.put("E", "2.7182818284590452354");
		library.put("pow", "(x,n -> n == 0 ? 1 : x * this(x, n - 1))");
		library.put("fibo",
				"(x -> CASE x WHEN 0 THEN 0 WHEN 1 THEN 1 ELSE this(x - 1) + this(x - 2) END)");
		library.put("avg", "(x,y -> (x + y) / 2 )");
		library.put("fact", "(x -> x == 0 ? 1 : this(x - 1) * x)");
		library.put(
				"filter",
				"(x,y -> x == {} ? {} : (y(x[0]) ? {x[0]}: {}) + (x.length > 1 ? this(x[1 .. x.length - 1], y) : {}))");
		library.put(
				"map",
				"(x,y -> x == {} ? {} : {y(x[0])} + (x.length > 1 ? this(x[1 .. x.length - 1], y) : {}))");
		library.put("reduce",
				"(x,y,z -> x == {} ? z : y(this(x[0 .. x.length - 2], y,z), x[x.length - 1]))");
		library.put(
				"qsort",
				"(x -> x.length <= 1  ? x : (this(mylib:filter(x[1 .. x.length - 1], (y -> y < x[0]))) + {x[0]} + this(mylib:filter(x[1 .. x.length - 1], (y -> y >= x[0])))))");
		library.put(
				"qsort3",
				"(x -> x.length <= 1  ? x : (this(mylib:filter(x[1 .. x.length - 1], (y -> y < x[0])))) + {x[0]})");
		library.put("qsort2",
				"(k -> mylib:filter(k[1 .. k.length - 1], (z -> z < k[0])))");
		library.put("arrayrand", "(x -> x[:labs(:lrandom()) % x.length])");
		Map<String, InputStream> expressions = new HashMap<>();
		// expressions.put("col1",
		// new FileInputStream("src/main/antlr/sample.txt"));
		// expressions.put("col2", new FileInputStream(
		// "src/main/antlr/sample2.txt"));
		// expressions.put("col3", new FileInputStream(
		// "src/main/antlr/sample3.txt"));
		// expressions.put("col4", new FileInputStream(
		// "src/main/antlr/sample4.txt"));
		expressions.put("col5", new FileInputStream(
				"src/main/antlr/sample5.txt"));

		ConfigBuilder<InterpreterParameters> builder = InterpreterFactory
				.instance().getConfigBuilder()
				.enable(InterpreterParameters.ALLOW_LAMBDA_DEFINITION);
		Interpreter inter = InterpreterFactory.instance().get(builder.build());
		inter.registerLibrary("mylib", library);
		inter.registerExpressionsStream(expressions);

		inter.addObserver(new Observer<Interpreter, InterpreterEvent>() {

			@Override
			public void notify(
					Observable<Interpreter, InterpreterEvent> observable,
					InterpreterEvent event) {
				// System.out.println("event: col=" + event.getColumn() +
				// " val="
				// + event.getValue() + " old=" + event.getOldValue());
			}
		});

		System.out.println("expr: " + inter.get("col5"));

		for (int i = 0; i < 1; i++) {
			Map<String, String> results = inter.evalToString();

			results.entrySet().stream().forEach(x -> {
				System.out.println(x.getKey() + " = " + x.getValue());
			});

			// inter.nextSequence();
		}
	}
}

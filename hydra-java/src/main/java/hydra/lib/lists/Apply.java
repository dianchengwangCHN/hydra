package hydra.lib.lists;

import hydra.Flows;
import hydra.compute.Flow;
import hydra.core.Name;
import hydra.core.Term;
import hydra.core.Type;
import hydra.dsl.Expect;
import hydra.dsl.Terms;
import hydra.graph.Graph;
import hydra.tools.PrimitiveFunction;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static hydra.Flows.*;
import static hydra.dsl.Types.*;


public class Apply<A> extends PrimitiveFunction<A> {
    public Name name() {
        return new Name("hydra/lib/lists.apply");
    }

    @Override
    public Type<A> type() {
        return lambda("x", lambda("y", function(list(function("x", "y")), list("x"), list("y"))));
    }

    // Note: this implementation does not use apply(), as actually applying the mapping function would require beta reduction,
    //       which would make the mapping function monadic.
    @Override
    protected Function<List<Term<A>>, Flow<Graph<A>, Term<A>>> implementation() {
        return args -> map2(Expect.list(Flows::pure, args.get(0)), Expect.list(Flows::pure, args.get(1)), new BiFunction<List<Term<A>>, List<Term<A>>, Term<A>>() {
                @Override
                public Term<A> apply(List<Term<A>> functions, List<Term<A>> arguments) {
                    List<Term<A>> apps = new LinkedList<>();
                    for (Term<A> f : functions) {
                        for (Term<A> a : arguments) {
                            apps.add(Terms.apply(f, a));
                        }
                    }
                    return Terms.list(apps);
                }
            });
    }

    public static <X, Y> Function<List<X>, List<Y>> apply(List<Function<X, Y>> functions) {
        return (args) -> apply(functions, args);
    }

    public static <X, Y> List<Y> apply(List<Function<X, Y>> functions, List<X> args) {
        List<Y> results = new LinkedList<>();
        for (Function<X, Y> f : functions) {
            for (X a : args) {
                results.add(f.apply(a));
            }
        }
        return results;
    }
}

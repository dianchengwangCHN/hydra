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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static hydra.dsl.Types.function;
import static hydra.dsl.Types.lambda;
import static hydra.dsl.Types.list;

public class Cons<A> extends PrimitiveFunction<A> {
    public static final Name NAME = new Name("hydra/lib/lists.cons");

    public Name name() {
        return NAME;
    }

    @Override
    public Type<A> type() {
        return lambda("x", function("x", list("x"), list("x")));
    }

    @Override
    protected Function<List<Term<A>>, Flow<Graph<A>, Term<A>>> implementation() {
        return args -> Flows.map(
                Expect.list(Flows::pure, args.get(1)),
                l -> Terms.list(Cons.apply(args.get(0), l)));
    }

    public static <X> List<X> apply(X el, List<X> l) {
        List<X> combined = new ArrayList<>();
        combined.add(el);
        combined.addAll(l);
        return combined;
    }
}

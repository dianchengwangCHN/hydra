package hydra.lib.maps;

import hydra.Flows;
import hydra.compute.Flow;
import hydra.core.Name;
import hydra.core.Term;
import hydra.core.Tuple;
import hydra.core.Type;
import hydra.dsl.Expect;
import hydra.graph.Graph;
import hydra.tools.PrimitiveFunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static hydra.dsl.Expect.list;
import static hydra.dsl.Types.function;
import static hydra.dsl.Types.lambda;
import static hydra.dsl.Types.list;
import static hydra.dsl.Types.map;
import static hydra.dsl.Types.pair;
import static hydra.dsl.Types.variable;

public class FromList<A> extends PrimitiveFunction<A> {
    public Name name() {
        return new Name("hydra/lib/maps.fromList");
    }

    @Override
    public Type<A> type() {
        return lambda("k", "v", function(list(pair(variable("k"), variable("v"))), map("k", "v")));
    }

    @Override
    protected Function<List<Term<A>>, Flow<Graph<A>, Term<A>>> implementation() {
        return args -> Flows.map(list(term -> Expect.pair(Flows::pure, Flows::pure, term), args.get(0)),
                (Function<List<Tuple.Tuple2<Term<A>, Term<A>>>, Term<A>>) pairs -> new Term.Map<>(apply(pairs)));
    }

    public static <K, V> Map<K, V> apply(List<Tuple.Tuple2<K, V>> pairs) {
        Map<K, V> mp = new HashMap<>();
        for (Tuple.Tuple2<K, V> pair : pairs) {
            mp.put(pair.object1, pair.object2);
        }
        return mp;
    }
}

get_size(null, 0) :- !.
get_size(tree(_, _, _, _, _, SZ), SZ).

update_size(L, R, K, P, V, T) :- get_size(L, LS), get_size(R, RS), S is LS + RS + 1, T = tree(K, P, V, L, R, S).

merge(V, V, null) :- !.
merge(V, null, V) :- !.
merge(V, tree(LK, LP, LV, LL, LR, _), tree(RK, RP, RV, RL, RR, S2)) :- 
		LP < RP, !, 
		merge(RES, LR, tree(RK, RP, RV, RL, RR, S2)), 
		update_size(LL, RES, LK, LP, LV, V).
merge(V, tree(LK, LP, LV, LL, LR, S1), tree(RK, RP, RV, RL, RR, _)) :- 
		LP >= RP, !, 
		merge(RES, tree(LK, LP, LV, LL, LR, S1), RL), 
		update_size(RES, RR, RK, RP, RV, V).

split(null, null, null, X) :- !.
split(LV, RV, tree(K, P, V, L, R, S), X) :- K < X, !, split(R1, RV, R, X), update_size(L, R1, K, P, V, LV).
split(LV, RV, tree(K, P, V, L, R, S), X) :- K >= X, !, split(LV, R2, L, X), update_size(R2, R, K, P, V, RV).


map_build([], null) :- !.
map_build([(K, V) | R], T) :- map_build(R, RES), map_put(RES, K, V, T), !.

map_get(tree(X, _, V, _, _, _), X, V) :- !.
map_get(tree(K, _, V, L, R, _), X, FV) :- K < X, !, map_get(R, X, FV).
map_get(tree(K, _, V, L, R, _), X, FV) :- K > X, !, map_get(L, X, FV).

map_remove(null, X, null) :- !.
map_remove(tree(K, P, V, L, R, S), X, NT) :- K < X, !, map_remove(R, X, NR), update_size(L, NR, K, P, V, NT). 
map_remove(tree(K, P, V, L, R, S), X, NT) :- K > X, !, map_remove(L, X, NL), update_size(NL, R, K, P, V, NT). 
map_remove(tree(K, P, V, L, R, S), K, NT) :- !, merge(NT, L, R). 

map_put(T, X, V, RES) :- rand_int(2147483647, P), map_remove(T, X, R), split(R1, R2, R, X), merge(M, R1, tree(X, P, V, null, null, 1)), merge(RES, M, R2).
map_submapSize(T, K1, K2, S) :- split(R1, R2, T, K1), split(R3, R4, R2, K2), get_size(R3, S).
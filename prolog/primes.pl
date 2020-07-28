mark(S, N, MAX) :-
    N =< MAX,
    assertz(composite(N)),
    N1 is N + S, mark(S, N1, MAX).

precalc(S, MAX) :- S > MAX, !.
precalc(S, MAX) :- \+ composite(S), assert(prime(S)), N is S * S, mark(S, N, MAX).
precalc(S, MAX) :- S1 is S + 1, precalc(S1, MAX).  

init(MAXN) :- precalc(2, MAXN).

next_prime(P, P1) :- P1 is P + 1, prime(P1), !.
next_prime(P, R) :-  P1 is P + 1, next_prime(P1, R).

find_divisors(S, V, P, [V]) :- P * P > S, !, V > 1. 
find_divisors(S, V, P, []) :- P * P > S, !. 
find_divisors(S, V, P, [P | R]) :- 0 is V mod P, V1 is V / P, find_divisors(S, V1, P, R), !.
find_divisors(S, V, P, [A | R]) :- next_prime(P, P1), find_divisors(S, V, P1, [A | R]).

mul(1, []).
mul(X, [X]) :- prime(X).
mul(X, [A, B | T]) :- A =< B, prime(A), mul(X1, [B | T]), X is X1 * A. 

prime_divisors(N, Divisors) :- is_list(Divisors), !, mul(N, Divisors).  

prime_divisors(1, []) :- !.
prime_divisors(N, D) :- find_divisors(N, N, 2, D).

merge([], X, X) :- !.
merge([A | T1], [A | T2], [A | R]) :- merge(T1, T2, R), !.
merge([A | T1], [B | T2], [A | R]) :- A < B, merge(T1, [B | T2], R), !.
merge(A, B, R) :- merge(B, A, R), !.

lcm(A, B, R) :- prime_divisors(A, PA), prime_divisors(B, PB), merge(PA, PB, R1), prime_divisors(R, R1).
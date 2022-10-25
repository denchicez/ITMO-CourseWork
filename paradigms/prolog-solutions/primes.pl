addbase(P, NEWP, N):- composite(N), NEWP is P, !.
addbase(P, NEWP, N):- NEWP is P + 1, assert(nth_prime(P, N)), !.

writes(N, _, MAX_N) :- N > MAX_N, !.
writes(_, K, _) :- composite(K), !.
writes(N, K, MAX_N) :-
	NK is N + K, assert(composite(NK)), writes(NK, K, MAX_N).

nth_prime(1, 2).
init(N, _, MAX_N) :- N > MAX_N, !.
init(N, P, MAX_N) :-
	writes(N, N, MAX_N),
	addbase(P, NEWP, N),
	N2 is N + 2, init(N2, NEWP, MAX_N).
init(MAX_N) :-
	writes(2, 2, MAX_N),
	init(3, 2, MAX_N).
prime(N) :-
	not(composite(N)).

find_k(N, K, K) :- 0 is mod(N, K), prime(K), !.
find_k(N, K, R) :- find_k(N, K+1, TR), R is TR.
find_k(N, R) :- find_k(N, 2, TR), R is TR.


prime_divisors(1, []) :- !.
prime_divisors(N, [N]) :- prime(N), !.
prime_divisors(N, [R | Divisors2]) :- 
	find_k(N, R),
	DIVN is div(N, R),
	prime_divisors(DIVN, Divisors2), !.


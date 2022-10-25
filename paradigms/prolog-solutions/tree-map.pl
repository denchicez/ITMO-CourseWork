map_build([], []):- !.

split(_, Now, _, RealEnd, _, _) :- RealEnd < Now, !.
split([], Now, RealMid, RealEnd, [], Right) :- !.
split([], Now, RealMid, RealEnd, Left, []) :- !.
split([(Key, Value) | TreeMap], Now, RealMid, RealEnd, [(Key, Value) | Left], Right) :-
	Now1 is Now + 1, Now < RealMid, split(TreeMap, Now1, RealMid, RealEnd, Left, Right), !.
split([(Key, Value) | TreeMap], Now, RealMid, RealEnd, Left, [(Key, Value) | Right]) :-
	Now1 is Now + 1, Now >= RealMid,split(TreeMap, Now1, RealMid, RealEnd, Left, Right), !.

map_build(ListMap, (LeftAns2, Mid, RightAns2)):-
	length(ListMap, R1),
	RealRight is R1 - 1,
	RealMid is RealRight // 2,
	split(ListMap, 0, RealMid, RealRight, LeftAns, [Mid | RightAns]),
	map_build(LeftAns, LeftAns2),
	map_build(RightAns, RightAns2), !.

map_get((Left, (Key, Value), Right), Key, Value) :-  !.
map_get((Left, (Key1, Value1), Right), Key, Value) :- Key < Key1, map_get(Left, Key, Value), !.
map_get((Left, (Key1, Value1), Right), Key, Value) :- Key1 < Key, map_get(Right, Key, Value), !.

map_replace([], Key, Value, []) :-  !.
map_replace((Left, (Key, Value1), Right), Key, Value, (Left, (Key, Value), Right)) :-  !.
map_replace((Left, (Key1, Value1), Right), Key, Value, (ResultLeft, (Key1, Value1), Right)) :- Key < Key1, map_replace(Left, Key, Value, ResultLeft), !.
map_replace((Left, (Key1, Value1), Right), Key, Value, (Left, (Key1, Value1), ResultRight)) :- Key1 < Key, map_replace(Right, Key, Value, ResultRight), !.
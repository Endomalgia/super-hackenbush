## RUNNING ON LINUX:
	Because java is weird depending on your window manager you might need to rename the window manager (if the screen is white or the title doesnt show up you probably need to do this). I used wmname on the aur and renamed my window manager to LG3D (arch wiki for more info XP).

## Current issues for people testing this:
* In order to enter levels press 1, 2, or 3 on the overworld.

* Theres some bug I cant figure out where after a first level is played none of the others update when you remove their edges, so reset the game to play more levels.

* Theres an issue with the idealMove finders thats throwing off the blunder/ideal move thing, Note to self I *think* this is bacuse the evaluator is getting the value of a game where the ideal move is played instead of the value of the game after that move has played.


## New module dependencies
* Note regarding build.gradle dependencies and sourceCompatibility 1.8:
- There are a lot of commented out dependencies within the individual gradle build files. This is
    because after debugging it was interesting to see how pared back the additions could be. It seems
    that a lot of the issues were caused by the java language level target being 1.7 and not 1.8.
    As ever, the additions were effective at one point or another along the way. They are left for reference.

## 229.
* Refactored all code in spawning improvement
- In fact just watched videos and implemented own code. Using array to collect agreed safe (single) spawns.
- Working but =>
  - only one obstacle is spawning at a time, where randomly this should be more.
    - This is I think because params need tweaking there is a limit of attempts at finding a suitable 
    position, but after the first new obstacle has been placed in a kind of spawn queue, the queue needs
    to be taken into consideration too. Basically this is an interesting problem that I would like to extract
    and re-invent.
  - Coins spawning above obstacles not working
*  Thoughts at time:  
- As soon as there is one obstacle or coin there are specific slots of opportunity pertaining to all
future obstacles in a particular spawn cycle.
  - Object 1 spawned at angle 0 + x for object width + x for next object of that type == slot for next
and so on. 
  - There are two spawning behaviours in play coins that only respawn when there are none left and
their time-delay has elapsed -> up to max allowed. 
  - obstacles that spawn on the time delay up to max allowed and what should be more than one at a time.

It strikes me that the use of a spawn queue where these die are rolled in advance and played out when criteria are met
is in order == 'Give me the first mapping for *two* obstacles with an origin of 67 without anything @ 10 and 230'.
Either an array of arrays or even poolable objects groups with quickly readable signatures? 

This could be a fun thing to try.

Also, I have not looked into the trigonometry running this yet, some research into this would be beneficial too.

## 230

Highscore is being retained but, have just noticed is being 'smoothed' with every score whereas score is not.
Part and parcel of not testing as going along, this kind of thing would not happen if own code all the way..
Would not move on until working, kind of slapdash, but going to pull together at end.

## 230Extra_scoring_circumference 

Decided to take a quick look at these -> circumference should read radius regarding spawning coins
successfully above existing obstacles. Just going to take a quick look.
Fixed the scoring decided to leave the rendering of coins until it is (possibly) completely redesigned.

## 234
Have observed being able to move through obstacles on occasion?! Set currently so coins spawn above 
planet surface. Flag update is being called but is not having required effect when called. This is, again
a debug within a system that I'm not happy with anyway == obstacle and coin spawning.

# GameController experimental refactor
~~Aim -> GameController is a massive class, I want to extract its actions into smaller classes starting with
SpawningController -> having all of the spawning logic in one place provides a good foundation for 
redesigning the spawning logic altogether.~~
Completed, monster (the player) is passed to the Spawning controller on spawning initialization.
This is the only interdependency. 
GameRenderer (another monster class) still gets from GameController, so SpawnController is invisible 
externally.

My thoughts at present are possibly:
* further separating Coin and Obstacle spawning
* Making SpawnController a singleton (but.. why?)
* further separating Scoring and Collision detection from Game Controller.

Note to self...The next day:
I did some research and purchased a couple of second hand books, one on LibGDX the other on game
patterns. So Instead of refactoring in a way that will probably turn out to be sub-optimal, I'm moving
on to the next larger game example and stubbing this branch for now.


Tyler Carberry  
2048  
Combine similar numbers to reach the score 2048  
This game is based off the original 2048 made by Gabriele Cirulli
at https://github.com/gabrielecirulli/2048


## New Features

1. Undos: Unlimited undos in practice mode and up to 10 in normal mode
2. Shuffle: Randomly move every tile. Use as many times as you want.
3. Autoplay: 4 different methods with up to a 94% win rate. [See more here](https://github.com/tytan34/2048/blob/master/README.md#autoplay "Autoplay")
4. Remove Low Values Power Up: Remove all 2s and 4s from the game 
5. Multiple grid sizes: Play on a 3x3 board or 20x25


## New Modes

1. Practice Mode: Unlimited Everything
2. Normal Mode: 10 Undos
3. Pro Mode: No Undos
4. Rush Mode:	Higher value tiles will spawn
5. Survival Mode: 30 seconds. Combine tiles for more time
6. XMode: Movable X tile that can't be combined
7. Corner Mode: Immovable Blocks In Corners
8. Speed Mode: Tiles appear every 2 seconds even if no move was made
9. Crazy Mode: A 5x5 grid with every other mode enabled
10. Custom Game: Customize everything about the game seen above

## Autoplay 

1. Recursive (94% win rate): A brute force method that beats the game in less than a minute
2. Circle: Moves up, right, down, left until the game is lost
3. Corner: Moves up and left until it can't move. Then go right. Only go down if it must.
4. Random: Extremely unlikely to win this way.

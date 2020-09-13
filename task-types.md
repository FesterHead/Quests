# Item names
[Use the Bukkit Enum Material list](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html).  Item material names can be UPPER or lower case.

# Break - Mining, excavation, digging, ...
```
task:
  type: "break"
  amount: <integer>              # Required: The number of blocks to break
  continue-evaluating: <boolean> # Optional: Continue evaluating other tasks;  default is false
  item: "<Material>"             # Optional: The specific block to break; default is all blocks
  reverse-progression: <boolean> # Optional: The same type of block placed will reverse progression; default is true
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
  coreprotect: <boolean>         # Optional: CoreProtect checking.  If player placed block no credit!
  coreprotect-seconds: <integer> # Optional: Number of seconds to look in CoreProtect log.  Default is 3600 seconds = 1 hour
```

```
task:
  type: "break-high"
  amount: <integer>              # Required: The number of blocks to break
  continue-evaluating: <boolean> # Optional: Continue evaluating other tasks;  default is false
  item: "<Material>"             # Optional: The specific block to break; default is all blocks
  reverse-progression: <boolean> # Optional: The same type of block placed will reverse progression; default is true
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
  coreprotect: <boolean>         # Optional: CoreProtect checking.  If player placed block no credit!
  coreprotect-seconds: <integer> # Optional: Number of seconds to look in CoreProtect log.  Default is 3600 seconds = 1 hour

This is a special break task where the event priority is elevated to HIGH.
Useful for when another plugin cancels the BlockBreakEvent, such as the BentoBox AOneBlock addon.
```

# Breed - Breeding, mating, ...
```
task:
  type: "breed"
  amount: <integer>              # Required: The number of animals to breed.
  item: "<Material>"             # Optional: The specific animal to breed; default is all animals
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
```

# Brew - Brew potions, ...
```
task:
  type: "brew"
  amount: <integer>              # Required: The number of The number of potions to brew.
  item: "<Material>"             # Optional: The specific potion to brew.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
```

# Craft - Crafting, making, constructing, ...
```
task:
  type: "craft"
  amount: <integer>              # Required: The number of items to craft.
  item: "<Material>"             # Optional: The specific item to craft.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
```

# Damage - Beat up mobs
```
task:
  type: "damage"
  amount: <integer>              # Required: The amount of damage.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
```

# Death - Killing animals, mobs, entities, ...
```
task:
  type: "death"
  amount: <integer>              # Required: The number of entities to kill.
  item: "<Material>"             # Optional: The specific entity to kill.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
```

# Drop - Farming, harvesting, collecting, ...
```
task:
  type: "drop"
  amount: <integer>              # Required: The number of block drops to collect.
  continue-evaluating: <boolean> # Optional: Continue evaluating other tasks;  default is false
  item: "<Material>"             # Optional: The specific block drop to collect.
  reverse-progression: <boolean> # Optional: The same type of block placed will reverse progression; default is true
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
```

# Exp - Earn experience
```
task:
  type: "exp"
  amount: <integer>              # Required: The amount of experience.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
```

# Enchant - Enchanting items, ...
```
task:
  type: "enchant"
  amount: <integer>              # Required: The number of items to enchant.
  item: "<Material>"             # Optional: The specific item to enchant.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
```

# Fish - Fishing, catching, ...
```
task:
  type: "fish"
  amount: <integer>              # Required: The number of items to catch.
  item: "<Material>"             # Optional: The specific item to catch.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
```

# Furnace - Cooking, smelting, ...
```
task:
  type: "furnace"
  amount: <integer>              # Required: The number of items to extract from a furnace.
  item: "<Material>"             # Optional: The specific item to extract from a furnace.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
```

# Inventory
```
task:
  type: "inventory"
  amount: <integer>                        # Required: Amount of item to have.
  item: "<Material>"                       # Required: The specific item.
  remove-items-when-complete: <boolean>    # Required: Take the items away on completion.  Default is false.
  update-progress: <boolean>               # Required: Update progress.  If this causes lag then disable it.
```

# Location - Be somewhere
```
task:
  type: "location"
  x: <integer>                   # Required: The x coordinate.
  y: <integer>                   # Required: The y coordinate.
  z: <integer>                   # Required: The z coordinate.
  world: "<string>"              # Required: World where this task is valid
  padding: <integer>             # Optional: Padding for when close is good enough.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
```

# Milk - Milk cows, ...
```
task:
  type: "milk"
  amount: <integer>              # Required: The number of cows to milk.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
```

# Move - Walk, run, fly a certain distance
```
task:
  type: "move"
  amount: <integer>              # Required: The number of blocks (distance) to to move.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
```

# Place - Building, placing, ...
```
task:
  type: "place"
  amount: <integer>              # Required: The number of blocks to place.
  continue-evaluating: <boolean> # Optional: Continue evaluating other tasks;  default is false
  item: "<Material>"             # Optional: The specific block to place.
  reverse-progression: <boolean> # Optional: The same type of block placed will reverse progression; default is true
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
```

```
task:
  type: "place-high"
  amount: <integer>              # Required: The number of blocks to place.
  continue-evaluating: <boolean> # Optional: Continue evaluating other tasks;  default is false
  item: "<Material>"             # Optional: The specific block to place.
  reverse-progression: <boolean> # Optional: The same type of block placed will reverse progression; default is true
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world

This is a special place task where the event priority is elevated to HIGH.
Useful for when another plugin cancels the BlockBreakEvent, such as the BentoBox AOneBlock addon.
```

# Playtime - Building, placing, ...
```
task:
  type: "playtime"
  amount: <integer>              # Required: The number of playtime minutes.

  Timer begins when quest is started.

  Seems to only work well once.  Recommend to not set these quests repeatable.
  However, multiple playtime quests may work (not tested).
```

# Shear - Shear sheep, ...
```
task:
  type: "shear"
  amount: <integer>              # Required: The number of sheep to shear.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
```

# Tame - Tame animals, ...
```
task:
  type: "tame"
  amount: <integer>              # Required: The number of animals to tame.
  item: "<Material>"             # Optional: The specific animal to tame.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
  world: "<string>"              # Optional: World where this task is valid; default is any world
```

# BentoBox - Requires BentoBox Level addon
```
task:
  type: "bentobox_level"
  level: <integer>               # Required: Minimum island level needed.

  If island level already exceeds the value, player must force a level recalculation.
```
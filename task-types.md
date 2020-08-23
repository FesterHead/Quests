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
```

# Brew - Brew potions, ...
```
task:
  type: "brew"
  amount: <integer>              # Required: The number of The number of potions to brew.
  item: "<Material>"             # Optional: The specific potion to brew.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
```

# Craft - Crafting, making, constructing, ...
```
task:
  type: "craft"
  amount: <integer>              # Required: The number of items to craft.
  item: "<Material>"             # Optional: The specific item to craft.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
```

# Death - Killing animals, mobs, entities, ...
```
task:
  type: "death"
  amount: <integer>              # Required: The number of entities to kill.
  item: "<Material>"             # Optional: The specific entity to kill.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
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
```

# Enchant - Enchanting items, ...
```
task:
  type: "enchant"
  amount: <integer>              # Required: The number of items to enchant.
  item: "<Material>"             # Optional: The specific item to enchant.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
```

# Fish - Fishing, catching, ...
```
task:
  type: "fish"
  amount: <integer>              # Required: The number of items to catch.
  item: "<Material>"             # Optional: The specific item to catch.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
```

# Furnace - Cooking, smelting, ...
```
task:
  type: "furnace"
  amount: <integer>              # Required: The number of items to extract from a furnace.
  item: "<Material>"             # Optional: The specific item to extract from a furnace.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
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

# Milk - Milk cows, ...
```
task:
  type: "milk"
  amount: <integer>              # Required: The number of cows to milk.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
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
```

# Tame - Tame animals, ...
```
task:
  type: "tame"
  amount: <integer>              # Required: The number of animals to tame.
  item: "<Material>"             # Optional: The specific animal to tame.
  present: "<string>"            # Optional: Present-tense action verb to be used in quest lore; default is none
  past: "<string>"               # Optional: Past-tense action verb to be used in quest lore; default is none
```

# BentoBox - Requires BentoBox Level addon
```
task:
  type: "bentobox_level"
  level: <integer>               # Required: Minimum island level needed.

  If island level already exceeds the value, player must force a level recalculation.
```
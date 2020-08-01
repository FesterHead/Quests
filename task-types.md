# Task types

[Use the Bukkit Enum Material list](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html).  Item material names can be UPPER or lower case.

Break - Mining, excavation, digging, ...
```
task:
  type: "break"
  amount: <integer>              # The number of blocks to break.
  item: "<Material>"             # If supplied, the specific block to break.
  present: "<string>"            # Optional present-tense action verb.
  past: "<string>"               # Optional past-tense action verb.

  Place tasks subtract from break tasks!
```

Breed - Breeding, mating, ...
```
task:
  type: "breed"
  amount: <integer>              # The number of animals to breed.
  item: "<Material>"             # If supplied, the specific animal to breed.
  present: "<string>"            # Optional present-tense action verb.
  past: "<string>"               # Optional past-tense action verb.
```

Brew - Brew potions, ...
```
task:
  type: "brew"
  amount: <integer>              # The number of The number of potions to brew.
  item: "<Material>"             # If supplied, the specific potion to brew.
  present: "<string>"            # Optional present-tense action verb.
  past: "<string>"               # Optional past-tense action verb.
```

Craft - Crafting, making, constructing, ...
```
task:
  type: "craft"
  amount: <integer>              # The number of items to craft.
  item: "<Material>"             # If supplied, the specific item to craft.
  present: "<string>"            # Optional present-tense action verb.
  past: "<string>"               # Optional past-tense action verb.
```

Death - Killing animals, mobs, entities, ...
```
task:
  type: "death"
  amount: <integer>              # The number of entities to kill.
  item: "<Material>"             # If supplied, the specific entity to kill.
  present: "<string>"            # Optional present-tense action verb.
  past: "<string>"               # Optional past-tense action verb.
```

Drop - Farming, harvesting, collecting, ...
```
task:
  type: "drop"
  amount: <integer>              # The number of block drops to collect.
  item: "<Material>"             # If supplied, the specific block drop to collect.
  present: "<string>"            # Optional present-tense action verb.
  past: "<string>"               # Optional past-tense action verb.
```

Enchant - Enchanting items, ...
```
task:
  type: "enchant"
  amount: <integer>              # The number of items to enchant.
  item: "<Material>"             # If supplied, the specific item to enchant.
  present: "<string>"            # Optional present-tense action verb.
  past: "<string>"               # Optional past-tense action verb.
```

Fish - Fishing, catching, ...
```
task:
  type: "fish"
  amount: <integer>              # The number of items to catch.
  item: "<Material>"             # If supplied, the specific item to catch.
  present: "<string>"            # Optional present-tense action verb.
  past: "<string>"               # Optional past-tense action verb.
```

Furnace - Cooking, smelting, ...
```
task:
  type: "furnace"
  amount: <integer>              # The number of items to extract from a furnace.
  item: "<Material>"             # If supplied, a specific item to extract from a furnace.
  present: "<string>"            # Optional present-tense action verb.
  past: "<string>"               # Optional past-tense action verb.
```

Inventory
```
task:
  type: "inventory"
  amount: <integer>                        # Amount of item to have.
  item: "<Material>"                       # The specific item.
  remove-items-when-complete: <boolean>    # Take the items away on completion.  Default is false.
  update-progress: <boolean>               # Update progress.  If this causes lag then disable it.
```

Milk - Milk cows, ...
```
task:
  type: "milk"
  amount: <integer>              # The number of cows to milk.
  present: "<string>"            # Optional present-tense action verb.
  past: "<string>"               # Optional past-tense action verb.
```

Place - Building, placing, ...
```
task:
  type: "place"
  amount: <integer>              # The number of blocks place.
  item: "<Material>"             # If supplied, the specific block to place.
  present: "<string>"            # Optional present-tense action verb.
  past: "<string>"               # Optional past-tense action verb.

  Break tasks subtract from place tasks!
```

Playtime - Building, placing, ...
```
task:
  type: "playtime"
  amount: <integer>              # The number of playtime minutes.

  Timer begins when quest is started.

  Seems to only work well once.  Recommend to not set these quests repeatable.
  However, multiple playtime quests may work (not tested).
```

Shear - Shear sheep, ...
```
task:
  type: "shear"
  amount: <integer>              # The number of sheep to shear.
  present: "<string>"            # Optional present-tense action verb.
  past: "<string>"               # Optional past-tense action verb.
```

Tame - Tame animals, ...
```
task:
  type: "tame"
  amount: <integer>              # The number of animals to tame.
  item: "<Material>"             # If supplied, the specific animal to tame.
  present: "<string>"            # Optional present-tense action verb.
  past: "<string>"               # Optional past-tense action verb.
```

BentoBox - Requires BentoBox Level addon
```
task:
  type: "bentobox_level"
  level: <integer>               # Minimum island level needed.

  If island level already exceeds the value, player must force a level recalculation.
```
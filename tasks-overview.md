- Each file ín the 'quests' folder defines a single quest.
- The name of the file is the quest ID. These must be alphanumeric and unique.
- Quest files must be in the .yml format.
- Quests can be put inside a category. When a player does /quests they will first see a menu of categories. They can click one and another menu of quests
under that category will show up. Categories can be disabled.
- A quest is a series of tasks which players must complete for a reward and may require a previous quest to start.
- A task is an objective such as breaking blocks or obtaining items.
- A reward is a command executed by the SERVER. Use {player} to get the players name.

Some of my changes include:
- Optional present/past tens action verb that can be used in the lore.
- The "reverse progress" option is available for break, place, and drop tasks.
- Added "item" and "amount" variables to include in task lore statements.
- Not all the the task typess from the main branch are here.  Some I may add back, some not.
- Many tasks renamed to match the event.
- Many tasks have the item as optional.  If one is not included it means any item will give credit.
- While you can have overlapping active tasks I don't guarantee which will get the credit.
- It's perfectly acceptable for a task to go into negative numbers.
- You can now configure multiple tasks to get credit for one break, place, or drop event.

Example task without a specific material/entity:
```
tasks:
  task1:
    type: "breed"
    amount: 2
    present: "breed"
    past: "bred"
display:
  name: "Breed Any Animal"
  lore-normal:
    - ""
    - "&7This quest requires you to:"
    - "&7 - {task1:present} {task1:amount} animals"
  lore-started:
    - ""
    - "&7Status:"
    - "&7 - {task1:progress}/{task1:amount} animals {task1:past}."
```
Example task with a spcific material/entity:
```
tasks:
  task1:
    type: "place"
    amount: 2
    item: "sand"
    present: "place"
    past: "placed"
display:
  name: "Place Specific Blocks"
  lore-normal:
    - ""
    - "&7This quest requires you to:"
    - "&7 - {task1:present} {task1:item}"
  lore-started:
    - ""
    - "&7Status:"
    - "&7 - {task1:progress}/{task1:amount} {task1:item} {task1:past}."
```

[Use the Bukkit Enum Material list](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html).  Item material names can be UPPER or lower case.
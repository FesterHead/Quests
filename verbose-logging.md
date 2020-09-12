# Verbose logging
If a quest or task doesn't seem to be processing correctly, set `verbose-logging-level: 3` in config.yml and restart the server.
Example debug output:
```
[Quests] Debug: --------------------
[Quests] Debug:               Quest: Place2
[Quests] Debug:
[Quests] Debug:       Checking task: task1
[Quests] Debug:                Type: place
[Quests] Debug:     Incoming object: DIRT
[Quests] Debug:     Expected object: SAND
[Quests] Debug:            Progress: 0
[Quests] Debug:                Need: 2
[Quests] Debug:           Completed: false
[Quests] Debug: --------------------
[Quests] Debug:               Quest: Place3
[Quests] Debug:
[Quests] Debug:       Checking task: task1
[Quests] Debug:                Type: place
[Quests] Debug:     Incoming object: DIRT
[Quests] Debug:     Expected object: n/a
[Quests] Debug:            Progress: 0
[Quests] Debug:                Need: 2
[Quests] Debug:           Completed: false
[Quests] Debug:                      Match!
[Quests] Debug:           Increment: 1
[Quests] Debug:        New progress: 1
[Quests] Debug:                      Continue task evaluation!
```

This will help you understand the order quests and tasks are processed.
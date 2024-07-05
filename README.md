# Sample Modpack Template

A modpack template made for the [ModPackUpdater](https://github.com/someramsey/ModPackUpdater), only contains placeholder files for demonstrating the required structure.

### Required Files

- Version File [`data/.version`](SampleModpack/data/.version) - Contains the version of the modpack
- Installation script [`setup.bat`](SampleModpack/setup.bat) - Script to install the modpack

---

The files don't have to necessarily be called the same, paths can be changed in the Config File. However if you decide to change the main structure, you will have to modify the install script too.

The install script can be any executable or script file, as long as it can be executed by the os. The mod will pass two arguments to the script. 

- The first one is the process id of minecraft, this is to allow the script to wait for the game to close before installing the modpack.

- The second one is the path to the game directory (The directory that contains the mods folder, config folder, saves folder, etc.)


#### Backups

The mod automatically creates a backup of the mods directory before starting the install script, so you don't have to worry about corruption. The maximum amount of backups can be set in the Config File.

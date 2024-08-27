# Minecraft Modpack Updater

This is a minecraft mod that automatically checks for new versions of your modpack and updates it. It can be used with any modpack, and you can create a custom installation script based on how the modpack is built. The mod does not host version info by itself it's a client sided mod, so you need to host it on a custom server and configure the mod to use it.

### Mod Installation and Configuration

- Download the mod from the [releases page](https://github.com/someramsey/ModPackUpdater/releases) and add it to your modpack.
- Download the [Sample Modpack](https://github.com/someramsey/ModPackUpdater/tree/SampleConfiguration) and configure it to your needs. You should host the modpack and the version info on a server. See [Hosting Modpack and Version Info](#hosting-modpack-and-version-info).
- Run the game with the mod at least once to generate the config files.
- Locate the config folder (Usually at `%APP_DATA%\.minecraft\versions\MODPACK_NAME\config`) and open the 
`updater-common.toml` file in a text editor

Once you open the config file:

- Set the `fetch_url` to the URL where the version info is hosted.
- Set the `decompression` to match how you store the modpack on the server.
- Change the `install_command` if you renamed/created a new install script. On windows you should always use the `cmd /c start` command to properly run the script due to how the file system works. 
_Though Idk about linux, havent tested it_ (～￣▽￣)～


---


#### Hosting Modpack and Version Info

The mod does not host the modpack or the version info by itself they need to be hosted on a server. The modpack can be hosted on anywhere as long as it has direct download via a url. _(You can also use dropbox or google drive)_ Although the version info can also be stored as a file on these services you should probably use a http server.

Put the modpack into an archive before uploading, supported archive formats are stated in the config file. (`Zip, BZip2, Gzip, Pack200, Z, Deflate, Snappy, LZ4`). Though `.rar` files are not supported.

The version info is supposed to be a JSON response. It should contain the latest version of the modpack and the download link for the modpack itself. The JSON should look like this:

```json
{
    "url": "somehostingservice.com/modpack.zip",
    "version": "1.0"
}
```

The modpack needs to have an install script that can be run after the download. See [Sample Modpack](https://github.com/someramsey/ModPackUpdater/tree/SampleConfiguration) for an example.

### Safety and Misuse

The mod allows the modpack author to change/swap any files in the modpack. This can be used to install malicious mods or scripts. Do not use this mod with people you do not trust.



### Example


https://github.com/user-attachments/assets/a93e7e91-e8db-4bb3-93f8-c76e43509940



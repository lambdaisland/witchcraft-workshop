# Clojure + Minecraft Workshop, ClojureD 2022

Instructions and REPL session code for the [Witchcraft](https://github.com/lambdaisland/witchcraft) (Clojure+Minecraft) workshop given at ClojureD, June 2022.

## Change the (Minecraft) World with Code

Presenters: Ariel Alexi, Felipe Barros, Arne Brasseur

In this fun and creative workshop you’ll play Minecraft via the REPL. Build amazing structures, add custom player interactions, or spawn exploding chickens.

For best results join the workshop as a pair, so you have one laptop to display the game, and one for the REPL. You can run both on a single laptop, but it’s more fun with two.

We kindly ask participants to install Minecraft (server+client, see below) before the workshop so we can dive into the interesting bits right away, and we don't pull down the conference wifi when the workshop begins.

## Quickstart for The Impatient

- clone the repo https://github.com/lambdaisland/witchcraft-workshop/
- run `bin/start-server` in one terminal
- run `bin/start-client username` in another terminal (Skip this step if you have your own copy of Minecraft)
- connect with your editor to nREPL at port 25555
- from inside the game (i.e. the client started above) connect to server `localhost:25565` (choose: Multiplayer)
- open `repl-sessions.s01-warmup` in your editor
- start evaluating forms

## Requirements (tl;dr)

- Java 17
- Clojure CLI
- A POSIX compatible shell (to run the install/start scripts)
- An nREPL-capable editor (e.g. VS Code+Calva, Emacs+CIDER, Vim+Conjure, Cursive, ...)

With these you should be able to install

- The Minecraft game (i.e. the client), version 1.18.2
- The PaperMC server

As per the instructions below

### POSIX Compatible Shell

The startup scripts assume a working Bourne Shell at `/bin/sh`. This is true of
all POSIX compatible UNIX systems, including Linux and MacOS. On Windows we
expect them to work on WSL, WSL2, git-bash, or Cygwin.

The scripts also assume a working `curl`.

### Java 17

Check the output of `java -version`, make sure it's at version 17.

```
$ java -version
openjdk version "17" 2021-09-14
OpenJDK Runtime Environment (build 17+35-2724)
OpenJDK 64-Bit Server VM (build 17+35-2724, mixed mode, sharing)
```

If you don't have Java yet or not the right version you will have to install it.
You can get Java from many different sources. If you are already using a package
manager for your operating system then use that, it's almost certain it will be
in there.

Cross platform:
- [download OpenJDK directly](https://jdk.java.net/17/) for your platform
- [sdkman](https://github.com/sdkman/sdkman-cli) is a cross platform tool for managing Java version (requires WSL/Cygwin/git-bash on Windows)

```
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 17-open
sdk use java 17-open
```

Linux:
- Ubuntu has a `openjdk-17-jdk` package
- Arch/Manjaro have `jdk17-openjdk`

Mac OS X:
- Java is available from Homebrew (`brew info java`)
- [sdkman](https://github.com/sdkman/sdkman-cli) is a cross platform tool for managing Java version

Windows:
- On Windows using Scoop is an option, using the [Java Bucket](https://github.com/ScoopInstaller/Scoop/wiki/Java), 
- Alternatively Windows users can use [Chocolatey](https://community.chocolatey.org/packages/openjdk17)

### Clojure CLI

Most versions should be fine, we don't use any of the breaking changes that were introduced. e.g.

```
$ clojure --version
Clojure CLI version 1.10.3.967
```

### Getting Minecraft Client, Option 1: scripted

If you have Minecraft already installed then you're all set, if not we
provide two scripts to easily install and run it.

Once you have an account (do not try to download the game from the above sources), run:

```
bin/install-client
bin/start-client myusername
```

These should work even in offline mode (once installation is completed), so we
can still run the workshop in case the conference wifi gets overloaded. Of
course we expect everyone to have a Minecraft player account.

### Getting Minecraft Client, Option 2: Launcher

Alternatively can also download the official launcher through the [Minecraft.net
site](https://www.minecraft.net/download) or by installing
[MultiMC](https://multimc.org/), and then creating a new instance.

You will need a Minecraft Java Edition player account, which you get when
[buying a copy of the game](https://www.minecraft.net/store/minecraft-java-edition), 
which at the time of writing costs €23.95.

Alternatively you can sign up for a “[XBOX PC Game Pass](https://www.xbox.com/xbox-game-pass/pc-game-pass)”
for €1 for the first month, and then [immediately cancel](https://account.microsoft.com/services/pcgamepass/cancel?fref=billing-cancel).

Note that these last two are only launchers, you will have to go in and from the
launcher install the actual game. We'll be using the latest stable version
1.18.2, so make sure you have that ready and are able to start the game. This
will require Java 17, which you will also need for the workshop.

### Server Setup

We are going to use the [PaperMC](https://papermc.io/) modded Minecraft server,
and add to it the [witchcraft-plugin](https://github.com/lambdaisland/witchcraft-plugin) which
will supply an nREPL inside the Minecraft process that we can then connect to.

Everything is encapsulated in the `bin/start-server` script. This will download
PaperMC and install the Witchcraft-plugin.

The server startup is successful when you see `nREPL server started on port 25555 on host localhost - nrepl://localhost:25555`

## Getting Connected

### nREPL

The PaperMC Minecraft server is pre-loaded with the witchcraft-plugin, which
exposes an nREPL server at port 25555. From your editor find the command to
"connect to nREPL", for instance in Emacs: `M-x cider-connect-clj`.

- Host: `localhost`
- Port: `25555`

### Client

Start the Minecraft game. You should see a screen with three buttons,
"Singleplayer", "Multiplayer", "Minecraft Realms". Click on "Multiplayer", this
will bring you to an (empty) list of servers to conect to.

Click on `Add Server`, and for the Server Address type in `localhost:25565`.
Click "Done", now you should see a server with as tagline "ClojureD Workshop" in
the list. Double click on it and the game launches.

At this point you want to press `F3+p` (hold `F3`, then press `p`), this toggles
"Pause on Lost Focus". Make sure it says "Pause on Lost Focus: Disabled". Now
you can switch to your editor (E.g. with Alt+Tab) while Minecraft stays visible.

This part is a little tricky, Minecraft has a tendency to grab your mouse
cursor. You can get it back with `ESC`, but that pauses the game, so you no
longer see what's happening. What tends to work is putting the minecraft window
and editor window side-by-side, then using a keyboard shortcut (e.g. `Alt-Tab`)
to switch to your editor. After that try not to hover over the minecraft window,
or it will capture your mouse cursor again.

Alternatively pair up with someone, so you can connect with minecraft from one
laptop, and with nREPL from the other. You can even have multiple players and
coders connected to the same server for even more fun (or more chaos).

## Understanding the Basics

This workshop is open to anyone, regardless of whether you've actually played
Minecraft before. That said it will help tremendously to be familiar with the
basic concepts and mechanics. This goes for using Witchcraft in general, it pays
off to spend a little bit of time just playing to game before you start
manipulating it with code.

The video series [Minecraft Survival
Guide](https://www.youtube.com/playlist?list=PLgENJ0iY3XBjpNDm056_NSPhIntVMG0P8)
by youtuber Pixlriffs contains a video on almost every aspect and mechanic in
the game, and especially the first few are very instructive for anyone just
starting out

- [The Minecraft Player's Dictionary](https://www.youtube.com/watch?v=u7lE0MG80qw&list=PLgENJ0iY3XBjpNDm056_NSPhIntVMG0P8&index=1)
- [Controls, Keyboard Shortcuts, and F3](https://www.youtube.com/watch?v=bkQqqxpqFo0&list=PLgENJ0iY3XBjpNDm056_NSPhIntVMG0P8&index=2)
- [Video Settings & Accessibility](https://www.youtube.com/watch?v=W6eYr9lkK_s&list=PLgENJ0iY3XBjpNDm056_NSPhIntVMG0P8&index=3)

### Keybindings

These are the default bindings, they might be different based on your keyboard layout. We're only mentioning a few of them to get yout started. You can always find the current bindings, and change them, by pressing ESC > Options... > Controls > Key Binds...

- `w`,`a`,`s`,`d` are your "arrow keys" for walking around
- spacebar: jump, shift: crouch
- left click: destroy blocks (hold it down), attack enemies (click)
- right click: place block or use item in hand
- `e` open inventory
- number keys (`1` - `9`) pick an item on your hotbar
- `F2` screenshot (look in `client/screenshots`)
- `F11` toggle fullscreen
- `F3` debug mode (shows lots of info including coordinates, biome, block type)

## The Workshop

We'll be going through the REPL session files that you can find under
`repl_sessions`. You can jump around between them, do things on your own pace,
jump ahead, or go on a complete tangent, but we'll likely present them roughly
in order during the workshop.

- `s01-warmup` : Some examples of the things Witchcraft can do, to whet your
  appetite. We're not going to dig too deep into things at this part, we just
  want you to try them out and see what happens.

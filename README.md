# Clojure + Minecraft Workshop, ClojureD 2022


Instructions and sample code for the
[Witchcraft](https://github.com/lambdaisland/witchcraft) (Clojure+Minecraft)
workshop given at ClojureD, June 2022.

## Change the (Minecraft) World with Code

Presenters: Ariel Alexi, Felipe Barros, Arne Brasseur

In this fun and creative workshop you’ll play Minecraft via the REPL. Build amazing structures, add custom player interactions, or spawn exploding chickens.

For best results join the workshop as a pair, so you have one laptop to display the game, and one for the REPL. You can run both on a single laptop, but it’s more fun with two.

We ask participants kindly to install Minecraft before the workshop so we can dive into the interesting bits right away.

## Getting Minecraft (Client Setup)

You will need a Minecraft Java Edition player account, which you get when
[buying a copy of the game](https://www.minecraft.net/de-de/store/minecraft-java-edition), which at
the time of writing costs €23.95. Alternatively you can sign up for a “[XBOX PC
Game Pass](https://www.xbox.com/de-DE/xbox-game-pass/pc-game-pass)” for €1 for
the first month, and then immediately cancel.

Download and install the Minecraft launcher before the conference, either
through the [official site](https://www.minecraft.net/de-de/download) or by
installing [MultiMC](https://multimc.org/). Install the latest stable version
1.18.2, and make sure you can start the game. This will require Java 17, which
you will also need for the workshop.

## Server Setup

We are going to use the [PaperMC](https://papermc.io/) modded Minecraft server,
and add to it the [witchcraft-plugin](https://github.com/lambdaisland/witchcraft-plugin) which
will supply an nREPL inside the Minecraft process that we can then connect to.

Everything is encapsulated in the `bin/start-server` script. This will download
PaperMC and install the Witchcraft-plugin.

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




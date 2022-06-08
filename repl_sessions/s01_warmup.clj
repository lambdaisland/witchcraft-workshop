(ns s01-warmup
  "Some fun things to help you get a taste of Witchcraft."
  (:require [lambdaisland.witchcraft :as wc]
            [lambdaisland.witchcraft.markup :as markup]))

;; Welcome to the Witchcraft workshop! Hopefully you managed to get your server
;; running, and managed to connect to it, both from the game (bin/start-client,
;; then connect to localhost:25565), and with your editor where you are reading
;; this, using nREPL (connect to localhost:25555).

;; If so then go ahead and start going through this file, this is an introduction
;; you can go through by yourself while we go around and make sure everyone has a
;; working setup. Once everyone is up and running we'll move on to part two.

;; If all went well you are standing on some grass with some birch and oak trees
;; nearby. Have a look around! You can move around with the mouse + w/a/s/d
;; keys. Try breaking a block by holding the left mouse button. Good job, you're
;; playing minecraft.

;; We want to manipulate this world you find yourself in, through a Clojure REPL.
;; But it's a big world, and we like to be able to see the changes we make, so
;; let's figure out where we are in the world first.

;; If there's only one player online then this will get you that player, or pass
;; it a player name to be explicit

(def me (wc/player #_"sunnyplexus"))

(wc/send-title me (markup/fAnCy "Hello from Clojure!" [:dark-blue :dark-green]))

;; `loc` will give you the location of something as a Clojure mapcat

(wc/loc me)

(wc/send-title me (pr-str (wc/locv me)))

;; (You can also try `locv`, `block`, `get-block`, `target-block`, `x`, `y`,
;; `z`, `inventory`)

;; Let's save our current location, so we have a point of reference
(defonce anchor (wc/loc me))
(prn anchor)

;; If it starts getting night and you can't see what you're doing, then time
;; travel back to the morning.
(wc/set-time 0)

;; Let's start with a splash, this puts a water block 5 blocks above your head
(wc/set-blocks [[0 5 0 :water]] {:anchor anchor})

;; And take it away again
(wc/undo!)

;; Let's put a beautiful blue and purple ball in the sky
(wc/set-blocks
 (for [x (range -6 7)
       y (range -6 7)
       z (range -6 7)
       :when (< (wc/distance [x y z] [0 0 0]) 5.3)]
   [x y z (rand-nth [:lapis-block :amethyst-block])])
 {:anchor (wc/add anchor [0 12 0])})

;; Want to have a better look from above? Either allow yourself to fly, as you
;; would in creative.

(wc/fly! me)

;; Now pressing SPACE will move you upwards, and SHIFT will get you down. Once
;; you've landed press SPACE twice to fly again.

;; Let's try some other things, let's spawn some chickens!

(def chickens
  (doall
   (repeatedly 10 #(wc/spawn (wc/add anchor [0 4 0]) :chicken))))
;; And blow them up!

(doseq [chicken chickens]
  (wc/despawn chicken)
  ;; Witchcraft will convert `chicken` to a location
  (wc/create-explosion chicken 5.0)
  (Thread/sleep 500))

;; Let's give ourselves some gear: a pickaxe with silk touch, some protection,
;; and some food to munch on.

(wc/into-inventory
 me
 [{:material :diamond-pickaxe
   :enchants {:silk-touch 1}
   :lore [[:<>
           [:gold "A "]
           [:white "gossamer "]
           [:gold "pick"]]]}

  {:material :netherite-chestplate
   :enchants {:protection 5
              :blast-protection 5}}

  {:material :netherite-boots
   :enchants {:feather-falling 5}}

  [:golden-carrot 64]])

;; You can jump around the server a bit to see different landscapes and biomes.
;; Note that this can put a lot of strain on your CPU (and battery), since it
;; needs to generate all that new landscape.
(wc/teleport me [(rand-int 10000) 100 (rand-int 10000)])

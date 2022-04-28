(ns s02-locations-blocks-entities
  "Let's look at the core concepts of Minecraft"
  (:require [lambdaisland.witchcraft :as wc]))

;; In this section we'll look at the core concepts that Minecraft is made of,
;; and the objects used to represent them. Witchcraft goes to great lengths to
;; make sure you don't have to deal with the actual Java classes, converting to
;; and from Clojure data structures as necessary. Still it's good to have a
;; sense of what is there.

;; Try looking at some things and checking the value of `target-block` or
;; `get-target-block`. This is the block you are currently looking at, it gets a
;; thin black outline in the game.

(def me (wc/player))

;; This returns a Clojure map with the information about the block. All blocks
;; have a :x, :y, :z location and a :material. Some will also have
;; a :direction (like `:east`, `:west`, `:south`, `:north`), and possibly
;; `:block-data`
(wc/target-block me)

;; This returns an instance of `org.bukkit.block.Block`. Note that witchraft
;; installs a print-handler, which is why it still looks kind of Clojure-y. We
;; don't currently install reader functions though, so this tagged literal is
;; not readable when used in source code.
(wc/get-target-block me)
(supers (class (wc/get-target-block me)))

;; Notice the symmetry between `target-block` and `get-target-block`. Witchcraft
;; has several of these pairs, where one function returns the underlying object,
;; and the other returns plain data.
;;
;; We either use this pattern with `get-`, or we have the plain-data version use
;; an abbreviated name. You can always tell which is which: the function that
;; returns a Java object has a longer name than the one that returns a Clojure
;; value.

(wc/loc me)
(wc/location me)

;; The Minecraft world consists of two things: blocks and entities, which are
;; placed at a given Location. Blocks are things like dirt, stone, wood, leaves,
;; flowers, lanterns, all the things that are placed in the world at a fixed
;; integer x/y/z coordinate.

;; Entities are all the "loose" things, these can be "mobile entities"
;; or "mobs" (from animals, villagers, players), items you can pick up, mine
;; carts, boats, the little green experience orbs



;; All of these are placed at a given x/y/z location in a specific world.
;; Minecraft normally has three worlds, the Overworld, which is where you
;; currently are and where much of the game is played, the Nether, a hellish
;; dimension of fire and peril, and the End, an eerily void place where you go
;; at the end of the game to fight the dragon.


(wc/set-time 0)

(wc/set-blocks
 (for [x (range 5)
       y [-1 0 1 2]
       z (range 5)]
   (cond
     (and (= y -1)
          (or (#{0 4} x)
              (#{0 4} z))
          (not (and (#{0 4} x)
                    (#{0 4} z))))
     [x y z :end-portal-frame {:facing (cond
                                         (= 0 x) :east
                                         (= 4 x) :west
                                         (= 0 z) :south
                                         (= 4 z) :north)
                               :eye (not= 0 (rand-int 3))}]

     :else
     [x y z :air]))
 {:anchor (wc/add me [0 1 5])})

(wc/add-inventory me :ender-eye 12)
(wc/add-inventory me :end-portal-frame 64)
(wc/add-inventory me :diamond-pickaxe)

(wc/teleport me {:world "world"})

(wc/target-block me)

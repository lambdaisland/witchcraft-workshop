(ns s02-locations-blocks-entities
  "Let's look at the core concepts of Minecraft. This section is a bit more
  theoretical, but it'll give you a much better foundation for what comes next,
  and hopefully give you a feeling for how the Witchcraft API is designed. It's
  quite intuitive once you see the patterns."
  (:require [lambdaisland.witchcraft :as wc]))

(def me (wc/player))
(wc/set-game-rule :do-daylight-cycle false)
(wc/set-time 2000)

;; In this section we'll look at the core concepts that Minecraft is made of,
;; and the objects used to represent them. Witchcraft goes to great lengths to
;; make sure you don't have to deal with the actual Java classes, converting to
;; and from Clojure data structures as necessary. Still it's good to have a
;; sense of what is there.

;; The Java classes we'll run into are from the Bukkit API. This is an
;; implementation-independent open-source API for Minecraft that many modded
;; servers implement, so that you are isolated from the implemenentation details
;; of the proprietary Minecraft code.

;; Let's start with locations. We need to position things in a three-dimensional
;; world, and to represent the position of a thing in that world Bukkit uses a
;; org.bukkit.Location

(wc/location me)

;; This returns an instance of `org.bukkit.Location`. Note that witchraft
;; installs a print-handler, which is why it still looks kind of Clojure-y. We
;; don't currently install reader functions though, so this tagged literal is
;; not readable when used in source code.

(class (wc/location me));; => org.bukkit.Location

;; Let's see what's in there, [[bean]] is a Clojure function that calls
;; all "getters" on a Java object, and returns them as a map.
(bean (wc/location me))

;; There's a bunch of things here, but the things that matter are `x`, `y`, `z`,
;; `pitch`, `yaw`, and `world`. These make up a location "value".

;; Of course Witchcraft has a more convenient function which gives you a nice
;; Clojure map:

(wc/loc me)

;; There's also [[wc/locv]], (alias: [[wc/xyz]]) which just returns the x/y/z
;; values as a vector, great for destructuring.

(let [[x y z] (wc/locv me)]
  ,,,)

;; Witchcraft functions will transparently accept all three, so you can ask for
;; the block at a given location using a map, a vector, or a Location
;; object (and more, anything that we can reasonably coerce to a Location). The
;; API is very forgiving like that.

(wc/get-block {:x 0 :y 62 :z 100})
(wc/get-block [0 62 100])
(wc/get-block (wc/location me))

;; - `x`: east-west position
;; - `z`: north-east position
;; - `y`: height level. Ranges from -64 to 320, with 62 being sea level.
;; - `world`: Minecraft has multiple worlds, you are currently in the
;;   Overworld (simply called `"world"` in the API). There's also the Nether and
;;   the End.

;; - `pitch`: the "tilt" of an entity, ranging from -90 (looking straight down),
;;   over 0 (looking ahead) to 90 (looking straight up)
;; - `yaw`: the direction you are facing. 0=south, 90=west, 180=north, 270=east

;; If you press F3 you'll get a debug screen where you can also find your
;; current location.

;; Now compare these two:

(wc/loc me)
(wc/loc (wc/get-target-block me))       ; this is the block you are looking at,
                                        ; it gets a thin black outline if you're
                                        ; close enough

;; The player location has floating point x/y/z, as well as pitch and yaw. The
;; block location has integer x/y/z, and no pitch or yaw.

;; The Minecraft world consists of two things: blocks and entities. Blocks are
;; things like dirt, stone, wood, leaves, flowers, lanterns, all the things that
;; are placed in the world at a fixed integer x/y/z coordinate.

;; Entities are all the "loose" things, these can be "mobile entities"
;; or "mobs" (from animals, villagers, players), items you can pick up, mine
;; carts, boats, the little green experience orbs, arrows, or blocks that are
;; falling down.

;; Entities have a type, blocks have a material.

(keys wc/entity-types)
(keys wc/materials)

;; There's a [[wc/add]] function for manipulating locations, it will return the
;; same type you give it as its first argument. The remaining arguments can be
;; anything location-like.

(wc/add (wc/location me) [0 3 0])
(wc/add (wc/loc me) [0 3 0])
(wc/add (wc/locv me) [0 3 0])

;; Blocks in the world are represented by a `org.bukkit.block.Block`, besides
;; their location their main defining feature is their material. Here too we
;; have a function that returns the Bukkit class instance ([[get-block]]), and
;; one that returns a plain Clojure map ([[block]]).

(wc/get-block [38 68 -1260])
(wc/block [38 68 -1260])

;; If you are using the world seed that we configured for the
;; workshop (level-seed=35), then this should find a block of Obsidian at this
;; location. Obsidian blocks always look the same, they don't have a direction
;; or any other variants.

;; Let's go see what we're actually looking at

(wc/loc me)
(wc/teleport me {:x 34 :y 69 :z -1268 :pitch 15.3 :yaw -17.6 :world "world"})

;; In front of this obsidian block there's a chest, and chests do have a
;; direction, and some other variant info represented by a "block-data" map

(wc/block [38 67 -1261])

;; Notice the `get-block` vs `block` and `location` vs `loc`. There's a pattern here.
;;
;; - location / loc
;; - get-block / block
;; - get-target-block / target-block
;; - get-inventory / inventory
;; - material / mat
;;
;; Some use `get-` for the Bukkit object version and omit the `get-` for the
;; Clojure version, others use an abbreviated form for the Clojure version. The
;; shorter of the two is always the Clojure version!


;; As with locations, if you need to specify a block you can use a map, vector,
;; or Block object. Let's put an anvil next to the chest, you can play around
;; with giving it a different direction.

(wc/set-block {:x 37 :y 67 :z -1261 :material :anvil :direction :east})
(wc/set-block [37 67 -1261 :anvil {:direction :north}])

;; Now that we're here at this broken portal, let's fix it and go see what the
;; Nether looks like. We'll fix this Nether portal with Obsidian blocks, and
;; then light it up with flint and steel.

(wc/set-blocks (for [x (range 4)
                     y (range 5)
                     z [0]]
                 [x y z (if (or (#{0 3} x)
                                (#{0 4} y))
                          :obsidian
                          :air)])
               {:anchor [35 67 -1260]})

(wc/add-inventory me :flint-and-steel)
;; use this to click on the bottom layer of obsidian blocks

;; The nether is a scary place, you can come back through the portal, or teleport back.

(wc/teleport me {:x 34 :y 69 :z -1268 :pitch 15.3 :yaw -17.6 :world "world"})

;; For completeness sake let's also find an end portal. These are normally
;; rather hard to find, but we'll just make one ourselves.

;; Keep an eye on this pattern: a x/y/z for expression, used with set-blocks
;; and :anchor.

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
                               :eye true}]
     (and (= y -1)
          (not (#{0 4} x))
          (not (#{0 4} z)))
     [x y z :end-portal]

     :else
     [x y z :air]))
 {:anchor (wc/add me [0 1 5])})

(ns s04-palettes-textures
  (:require [lambdaisland.witchcraft :as wc]
            [lambdaisland.witchcraft.cursor :as c]
            [lambdaisland.witchcraft.matrix :as m]
            [lambdaisland.witchcraft.palette :as palette]
            [lambdaisland.witchcraft.shapes :as shapes]))

(def me (wc/player))

;; So far we've looked at the shape of things with the shapes API, but in order
;; to put something into the world you need to decide which kinds of blocks to
;; use. Minecraft creators talk a lot about "palettes" and "gradients".
;;
;; If you're not a seasoned minecrafter it can be hard to know which options
;; there are, how do you find out about blocks that go well with other blocks?
;; For these things the palette namespace comes to the resque.
;;
;; I've used a color picker to pick an RGB color (google "color picker"), this
;; is kind of a warm salmon color.

(palette/nearest-material [235 64 52])
;;=> :red-wool

;; Turns out the closest equivalent in the game is the red wool block. But
;; building an entire structure out of red wool is kind of boring. What you want
;; to do is "texturize" it, replace some of them with other blocks that add some
;; variation.

;; Let's see what has a similar color. The neighbors function returns a sequence
;; of all materials in the game, sorted by the color distance from the given
;; block.

(def anchor (wc/add (wc/target-block me) [0 3 0]))

;; Let's take the top matches, and lay them out in a 5x5 square so we can have a
;; better look.

(let [materials (->> :red-wool
                     palette/neighbors
                     (map first)
                     (take 24)
                     (cons :red-wool)
                     (partition 5))]
  (wc/set-blocks
   (for [x (range 5)
         y (range 5)
         z [0]]
     [x y z (nth (nth materials x) y)])
   {:anchor anchor}))

;; You can see some of the shortcomings here, for instance the bookcase is in
;; there because red is the most common color in that texture, but it's clearly
;; not a red block. The empty spot is for a lightning rod, which is a material
;; but not a full block. Still, we can see some good candidates to combine. You
;; can use F3 and point at a block if you're not sure what the material is
;; called.

;; I'm going to pick out a few and see if we can come up with a nice use for
;; them:

:red-wool
:red-concrete
:nether-wart-block
:red-mushroom-block

;; And I'll keep this one aside as a sort of "accent" block

:red-glazed-terracotta

;; rand-palette takes a map from material to probability, and returns a
;; material. You can evaluate this a few times to see for yourself. We'll start
;; with having all blocks be of equal weight. Let's put up a wall with this.

(palette/rand-palette
 {:red-wool 1
  :red-concrete 1
  :nether-wart-block 1
  :red-mushroom-block 1})

(wc/set-blocks
 (for [x (range 10)
       y (range 10)
       z [0]]
   [x y z
    (palette/rand-palette
     {:red-wool 1
      :red-concrete 1
      :nether-wart-block 1
      :red-mushroom-block 1})])
 {:anchor anchor})

;; Not sure if it looks like anything... but it's cool.

;; After some experimentation I settled on a pattern of mostly nether-wart,
;; accentuated with red wool and concrete. I decided the mushroom blocks were a
;; bit too particular.

(wc/set-blocks
 (for [x (range 10)
       y (range 10)
       z [0]]
   [x y z
    (palette/rand-palette
     {:red-wool 2
      :red-concrete 1
      :nether-wart-block 20
      :red-mushroom-block 0})])
 {:anchor anchor})

;; All of the `shapes` take a `:material` option, this can be a simple
;; material (as a keyword), or a function which takes the current position, and
;; returns a material.

;; Here you notice that a palette like this can give a very different impression
;; at a distance than up-close. Try adding in the mushroom blocks again, to get
;; a more "mottled" appearance.

(def red-palette (fn [& _]
                   (palette/rand-palette
                    {:red-wool 2
                     :red-concrete 1
                     :nether-wart-block 20
                     :red-mushroom-block 0})))

(wc/set-blocks
 (shapes/torus {:radius 60
                :tube-radius 12
                :material red-palette
                :start [0 50 0]})
 {:anchor anchor})

;; You can also play with the fact that the material function receives the x/y/z of
;; the block:

(wc/set-blocks
 (shapes/torus {:radius 60
                :tube-radius 12
                :material (fn [[x y z]]
                            (cond
                              (< 65 y 73)
                              :orange-stained-glass
                              (<= 73 y)
                              :air
                              :else
                              (palette/rand-palette
                               {:red-wool (Math/abs x)
                                :orange-concrete (Math/abs z)})))
                :start [0 74 0]})
 {:anchor anchor})

;; The palette namespace also contains functions to help you construct "gradients".
;; Let's see what that looks like.

;; With this it tries to interpolate the block colors, finding intermediate blocks
;; that complete the sequence:

(let [materials (palette/material-gradient :pink-terracotta :red-sandstone 8)]
  (wc/set-blocks
   (for [x (range 8)
         y [0]
         z [0]]
     [x y z (nth materials x)])
   {:anchor anchor}))

;; I'm not entirely convinced that that red terracotta should be in there, but the
;; rest looks quite nice. Our gradient then becomes:

:pink-terracotta
:stripped-acacia-log
:acacia-planks
:cut-red-sandstone
:red-sandstone

;; To actually make it work like a gradient you want to randomize the transitions a
;; bit. Otherwise you get this very sudden change:

(let [gradient (palette/gradient-gen {:palette
                                      [:pink-terracotta
                                       :pink-terracotta
                                       :stripped-acacia-log
                                       :acacia-planks
                                       :cut-red-sandstone
                                       :red-sandstone]
                                      ;; alternative pallette that's a bit more
                                      ;; pronounced.
                                      #_
                                      [:pink-terracotta
                                       :pink-terracotta
                                       :brown-mushroom-block
                                       :lime-terracotta
                                       :slime-block
                                       :emerald-block]
                                      :spread 16
                                      :bleed-distance 4
                                      :bleed 0.7})]
  (wc/set-blocks
   (shapes/tube {:length 80
                 :direction [0 1 0]
                 :radius 10
                 :inner-radius 8.5
                 :material (fn [[x y z]]
                             (gradient y))})
   {:anchor anchor}))

(wc/undo!)
(wc/clear-weather)

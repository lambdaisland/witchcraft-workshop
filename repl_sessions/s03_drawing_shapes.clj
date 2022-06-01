(ns s03-drawing-shapes
  "In this section we'll create a shape out of smaller shapes, and show how you
  can start building cool things through code."
  (:require [lambdaisland.witchcraft :as wc]
            [lambdaisland.witchcraft.shapes :as shapes]
            [lambdaisland.witchcraft.matrix :as m]
            [lambdaisland.witchcraft.gallery.big-chicken
             :as chicken
             :refer [chicken-shape]]))

(def me (wc/player))

;; Let's use the shapes API to create some structures. Start by finding a place
;; where you have a good vantage point. If you're on the world seed we're
;; using (34), then this is a good spot. If you're using a different spot then
;; save it here so you can always jump back.

(.getSeed (wc/world "world")) ;;=> 34

;; (wc/loc me)
(wc/teleport
 me
 {:x 2580.45
  :y 119.0
  :z -120.69
  :pitch 15.45
  :yaw -20.98
  :world "world"})

;; Now fly into the valley and pick a block that's sort of in the middle of your
;; view, we'll build our things relative to this point, so this will be our
;; anchor.

;; (wc/target-block me)
(def anchor {:x 2603 :y 95 :z -59})

;; Let's change that to a nice recognizable color
(wc/set-block anchor :pink-wool)

;; We can go one step further and put two more points to help us recognize which
;; direction is which. This will put a blue block towards positive X, and a red
;; block towards positive z.

(wc/set-blocks
 [[0 1 0 :pink-wool]
  [7 1 0 :blue-wool]
  [0 1 7 :red-wool]]
 {:anchor anchor})


;; And try to build something there, this chicken is part of the witchcraft
;; gallery, a number of example namespaces that you can explore.

(wc/set-blocks
 (chicken-shape (wc/add anchor {:y 5})))

;; You can have a look at what `chicken-shape` returns
(chicken-shape (wc/add anchor {:y 5}))

;; Ok, let's get rid of that and do our own thing

(wc/undo!)

;; We're going to draw a version of the clown emoji! 🤡

;; The plan is this
;; - draw a big beige-ish ball that will be the head
;; - stick a bright red nose in the middle
;; - give it eyes surrounded by blue makeup
;; - give it a mouth, this one is trickier but we can get there by creating a
;;   ball and slicing out the part we need
;; - generate clouds of pink wool for the hair

;; The center of the head will be thirty blocks above ground, so we'll use that
;; as our new anchor point for all that follows.
(def clown-anchor (wc/add anchor [0 30 0]))

;; The head is easy enough, 30 blocks above ground, and with a 14.5 block
;; diameter. We won't actually get half blocks, but I've found that using
;; non-integer numbers leads to slightly nicer shapes sometimes. This uses a
;; distance function to find all blocks within a certain distance for the
;; center. We'll use white terracotta, which really has a more pinkish hue.

;; You can play around with these things, I've put these together with a bunch
;; of trial and error on the REPL, `(wc/undo!)` is your friend!

(wc/set-blocks
 (shapes/ball {:center [0 0 0] ;; this is optional since it's [0 0 0]
               :radius 14.5
               :material :white-terracotta})
 {:anchor clown-anchor})

;; Draw the nose. Here the three anchor points come in handy, the red wool block
;; is towards positive z relative to the pink block, so we know that the nose
;; needs to be on the negative-z side of the face for us to see it.

(wc/set-blocks
 (shapes/ball {:center [0 0 -14]
               :radius 2.4
               :material :red-concrete})
 {:anchor clown-anchor})

;; For the eye we'll use another for loop, making the inner two blocks black,
;; the outer blocks blue, and the ones in the middle white.

;; Note that with everything we're doing we're working first with relative
;; coordinates (around [0 0 0]), which we then offset to a specific place in the
;; world. This means this code can be used to draw the same shape elsewhere with
;; ease.

(def eye
  (for [x (range 5)
        y (range 6)
        z [0]]
    [x y z
     (cond
       (and (#{2} x) (#{2 3} y))  :coal-block
       (or (#{0 4} x) (#{0 5} y)) :light-blue-concrete
       :else                      :white-concrete)]))

(wc/set-blocks eye {:anchor (wc/add clown-anchor [3 1 -13])})
(wc/set-blocks eye {:anchor (wc/add clown-anchor [-8 1 -13])})

;; The mouth is trickier, we'll start with a ball that's red on the outside and
;; white on the inside, then use `filter` to only retain a single "slice" from
;; the bottom middle.

(wc/set-blocks
 (filter (fn [[x y z]]
           (and (< -9 x 9)
                (< y -4)
                (< -2 z 2)))
         (shapes/ball {:center [0 0 0]
                       :radius 9.5
                       :material :red-wool
                       :fill :white-wool}))
 {:anchor (wc/add clown-anchor {:z -12})})

;; Exercise for the reader: see if you can make the top line of the mouth red as
;; well.

;; The hair is trickier, we want it to be fluffy and cloudy. You could try
;; combining a bunch of smaller balls, but we'll do something else.

;; This function returns an infinite sequence of random locations, at most delta
;; blocks away in any direction from [0 0 0]. We want it to return both positive
;; and negative numbers, hence the (- (* 2 ...) ...).

(defn random-positions [delta]
  (repeatedly #(do [(- (* 2 (rand) delta) delta)
                    (- (* 2 (rand) delta) delta)
                    (- (* 2 (rand) delta) delta)])))

;; let's see what that looks like. Limit the result with `take`, or your server
;; will freeze up!

(take 10 (random-positions 10))

;; You can draw the result to get an idea, add a material to the location
;; vectors, and put it somewhere where we can see it, next to the clown. The
;; result is basically a sparse cube.

(wc/set-blocks
 (map #(conj % :polished-andesite)
      (take 1000 (random-positions 10)))
 {:anchor (wc/add anchor [-30 30 0])})

(wc/undo!)

;; Now we filter these, we use a distance function to get blocks that are within
;; a ball around the center, rather than within a cube. We use some
;; randomization as well, blocks that are closer to the center are more likely
;; to be retained than blocks that are further away.

(defn surrounding-positions [delta]
  (filter (fn [[x y z]]
            (<
             (wc/distance [0 0 0] [x y z])
             (* delta (rand))))
          (random-positions delta)))

;; You can see that in action too:

(wc/set-blocks
 (map #(conj % :polished-andesite)
      (take 500 (surrounding-positions 10)))
 {:anchor (wc/add anchor [-30 30 0])})

;; Put these in the right spot, and we have our fluffy tufts of hair

(wc/set-blocks
 (map #(conj % :pink-wool)
      (take 500 (surrounding-positions 8.5)))
 {:anchor (wc/add anchor [-11 40 -3])})

(wc/set-blocks
 (map #(conj % :pink-wool)
      (take 500 (surrounding-positions 8.5)))
 {:anchor (wc/add anchor [11 40 -3])})

;; So far we've only used the ball shapes, but you can try out the other
;; functions in the shapes namespace, like `torus`, `line`, `tube`, `torus` or
;; `rectube`, to make your own creation. In the process you might want to find
;; nice materials that give you the color and texture you want, that's what
;; we'll talk about in the next section.

;; The shapes API works well with the matric API, which gives you a number of
;; linear algebra operations with vectors and matrices.


(doseq [angle [0 Math/PI (/ Math/PI 2) (- (/ Math/PI 2))]]
  (wc/set-blocks
   (->> chicken/chicken
        (m/rotate angle :x :z)
        (m/translate (wc/target-block me)))))

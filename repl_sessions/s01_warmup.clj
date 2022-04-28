(ns s01-warmup
  "Some fun things to help you get a taste of Witchcraft."
  (:require [lambdaisland.witchcraft :as wc]))

;; If there's only one player online then this will get you that player, or pass
;; it a player name to be explicit

(def me (wc/player))

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

;; Alternatively you can give yourself wings, and propel yourself with firework rockets

;; Let's turn that off again first
(wc/allow-flight me false)

;; Add these
(wc/add-inventory (wc/player) :elytra 1)
(wc/add-inventory (wc/player) :firework-rocket 64)

;; Open your inventory (`e`), and move the wings to the chestplate armor
;; slot (top left next to your avatar). Close your inventory again, and make
;; sure you have the rockets in your hand (mouse scroll wheel or number keys
;; 1-9).
;;
;; Now press SPACE twice and click right. Free like a bird. Be careful landing!

;; Let's spawn some chickens!
(def chickens
  (doall
   (repeatedly 10 #(wc/spawn (wc/add anchor [0 4 0]) :chicken))))
;; And blow them up!

(doseq [chicken chickens]
  (wc/despawn chicken)
  ;; Witchcraft will convert `chicken` to a location
  (wc/create-explosion chicken 5.0)
  (Thread/sleep 500))

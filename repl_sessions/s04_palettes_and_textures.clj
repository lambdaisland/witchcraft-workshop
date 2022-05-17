(ns s03-drawing-shapes
  (:require [lambdaisland.witchcraft :as wc]
            [lambdaisland.witchcraft.cursor :as c]
            [lambdaisland.witchcraft.shapes :as shapes]
            [lambdaisland.witchcraft.palette :as palette]))

;; This section is WIP

(defn spiral-step-seq [target]
  (reduce
   (fn [v i]
     (let [sum (apply + v)]
       (cond
         (= (+ sum i) target)
         (reduced (conj v i))
         (< target (+ sum i))
         (reduced (conj v (- (+ sum i) target)))
         :else
         (conj v i))))
   []
   (cons 1 (mapcat #(list % %) (next (range))))))

(defn spiral [materials]
  (let [loc (-> (c/start [0 0 0])
                (c/material (fn [{:keys [blocks]}]
                              (nth materials (count blocks)))))]
    (reduce #(-> %1 (c/rotate 2) (c/steps %2)) loc (spiral-step-seq (count materials)))))

(spiral [:bricks
         :granite])

(wc/set-blocks
 (:blocks
  (spiral (take 50 (map first (palette/neighbors :bricks)))))
 {:anchor
  {:x 2122, :y 70, :z 248}})

(wc/set-blocks
 [[0 0 0 :bricks]
  [0 0 1 :granite]
  [1 0 0 :polished-granite]
  [1 0 1 :exposed-copper]
  [-1 0 0 :raw-copper-block]
  [0 0 -1 :brown-glazed-terracotta]
  [-1 0 -1 :terracotta]
  [1 0 -1 :dripstone-block]
  [-1 0 1 :nether-quartz-ore]]
 {:anchor (wc/target-block me)})

(wc/teleport me   {:x 2122, :y 70, :z 248})

(wc/target-block me)

(wc/add-inventory me :honeycomb,
                  )

(wc/set-blocks
 (for [x (range -10 10)
       y [0]
       z (range -15 15)]
   (cond
     (#{5 -4} x)
     [x y z :deepslate-brick-stairs :east]
     (#{4 -5} x)
     [x y z :deepslate-brick-stairs :west]
     (< -4 x 4)
     [x y z
      (palette/rand-palette {
                             ;; :bricks 0.20
                             ;; :granite 0.40
                             ;; :polished-granite 10
                             ;; :waxed-exposed-cut-copper 13
                             ;; :waxed-exposed-copper 3
                             ;; :spruce-planks 5
                             ;; :raw-copper-block 1
                             ;; :stripped-spruce-wood 3

                             :basalt 1
                             :cobbled-deepslate 1
                             ;; :cyan-terracotta 1
                             :deepslate-coal-ore 1
                             :cracked-deepslate-bricks 1
                             :gray-glazed-terracotta 1
                             :furnace 1
                             :cobblestone 0
                             :polished-andesite 0
                             ;; :mossy-stone-bricks 12
                             ;; :mossy-cobblestone 5
                             })
      (rand-nth [:north :west :east :south])]
     (< -9 x 9)
     [x y z
      (palette/rand-palette {;;:bricks 1
                             :granite 1
                             :polished-granite 1
                             :waxed-exposed-copper 1
                             :dripstone-block 1

                             })
      (rand-nth [:north :west :east :south])]
     (< -10 x 10)
     [x y z :deepslate-bricks]))
 {:anchor {:x 2122, :y 70, :z 248}})

(palette/neighbors :furnace)

(wc/add-inventory me :scaffolding 64)

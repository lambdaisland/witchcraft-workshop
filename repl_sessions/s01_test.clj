(ns s01-test
  (:require [lambdaisland.witchcraft :as wc]))

(wc/add-inventory (wc/player) :elytra 1)
(wc/add-inventory (wc/player) :firework-rocket 64)

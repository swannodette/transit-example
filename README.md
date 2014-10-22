# transit-example

A basic example showing how to use Transit from Clojure and ClojureScript.

It is also a benchmark of JSON.parse, Transit and EDN implementation.

## Some interesting benchmark results.

- Firefox 33 JSON.parse is differ with Transit not more than 4 times. And JSON.parse is really fast.
- On Chromium 37 Transit is little faster than JSON.parse, but the latter is little slower than on Firefox 33.
- At least on Chromium 37, Transit is 3 times faster than on Firefox 33.

- EDN is about 2 orders of magnitude slower than Transit!!!

So, you should think about to switch from plain EDN to Transit.

# func-cache

A Clojure library using first class functions as a synchronisation mechanism
based on [go-cache](https://github.com/zpatrick/go-cache)

The storage is provided by a transient hash-map which is exposed over a channel,
meaning all operations a serialized and therefore don't require locking. Only
one thread is ever operating on the map even if the handle is passed to many.

## Usage


```clojure
(let [handle (func-cache)]
  (cache-empty? handle) ;; true
  (cache-insert handle :foo "bar")
  (cache-get handle :foo) ;; "bar"
  (cache-empty? handle) ;; false
  (cache-destroy handle)) ;; close the handle
```

## Todo

- [ ] make this a map like interface for easier usage everywhere?


## License

Copyright © 2017 Philipp Fehre

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

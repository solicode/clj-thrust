clj-thrust
==========

Clojure bindings for [Thrust](https://github.com/breach/thrust)

Overview
--------

clj-thrust allows you to write desktop GUI applications as if you were writing a web app ([Atom](https://github.com/atom/atom) and [Light Table](https://github.com/LightTable/LightTable) are 2 popular applications that follow a similar approach).

By doing so, it becomes possible to target the desktop and web simultaneously with the same codebase without requiring too much extra effort (of course, this also depends on how you design your project and what kind of dependencies you have).

Also, since clj-thrust is written in Clojure, it's possible to use it to write applications where both the server and client code are written entirely in Clojure and ClojureScript. And you can do all this with a REPL and live code reloading.

There's also [Middlebrow](https://github.com/solicode/middlebrow), which exposes a common interface for multiple web-view containers (the outer shell which hosts your web app), allowing you to easily switch hosts/engines with minimal code changes. Middlebrow supports clj-thrust, as well as JavaFX and SWT with their built-in web-view components.

Getting Started
---------------

### Installation

Add the following dependency to your `project.clj` file:

```clojure
[net.solicode/clj-thrust "0.1.0-SNAPSHOT"]
```

You will also need the Thrust runtime in order to run any programs that use Thrust. You can download it [here](https://github.com/breach/thrust/releases). By default, clj-thrust looks for the runtime in `$HOME/.thrust/`, but it's possible to specify a different location with:

```clojure
(create-process :thrust-directory "/path/to/thrust/directory/")
```

### Examples

The simplest example illustrating how to use clj-thrust is the following:

```clojure
(ns my-app.core
  (:require [clj-thrust.core :refer [create-process destroy-process]]
            [clj-thrust.window :as w]))

(let [process (create-process) ; `create-process` also takes path to Thrust directory
      window (w/create-window process
               :root-url "http://localhost:8080" ; URL to your web app
               :size {:width 400 :height 300})]
  (w/listen-closed window
    (fn [e]
      (destroy-process process))) ; Optionally call `(System/exit 0)` here.
  (w/show window)
  (w/focus window true))
```

### Sample Project

For a more complete example which includes both server and client code, you can take a look at [this sample project](https://github.com/solicode/clj-thrust-example-notepad). It's a very basic Notepad-like application which shows how one might structure an app that uses clj-thrust.

License
-------

Copyright © 2015 Solicode

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.

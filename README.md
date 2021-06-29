# dev-nrepl

Start your NREPL externally.

## Usage

Deps project should add this new aliases.

``` clojure
  :nrepl
  {:extra-deps {wandersoncferreira/dev-nrepl {:git/url "https://github.com/wandersoncferreira/dev-nrepl" :sha "647cb8a6e07e2ae5c3857ced8bffaed73f43fd10"}}
   :main-opts ["-m" "dev-nrepl.core"]}
```


Then:

``` sh
clj -M:nrepl
```

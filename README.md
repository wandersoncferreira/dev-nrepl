# dev-nrepl

Start your NREPL externally.

## Usage

Deps project should add this new aliases.

``` clojure
  :nrepl
  {:extra-deps {wandersoncferreira/dev-nrepl {:git/url "https://github.com/wandersoncferreira/dev-nrepl" 
                                              :sha "647cb8a6e07e2ae5c3857ced8bffaed73f43fd10"}}
   :main-opts ["-m" "dev-nrepl.core"]}
```


Then:

``` sh
clj -M:nrepl
```


## Configuration

We can change environment variables to control `NREPL_PORT` and `ENABLE_CIDER`.

``` sh
export NREPL_PORT=1200  ## default to 17042
export ENABLE_CIDER=false ## default to true
```

The package also contains the
[Pomegranate](https://github.com/clj-commons/pomegranate) dependency that can be
used to perform hot reload when you add dependencies to your project.



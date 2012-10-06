# fobos_clj

Implementation of Forward Backward Splitting in clojure.

## Usage

Following is how to use the document classifier:

```sh
$ wget http://www.csie.ntu.edu.tw/~cjlin/libsvmtools/datasets/binary/news20.binary.bz2
$ bzip2 -d news20.binary.bz2
$ ruby shuffle news20.binary > tmp.txt
$ head -n 15000 tmp.txt > train.txt; tail -n 4996 tmp.txt > test.txt
$ lein run --help
$ lein run --mode train --model fobos_clj.logistic.Logistic --train-filename train.txt --max-iter 100 --eta 1.0 --lambda 1.0 --model-file weight.model
$ lein run --mode test --test-filename test.txt --model-file weight.model
```

## License

Copyright (C) 2012 Yasuhisa Yoshida

Distributed under the Eclipse Public License, the same as Clojure.

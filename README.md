# BB-DOB W-Model

[<img alt="Travis CI Build Status" src="https://img.shields.io/travis/thomasWeise/BBDOB_W_Model/master.svg" height="20"/>](https://travis-ci.org/thomasWeise/BBDOB_W_Model/)

The W-Model, is a tunable Black-Box Discrete Optimization Benchmarking (BB-DOB) problem using a bit-string representation of either fixed length (n) or variable length. It has some nice features, including:

1. For a problem instance with bit length `n`, the objective values range from `0` (the global optimum) to `n`.
2. The global optimum is always known and can easily be computed.
3. Problem instances are entirely defined by their parameters in a deterministic way, i.e., no files are needed to store and load them.
4. In the plain problem without any activated features, the objective function is defined as the Hamming distance to a specific bit string (`01010101...`), i.e., the plain problem is easy to solve and should behave exactly as the well-understood and well-researched Max-One problem.
5. Several problematic features such as redundancy/neutrality, epistasis (non-separability), and ruggedness/deceptiveness can be fine tuned.
6. Computing the objective values is very fast and can be done in `O(n)` for problems without epistasis or low epistasis and in at most `O(n^2)` for highly-epistatic problems.
7. The problems can be tuned to be hard even for small scales `n`, allowing for fast experiments with low memory footprint.

## The Difficult Features 

1. Uniform neutrality can be set via parameter `mu` and increases the representation length to `mu*n` via uniform reduncancy. This does not change the number of possible objective values. For `mu=1`, no neutrality is added. This translation can take place in `O(n*m)`.
2. Epistasis, i.e., an inter-dependency of decision variables, can be added by tuning a parameter `nu` from  `2` (no added epistasis) to `n` (maximum epistasis). Epistasis here is defined as follows: "The contribution of one decision variable (bit) to the objective value depends on the state of `nu-2` other decision variables." Epistasis is implemented as a bijective mapping of `e_nu : {0,1}^nu -> {0,1}^nu` with the following feature: If the Hamming distance between two strings `x1` and `x2` is `1`, then the Hamming distance of `e_nu(x1)` and `e_nu(x2)` is at least `nu-1`. If the value of single decision variable in a candidate solution is changed, this will change the value of at least `nu-1` variables in the mapping result (the original variable and `nu-2`, related others). The `n` bits of a solution are divided into `n/nu` pieces, each of which is mapped separately by `e_nu`, and the remaining `n mod nu` bits are mapped by `e_(n mod nu)`. This translation can take place in `O(n * nu)` steps.
3. Ruggedness and deceptiveness are introduced by permutating the objective values according to a permutation `r_gamma` which always leaves `0` untouched (the optimum stays the same). The integer parameter `gamma` ranges from `0` (`r_0`  is the canonical permutation and leaves the objective values unchanged) to `n(n-1)/2`, which corresponds to a maximally deceptive problem. Between the plain problem and the maximally deceptive problems, we can first observe increasingly rugged instances and then more deceptive ones. The ruggedness mapping takes `O(1)` time and the required permutation can be computed according to a deterministic algorithm before running the experiments.
4. The W-Model can be extended to multi-objectivity by splitting the candidate solution into multiple, interleaved instances of the original problem. These are independent/orthogonal in the original setup but can be made conflicting by using epistasis (`nu>2`).

## Problem Implementation

Here we implement the problem for a representation using `boolean[]`s as candidate solution and for one using `wrapped long[]`s (where each of the 64 bits of a `long` stand for a decision variable). The former representation tends to be faster in our initial experiments and memory consumption is not an issue since the data structures are not that big.
 
We provide a complete set of JUnit tests which assert that a problem implementation obeys to all promises/specifications given above. These JUnit tests are used to validate our two implementations and can be used to check other implementations. We furthermore provide a JUnit test for asserting that two model implementations are equivalent. With these tests, other researchers may create other implementations (for other solution representations) and check whether they comply with the problem definition. This would allow them to conduct faster experiments, because they do not need to map their solutions to `boolean[]` first to evaluate them.
 
## Experimentation Environment

Besides providing a standard implementation of our model, we also provide an experimentation environment with the following features:

1. Experiments with a set of standard parameterizations of the W-Model are automatically executed in parallel.
2. All improvements that an algorithm makes are automatically logged to textual log files in a specific folder structure.
3. These log files also contain other information, such as system information, the problem settings, and the random seed.
4. The user can also store algorithm specific parameters in the log files.
5. The log files contain the consumed function evaluations, time stamps, and objective values.
6. Since we store the random seeds in the log files and hand a random generator to the user-implemented algorithms initialized with these seeds, the experiments 100% reproducible.
7. The random seeds are derived from the problem configuration and run index, hence experiments with different algorithms are comparable. For instance, if two local search algorithms use the same random procedure to generate their initial candidate solution, they will start at the same point with the same random seed.
8. The experiments are executed in parallel, using `max(1, p-1)` processor cores if the system has `p` processor cores. One core is left empty for system things.
9. The file names used in the experiments are unique and deterministic, thus multiple instances of the experimenter could be executed on different computers working on a shared folder and would them automatically complement each other.
10. If the experiment is finished, the results can automatically packaged into a `tar.xz` archive that can be downloaded if a cluster is used (works only under Linux with the proper `tar` installation).

We also implement a set of example algorithms, ranging from Hill Climbers over Random Sampling and Exhaustive Enumeration to a simple (mu+lamba) Evolutionary Algorithm. Thus, the user can see how to use the system.

## License

The copyright holder of this package is Prof. Dr. Thomas Weise (see Contact).
The package is licensed under the  GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007.

## Contact

If you have any questions or suggestions, please contact
[Prof. Dr. Thomas Weise](http://iao.hfuu.edu.cn/team/director) of the
[Institute of Applied Optimization](http://iao.hfuu.edu.cn/) at
[Hefei University](http://www.hfuu.edu.cn) in
Hefei, Anhui, China via
email to [tweise@hfuu.edu.cn](mailto:tweise@hfuu.edu.cn).
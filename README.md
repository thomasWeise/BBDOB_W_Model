# BB-DOB W-Model

[<img alt="Travis CI Build Status" src="https://img.shields.io/travis/thomasWeise/BBDOB_W_Model/master.svg" height="20"/>](https://travis-ci.org/thomasWeise/BBDOB_W_Model/)

## 1. Introduction

The W-Model, is a tunable Black-Box Discrete Optimization Benchmarking (BB-DOB) problem using a bit-string representation of either fixed length (`n`) or variable length. It has some nice features, including:

1. For a problem instance with bit length `n`, the objective values range from `0` (the global optimum) to `n`.
2. The global optimum is always known and can easily be computed.
3. Problem instances are entirely defined by their parameters in a deterministic way, i.e., no files are needed to store and load them.
4. In the plain problem without any activated features, the objective function is defined as the Hamming distance to a specific bit string (`01010101...`), i.e., the plain problem is easy to solve and should behave exactly as the well-understood and well-researched One Max problem.
5. Several problematic features such as redundancy/neutrality, epistasis (non-separability), and ruggedness/deceptiveness can be fine tuned.
6. Computing the objective values is very fast and can be done in `O(n)` for problems without epistasis or low epistasis and in at most `O(n^2)` for highly-epistatic problems.
7. The problems can be tuned to be hard even for small scales `n`, allowing for fast experiments with low memory footprint.

## 2. How to Use

The source code is provided at [GitHub](http://github.com/thomasWeise/BBDOB_W_Model) as a [Maven](http://en.wikipedia.org/wiki/Apache_Maven) project for [Java 1.8](http://en.wikipedia.org/wiki/Java_version_history#Java_SE_8) along with settings for the [Eclipse](http://www.eclipse.org/) developer environment.
I therefore recommend using Eclipse for exploring and playing around with it.
Once you have checked-out the code and imported it as existing project in Eclipse, you need to right-click the project, choose "Maven" and then "Update Project".
This will automatically download the required dependencies (currently, only [Junit 4.12](http://junit.org/junit4/)) and set up the folder structure properly.

You can also use this package as a `jar` library directly, which will allow you to use all the implemented algorithms and API directly in your project.
If your software project uses Maven as well, then you can even directly link these sources as dependency.
Therefore, you need to do the following.

First, you need to add the following repository, which is a repository that can kind of dynamically mirror repositories at GitHub:

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

Than you can add the dependency on our `BBDOB_W_Model` repository into your `dependencies` section.
Here, `1.0.5` is the current version of  `BBDOB_W_Model`.
Notice that you may have more dependencies in your `dependencies` section, say on `junit`, but here I just put the one for `aitoa-code` as example.

```xml
<dependencies>
  <dependency>
    <groupId>com.github.thomasWeise</groupId>
    <artifactId>BBDOB_W_Model</artifactId>
    <version>1.0.5</version>
  </dependency>
</dependencies>

```

Finally, in order to include all required external `jar`s into your `jar` upon compilation, you may want to add the following plugin into your `<build><plugins>` section:
In the snippet below, you must then replace `${project.mainClass}` with the main class that should run when your `jar` is executed.
This should result in a so-called "fat" `jar`, which includes the dependencies.
In other words, you do not need to have our `jar` in the classpath anymore but can ship your code as one single `jar`.

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-shade-plugin</artifactId>
  <version>3.2.0</version>
  <executions>
    <execution>
      <phase>package</phase>
      <goals>
        <goal>shade</goal>
      </goals>
      <configuration>
        <createSourcesJar>true</createSourcesJar>
        <shadeTestJar>true</shadeTestJar>
        <minimizeJar>false</minimizeJar>
        <shadedArtifactAttached>true</shadedArtifactAttached>
        <createDependencyReducedPom>false</createDependencyReducedPom>

        <shadedClassifierName>full</shadedClassifierName>

        <filters>
          <filter>
            <artifact>*:*</artifact>
            <excludes>
              <exclude>META-INF/*.SF</exclude>
              <exclude>META-INF/*.DSA</exclude>
              <exclude>META-INF/*.RSA</exclude>
            </excludes>
          </filter>
        </filters>

        <transformers>
          <transformer
            implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
            <mainClass>${project.mainClass}</mainClass>
          </transformer>
          <transformer
            implementation="org.apache.maven.plugins.shade.resource.ApacheLicenseResourceTransformer" />
          <transformer
            implementation="org.apache.maven.plugins.shade.resource.ApacheNoticeResourceTransformer" />
          <transformer
            implementation="org.apache.maven.plugins.shade.resource.PluginXmlResourceTransformer" />
          <transformer
            implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer" />
        </transformers>
      </configuration>
    </execution>
  </executions>
</plugin>
```


## 3. References

1. Thomas Weise and Zijun Wu. Difficult Features of Combinatorial Optimization Problems and the Tunable W-Model Benchmark Problem for Simulating them. In Black Box Discrete Optimization Benchmarking (BB-DOB) Workshop of Companion Material Proceedings of the Genetic and Evolutionary Computation Conference (GECCO 2018), July 15th-19th 2018, Kyoto, Japan, pages 1769-1776, ISBN: 978-1-4503-5764-7. ACM.
doi:[10.1145/3205651.3208240](http://dx.doi.org/10.1145/3205651.3208240) / [pdf](http://iao.hfuu.edu.cn/images/publications/W2018TWMATBBDOBPIFTBGW.pdf) / [slides](http://iao.hfuu.edu.cn/images/publications/W2018TWMATBBDOBPIFTBGW_slides.pdf)

2. Experimental results with the W-Model and Runner version 1.0.0, can be found at doi:[10.5281/zenodo.1256883](http://dx.doi.org/10.5281/zenodo.1256883). These also do include settings of the form nu=2+(4*v) which we discourage.

3. Thomas Weise, Stefan Niemczyk, Hendrik Skubch, Roland Reichle, and Kurt Geihs. A Tunable Model for Multi-Objective, Epistatic, Rugged, and Neutral Fitness Landscapes. In Maarten Keijzer, Giuliano Antoniol, Clare Bates Congdon, Kalyanmoy Deb, Benjamin Doerr, Nikolaus Hansen, John H. Holmes, Gregory S. Hornby, Daniel Howard, James Kennedy, Sanjeev P. Kumar, Fernando G. Lobo, Julian Francis Miller, Jason H. Moore, Frank Neumann, Martin Pelikan, Jordan B. Pollack, Kumara Sastry, Kenneth Owen Stanley, Adrian Stoica, El-Ghazali, and Ingo Wegener, editors, *Proceedings of the 10th Annual Conference on Genetic and Evolutionary Computation Conference (GECCO'08)*, pages 795-802, July 12-16, 2008, Renaissance Atlanta Hotel Downtown: Atlanta, GA, USA. ISBN: 978-1-60558-130-9, New York, NY, USA: ACM Press. doi:[10.1145/1389095.1389252](http://dx.doi.org/10.1145/1389095.1389252)

4. Stefan Niemczyk. Ein Benchmark Problem für Globale Optimierungsverfahren. Bachelor’s thesis. 2008. Distributed Systems Group, University of Kassel. Supervisor: Thomas Weise.


## 4. The Difficult Features 

1. Uniform neutrality can be set via parameter `mu` and increases the representation length to `mu*n` via uniform reduncancy. This does not change the number of possible objective values. For `mu=1`, no neutrality is added. This translation can take place in `O(n*m)`.
2. Epistasis, i.e., an inter-dependency of decision variables, can be added by tuning a parameter `nu` from  `2` (no added epistasis) to `n` (maximum epistasis). Epistasis here is defined as follows: "The contribution of one decision variable (bit) to the objective value depends on the state of `nu-2` other decision variables." Epistasis is implemented as a bijective mapping of `e_nu : {0,1}^nu -> {0,1}^nu` with the following feature: If the Hamming distance between two strings `x1` and `x2` is `1`, then the Hamming distance of `e_nu(x1)` and `e_nu(x2)` is at least `nu-1`. If the value of single decision variable in a candidate solution is changed, this will change the value of at least `nu-1` variables in the mapping result (the original variable and `nu-2`, related others). The `n` bits of a solution are divided into `n/nu` pieces, each of which is mapped separately by `e_nu`, and the remaining `n mod nu` bits are mapped by `e_(n mod nu)`. This translation can take place in `O(n * nu)` steps.
3. Ruggedness and deceptiveness are introduced by permutating the objective values according to a permutation `r_gamma` which always leaves `0` untouched (the optimum stays the same). The integer parameter `gamma` ranges from `0` (`r_0`  is the canonical permutation and leaves the objective values unchanged) to `n(n-1)/2`, which corresponds to a maximally deceptive problem. Between the plain problem and the maximally deceptive problems, we can first observe increasingly rugged instances and then more deceptive ones. The ruggedness mapping takes `O(1)` time and the required permutation can be computed according to a deterministic algorithm before running the experiments.
4. The W-Model can be extended to multi-objectivity by splitting the candidate solution into multiple, interleaved instances of the original problem. These are independent/orthogonal in the original setup but can be made conflicting by using epistasis (`nu>2`).

## 5. Problem Implementation

Here we implement the problem for a representation using `boolean[]`s as candidate solution and for one using `wrapped long[]`s (where each of the 64 bits of a `long` stand for a decision variable). The former representation tends to be faster in our initial experiments and memory consumption is not an issue since the data structures are not that big.
 
We provide a complete set of JUnit tests which assert that a problem implementation obeys to all promises/specifications given above. These JUnit tests are used to validate our two implementations and can be used to check other implementations. We furthermore provide a JUnit test for asserting that two model implementations are equivalent. With these tests, other researchers may create other implementations (for other solution representations) and check whether they comply with the problem definition. This would allow them to conduct faster experiments, because they do not need to map their solutions to `boolean[]` first to evaluate them.
 
## 6. Experimentation Environment

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

## 7. License

The copyright holder of this package is Prof. Dr. Thomas Weise (see Contact).
The package is licensed under the  GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007.

## 8. Contact

If you have any questions or suggestions, please contact
[Prof. Dr. Thomas Weise](http://iao.hfuu.edu.cn/team/director) of the
[Institute of Applied Optimization](http://iao.hfuu.edu.cn/) at
[Hefei University](http://www.hfuu.edu.cn) in
Hefei, Anhui, China via
email to [tweise@hfuu.edu.cn](mailto:tweise@hfuu.edu.cn).
package cn.edu.hfuu.iao.WModel;

/** A single objective {@code boolean[]} version of our model */
public final class WModel_SingleObjective_Boolean
    extends WModel_SingleObjective<boolean[]> {

  /** the factory */
  private static final Factory<boolean[]> FACTORY = (_n, _mu, _nu,
      _gamma) -> new WModel_SingleObjective_Boolean(_n, _mu, _nu, _gamma);

  /** the length of the candidate solutions */
  private final int m_n;
  /** the neutrality mu */
  private final int m_mu;
  /** the epistasis nu */
  private final int m_nu;
  /** the ruggedness gamma */
  private final int m_gamma;

  /** the buffer for the neutralized solution */
  private final boolean[] m_neutralized;
  /** the buffer for the epistasisized solution */
  private final boolean[] m_epistasized;
  /** the ruggedness */
  private final int[] m_ruggedness;

  /**
   * create the single-objective version of the W-Model
   *
   * @param _n
   *          the reduced size of a candidate solution
   * @param _mu
   *          the neutrality factor
   * @param _nu
   *          the epistasis nu
   * @param _gamma
   *          the ruggedness gamma
   */
  public WModel_SingleObjective_Boolean(final int _n, final int _mu,
      final int _nu, final int _gamma) {
    super();

    this.m_n = _n;

    if (_mu > 1) {
      this.m_neutralized = new boolean[this.m_n];
      this.m_mu = _mu;
    } else {
      this.m_neutralized = null;
      this.m_mu = 1;
    }

    if (_nu > 2) {
      this.m_nu = _nu;
      this.m_epistasized = new boolean[this.m_n];
    } else {
      this.m_epistasized = null;
      this.m_nu = 2;
    }

    if (_gamma > 0) {
      this.m_gamma = _gamma;
      this.m_ruggedness = WModel_Ruggedness.ruggedness(this.m_gamma,
          this.m_n);
    } else {
      this.m_gamma = 0;
      this.m_ruggedness = null;
    }
  }

  /** {@inheritDoc} */
  @Override
  public final int get_n() {
    return this.m_n;
  }

  /** {@inheritDoc} */
  @Override
  public final int get_mu() {
    return this.m_mu;
  }

  /** {@inheritDoc} */
  @Override
  public final int get_nu() {
    return this.m_nu;
  }

  /** {@inheritDoc} */
  @Override
  public final int get_gamma() {
    return this.m_gamma;
  }

  /** {@inheritDoc} */
  @Override
  public final int get_candidate_solution_length() {
    return this.m_mu * this.m_n;
  }

  /**
   * get the factory
   *
   * @return the factory for this objective function
   */
  public static final Factory<boolean[]> factory() {
    return WModel_SingleObjective_Boolean.FACTORY;
  }

  /** {@inheritDoc} */
  @Override
  public final int applyAsInt(final boolean[] value) {
    final boolean[] neutral;

    if (this.m_neutralized != null) {
      neutral = this.m_neutralized;
      WModel_Boolean.neutrality(value, this.m_mu, neutral);
    } else {
      neutral = value;
    }

    final boolean[] epi;
    if (this.m_epistasized != null) {
      epi = this.m_epistasized;
      WModel_Boolean.epistasis(neutral, this.m_nu, epi);
    } else {
      epi = neutral;
    }

    final int f = WModel_Boolean.f(epi, this.m_n);
    if (this.m_ruggedness != null) {
      return this.m_ruggedness[f];
    }
    return f;
  }
}

package edu.nyu.fc.pricing;

import org.omg.CORBA.portable.ApplicationException;

/**
 * This class is used as a simulation manager to run MonteCarloSimulation for
 * pricing options according to different input parameters. It allows clients of
 * this class to select different payout as well as random number distribution
 * options.
 * 
 * @author Yourii Martiak
 * 
 */
public class MonteCarloSimulation {

    /**
     * Interval of number of simulations at which to evaluate margin of error to
     * decide when to stop our simulations
     */
    private static final int SNAPSHOT_RATE = 100;

    // required parameters
    private final int daysToExpire;
    private final double price;
    private final double rate;
    private final double sigma;
    private final double strikePrice;
    private final double probability;
    private final double estimationError;

    // optional parameters for which we use defaults
    private final OptionType optionType;
    private final Distribution distribution;

    /**
     * Creates new instance of monte carlo simulation as per input parameters
     * 
     * @param daysToExpire
     *            days to expiration date
     * @param price
     *            current asset price
     * @param rate
     *            interest rate
     * @param sigma
     *            volatility per day
     * @param strikePrice
     *            option strike price
     * @param probability
     *            coefficient (z-score)
     * @param estimationError
     *            percentage of error
     * @param optionType
     *            type of option
     * @param distribution
     *            type of random number distribution
     */
    public MonteCarloSimulation(final int daysToExpire, final double price,
            final double rate, final double sigma, final double strikePrice,
            final double probability, final double estimationError,
            final OptionType optionType, final Distribution distribution) {
        this.daysToExpire = daysToExpire;
        this.price = price;
        this.rate = rate;
        this.sigma = sigma;
        this.strikePrice = strikePrice;
        this.probability = probability;
        this.estimationError = estimationError;
        this.optionType = optionType;
        this.distribution = distribution;
    }

    /**
     * Main method to start and run simulations. If used to run from a terminal
     * need to supply the following parameters
     * 
     * <pre>
     * Usage: MonteCarloSimulation [required parameters] [optional]
     * use format parameter=value where required parameters include:
     *   d    days to expire
     *   p    asset price
     *   r    interest rate
     *   s    sigma
     *   sp   strike price
     *   pr   probability coefficient (z-score)
     *   e    margin of error
     * and optional parameters are:
     *   t    option type, one of the following:
     *        US_CALL  American call option (default)
     *        AS_CALL  Asian call option
     *   ds   random number distribution, one of the following:
     *        SND      standard normal disribution (default)
     *        GND      gaussian normal distribution
     *        ATD      antithetic normal distribution
     *        
     * Example 1: MonteCarloSimulation d=252 p=152.35 r=0.0001 s=0.01 sp=165 pr=2.05 e=0.01
     * creates new monte carlo simulation of American call option payout that expires
     * in 252 days with asset price $152.35, rate of 0.1%, daily volatility of
     * 1%, strike price of $165.00, probability of 96% having estimation error
     * under 1% and using standard normal distribution with zero mean and 1.0
     * deviation
     * 
     * Example 2: MonteCarloSimulation d=252 p=152.35 r=0.0001 s=0.01 sp=164 pr=2.05 e=0.01 t=AS_CALL ds=ATD
     * creates new monte carlo simulation of Asian call option payout that expires
     * in 252 days with asset price $152.35, rate of 0.1%, daily volatility of
     * 1%, strike price of $164.00, probability of 96% having estimation error
     * under 1% and using antithetic gaussian normal distribution
     * </pre>
     */
    public void run() {
        double error = 1.0;
        double payoutValue = 0.0;
        double payoutSum = 0.0;
        int i = 1;

        // create payout and random number generator implementations
        final IPayout payout = createPayout(strikePrice, optionType);
        final IRandomVectorGenerator vectorGenerator = createVectorGenerator(
                daysToExpire, distribution);
        // take start timestamp
        final long start = System.currentTimeMillis();

        // run simulation until our error falls under the threshold
        while (error > estimationError) {
            payoutSum += payout.getPayout(new StockPath(price, rate, sigma,
                    vectorGenerator.getVector()));

            // take snapshots at preset intervals
            if (i % SNAPSHOT_RATE == 0) {
                // get the average payout value
                payoutValue = payoutSum / i;
                // discount payout values in today's terms
                payoutValue *= Math.exp(-rate * daysToExpire);
                // calculate margin of error
                error = probability * payoutValue / Math.sqrt(i);
            }
            i++;
        }

        // take stop timestamp
        final long stop = System.currentTimeMillis();

        // calculate run time
        final long runTime = stop - start;

        System.out.println("===");
        System.out.println(String.format(
                "MonteCarloSimulation finished in %d ms.", runTime));
        System.out.println(String.format("Run through %d simulations", i));
        System.out.println(String.format(
                "Snapshot taken at every %d simulation", SNAPSHOT_RATE));
        System.out.println(String.format(
                "Estimated payout value=%f with error=%f", payoutValue, error));
    }

    /**
     * Display usage information
     */
    private static void usage() {
        System.out
                .println("Usage: ./MonteCarloSimulation [required parameters] [optional]");
        System.out.println("use format parameter=value");
        System.out.println("where required parameters include:");
        System.out.println("\td\tdays to expire");
        System.out.println("\tp\tasset price");
        System.out.println("\tr\tinterest rate");
        System.out.println("\ts\tsigma");
        System.out.println("\tsp\tstrike price");
        System.out.println("\tpr\tprobability coefficient (z-score)");
        System.out.println("\te\tmargin of error");
        System.out.println("and optional parameters are:");
        System.out.println("\tt\toption type, one of the following:");
        System.out.println("\t\tUS_CALL\tAmerican call option (default)");
        System.out.println("\t\tAS_CALL\tAsian call option");
        System.out
                .println("\tds\trandom number distribution, one of the following:");
        System.out.println("\t\tSND\tstandard normal disribution (default)");
        System.out.println("\t\tGND\tgaussian normal distribution");
        System.out.println("\t\tATD\tantithetic normal distribution");
        System.out
                .println("Example: ./MonteCarloSimulation d=252 p=152.35 r=0.0001 s=0.01 sp=165 pr=2.05 e=0.01");
    }

    /**
     * Main method that starts monte carlo simulation
     * 
     * @param args
     *            input parameters for the simulation
     */
    public static void main(final String[] args) {

        try {
            final MonteCarloSimulation mcs = createNewSimulation(args);
            System.out.println("\nStarting " + mcs);
            mcs.run();
        } catch (final Exception e) {
            System.err
                    .println("Exception occured while creating a new MonteCarloSimulation "
                            + e.getMessage());
            usage();
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a specific implementation of the payout for the option type as
     * specified by input parameter
     * 
     * @param strikePrice
     * @param optionType
     * @return payout for a given option type
     */
    private static IPayout createPayout(final double strikePrice,
            final OptionType optionType) {
        if (OptionType.US_CALL == optionType) {
            return new AmericanCallOptionPayout(strikePrice);
        } else if (OptionType.AS_CALL == optionType) {
            return new AsianCallOptionPayout(strikePrice);
        }

        return new AmericanCallOptionPayout(strikePrice);
    }

    /**
     * Creates a specific random number distribution as per input parameter
     * 
     * @param daysToExpire
     * @param distribution
     * @return random number generator for a given distribution type
     */
    private static IRandomVectorGenerator createVectorGenerator(
            final int daysToExpire, final Distribution distribution) {
        if (Distribution.SND == distribution) {
            return new RandomVectorGenerator(daysToExpire, 1.0);
        } else if (Distribution.GND == distribution) {
            return new GaussianRandomVectorGenerator(daysToExpire);
        } else if (Distribution.ATD == distribution) {
            return new AntiTheticRandomVectorGenerator(
                    new GaussianRandomVectorGenerator(daysToExpire));
        }

        return new RandomVectorGenerator(daysToExpire, 1.0);
    }

    /**
     * Creates a new instance of monte carlo simulation as per input parameters
     * 
     * @param args
     *            input parameters
     * @return new instance of monte carlo simulation for a given list of input
     *         parameters
     * @throws Exception
     *             if required parameters are missing, wrong input format,
     *             failure to parse or any other error occurs while creating new
     *             instance of monte carlo simulation
     */
    private static MonteCarloSimulation createNewSimulation(final String[] args)
            throws Exception {
        if (args == null || args.length < 7) {
            throw new IllegalArgumentException("Wrong number of arguments!");
        }

        int daysToExpire = 0;
        double price = 0;
        double rate = 0;
        double sigma = 0;
        double strikePrice = 0;
        double probability = 0;
        double estimationError = 0;
        OptionType optionType = OptionType.US_CALL;
        Distribution distribution = Distribution.SND;

        for (final String s : args) {
            final NameValue nameValue = new NameValue(s);
            if (Parameter.d == nameValue.name) {
                daysToExpire = Integer.parseInt(nameValue.value);
            } else if (Parameter.ds == nameValue.name) {
                distribution = Distribution.valueOf(nameValue.value);
            } else if (Parameter.e == nameValue.name) {
                estimationError = Double.parseDouble(nameValue.value);
            } else if (Parameter.p == nameValue.name) {
                price = Double.parseDouble(nameValue.value);
            } else if (Parameter.pr == nameValue.name) {
                probability = Double.parseDouble(nameValue.value);
            } else if (Parameter.r == nameValue.name) {
                rate = Double.parseDouble(nameValue.value);
            } else if (Parameter.s == nameValue.name) {
                sigma = Double.parseDouble(nameValue.value);
            } else if (Parameter.sp == nameValue.name) {
                strikePrice = Double.parseDouble(nameValue.value);
            } else if (Parameter.t == nameValue.name) {
                optionType = OptionType.valueOf(nameValue.value);
            }
        }
        if (daysToExpire == 0 || price == 0 || rate == 0 || sigma == 0
                || strikePrice == 0 || probability == 0 || estimationError == 0) {
            throw new IllegalArgumentException("Required parameter not set");
        }

        return new MonteCarloSimulation(daysToExpire, price, rate, sigma,
                strikePrice, probability, estimationError, optionType,
                distribution);
    }

    @Override
    public String toString() {
        return String
                .format("MonteCarloSimulation {daysToExpire=%d, price=%f, rate=%f, sigma=%f, strikePrice=%f, probability=%f, estimationError=%f, optionType=%s, distribution=%s}",
                        daysToExpire, price, rate, sigma, strikePrice,
                        probability, estimationError, optionType, distribution);
    }

    /**
     * All applicable parameters
     */
    public static enum Parameter {
        /**
         * Days to expire
         */
        d,

        /**
         * Price
         */
        p,

        /**
         * Rate
         */
        r,

        /**
         * Sigma
         */
        s,

        /**
         * Strike price
         */
        sp,

        /**
         * Probability
         */
        pr,

        /**
         * Estimation error
         */
        e,

        /**
         * Option type
         */
        t,

        /**
         * Random number distribution type
         */
        ds
    };

    /**
     * List option types that we are able to handle
     */
    public static enum OptionType {
        /**
         * US Call Option
         */
        US_CALL,

        /**
         * Asian Call Option
         */
        AS_CALL
    };

    /**
     * Types of random number distributions
     */
    public static enum Distribution {
        /**
         * Standard normal distribution with mean=0.0 and deviation=1.0
         */
        SND,

        /**
         * Antithetic normal distribution
         */
        ATD,

        /**
         * Gaussian normal distribution
         */
        GND
    };

    /**
     * A name=value pair representation for our input parameter
     */
    private static class NameValue {
        private Parameter name;
        private String value;

        public NameValue(final String s) {
            if (s == null) {
                throw new IllegalArgumentException("Invalid parameter " + s
                        + " Cannot use null to create NameValue");
            }
            if (s.indexOf("=") < 1) {
                throw new IllegalArgumentException("Invalid parameter " + s
                        + " Must use format name=value to create NameValue");
            }
            final String[] nameValue = s.split("=");
            if (nameValue == null || nameValue.length != 2) {
                throw new IllegalArgumentException(
                        "Invalid parameter "
                                + s
                                + " Could not find equal sign, must use name=value format");
            }
            try {
                name = Parameter.valueOf(nameValue[0]);
                value = nameValue[1];
            } catch (final Exception e) {
                throw new IllegalArgumentException("Invalid parameter " + s);
            }
        }
    }

}

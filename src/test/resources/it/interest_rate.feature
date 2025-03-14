Feature: interest rate retrieval

  Scenario: client makes call to get interest rate successfully
    When client makes get interest rate request
    Then client receives http status code 200
    And the client receives following interest rates
      | maturityPeriodInYears | interestRatePercentage |
      |                     1 |                   4.25 |
      |                     2 |                   4.09 |
      |                     3 |                   3.78 |
      |                     5 |                   3.78 |
      |                     6 |                   3.95 |
      |                     7 |                   4.02 |
      |                    10 |                   4.07 |
      |                    12 |                   4.32 |
      |                    15 |                   4.40 |
      |                    17 |                   4.40 |
      |                    20 |                   4.51 |
      |                    30 |                   4.71 |


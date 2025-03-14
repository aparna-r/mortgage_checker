Feature: mortgage check

  Scenario Outline: client makes successful calls to check mortgage feasibility
    When client makes check mortgage request with <income>, <maturityPeriod>, <loanValue>, <homeValue>
    Then client receives http status code 200
    And client receives check mortgage response with <mortgagePossible>, <mortgageMonthlyAmount>
    Examples:
      | income | maturityPeriod | loanValue | homeValue | mortgagePossible | mortgageMonthlyAmount |
      | 100000 |             30 |     85000 |     90000 |             true |                405.80 |

  Scenario Outline: client makes calls to check mortgage feasibility but fails
    When client makes check mortgage request with <income>, <maturityPeriod>, <loanValue>, <homeValue>
    Then client receives http status code <httpStatus>
    And client receives error response with <errorCode>, <message>
    Examples:
      | income | maturityPeriod | loanValue | homeValue | httpStatus | errorCode |             message  |
      |    100 |             10 |       150 |       150 |        500 |       100 |       'unknown error'|


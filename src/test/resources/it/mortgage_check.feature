Feature: mortgage check

  Scenario Outline: client makes successful calls to check mortgage feasibility
    When client makes check mortgage request with <income>, <maturityPeriodInYears>, <loanValue>, <homeValue>
    Then client receives http status code 200
    And client receives check mortgage response with <mortgagePossible>, <monthlyCost>
    Examples:
      | income | maturityPeriodInYears | loanValue | homeValue | mortgagePossible | monthlyCost |
      | 100000 |                    30 |     85000 |     90000 |             true |      441.35 |
      | 100000 |                     5 |    300000 |    300000 |             true |     5495.22 |
      | 100000 |                    10 |    500000 |    500000 |            false |           0 |
      | 100000 |                    20 |    500000 |    400000 |            false |           0 |

  Scenario: client makes calls to check mortgage feasibility but fails
    When client makes check mortgage request with non existing maturity period 29
    Then client receives http status code 400
    And client receives error response with 102, 'maturity period not found'


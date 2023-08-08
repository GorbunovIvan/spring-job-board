Feature: Vacancies pages

  Scenario: Vacancies page
    Then Open url "http://localhost:8080/vacancies"
    Then Content on tag "h4" with text "Vacancies" visible
    Then Click "new vacancy 2 3" link
    Then Content on tag "h4" with text "Vacancy" visible

  Scenario: Vacancy page
    Then Open url "http://localhost:8080/vacancies/2"
    Then Content on tag "h4" with text "Vacancy" visible
    Then Content on tag "span" with text "new vacancy 2 3" visible
    Then Content on tag "p" containing text "By employer" visible
    Then Click "new employer 23" link
    Then Content on tag "h4" with text "Employer" visible
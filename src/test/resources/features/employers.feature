Feature: Employers pages

  Scenario: Employers page
    Then Open url "http://localhost:8080/employers"
    Then Content on tag "h4" with text "Employers" visible
    Then Click "new employer 23" link
    Then Content on tag "h4" with text "Employer" visible

  Scenario: Employer page
    Then Open url "http://localhost:8080/employers/1"
    Then Content on tag "h4" with text "Employer" visible
    Then Content on tag "span" with text "new employer 23" visible
    Then Content on tag "p" with text "Vacancies:" visible
    Then Content on tag "a" with text "new vacancy 2 3" visible
    Then Click "new vacancy 2 3" link
    Then Content on tag "h4" with text "Vacancy" visible
Feature: Menu items

  Scenario: Menu items

    Then Open url "http://localhost:8080"

#    vacancies
    Then Click on menu item "Vacancies"
    Then Content on tag "h4" with text "Vacancies" visible

#    employers
    Then Click on menu item "Employers"
    Then Content on tag "h4" with text "Employers" visible

#    applicants
    Then Click on menu item "Applicants"
    Then Content on tag "h4" with text "Applicants" visible

#    log in
    Then Click on menu item "Login"
    Then Content on tag "h4" with text "Log in" visible

#    registration
    Then Open url "http://localhost:8080"
    Then Click on menu item "Register"
    Then Content on tag "h4" with text "Registration" visible
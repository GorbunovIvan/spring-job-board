Feature: Applicants pages

  Scenario: Applicants page
    Then Open url "http://localhost:8080/applicants"
    Then Content on tag "h4" with text "Applicants" visible
    Then Click "bob power" link
    Then Content on tag "h4" with text "Applicant" visible

  Scenario: Applicant page
    Then Open url "http://localhost:8080/applicants/1"
    Then Content on tag "h4" with text "Applicant" visible
    Then Content on tag "span" with text "bob" visible
    Then Content on tag "span" with text "power" visible
    Then Content on tag "legend" with text "Skills" visible
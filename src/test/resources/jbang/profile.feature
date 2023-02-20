Feature: Test profile

  Background:
    Given camel jbang is installed

  Scenario: User create integration and custom profile
    Given user create file "profile.yaml" with content:
      """
      # camel-k: language=yaml

      # Write your routes here, for example:
      - from:
          uri: "timer:yaml"
          parameters:
            period: "1000"
          steps:
            - setBody:
                constant: "{{test.body}}"
            - log: "${body}"
      """
    And user create file "custom_profile.properties" with content:
      """
      test.body=Hello from custom profile
      """
    When user execute "camel run app/profile.yaml --profile=app/custom_profile" in parallel
    Then integration "profile" logs "Hello from custom profile"
    And user execute "camel stop profile"
Feature: Test developer console

  Background:
    Given camel jbang is installed

  Scenario: User execute app and interact with developer console
    Given user execute "camel init developer_console.yaml --directory=app"
    Then file "developer_console.yaml" is created
    When user execute "camel run app/developer_console.yaml --console" in parallel
    Then integration "developer_console" logs "Hello Camel from yaml"
    When user execute HTTP request "/q/dev/"
    Then HTTP response contains "context: Overall information about the CamelContext"
    When user execute HTTP request "/q/dev/top"
    Then HTTP response contains "developer_console"
    And user execute "camel stop developer_console"
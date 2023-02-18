Feature: Test camel run

  Background:
    Given camel jbang is installed

  Scenario: User execute camel run on cli
    Given user execute "camel init cheese.yaml --directory=app"
    Then file "cheese.yaml" is created
    When user execute "camel run app/cheese.yaml" in parallel
    Then integration "cheese" logs "Hello Camel from yaml"
    And integration "cheese" logs expected camel version
    And user execute "camel stop cheese"

  Scenario: Running routes from multiple files
    Given user execute "camel init route1.yaml --directory=app"
    And user create integration "route2.yaml" with content:
      """
      # camel-k: language=yaml

      # Write your routes here, for example:
      - from:
          uri: "timer:yaml"
          parameters:
            period: "1000"
          steps:
            - setBody:
                constant: "Hello Camel from custom integration"
            - log: "${body}"
      """
    Then file "route1.yaml" is created
    Then file "route2.yaml" is created
    When user execute "camel run app/route1.yaml app/route2.yaml" in parallel
    Then integration "route1" logs "Hello Camel from yaml"
    And integration "route1" logs "Hello Camel from custom integration"
    And user execute "camel stop route1"

  # TODO Disabled due to docker client that replace ' with "
  #Scenario: Running route from input parameter
  #  Given user execute "camel run --code='from(\"kamelet:beer-source\").to(\"log:beer\")'"
    # TODO Improve Then
  #  Then integration "CodeRoute" logs "[ - timer://beer] beer"
  #  And user execute "camel stop CodeRoute"

  Scenario: Dev mode with live reload
    Given user create integration "live-reload.yaml" with content:
      """
      # camel-k: language=yaml

      # Write your routes here, for example:
      - from:
          uri: "timer:yaml"
          parameters:
            period: "1000"
          steps:
            - setBody:
                constant: "Hello Camel from custom integration"
            - log: "${body}"
      """
    When user execute "camel run app/live-reload.yaml --dev" in parallel
    Then integration "live-reload" logs "Hello Camel from custom integration"
    When user update "live-reload.yaml" content with:
      """
      # camel-k: language=yaml

      # Write your routes here, for example:
      - from:
          uri: "timer:yaml"
          parameters:
            period: "1000"
          steps:
            - setBody:
                constant: "Hello Camel with updated content"
            - log: "${body}"
      """
    Then integration "live-reload" logs "Hello Camel with updated content"
    And user execute "camel stop live-reload"
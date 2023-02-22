FROM eclipse-temurin:17.0.6_10-jdk

ARG CAMEL_REF=main

WORKDIR /home

RUN curl -Ls https://sh.jbang.dev | bash -s - app setup

ENV PATH=/root/.jbang/bin:$PATH

RUN jbang trust add https://github.com/apache/camel/ \
    && jbang app install camel@apache/camel/$CAMEL_REF

EXPOSE 8080

ENTRYPOINT ["tail", "-f", "/dev/null"]

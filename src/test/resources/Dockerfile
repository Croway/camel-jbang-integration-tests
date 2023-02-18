FROM quay.io/ubi8/openjdk-17

WORKDIR /home

RUN curl -LjO https://github.com/jbangdev/jbang/releases/download/v0.103.1/jbang.zip && \
    jar xf jbang.zip && \
    rm jbang.zip && \
    chmod +x jbang/bin/jbang

ENV PATH="${PATH}:/home/jbang/bin"

RUN jbang trust add https://raw.githubusercontent.com/apache/camel/camel-3.20.1/dsl/camel-jbang/camel-jbang-main/src/main/jbang/main/ && \
    jbang app install https://raw.githubusercontent.com/apache/camel/camel-3.20.1/dsl/camel-jbang/camel-jbang-main/src/main/jbang/main/CamelJBang.java

RUN  mkdir app

RUN cp /root/.jbang/bin/CamelJBang /root/.jbang/bin/camel
ENV PATH=${PATH}:/root/.jbang/bin

ENTRYPOINT ["tail", "-f", "/dev/null"]

# ENTRYPOINT ["/root/.jbang/bin/CamelJBang"]
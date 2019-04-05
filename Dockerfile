FROM registry01.payulatam.com:5000/payu/java8

ENV APP_JAR /opt/docker/proxy-adapter-payu.jar
ENV PROXY_PORT ${PROXY_PORT}
ENV PROXY_HOST ${PROXY_HOST}
ENV PROXY_REPLICAS_HOST ${REPLICAS_HOST}
ENV MOCK_HOST ${MOCK_HOST}

COPY docker/opt /tmp
COPY target/*.jar /tmp/docker/proxy-adapter-payu.jar
RUN mv /tmp/docker /opt

WORKDIR /opt/docker/bin
RUN chmod +x run_container
CMD ["./run_container"]

FROM openjdk:8
RUN apt-get update
RUN apt-get install -y libx11-dev
RUN apt-get install -y libxext-dev
RUN apt-get install -y libxrender-dev
RUN apt-get install -y libxrandr-dev
RUN apt-get install -y libxtst-dev
RUN apt-get install -y libxt-dev
COPY . /
WORKDIR /

CMD ["java", "-jar", "1660Runner.jar"]

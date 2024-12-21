echo "------------------ mvnw ------------------"
RUN --mount=type=cache,target=/root/.m2 ./mvnw -f $HOME/pom.xml clean package


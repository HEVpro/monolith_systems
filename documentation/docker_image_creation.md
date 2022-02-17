# DOCKER: JAVA APP
## Create docker compose
```sh
    monolith:
        image: monolith-systems:latest // the image name to push docker
        container_name: monolith // to see better the name on  docker container ps command
        build:   // necessary if it is an app or requires a build the own app properly
        context: monolith
        dockerfile: Dockerfile  // our dockerFile configuration inside the app folder
        ports:
            - '8080:8080'  // expose the ports
        environment:
            SPRING_DATASOURCE_URL: 'jdbc:mariadb://mariadb:3306/shrtdb'
        depends_on:
            - mariadb
```
1. If we have to build and app, the docker command is, every change on docker compose file:
```sh
    docker-compose up --build <image_name>
```
2. Create the Dockerfile as entrypoint on the project folder
```sh
    FROM openjdk:17-slim //version of java
    WORKDIR /app  //create our docker system file and named app
    COPY mvnw mvnw   // copy from host project to docker project the folder
    COPY .mvn .mvn
    COPY mvnw mvnw
    COPY pom.xml pom.xml
    COPY src src
    RUN ./mvnw package -DskipTests   // run commands
    EXPOSE 8080     // expose the port
    CMD ["java", "-jar", "/app/target/shrtr-0.0.1-SNAPSHOT.jar"]  // the terminal command to up the app
```
### TRICK
If we need to reorganize the folder in java app, we have to delete the mvn folder and target (in intellij).
Delete the folder target, and then, build with the IDE project.
3. Push the image to docker hub (manually):
```sh
    docker login -u <user>
    + password on command line
    docker build . -t <image_name>:latest -t <username>/<image_name>:latest  // -t = tag, tag your last image and then tag to your Docker repository
    docker push <username>/<image_name>:latest
```
4. Push the image to docker hub (automatic):
    1. Create the folder on main path repo
        ```sh
        .github/workflows
        ```
    2. Add <name_process>.yaml:
        ```sh
        name: ci  // name of the process

        on:   // when it happens
          push:
            branches:
              - 'main'
        
        jobs:  //works to do after on
          docker:
            runs-on: ubuntu-latest
            steps:
              -
                name: Set up QEMU  // add QEMU docker
                uses: docker/setup-qemu-action@v1
              -
                name: Set up Docker Buildx   // build docker
                uses: docker/setup-buildx-action@v1
              -
                name: Login to DockerHub  // login in docker hub
                uses: docker/login-action@v1
                with:   //add secrets on github repository
                  username: ${{ secrets.DOCKERHUB_USERNAME }}
                  password: ${{ secrets.DOCKERHUB_TOKEN }}
              -
                name: Build and push  //our own process to push image to docker
                uses: docker/build-push-action@v2
                with:  
                  context: "{{defaultContext}}:monolith"   //our project image path
                  push: true
                  tags: hacheev/monolith_systems_monolith:latest, hacheev/monolith_systems_monolith:${{ github.sha }}  //two methods to tag the image
        ```


FROM eclipse-temurin:17-jdk

ENV WORK_DIR /home/starwars


WORKDIR "${WORK_DIR}"

# Point `JAVA_HOME` and `PATH` environment variables to the location of the
# Java binaries.
ENV JAVA_HOME /usr/local/java
ENV PATH $JAVA_HOME/bin:$PATH


ADD "@project.build.finalName@.jar" "@project.build.finalName@.jar"
CMD exec java ${JAVA_OPTIONS} ${STARWARS_JAVA_OPTIONS} -jar "@project.build.finalName@.jar"

# Stage 1: Run tests using Maven + Chrome
# maven:3.9-eclipse-temurin-17 provides Maven 3.9.x and JDK 17 on Debian
FROM maven:3.9-eclipse-temurin-17

# Install Chrome and dependencies in one layer to keep image smaller
# --no-install-recommends avoids pulling unnecessary packages like fonts/X11
RUN apt-get update && apt-get install -y --no-install-recommends \
    wget \
    gnupg2 \
    && wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome.gpg \
    && echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y --no-install-recommends google-chrome-stable \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy pom.xml first — Docker caches this layer so dependencies aren't
# re-downloaded on every code change (only when pom.xml changes)
COPY pom.xml .
# Downloads ALL dependencies including plugins and agents
RUN mvn dependency:go-offline -q && \
    mvn dependency:copy -Dartifact=org.aspectj:aspectjweaver:1.9.22.1 \
    -DoutputDirectory=/tmp -q && \
    rm -rf /tmp/aspectjweaver-*.jar

# Copy source code (changes frequently — this layer rebuilds on code edits)
COPY src ./src

# Run tests by default when container starts
# -Dmaven.test.failure.ignore=true ensures allure-results are generated
# even when tests fail, so the report can still be viewed
CMD ["mvn", "clean", "test", "-Dmaven.test.failure.ignore=true"]
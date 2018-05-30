# FROM 指定使用哪个镜像作为基准
FROM openjdk:8-jdk-alpine
# VOLUME 为挂载路径  -v
VOLUME /tmp
# 复制文件到镜像中
COPY qms-1.0.0-SNAPSHOT.jar app.jar
# 指定容器内的时区
RUN echo "Asia/Shanghai" > /etc/timezone;
# ENV为设置环境变量
ENV JAVA_OPTS=""
# ENTRYPOINT 为启动时运行的命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar"]